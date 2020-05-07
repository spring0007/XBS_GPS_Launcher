package com.sczn.wearlauncher.notification;

import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;

/*
 * 手机推送通知，整合同一应用
 */
public class ModelPhonePkgNotification implements Parcelable{
	
	private String pkgName;
	private String AppName;
	private ArrayList<ModlePhoneMessage> mMessageList;

	public ModelPhonePkgNotification() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ModelPhonePkgNotification(ModlePhoneMessage message) {
		this();
		// TODO Auto-generated constructor stub
		addMessage(message);
		
		setPkgName(message.getPackageName());
		setAppName(message.getAppName());
	}

	public String getPkgName() {
		return pkgName;
	}

	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}

	public String getAppName() {
		return AppName;
	}

	public void setAppName(String appName) {
		AppName = appName;
	}
	
	public void setmMessageList(ArrayList<ModlePhoneMessage> mMessageList) {
		this.mMessageList = mMessageList;
	}

	public ArrayList<ModlePhoneMessage> getmMessageList() {
		return mMessageList;
	}

	public void addMessage(ModlePhoneMessage message){
		if(mMessageList == null){
			mMessageList = new ArrayList<ModlePhoneMessage>();
		}
		mMessageList.add(0, message);
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(pkgName);
		dest.writeString(AppName);
		dest.writeTypedList(mMessageList);
	}
	
	public static final Parcelable.Creator<ModelPhonePkgNotification> CREATOR = new Creator<ModelPhonePkgNotification>() {
		@Override
        public ModelPhonePkgNotification createFromParcel(Parcel source) {
        	ModelPhonePkgNotification ModelPhonePkgNotification = new ModelPhonePkgNotification();
        	ModelPhonePkgNotification.pkgName = source.readString();
        	ModelPhonePkgNotification.AppName = source.readString();

        	final ArrayList<ModlePhoneMessage> mMessges = new ArrayList<ModlePhoneMessage>();
			source.readTypedList(mMessges, ModlePhoneMessage.CREATOR);
			ModelPhonePkgNotification.setmMessageList(mMessges);
			
            return ModelPhonePkgNotification;
        }

        public ModelPhonePkgNotification[] newArray(int size) {
            return new ModelPhonePkgNotification[size];
        }
    };
}
