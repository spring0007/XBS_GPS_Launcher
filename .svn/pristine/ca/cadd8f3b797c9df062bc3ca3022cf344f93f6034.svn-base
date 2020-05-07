package com.sczn.wearlauncher.notification;

import android.os.Parcel;
import android.os.Parcelable;
import android.service.notification.StatusBarNotification;

public class ModelNotification implements Parcelable {

    public static final int NTF_TYPE_UNKNOW = 0;
    public static final int NTF_TYPE_WATCH = 1;
    public static final int NTF_TYPE_PHONE = 2;

    private StatusBarNotification mWatchNtf;        //手表系统通知
    private ModelPhonePkgNotification mPhoneNtf;        //手机推送通知
    private int ntfType;

    public ModelNotification() {
    }

    public ModelNotification(ModelPhonePkgNotification mPhoneNtf) {
        this();
        setPhoneNtf(mPhoneNtf);
    }

    public ModelNotification(StatusBarNotification mWatchNtf) {
        this();
        setWatchNtf(mWatchNtf);
    }

    public void setWatchNtf(StatusBarNotification ntf) {
        this.mWatchNtf = ntf;
        ntfType = NTF_TYPE_WATCH;
    }

    public void setPhoneNtf(ModelPhonePkgNotification ntf) {
        this.mPhoneNtf = ntf;
        ntfType = NTF_TYPE_PHONE;
    }

    public int getNtfType() {
        return ntfType;
    }

    public StatusBarNotification getWatchNtf() {
        if (NTF_TYPE_WATCH != ntfType) {
            throw new IllegalStateException("ntfType=" + ntfType + ", not has a StatusBarNotification");
        }
        return mWatchNtf;
    }

    public ModelPhonePkgNotification getPhoneNtf() {
        if (NTF_TYPE_PHONE != ntfType) {
            throw new IllegalStateException("ntfType=" + ntfType + ", not has a ModelPhonePkgNotification");
        }
        return mPhoneNtf;
    }

    public void addNewPhoneNtf(ModlePhoneMessage message) {
        if (mPhoneNtf == null) {
            mPhoneNtf = new ModelPhonePkgNotification();
        }
        mPhoneNtf.addMessage(message);
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeInt(ntfType);
        dest.writeParcelable(mWatchNtf, flags);
        dest.writeParcelable(mPhoneNtf, flags);
    }

    public static final Parcelable.Creator<ModelNotification> CREATOR = new Creator<ModelNotification>() {

        @Override
        public ModelNotification createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            ModelNotification ntf = new ModelNotification();
            ntf.ntfType = source.readInt();
            ntf.mWatchNtf = source.readParcelable(StatusBarNotification.class.getClassLoader());
            ntf.mPhoneNtf = source.readParcelable(ModelPhonePkgNotification.class.getClassLoader());
            return ntf;
        }

        @Override
        public ModelNotification[] newArray(int size) {
            // TODO Auto-generated method stub
            return new ModelNotification[size];
        }
    };
}
