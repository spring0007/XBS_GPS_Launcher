package com.sczn.wearlauncher.card;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.absFragment;
import com.sczn.wearlauncher.base.util.DateFormatUtil;
import com.sczn.wearlauncher.card.healthalarm.ActivityHealthAlarm;
import com.sczn.wearlauncher.card.healthalarm.ModelAlarm;
import com.sczn.wearlauncher.card.healthalarm.UtilHealthAlarm;
import com.sczn.wearlauncher.card.healthalarm.UtilHealthAlarm.IHealthAlarmListen;

public class CardFragmentHealthAlam extends absFragment implements android.view.View.OnClickListener,IHealthAlarmListen{
	
	public static final String ARG_IS_TMP = "is_tmp";
	
	public static CardFragmentHealthAlam newInstance(boolean isTmp) {
		CardFragmentHealthAlam fragment = new CardFragmentHealthAlam();
		Bundle bdl = new Bundle();
		bdl.putBoolean(ARG_IS_TMP, isTmp);
		fragment.setArguments(bdl);
		return fragment;

	}

	private boolean isTmp;
	private TextView mSitAlarm;
	private TextView mDrinkAlarm;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bdl = getArguments();
		if (bdl != null) {
			isTmp = bdl.getBoolean(ARG_IS_TMP, false);
		}
	}
	
	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_card_healthalarm;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mSitAlarm = findViewById(R.id.healthalarm_sit);
		mDrinkAlarm = findViewById(R.id.healthalarm_drink);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		
		mSitAlarm.setOnClickListener(this);
		mDrinkAlarm.setOnClickListener(this);
		
		freshAlarmState();
	}
	
	@Override
	protected void startFreshData() {
		// TODO Auto-generated method stub
		super.startFreshData();
		freshAlarmState();
		UtilHealthAlarm.getInstance().addListen(this);
	}

	@Override
	protected void endFreshData() {
		// TODO Auto-generated method stub
		super.endFreshData();
		UtilHealthAlarm.getInstance().removeListen(this);
	}
	
	private void freshAlarmState(){
		final ModelAlarm alarmDrink = UtilHealthAlarm.getInstance().getNextDrinkAlarm();
		if(alarmDrink == null){
			mDrinkAlarm.setText(R.string.alarm_none);
		}else{
			mDrinkAlarm.setText(alarmDrink.getTimeString());
		}
		
		final ModelAlarm alarmSit = UtilHealthAlarm.getInstance().getNextSitAlarm();
		if(alarmSit == null){
			mSitAlarm.setText(R.string.alarm_none);
		}else{
			mSitAlarm.setText(alarmSit.getTimeString());
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i = new Intent(mActivity, ActivityHealthAlarm.class);
		switch (v.getId()) {
			case R.id.healthalarm_sit:
				i.putExtra(ActivityHealthAlarm.EXTRA_ALARM_TYPE, ModelAlarm.ALARM_TYPE_SIT);
				break;
			case R.id.healthalarm_drink:
				i.putExtra(ActivityHealthAlarm.EXTRA_ALARM_TYPE, ModelAlarm.ALARM_TYPE_DRINK);
				break;
			default:
				return;
		}
		mActivity.startActivity(i);
	}

	@Override
	public void onHealthAlarmChanged() {
		// TODO Auto-generated method stub
		freshAlarmState();
	}

}
