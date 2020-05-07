package com.sczn.wearlauncher.status.util;

import com.sczn.wearlauncher.base.AbsBroadcastReceiver;

import android.app.Notification.Action;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class StatusUtil {
	private static class Hold{
		private static StatusUtil instance;
	}
	
	public static StatusUtil getInstance(){
		return Hold.instance;
	}
	
	private IModeListen mModeListen;
	
	
	private class StatusReceiver extends AbsBroadcastReceiver{
		private static final String SIM_STATE_ACTION = "android.intent.action.SIM_STATE_CHANGED";
		public static final String RINGER_MODE_CHANGED_ACTION = "android.media.RINGER_MODE_CHANGED";
		@Override
		public IntentFilter getIntentFilter() {
			// TODO Auto-generated method stub
			final IntentFilter filter = new IntentFilter();
			
			//情景模式
			filter.addAction(RINGER_MODE_CHANGED_ACTION);
			
			//电量状态
			filter.addAction(Intent.ACTION_BATTERY_CHANGED);
			
			//蓝牙开关
			filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
			
			//SIM卡状态
			filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
			filter.addAction(SIM_STATE_ACTION);
			
			return filter;
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			final String action = intent.getAction();
			if(RINGER_MODE_CHANGED_ACTION.equals(action)){
				if(mModeListen != null){
					mModeListen.onModeChange();
				}
			}
		}
		
	}
	
	public interface IModeListen{ public void onModeChange(); }
}
