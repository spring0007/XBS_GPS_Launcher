package com.sczn.wearlauncher.card.healthalarm;

import android.content.ContentUris;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.absFragment;
import com.sczn.wearlauncher.base.util.MxyToast;
import com.sczn.wearlauncher.base.view.MyRecyclerView;
import com.sczn.wearlauncher.base.view.WrapLinearLayoutManager;
import com.sczn.wearlauncher.card.healthalarm.AdapterAlarm.IAlarmClickLiten;
import com.sczn.wearlauncher.card.healthalarm.UtilHealthAlarm.IHealthAlarmListen;
import com.sczn.wearlauncher.db.Provider;
import com.sczn.wearlauncher.db.Provider.ColumnsHealthAlarm;

public class FragmentAlarmList extends absFragment implements OnClickListener,IAlarmClickLiten,IHealthAlarmListen{

	public static final String ARG_ALARM_TYPE = "alarm_type";
	public static final String FRAGMENT_TAG_ALARM_EDIT = "alarm_edit";
	
	public static FragmentAlarmList newInstance(int type){
		FragmentAlarmList fragment = new FragmentAlarmList();
		final Bundle bdl = new Bundle();
		bdl.putInt(ARG_ALARM_TYPE, type);
		fragment.setArguments(bdl);
		return fragment;
	}
	
	private int alarmType = ModelAlarm.ALARM_TYPE_DRINK;
	private MyRecyclerView mRecyclerView;
	private TextView mAddButton;
	private AdapterAlarm mAdapterAlarm;

	public int getAlarmType(){
		return this.alarmType;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		final Bundle bdl = getArguments();
		
		if(bdl != null){
			alarmType = bdl.getInt(ARG_ALARM_TYPE, ModelAlarm.ALARM_TYPE_DRINK);
		}
		
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_card_healthalarm_alarmlist;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mRecyclerView = findViewById(R.id.healthalarm_list);
		mAddButton = findViewById(R.id.healthalarm_add);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		mAdapterAlarm = new AdapterAlarm(mActivity);
		mRecyclerView.setLayoutManager(new WrapLinearLayoutManager(mActivity));
		mRecyclerView.setAdapter(mAdapterAlarm);
		mRecyclerView.setEmpty(findViewById(R.id.healthalarm_empty));
		
		mAdapterAlarm.setOnClickListen(this);
		mAddButton.setOnClickListener(this);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		freshAlarm();
		UtilHealthAlarm.getInstance().addListen(this);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		UtilHealthAlarm.getInstance().removeListen(this);
		super.onPause();
	}
	
	private void freshAlarm(){
		switch (alarmType) {
			case ModelAlarm.ALARM_TYPE_DRINK:
				mAdapterAlarm.setData(UtilHealthAlarm.getInstance().getDrinkAlarms());
				break;
			case ModelAlarm.ALARM_TYPE_SIT:
				mAdapterAlarm.setData(UtilHealthAlarm.getInstance().getSitAlarms());
				break;
			default:
				return;
		}
		mAdapterAlarm.notifyDataSetChanged();
	}
	
	private void addAlarm(){
		if(mRecyclerView.getAdapter().getItemCount() >= UtilHealthAlarm.MAX_ALARM_COUNT){
			MxyToast.showShort(mActivity, R.string.alarm_count_limit);
			return;
		}
		ModelAlarm alarm = new ModelAlarm();
		alarm.setType(alarmType);
		alarm.setID(ModelAlarm.INVALUED_ID);
		gotoAlarmEdit(alarm);
	}
	
	private void gotoAlarmEdit(ModelAlarm alarm){
		FragmentAlarmEdit.newInstance(alarm).show(getChildFragmentManager(), FRAGMENT_TAG_ALARM_EDIT);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.healthalarm_add:
				addAlarm();
				break;
			default:
				break;
		}
	}

	@Override
	public void onDeleteClick(int position, ModelAlarm alarm) {
		// TODO Auto-generated method stub
		try {
			
			//MxyLog.d(this, "onDeleteClick uri=" +  ContentUris.withAppendedId(HealthAlarmcolumns.CONTENT_URI, alarm.getID()));
			if(LauncherApp.appContext.getContentResolver().delete(ContentUris.withAppendedId(ColumnsHealthAlarm.CONTENT_URI, alarm.getID()),
					null, null) <= 0){
				MxyToast.showShort(getActivity(), R.string.db_unusual);
			}else{
				MxyToast.showShort(getActivity(), R.string.db_opration_success);
			}
		} catch (Exception e) {
			// TODO: handle exception
			MxyToast.showShort(LauncherApp.appContext, R.string.db_unusual);
		}
	}

	@Override
	public void onAlarmClick(View v) {
		// TODO Auto-generated method stub
		try {
			final ModelAlarm alarm = (ModelAlarm) v.getTag();
			gotoAlarmEdit(alarm);
		} catch (Exception e) {
			// TODO: handle exception
			MxyToast.showShort(LauncherApp.appContext, R.string.db_unusual);
		}
	}

	@Override
	public void onAlarmEnable(View v) {
		// TODO Auto-generated method stub
		v.setEnabled(false);
		try {
			v.setSelected(!v.isSelected());
			final ModelAlarm alarm = (ModelAlarm) v.getTag();
			ContentValues values = new ContentValues();
			values.put(Provider.ColumnsHealthAlarm.COLUMNS_TIME, alarm.getTimeInDay());
			values.put(Provider.ColumnsHealthAlarm.COLUMNS_TYPE, alarm.getType());
			values.put(Provider.ColumnsHealthAlarm.COLUMNS_REPEAT, alarm.getRepeatDay());
			values.put(Provider.ColumnsHealthAlarm.COLUMNS_EBABLE, v.isSelected()?1:0);
			
			if(LauncherApp.appContext.getContentResolver().update(ContentUris.withAppendedId(ColumnsHealthAlarm.CONTENT_URI, alarm.getID()),
					values, null, null)  <= 0){
				MxyToast.showShort(getActivity(), R.string.alarm_sava_failed);
			}else{
				MxyToast.showShort(getActivity(), R.string.alarm_sava_success);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		v.setEnabled(true);
	}

	@Override
	public void onHealthAlarmChanged() {
		// TODO Auto-generated method stub
		freshAlarm();
	}

}
