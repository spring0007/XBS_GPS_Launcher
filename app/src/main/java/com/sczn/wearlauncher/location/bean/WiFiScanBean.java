package com.sczn.wearlauncher.location.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Description:
 * Created by Bingo on 2019/5/14.
 */
public class WiFiScanBean implements Parcelable {
    private String ssid;
    private int rssi;

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public WiFiScanBean() {

    }

    protected WiFiScanBean(Parcel in) {
        ssid = in.readString();
        rssi = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ssid);
        dest.writeInt(rssi);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WiFiScanBean> CREATOR = new Creator<WiFiScanBean>() {
        @Override
        public WiFiScanBean createFromParcel(Parcel in) {
            return new WiFiScanBean(in);
        }

        @Override
        public WiFiScanBean[] newArray(int size) {
            return new WiFiScanBean[size];
        }
    };
}
