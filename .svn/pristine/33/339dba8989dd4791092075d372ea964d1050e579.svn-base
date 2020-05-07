package com.sczn.wearlauncher.card;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.absFragment;
import com.sczn.wearlauncher.base.util.DateFormatUtil;
import com.sczn.wearlauncher.base.util.MxyToast;
import com.sczn.wearlauncher.card.geographic.ModelGeographic;
import com.sczn.wearlauncher.card.geographic.UtilGeographic;
import com.sczn.wearlauncher.card.geographic.UtilGeographic.IGeographicListen;

public class CardFragmentGeographic extends absFragment implements IGeographicListen{
	
public static final String ARG_IS_TMP = "is_tmp";
	
	public static CardFragmentGeographic newInstance(boolean isTmp) {
		CardFragmentGeographic fragment = new CardFragmentGeographic();
		Bundle bdl = new Bundle();
		bdl.putBoolean(ARG_IS_TMP, isTmp);
		fragment.setArguments(bdl);
		return fragment;

	}

	private boolean isTmp;
	private TextView mTime;
	private TextView mAtmosphere;
	private TextView mAltitude;
	private ProgressBar mAltitudeProgress;
	private ProgressBar mAtmosphereProgress;
	
	private Handler mHandler = new Handler();
	
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
		return R.layout.fragment_card_geographic;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mTime = findViewById(R.id.geographic_time);
		mAtmosphere = findViewById(R.id.geographic_atmosphere);
		mAltitude = findViewById(R.id.geographic_aititude);
		mAltitudeProgress = findViewById(R.id.aititude_progress);
		mAtmosphereProgress = findViewById(R.id.atmosphere_progress);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		mTime.setText(DateFormatUtil.getCurrTimeString(DateFormatUtil.YYYY_MM_DD_HM));
		mAtmosphere.setText(getString(R.string.geographic_atmosphere, 1000.0));
		mAltitude.setText(getString(R.string.geographic_altitude, 100.0));
		
		initState();
	}
	
	private void initState(){
		mAltitude.setVisibility(View.INVISIBLE);
		mAltitudeProgress.setVisibility(View.VISIBLE);
		
		mAtmosphere.setVisibility(View.INVISIBLE);
		mAtmosphereProgress.setVisibility(View.VISIBLE);
	}

	@Override
	protected void startFreshData() {
		// TODO Auto-generated method stub
		if(isTmp){
			return;
		}
		super.startFreshData();
		mHandler.postDelayed(new Runnable(){   

		    public void run() {   
		    	mHandler.removeCallbacksAndMessages(null);
		    	mTime.setText(DateFormatUtil.getCurrTimeString(DateFormatUtil.YYYY_MM_DD_HM));
		    	mHandler.postDelayed(this,1000);
		    }   

		 }, 1000);
		UtilGeographic.getInstance().startGetAltitude(this);
	}
	
	@Override
	protected void endFreshData() {
		// TODO Auto-generated method stub
		if(isTmp){
			return;
		}
		super.endFreshData();
		UtilGeographic.getInstance().stopGetAltitude();
		mHandler.removeCallbacksAndMessages(null);
	}

	@Override
	public void onAtmosphereFailed() {
		// TODO Auto-generated method stub
		
		//mAtmosphere.setVisibility(View.INVISIBLE);
		//mAtmosphereProgress.setVisibility(View.VISIBLE);
		//MxyToast.showShort(mActivity, R.string.pressure_bad_signal);
		mAtmosphere.setVisibility(View.VISIBLE);
		mAtmosphereProgress.setVisibility(View.INVISIBLE);
		mAtmosphere.setText(R.string.pressure_bad_signal);
	}

	@Override
	public void onAltitudeFailed() {
		// TODO Auto-generated method stub
		//mAltitude.setVisibility(View.INVISIBLE);
		//mAltitudeProgress.setVisibility(View.VISIBLE);
		//MxyToast.showShort(mActivity, R.string.pressure_bad_signal);
		
		mAltitude.setVisibility(View.VISIBLE);
		mAltitudeProgress.setVisibility(View.INVISIBLE);
		mAltitude.setText(R.string.pressure_bad_signal);
	}

	@Override
	public void onAtmosphere(ModelGeographic info) {
		// TODO Auto-generated method stub
		mAtmosphere.setVisibility(View.VISIBLE);
		mAtmosphereProgress.setVisibility(View.INVISIBLE);
		//mAtmosphere.setText(String.format("%.2f", info.getmAtmosphere().getPressure()));
		mAtmosphere.setText(String.format(info.getmAtmosphere().getPressure()));
		//MxyToast.showShort(mActivity, R.string.weather_get_success);
	}

	@Override
	public void onAltitude(double altitude) {
		// TODO Auto-generated method stub
		mAltitude.setVisibility(View.VISIBLE);
		mAltitudeProgress.setVisibility(View.INVISIBLE);
		mAltitude.setText(String.format("%.2f", altitude));
		//mAltitude.setText(String.valueOf(altitude));
	}
}
