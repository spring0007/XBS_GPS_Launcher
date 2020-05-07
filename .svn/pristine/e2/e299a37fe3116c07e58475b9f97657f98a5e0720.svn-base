package com.sczn.wearlauncher.notification;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import com.google.gson.Gson;
import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.OperationService;
import com.sczn.wearlauncher.base.AbsBroadcastReceiver;
import com.sczn.wearlauncher.app.MxyLog;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.service.notification.StatusBarNotification;

public class UtilNotification extends Observable{
	
	private static class Hold{
		private static UtilNotification instance = new UtilNotification();
	}
	
	public static UtilNotification getInstance(){
		return Hold.instance;
	}
	
	private ReceiverNotification mReceiverNotification;
	private ArrayList<ModelNotification> mNotificationList;
	private INotificationListen mListen;
	
	public UtilNotification(){
		mReceiverNotification = new ReceiverNotification();
	}
	public void startMgr(Context context){
		mReceiverNotification.register(context);
	}
	public void stopMgr(Context context){
		mReceiverNotification.unRegister(context);
	}
	
	public void setListen(INotificationListen listen){
		this.mListen = listen;
	}
	
	public void removeListen(INotificationListen listen){
		mListen = null;
	}
	
	public ArrayList<ModelNotification> getNotificationList(){
		if(mNotificationList == null){
			mNotificationList = new ArrayList<ModelNotification>();
		}
		return mNotificationList;
	}
	
	public synchronized void addWatchNotification(StatusBarNotification notification){	
		MxyLog.d(this, "addWatchNotification--" + notification.getPackageName());
		final int id = notification.getId();
		
		final int size = mNotificationList.size();
		for(int i=0; i < size; i++){
			final ModelNotification ntf = mNotificationList.get(i);
			if(ModelNotification.NTF_TYPE_WATCH == ntf.getNtfType() && id == ntf.getWatchNtf().getId()){
				removeNotification(i);
				addNewNotification(new ModelNotification(notification));
				return;
			}
		}
	
		addNewNotification(new ModelNotification(notification));
	}
	
	public synchronized void removeWatchNotification(StatusBarNotification notification){

		final int id = notification.getId();

		final int size = mNotificationList.size();
		for(int i=0; i < size; i++){
			final ModelNotification ntf = mNotificationList.get(i);
			if(ModelNotification.NTF_TYPE_WATCH == ntf.getNtfType() && id == ntf.getWatchNtf().getId()){
				removeNotification(i);
				return;
			}
		}
	
	}
	
	public synchronized void removeNotificationDetails(ModelPhonePkgNotification pkgNtf){
		final int size = mNotificationList.size();
		for(int i=0; i < size; i++){
			final ModelNotification ntf = mNotificationList.get(i);
			if(ModelNotification.NTF_TYPE_PHONE == ntf.getNtfType() && ntf.getPhoneNtf().equals(pkgNtf)){
				removeNotification(i);
				return;
			}
		}
	}
	
	public synchronized void addPhoneNotification(String message){
		
		ModlePhoneMessage notification = null;
		 try {
            Gson gson = new Gson();
            notification = gson.fromJson(message, ModlePhoneMessage.class);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }finally{
        	if(notification == null){
        		return;
        	}
        }
		final int size = mNotificationList.size();
		final String pkgName = notification.getPackageName();
		for(int i=0; i < size; i++){
			final ModelNotification ntf = mNotificationList.get(i);
			if(ModelNotification.NTF_TYPE_PHONE == ntf.getNtfType()){
				if(pkgName.equals(ntf.getPhoneNtf().getPkgName())){
					
					if(i == 0){
						ntf.getPhoneNtf().addMessage(notification);
						if(mListen != null){
							mListen.addNewDetail(pkgName, 0);
						}
						return;
					}

					removeNotification(i);
					ntf.addNewPhoneNtf(notification);
					if(mListen != null){
						mListen.addNewDetail(pkgName, 0);
					}
					addNewNotification(ntf);
					return;
				}
			}
		}
		addNewNotification(new ModelNotification(new ModelPhonePkgNotification(notification)));

	}
	
	private void addNewNotification(ModelNotification notification){
		//MxyLog.d(TAG, "addNewNotification" + "notification=" + notification.toString());
		final int oldSize = mNotificationList.size();
		mNotificationList.add(0, notification);
		if(mListen != null){
			if(oldSize == 0){
				mListen.freshAll();
			}else{
				mListen.addNew(0);
			}
		}
		setChanged();
		notifyObservers();
	}
	private void removeNotification(int i){
		MxyLog.d(this, "removeNotification" + "position=" + i);
		mNotificationList.remove(i);
		if(mListen != null){
			mListen.removePosition(i);
		}
		setChanged();
		notifyObservers();
	}
	
	public void clearNotificationList(){
		ArrayList<ModelNotification> tmpList = new ArrayList<ModelNotification>();
		for(ModelNotification ntf : mNotificationList){
			if(ModelNotification.NTF_TYPE_WATCH == ntf.getNtfType() && !ntf.getWatchNtf().isClearable()){
				tmpList.add(ntf);
			}
		}
		mNotificationList.clear();
		mNotificationList.addAll(tmpList);
		if(mListen != null){
			mListen.freshAll();
		}
		setChanged();
		notifyObservers();
	}
	
	public void removeUsbNotification() {
		// TODO Auto-generated method stub
		if(mNotificationList == null){
			mNotificationList = new ArrayList<ModelNotification>();
		}
		final ArrayList<ModelNotification> tepList = new ArrayList<ModelNotification>(mNotificationList);
		
		
		for (Iterator iterator = tepList.iterator(); iterator.hasNext(); ) {
			final ModelNotification ntf = (ModelNotification) iterator.next();
			if(ModelNotification.NTF_TYPE_WATCH == ntf.getNtfType()){
				String packageName = ntf.getWatchNtf().getPackageName();
				if( packageName.equals("android") || packageName.equals("com.android.systemui")){
					iterator.remove();
				}
			}
        }
		mNotificationList.clear();
		mNotificationList.addAll(tepList);
		if(mListen != null){
			mListen.freshAll();
		}
		setChanged();
		notifyObservers();
	}
	
	private void onReceivePhoneNotification(String message){
		
		final Intent i = new Intent(LauncherApp.appContext, OperationService.class);
		i.putExtra(OperationService.AGR_OPRATION_TYPE, OperationService.NTF_ADD_PHONE);
		i.putExtra(OperationService.EXTRA_NTF_PHONE, message);
		LauncherApp.appContext.startService(i);
		
		//addPhoneNotification(message);
	}
	private void onReceiveWatchNotification(StatusBarNotification ntf){
		final Intent i = new Intent(LauncherApp.appContext, OperationService.class);
		i.putExtra(OperationService.AGR_OPRATION_TYPE, OperationService.NTF_ADD_WATCH);
		i.putExtra(OperationService.EXTRA_NTF_WATCH, ntf);
		LauncherApp.appContext.startService(i);
		//addWatchNotification(ntf);
	}
	private void onUsbStateChanged(boolean isConnected){
		final Intent i = new Intent(LauncherApp.appContext, OperationService.class);
		i.putExtra(OperationService.AGR_OPRATION_TYPE, OperationService.NTF_USB_PLUGOUT);
		LauncherApp.appContext.startService(i);
		//removeUsbNotification();
	}
	
	private class ReceiverNotification extends AbsBroadcastReceiver{

		public static final String ACTION_PHONE_NOTIFICATION = "NotificTionMessage_from_telephone";
		public static final String ACTION_USB_CONNECTED = "android.hardware.usb.action.USB_STATE";
		public static final String ACTION_WATCH_NOTIFICATION = "com.sczn.s001_launcher.NOTIFICATION_LISTENER";
		public static final String EXTRA_NOTIFICATION = "s001";
		
		@Override
		public IntentFilter getIntentFilter() {
			// TODO Auto-generated method stub
			IntentFilter filter = new IntentFilter();
			filter.addAction(ACTION_PHONE_NOTIFICATION);
			filter.addAction(ACTION_USB_CONNECTED);
			filter.addAction(ACTION_WATCH_NOTIFICATION);
			return filter;
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(ACTION_PHONE_NOTIFICATION.equals(intent.getAction())){
				final String message = intent.getStringExtra("NotificTionMessage");
				if (message != null) {
					onReceivePhoneNotification(message);
	            }
				return;
			}
			
			if(ACTION_WATCH_NOTIFICATION.equals(intent.getAction())){
				final StatusBarNotification notification = intent.getParcelableExtra(EXTRA_NOTIFICATION);
				if(notification != null){
					onReceiveWatchNotification(notification);
				}
				return;
			}
			
			if(ACTION_USB_CONNECTED.equals(intent.getAction())){
				final boolean connected = intent.getExtras().getBoolean("connected");
				if(!connected){
					onUsbStateChanged(connected);
				}
				return;
			}
		}
		
	}
	
	public interface INotificationListen{
		public void removePosition(int position);
		public void addNew(int position);
		public void addNewDetail(String pkgName, int position);
		public void freshAll();
	}
}
