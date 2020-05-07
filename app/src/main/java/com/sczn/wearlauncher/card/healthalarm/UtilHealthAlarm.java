package com.sczn.wearlauncher.card.healthalarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.AbsBroadcastReceiver;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.base.util.MxyToast;
import com.sczn.wearlauncher.base.util.StringUtils;
import com.sczn.wearlauncher.card.sport.SportMgrUtil;
import com.sczn.wearlauncher.db.Provider;
import com.sczn.wearlauncher.db.Provider.ColumnsHealthAlarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;



public class UtilHealthAlarm {
	
	private static class Holder{
		private static UtilHealthAlarm instance = new UtilHealthAlarm();
	}
	public static UtilHealthAlarm getInstance(){
		return Holder.instance;
	} 
	public static final int MAX_ALARM_COUNT = 10;
	public static final String ACTION_HEALTHALARM_DRINK = "broadcast.alarm.healthalarm.drink";
	public static final String ACTION_HEALTHALARM_SIT = "broadcast.alarm.healthalarm.sit";
	
	public static final String MAP_KEY_DRINKS = "alarm_drinks";
	public static final String MAP_KEY_SITS = "alarm_sits";
	public static final String MAP_KEY_NEXT_DRINK = "alarm_next_drink";
	public static final String MAP_KEY_NEXT_SIT = "alarm_next_sit";
	public static final String MAP_KEY_NEXT_ALARM = "alarm_next_alarm";
	

	private HashMap<String, ArrayList<ModelAlarm>> mAlarmMap;
	private GetAllAlarmTask mGetAllAlarmTask;
	private HealAlarmDbObserver mHealAlarmDbObserver;
	private ArrayList<IHealthAlarmListen> mListens;
	
	private AlarmManager mAlarmManager;
	private PendingIntent mHealyhAlarmIntent;
	private HealAlarmReceiver mHealAlarmReceiver;
	
	private UtilHealthAlarm(){
		mAlarmMap = new HashMap<String, ArrayList<ModelAlarm>>();
		mHealAlarmDbObserver = new HealAlarmDbObserver(new Handler());
		mHealAlarmReceiver = new HealAlarmReceiver();
		
		mAlarmManager = (AlarmManager) LauncherApp.appContext.getSystemService(Context.ALARM_SERVICE);
		mHealyhAlarmIntent = PendingIntent.getBroadcast(LauncherApp.appContext, 0, new Intent(HealAlarmReceiver.ACTION_ALARM_HEALTHALARM), 0);
	}
	
	public void addListen(IHealthAlarmListen listen){
		if(mListens == null){
			mListens = new ArrayList<UtilHealthAlarm.IHealthAlarmListen>();
		}
		if(!mListens.contains(listen)){
			mListens.add(listen);
		}
	}
	public void removeListen(IHealthAlarmListen listen){
		if(mListens == null){
			return;
		}
		mListens.remove(listen);
	}
	
	public ArrayList<ModelAlarm> getDrinkAlarms(){
		return mAlarmMap.get(MAP_KEY_DRINKS);
	}
	public ArrayList<ModelAlarm> getSitAlarms(){
		return mAlarmMap.get(MAP_KEY_SITS);
	}

	public ModelAlarm getNextDrinkAlarm(){
		final ArrayList<ModelAlarm> alarms = mAlarmMap.get(MAP_KEY_NEXT_DRINK);
		if(alarms != null && alarms.size() > 0){
			return alarms.get(0);
		}
		return null;
	}
	public ModelAlarm getNextSitAlarm(){
		final ArrayList<ModelAlarm> alarms = mAlarmMap.get(MAP_KEY_NEXT_SIT);
		if(alarms != null && alarms.size() > 0){
			return alarms.get(0);
		}
		return null;
	}
	
	public void startMgr(Context context){
		mHealAlarmDbObserver.register(context);
		mHealAlarmReceiver.register(context);
		startGetAlarm();
	}
	
	public void stopMgr(Context context){
		mHealAlarmReceiver.unRegister(context);
		mHealAlarmDbObserver.unReidter(context);
		mHealAlarmDbObserver = null;
		
		stopGetAlarm();
	}
	
	private void startGetAlarm(){
		stopGetAlarm();
		mGetAllAlarmTask = new GetAllAlarmTask();
		mGetAllAlarmTask.execute();
	}
	private void stopGetAlarm(){
		if(mGetAllAlarmTask != null){
			mGetAllAlarmTask.cancel(true);
			mGetAllAlarmTask = null;
		}
	}
	
	private void setAlarmIntent(){
		final ArrayList<ModelAlarm> alarms = mAlarmMap.get(MAP_KEY_NEXT_ALARM);
		if(alarms != null && alarms.size() > 0){
			mAlarmManager.cancel(mHealyhAlarmIntent);
			
			final Calendar calendar = Calendar.getInstance();
			final long currTime = calendar.getTimeInMillis();
			
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);

			final long alarmTime = alarms.get(0).getTimeInDay() - (currTime - calendar.getTimeInMillis());
			if(alarmTime < 0){
				MxyLog.d(this, "setAlarmIntent error");
				return;
			}

			mAlarmManager.setExact(AlarmManager.RTC_WAKEUP,currTime + alarmTime, mHealyhAlarmIntent);
			MxyToast.showShort(LauncherApp.appContext, String.format(LauncherApp.appContext.getString(R.string.alarm_at),
					alarmTime/3600000, (alarmTime%3600000)/60000, ((alarmTime%3600000)%60000)/1000));
		}
	}
	
	private void alarmChanged(HashMap<String, ArrayList<ModelAlarm>> alarms){
		if(alarms == null){
			alarms = new HashMap<String, ArrayList<ModelAlarm>>();
		}
		
		//MxyLog.i(this, "alarmChanged--MAP_KEY_NEXT_SIT=" + alarms.get(MAP_KEY_NEXT_SIT));
		//MxyLog.i(this, "alarmChanged--MAP_KEY_NEXT_ALARM=" + alarms.get(MAP_KEY_NEXT_ALARM));
		//MxyLog.i(this, "alarmChanged--MAP_KEY_DRINKS=" + alarms.get(MAP_KEY_DRINKS));
		this.mAlarmMap = alarms;
		if(mListens != null){
			for(IHealthAlarmListen listen : mListens){
				listen.onHealthAlarmChanged();
			}
		}
		
		setAlarmIntent();
	}
	
	private class HealAlarmDbObserver extends ContentObserver{
		
		private void register(Context context){
			context.getContentResolver().registerContentObserver(getUri(), true, this);
		}
		
		public void unReidter(Context context){
			context.getContentResolver().unregisterContentObserver(this);
		}
		
		private Uri getUri(){
			return ColumnsHealthAlarm.CONTENT_URI;
		}

		public HealAlarmDbObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
			startGetAlarm();
		}
		
		@Override
		public void onChange(boolean selfChange, Uri uri) {
			// TODO Auto-generated method stub
			//MxyLog.d(UtilHealthAlarm.this, "selfChange=" + selfChange + "--uri=" + uri);
			super.onChange(selfChange, uri);
			startGetAlarm();
		}
	}
	
	private class GetAllAlarmTask extends AsyncTask<Void, Void, HashMap<String, ArrayList<ModelAlarm>>>{

		@Override
		protected HashMap<String, ArrayList<ModelAlarm>> doInBackground(Void... params) {
			// TODO Auto-generated method stub
		
			Cursor cursor = LauncherApp.appContext.getContentResolver().query(ColumnsHealthAlarm.CONTENT_URI,
					null, null, null, null);

			if(isCancelled()){
				cursor.close();
				return null;
			}
			
			if(cursor == null){
				return null;
			}
			if(!cursor.moveToFirst()){
				cursor.close();
				return null;
			}
			
			final HashMap<String, ArrayList<ModelAlarm>> alarms = new HashMap<String, ArrayList<ModelAlarm>>();
			final ArrayList<ModelAlarm> drinkAlarms = new ArrayList<ModelAlarm>();
			final ArrayList<ModelAlarm> sitAlarms = new ArrayList<ModelAlarm>();
			final ArrayList<ModelAlarm> nextAlarm = new ArrayList<ModelAlarm>();
			final ArrayList<ModelAlarm> nextDrinkAlarm = new ArrayList<ModelAlarm>();
			final ArrayList<ModelAlarm> nextSitAlarm = new ArrayList<ModelAlarm>();
			
			final Calendar calendar = Calendar.getInstance();
			final int currWekkday = calendar.get(Calendar.DAY_OF_WEEK);
			final long currtime = calendar.getTimeInMillis();
			
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			final long currOffset = currtime - calendar.getTimeInMillis() - 0*1000;		//预留10S处理数据
			
			boolean hasNextAlarm = false;
			boolean hasNextDrink = false;
			boolean hasMextSit = false;
			long nextAlarmTime = Long.MIN_VALUE; 
			do{
				if(isCancelled()){
					cursor.close();
					return null;
				}
				
				final ModelAlarm alarm = new ModelAlarm();
				final int type = cursor.getInt(cursor.getColumnIndex(Provider.ColumnsHealthAlarm.COLUMNS_TYPE));
				final long time = cursor.getLong(cursor.getColumnIndex(Provider.ColumnsHealthAlarm.COLUMNS_TIME));
				final int repeat = cursor.getInt(cursor.getColumnIndex(Provider.ColumnsHealthAlarm.COLUMNS_REPEAT));
				final boolean enable = cursor.getInt(cursor.getColumnIndex(Provider.ColumnsHealthAlarm.COLUMNS_EBABLE)) == 1;
				
				alarm.setID(cursor.getInt(cursor.getColumnIndex(Provider.ColumnsHealthAlarm._ID)));
				alarm.setTimeInDay(time);
				alarm.setRepeatDay(repeat);
				alarm.setEnable(enable);
				alarm.setType(type);
				
				//MxyLog.d(this, "doInBackground--currOffset=" + currOffset + "--currWekkday=" + currWekkday + "--isValue=" + alarm.isValue(currOffset, currWekkday));
				
				switch (type) {
					case ModelAlarm.ALARM_TYPE_DRINK:
						if(!hasNextAlarm){
							if(alarm.isValue(currOffset, currWekkday)){
								nextAlarm.add(alarm);
								nextAlarmTime = alarm.getTimeInDay();
								hasNextAlarm = true;
							}
						}else if(nextAlarmTime == alarm.getTimeInDay()){
							if(alarm.isValue(currOffset, currWekkday)){
								nextAlarm.add(alarm);
							}
						}
						if(!hasNextDrink){
							if(alarm.isValue(currOffset, currWekkday)){
								nextDrinkAlarm.add(alarm);
								hasNextDrink = true;
							}
						}
						drinkAlarms.add(alarm);
						break;
					case ModelAlarm.ALARM_TYPE_SIT:
						if(!hasNextAlarm){
							if(alarm.isValue(currOffset, currWekkday)){
								nextAlarm.add(alarm);
								hasNextAlarm = true;
							}
						}else if(nextAlarmTime == alarm.getTimeInDay()){
							if(alarm.isValue(currOffset, currWekkday)){
								nextAlarm.add(alarm);
							}
						}
						if(!hasMextSit){
							if(alarm.isValue(currOffset, currWekkday)){
								nextSitAlarm.add(alarm);
								hasMextSit = true;
							}
						}
						sitAlarms.add(alarm);
						break;
					default:
						break;
				}
			}while(cursor.moveToNext());
			
			cursor.close();
			
			alarms.put(MAP_KEY_NEXT_ALARM, nextAlarm);
			alarms.put(MAP_KEY_NEXT_DRINK, nextDrinkAlarm);
			alarms.put(MAP_KEY_NEXT_SIT, nextSitAlarm);
			alarms.put(MAP_KEY_DRINKS, drinkAlarms);
			alarms.put(MAP_KEY_SITS, sitAlarms);
			
			return alarms;
		}
		
		@Override
		protected void onPostExecute(HashMap<String, ArrayList<ModelAlarm>> result) {
			// TODO Auto-generated method stub
			if(isCancelled()){
				return;
			}
			alarmChanged(result);
		}
	}
	
	public interface IHealthAlarmListen{
		public void onHealthAlarmChanged();
	}
	
	private void ResetAlarm(){
		mAlarmManager.cancel(mHealyhAlarmIntent);
		startGetAlarm();
	}
	
	private void doAlarmAction(Context context) {
		// TODO Auto-generated method stub
		ActivityAlarm.startActivity(context);
		
		new Thread(disableAlarm).start();
	}
	
	private Runnable disableAlarm = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			final ArrayList<ModelAlarm> alarms = mAlarmMap.get(MAP_KEY_NEXT_ALARM);
			String where = "";
			String whereArg = "";
			for(ModelAlarm alarm : alarms){
				if((alarm.getRepeatDay() & ModelAlarm.REPEAT_FLAG_ENABLE) == 0){
					if(StringUtils.isEmpty(where)){
						where = ColumnsHealthAlarm._ID  + " IN";
						whereArg += "(";
						whereArg += alarm.getID();
					}else{
						whereArg += ",";
						whereArg += alarm.getID();
					}
					
				}
			}
			if(!StringUtils.isEmpty(where)){
				ContentValues values = new ContentValues();
				values.put(ColumnsHealthAlarm.COLUMNS_EBABLE, 0);
				whereArg += ")";
				LauncherApp.appContext.getContentResolver().update(ColumnsHealthAlarm.CONTENT_URI,values, where + whereArg, null);
			}else{
				ResetAlarm();
			}
		}
	};
	
	private class HealAlarmReceiver extends AbsBroadcastReceiver{

		public static final String ACTION_ALARM_HEALTHALARM = "com.sczn.bdcast.alarm.healthalarm";
		@Override
		public IntentFilter getIntentFilter() {
			// TODO Auto-generated method stub
			final IntentFilter filter = new IntentFilter();
			filter.addAction(Intent.ACTION_DATE_CHANGED);
			filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
			filter.addAction(Intent.ACTION_TIME_CHANGED);
			filter.addAction(ACTION_ALARM_HEALTHALARM);
			
			filter.addAction(SportMgrUtil.ACTION_ALARM_NEW_DAY);
			return filter;
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			final String action = intent.getAction();
			
			if(Intent.ACTION_DATE_CHANGED.equals(action)){
				ResetAlarm();
				return;
			}
			if(Intent.ACTION_TIMEZONE_CHANGED.equals(action)){
				ResetAlarm();			
				return;
			}
			if(Intent.ACTION_TIME_CHANGED.equals(action)){
				ResetAlarm();
				return;
			}
			if(ACTION_ALARM_HEALTHALARM.equals(action)){
				doAlarmAction(context);
				return;
			}
			
			if(SportMgrUtil.ACTION_ALARM_NEW_DAY.equals(action)){
				ResetAlarm();
				return;
			}
		}
	}
}
