package com.sczn.wearlauncher.status.view;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.AbsBroadcastReceiver;
import com.sczn.wearlauncher.app.MxyLog;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.AttributeSet;

public class BatteryIcon extends StatusIconWithText {
	
	public static final int BATTERY_STATE_NORMAL = 0;
	public static final int BATTERY_STATE_CHARGING= 1;
	public static final int BATTERY_STATE_FULLCHARGING = 2;
	
	private BatteryReceiver mBatteryReceiver;
	private int mState = BATTERY_STATE_NORMAL;
	
	public BatteryIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mBatteryReceiver = new BatteryReceiver();
	}
	
	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		MxyLog.d(this, "onAttachedToWindow");
		super.onAttachedToWindow();
		mBatteryReceiver.register(getContext());
	}
	
	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		MxyLog.d(this, "onDetachedFromWindow");
		mBatteryReceiver.unRegister(getContext());
		super.onDetachedFromWindow();
	}
	
	private void freshState(int state,int level){
		boolean isCharging = false;
		
		switch (state) {
			case BATTERY_STATE_FULLCHARGING:
				mText.setText(R.string.status_battery_full);
				isCharging = true;
				break;
			case BATTERY_STATE_CHARGING:
				//mText.setText(R.string.status_battery_charging);
				mText.setText(getResources().getString(R.string.status_battery_normal,level));
				isCharging = true;
				break;
			case BATTERY_STATE_NORMAL:
				mText.setText(getResources().getString(R.string.status_battery_normal,level));
				break;
			default:
				break;
		}
		
		if(level > 80){
			mIcon.setImageResource(isCharging ? R.drawable.statu_icon_battery_charging5 : R.drawable.statu_icon_battery_5);
		}else if(level > 60){
			mIcon.setImageResource(isCharging ? R.drawable.statu_icon_battery_charging4 : R.drawable.statu_icon_battery_4);
		}else if(level > 40){
			mIcon.setImageResource(isCharging ? R.drawable.statu_icon_battery_charging3 : R.drawable.statu_icon_battery_3);
		}else if(level > 20){
			mIcon.setImageResource(isCharging ? R.drawable.statu_icon_battery_charging2 : R.drawable.statu_icon_battery_2);
		}else{
			mIcon.setImageResource(isCharging ? R.drawable.statu_icon_battery_charging1 : R.drawable.statu_icon_battery_1);
		}
	}

	
	public void startFresh(){
		//mBatteryReceiver.register(getContext());
	}
	
	public void stopFresh(){
		//mBatteryReceiver.unRegister(getContext());
	}
	
	private class BatteryReceiver extends AbsBroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())){
				//MxyLog.d(this, "level=" + intent.getIntExtra("level", 0));
				final int level = intent.getIntExtra("level", 0);
				switch (intent.getIntExtra("status", 0)) {
	                case BatteryManager.BATTERY_STATUS_FULL:
	                	freshState(BATTERY_STATE_FULLCHARGING,level);
	                    break;
	                case BatteryManager.BATTERY_STATUS_CHARGING:
	                	freshState(BATTERY_STATE_CHARGING,level);
	                    break;
	                case BatteryManager.BATTERY_STATUS_UNKNOWN:
	                case BatteryManager.BATTERY_STATUS_DISCHARGING:
	                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
	                	freshState(BATTERY_STATE_NORMAL,level);
	                    break;
	            }
			}
		}
		@Override
		public IntentFilter getIntentFilter() {
			// TODO Auto-generated method stub
			IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
			return filter;
		}
		
	}

}
