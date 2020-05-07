package com.sczn.wearlauncher.location.bean;

import android.support.annotation.NonNull;

/**
 * Description:wifi数据
 * Created by Bingo on 2019/1/15.
 */
public class WifiDataBean implements Comparable<WifiDataBean> {
    private String wifiName;//wifi信息名字
    private String macAddress;//wifi的MAC地址
    private int signalStrength;//wifi信号强度

    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public int getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(int signalStrength) {
        this.signalStrength = signalStrength;
    }

    @Override
    public String toString() {
        return "WifiDataBean{" +
                "wifiName='" + wifiName + '\'' +
                ", macAddress='" + macAddress + '\'' +
                ", signalStrength='" + signalStrength + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull WifiDataBean o) {
        return o.signalStrength - signalStrength;
    }

    /**
     * 为了防止添加wifi的列表重复，复写equals方法
     *
     * @param obj
     * @return
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else {
            if (obj instanceof WifiDataBean) {
                WifiDataBean bean = (WifiDataBean) obj;
                if (bean.signalStrength == signalStrength) {
                    return bean.macAddress.equals(this.macAddress);
                }
            }
        }
        return false;
    }
}
