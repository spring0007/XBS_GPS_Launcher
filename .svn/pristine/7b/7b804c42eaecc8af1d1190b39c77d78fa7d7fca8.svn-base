package com.sczn.wearlauncher.card.healthalarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.absFragment;
import com.sczn.wearlauncher.sp.SPUtils;

public class FragmentDrink extends absFragment implements OnClickListener{
	
	public static FragmentDrink newInstance(){
		FragmentDrink fragment = new FragmentDrink();
		Bundle bdl = new Bundle();
		fragment.setArguments(bdl);
		return fragment;
	}

	private TextView mDrinkTarget;
	private TextView mDrinkCount;
	
	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_card_healthalarm_drink;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mDrinkTarget = findViewById(R.id.drink_target);
		mDrinkCount = findViewById(R.id.drink_count);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		findViewById(R.id.drink_much).setOnClickListener(this);
		findViewById(R.id.drink_little).setOnClickListener(this);
		findViewById(R.id.drink_setting).setOnClickListener(this);
		
		freshValue();
	}
	
	private void freshValue(){
		mDrinkTarget.setText(String.format(getString(R.string.drink_target),
				SPUtils.getIntParam(mActivity, FragmentDrinkSetting.KEY_DRINK_TARGET, FragmentDrinkSetting.DEFAULT_TARGET)));
		mDrinkCount.setText(String.format(getString(R.string.drink_count),
				SPUtils.getIntParam(mActivity, FragmentDrinkSetting.KEY_DRINK_ALREADY, 0)));
	}

	private void gotoFragmentSetting(){
		Intent i = new Intent(getActivity(), ActivityAlarmSetting.class);
		i.putExtra(ActivityAlarmSetting.ARG_ALATM_TYPE, ModelAlarm.ALARM_TYPE_DRINK);
		getActivity().startActivity(i);
	}
	
	private void drinkMuch(){
		findViewById(R.id.drink_much).setEnabled(false);
		
		final int drinked = SPUtils.getIntParam(mActivity, FragmentDrinkSetting.KEY_DRINK_ALREADY, 0);
		final int much = SPUtils.getIntParam(mActivity, FragmentDrinkSetting.KEY_DRINK_MUCH, FragmentDrinkSetting.DEFAULT_MUCH);
		SPUtils.setParam(mActivity, FragmentDrinkSetting.KEY_DRINK_ALREADY, Integer.valueOf(drinked + much));
		freshValue();
		
		findViewById(R.id.drink_much).setEnabled(true);
	}
	
	private void drinkLittle(){
		findViewById(R.id.drink_little).setEnabled(false);
		
		final int drinked = SPUtils.getIntParam(mActivity, FragmentDrinkSetting.KEY_DRINK_ALREADY, 0);
		final int little = SPUtils.getIntParam(mActivity, FragmentDrinkSetting.KEY_DRINK_LITTLE, FragmentDrinkSetting.DEFAULT_LITTLE);
		SPUtils.setParam(mActivity, FragmentDrinkSetting.KEY_DRINK_ALREADY, Integer.valueOf(drinked + little));
		freshValue();
		
		findViewById(R.id.drink_little).setEnabled(true);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.drink_much:
			drinkMuch();
			break;
		case R.id.drink_little:
			drinkLittle();
			break;
		case R.id.drink_setting:
			gotoFragmentSetting();
			break;
		default:
			break;
		}
	}

}
