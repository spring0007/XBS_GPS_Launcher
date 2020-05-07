package com.sczn.wearlauncher.card.healthalarm;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.absFragment;
import com.sczn.wearlauncher.base.util.MxyToast;
import com.sczn.wearlauncher.sp.SPUtils;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class FragmentDrinkSetting extends absFragment implements OnClickListener{
	
	public static FragmentDrinkSetting newInstance(){
		final FragmentDrinkSetting fragment = new FragmentDrinkSetting();
		return fragment;
	}
	
	public static final String KEY_DRINK_SETTED = "has_setted";
	public static final String KEY_DRINK_TARGET = "drink_ratget";
	public static final String KEY_DRINK_MUCH = "drink_much";
	public static final String KEY_DRINK_LITTLE = "drink_little";
	public static final String KEY_DRINK_ALREADY= "drink_already";
	
	public static final int DEFAULT_TARGET = 1200;
	public static final int DEFAULT_MUCH = 250;
	public static final int DEFAULT_LITTLE = 50;

	private TextView mDrinkMuch;
	private TextView mDrinkLittle;
	private TextView mDrinkTarget;
	private TextView mDrinkAlready;
	private TextView mReset;
	private TextView mClear;
	
	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_card_healthalarm_drink_setting;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mDrinkMuch = findViewById(R.id.drink_more);
		mDrinkLittle = findViewById(R.id.drink_little);
		mDrinkTarget = findViewById(R.id.drink_target);
		mDrinkAlready = findViewById(R.id.drink_already);
		mReset = findViewById(R.id.drink_setting_reset);
		mClear = findViewById(R.id.drink_setting_clear);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		mReset.setOnClickListener(this);
		mClear.setOnClickListener(this);
		
		freshValue();
	}
	
	private void freshValue(){
		mDrinkTarget.setText(String.valueOf(SPUtils.getIntParam(mActivity, KEY_DRINK_TARGET, DEFAULT_TARGET)));
		mDrinkMuch.setText(String.valueOf(SPUtils.getIntParam(mActivity, KEY_DRINK_MUCH, DEFAULT_MUCH)));
		mDrinkLittle.setText(String.valueOf(SPUtils.getIntParam(mActivity, KEY_DRINK_LITTLE, DEFAULT_LITTLE)));
		mDrinkAlready.setText(String.valueOf(SPUtils.getIntParam(mActivity, KEY_DRINK_ALREADY, 0)));
	}

	private void resetAll(){
		SPUtils.setParam(mActivity, KEY_DRINK_SETTED, Boolean.valueOf(false));
		SPUtils.setParam(mActivity, KEY_DRINK_TARGET, Integer.valueOf(DEFAULT_TARGET));
		SPUtils.setParam(mActivity, KEY_DRINK_MUCH, Integer.valueOf(DEFAULT_MUCH));
		SPUtils.setParam(mActivity, KEY_DRINK_LITTLE, Integer.valueOf(DEFAULT_LITTLE));
		SPUtils.setParam(mActivity, KEY_DRINK_ALREADY, Integer.valueOf(0));
		
		freshValue();
	}
	private void clearDrinked(){
		SPUtils.setParam(mActivity, KEY_DRINK_ALREADY, Integer.valueOf(0));
		mDrinkAlready.setText(String.valueOf(0));
	}
	
	private void savaAll(){
		try {
			SPUtils.setParam(mActivity, KEY_DRINK_TARGET, Integer.parseInt(mDrinkTarget.getText().toString()));
			SPUtils.setParam(mActivity, KEY_DRINK_MUCH, Integer.parseInt(mDrinkMuch.getText().toString()));
			SPUtils.setParam(mActivity, KEY_DRINK_LITTLE, Integer.parseInt(mDrinkLittle.getText().toString()));
			SPUtils.setParam(mActivity, KEY_DRINK_SETTED, Boolean.valueOf(true));
		} catch (Exception e) {
			// TODO: handle exception
			MxyToast.showShort(getActivity(), R.string.drink_setting_sava_failued);
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.drink_setting_reset:
				//resetAll();
				savaAll();
				break;
			case R.id.drink_setting_clear:
				clearDrinked();
				break;
			default:
				break;
		}
	}

}
