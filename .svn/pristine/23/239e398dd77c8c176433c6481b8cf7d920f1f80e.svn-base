package com.sczn.wearlauncher.card.healthalarm;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public abstract class HealthAlarmActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(getLayoutId());
		initView();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initData();
	}

	protected abstract int getLayoutId();
	protected abstract void initView();
	protected abstract void initData();
}
