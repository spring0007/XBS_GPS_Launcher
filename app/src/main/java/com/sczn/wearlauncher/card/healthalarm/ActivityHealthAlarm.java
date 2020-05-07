package com.sczn.wearlauncher.card.healthalarm;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.sczn.wearlauncher.R;

public class ActivityHealthAlarm extends HealthAlarmActivity{

	public static final String EXTRA_ALARM_TYPE = "alarm_type";

	private int contentId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		setIntent(intent);
	}
	
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_card_healthalarm;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		contentId = R.id.healthalarm_content;
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		initFragment();
	}

	private void initFragment() {
		// TODO Auto-generated method stub
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.replace(contentId, getFragment());
		
		ft.commitAllowingStateLoss();
	}
	
	public Fragment getFragment(){
		
		final int alarmType = getIntent().getIntExtra(EXTRA_ALARM_TYPE, ModelAlarm.ALARM_TYPE_DRINK);
		//MxyToast.showShort(getApplication(), "onNewIntent--fragmentIndex=" + fragmentIndex);
		switch (alarmType) {
			case ModelAlarm.ALARM_TYPE_DRINK:
				return FragmentDrink.newInstance();
			case ModelAlarm.ALARM_TYPE_SIT:
			default:
				return FragmentSit.newInstance();
		}
	}

}
