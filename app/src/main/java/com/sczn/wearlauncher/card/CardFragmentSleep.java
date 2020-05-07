package com.sczn.wearlauncher.card;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.AbsBroadcastReceiver;
import com.sczn.wearlauncher.base.absDialogFragment;
import com.sczn.wearlauncher.base.util.DateFormatUtil;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.card.sport.ModelSleepState;
import com.sczn.wearlauncher.card.sport.SportMgrUtil;
import com.sczn.wearlauncher.card.sport.SleepActivity;
import com.sczn.wearlauncher.db.Provider.ColumnsSleep;

public class CardFragmentSleep extends absDialogFragment {
	
	public static final String ARG_IS_TMP = "is_tmp";
	
	public static CardFragmentSleep newInstance( ) {
		CardFragmentSleep fragment = new CardFragmentSleep();
		
		return fragment;

	}

	private boolean isTmp;
	
	public static final long DURATION_IN_MILLLI = 12*60*60*1000;
	public static final long PER_HOUR = 60*60*1000;
	private Context mContext;
	private TextView mAllSleep,mDeepSleep,mLightSleep,mSleepState;
	private QueryDbTask mQueryDbTask;
	private ArrayList<ModelSleepState> mValues;
	private String timeDeepSleep = "0.0";
	private String timeLightSleep = "0.0";
	private String timeAllSleep = "00.0";
	private double barValue = 0.0;
	private SleepFreshReceiver mSleepFreshReceiver;
	

	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		mContext = getActivity();
		return R.layout.fragment_card_sleep;
	}

	
	protected void initView() {
		// TODO Auto-generated method stub
		mAllSleep = findViewById(R.id.total_hours_text);
		mDeepSleep = findViewById(R.id.depp_sleep_hours_text);
		mLightSleep = findViewById(R.id.light_sleep_hours_text);
		mSleepState = findViewById(R.id.sleep_state);
	}

	
	protected void initData() {
		// TODO Auto-generated method stub
		
		mDeepSleep.setText(timeDeepSleep);
		mLightSleep.setText(timeLightSleep);
		mAllSleep.setText(timeAllSleep);
		
		float deepHour;
		try {
			deepHour = Float.parseFloat(timeDeepSleep);
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			deepHour = 0;
		}
		if(deepHour > 2){
        	mSleepState.setText(R.string.sleep_state_good);
        }else if(deepHour > 1){
        	mSleepState.setText(R.string.sleep_state_normal);
        }else if(deepHour > 0){
        	mSleepState.setText(R.string.sleep_state_bad);
        }else if(deepHour == 0){
        	mSleepState.setText(R.string.sleep_state_null);
        }
		
		mRootView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Intent i = new Intent(mContext, SleepActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.putParcelableArrayListExtra(SleepActivity.ARG_SLEEP_DATA, mValues);
					i.putExtra(SleepActivity.ARG_TIME_DEEP, timeDeepSleep);
					i.putExtra(SleepActivity.ARG_TIME_LIGHT, timeLightSleep);
					i.putExtra(SleepActivity.ARG_TIME_ALL, timeAllSleep);
					mContext.startActivity(i);
				} catch (Exception e) {
					// TODO: handle exception
					MxyLog.e(this, "gotoSleepActivity--" + e.toString());
				}
			}
		});
	}
	
	
	
	
	@Override
	protected void creatView() {
		// TODO Auto-generated method stub
		mSleepFreshReceiver = new SleepFreshReceiver();
		initView() ;
		initData();
		if(isTmp){
			return;
		}
	
		mSleepFreshReceiver.register(mContext);
		startGetData();
	}

	@Override
	protected void destorytView() {
		// TODO Auto-generated method stub
		if(isTmp){
			return;
		}
		mSleepFreshReceiver.unRegister(mContext);
		stopGetData();
	}
	private void setData(ArrayList<ModelSleepState> values){
		this.mValues = values;
		if(mValues != null){
			final float deepHour = 10*ModelSleepState.deepSleepTime/PER_HOUR;
			final float lightHour = 10*ModelSleepState.lightSleepTime/PER_HOUR;
			MxyLog.d(this,"SleepState.lightSleepTime=" + ModelSleepState.lightSleepTime + "--PER_HOUR=" + PER_HOUR + "--lightHour=" + lightHour);
			DecimalFormat decimalFormat=new DecimalFormat("0.0");
			timeDeepSleep = decimalFormat.format(deepHour/10.0);
			timeLightSleep = decimalFormat.format(lightHour/10.0);
			timeAllSleep = decimalFormat.format(deepHour/10.0 + lightHour/10.0);
			barValue = deepHour/10.0 + lightHour/10.0;
			
			mDeepSleep.setText(timeDeepSleep);
			mLightSleep.setText(timeLightSleep);
			mAllSleep.setText(timeAllSleep);
			
			if(deepHour > 2){
	        	mSleepState.setText(R.string.sleep_state_good);
	        }else if(deepHour > 1){
	        	mSleepState.setText(R.string.sleep_state_normal);
	        }else if(deepHour > 0){
	        	mSleepState.setText(R.string.sleep_state_bad);
	        }else if(deepHour == 0 && lightHour == 0){
	        	mSleepState.setText(R.string.sleep_state_null);
	        }
		}
	}
	
	private void stopGetData(){
		if(mQueryDbTask != null && !mQueryDbTask.isCancelled()){
			mQueryDbTask.cancel(true);
		}
	}
	private void startGetData(){
		stopGetData();
		mQueryDbTask = new QueryDbTask();
		mQueryDbTask.execute();
		
	}

	
	private class QueryDbTask extends AsyncTask<Void, Void, ArrayList<ModelSleepState>>{
		
		private long getEndTime(){
			
			final Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 9);
	        calendar.set(Calendar.MILLISECOND,0);
	        calendar.set(Calendar.SECOND,0);
	        calendar.set(Calendar.MINUTE,0);
			return calendar.getTimeInMillis() + SportMgrUtil.ALARM_DELAY_NORMAL;
		}
		
		@Override
		protected ArrayList<ModelSleepState> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			final long endTime = getEndTime();
			final long startTime = endTime - DURATION_IN_MILLLI - SportMgrUtil.ALARM_DELAY_NORMAL;
			//MxyLog.d(this, "startTime="  +startTime + "--endTime=" + endTime);
			String selection = ColumnsSleep.COLUMNS_END_TIME + " >= " + startTime 
					+ " AND " + ColumnsSleep.COLUMNS_START_TIME + " <= " + endTime ;
			if(getActivity() == null){
				return null;
			}
            Cursor cursor = getActivity().getContentResolver().query(ColumnsSleep.CONTENT_URI,null,selection,null,null);
            if(cursor == null){
            	return null;
            }
            MxyLog.d(this, "cursor.getColumnCount=" + cursor.getColumnCount() + "--cursor.getCount=" + cursor.getCount());
            if(!cursor.moveToFirst()){
            	cursor.close();
            	return null;
            }
            final ArrayList<ModelSleepState> sleepRecords = new ArrayList<ModelSleepState>();
            ModelSleepState.deepSleepTime = 0;
            ModelSleepState.lightSleepTime = 0;
            long starTmp;
            long endTmp;
            do{

            	final int state = cursor.getInt(cursor.getColumnIndex(
            			ColumnsSleep.COLUMNS_SLEEP_STATE));
            	if(ModelSleepState.STATE_DEEP_SLEEP != state && ModelSleepState.STATE_LIGHT_SLEEP != state){
            		continue;
            	}

            	MxyLog.d(this, DateFormatUtil.getTimeString(DateFormatUtil.HMS, cursor.getLong(cursor.getColumnIndex(
            			ColumnsSleep.COLUMNS_START_TIME))));
            	MxyLog.d(this, DateFormatUtil.getTimeString(DateFormatUtil.HMS, cursor.getLong(cursor.getColumnIndex(
            			ColumnsSleep.COLUMNS_END_TIME))));
            	
        		final ModelSleepState  record = new ModelSleepState();
        		starTmp = cursor.getLong(cursor.getColumnIndex(
            			ColumnsSleep.COLUMNS_START_TIME));
        		endTmp = cursor.getLong(cursor.getColumnIndex(
            			ColumnsSleep.COLUMNS_END_TIME));
        		starTmp = starTmp >= startTime ? starTmp : startTime;
        		endTmp = endTmp <= endTime ? endTmp : endTime;
        		record.setState(state);
        		record.setStartOffset(starTmp - startTime);
        		record.setEndOffset(endTmp - startTime);
        		sleepRecords.add(record);
        		if(ModelSleepState.STATE_DEEP_SLEEP == state){
        			ModelSleepState.deepSleepTime += (endTmp - starTmp);
        		}else{
        			ModelSleepState.lightSleepTime += (endTmp - starTmp);
        		}
        		if(isCancelled()){
        			cursor.close();
        			return null;
        		}
            }while(!isCancelled() && cursor.moveToNext());
            cursor.close();
            if(isCancelled()){
            	return null;
            }
			return sleepRecords;
		}

		@Override
		protected void onPostExecute(ArrayList<ModelSleepState> result) {
			// TODO Auto-generated method stub
			if(isCancelled()){
				return;
			}
			if(result == null){
				ModelSleepState.deepSleepTime = 0;
	            ModelSleepState.lightSleepTime = 0;
				result = new ArrayList<ModelSleepState>();
			}
			setData(result);
		}
	}
	
	private class SleepFreshReceiver extends AbsBroadcastReceiver{

		@Override
		public IntentFilter getIntentFilter() {
			// TODO Auto-generated method stub
			final IntentFilter filter = new IntentFilter();
			filter.addAction(Intent.ACTION_DATE_CHANGED);
			filter.addAction(Intent.ACTION_TIME_CHANGED);
			filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
			return filter;
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			startGetData();
		}
		
	}

	
}