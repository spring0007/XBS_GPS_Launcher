package com.sczn.wearlauncher.app;

import android.net.NetworkInfo;
import android.os.BatteryManager;

/**
 * Description:
 * Created by Bingo on 2019/1/21.
 */
public class AppConfig {

    private final String TAG = "AppConfig";
    private static AppConfig helper;

    /**
     * 单例
     *
     * @return
     */
    public static AppConfig getInstance() {
        if (null == helper) {
            synchronized (AppConfig.class) {
                if (null == helper) {
                    helper = new AppConfig();
                }
            }
        }
        return helper;
    }

    private int gsmLevel = -1;//获得手机信号
    private int batteryLevel = 0;//电量
    private int batteryState = BatteryManager.BATTERY_STATUS_UNKNOWN;//电量状态

    // 是否处于低电量状态
    private boolean isLowBatteryState = false;
    //最近的 同步标志
    private int syncState = -2;
    // 获取 wtoken 状态
    private int wTokenState = -2;
    // 开机是否出现过 4G
    private Boolean haveLteEver;
    // GPS 定位状态
    private int mGpsLocationState = -1;
    //最近的 NetworkInfo
    private NetworkInfo mNetworkInfo;

    public int getGsmLevel() {
        return gsmLevel;
    }

    public void setGsmLevel(int gsmLevel) {
        this.gsmLevel = gsmLevel;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public int getBatteryState() {
        return batteryState;
    }

    public void setBatteryState(int batteryState) {
        this.batteryState = batteryState;
    }

    public void setSyncState(int syncState) {
        this.syncState = syncState;
    }

    public int getSyncState() {
        return syncState;
    }

    public void setWTokenState(int wTokenState) {
        this.wTokenState = wTokenState;
    }

    public int getWTokenState() {
        return wTokenState;
    }

    public Boolean getHaveLteEver() {
        return haveLteEver;
    }

    public void setHaveLteEver(Boolean haveLteEver) {
        this.haveLteEver = haveLteEver;
    }

    public int getGpsLocationState() {
        return mGpsLocationState;
    }

    public void setGpsLocationState(int state) {
        this.mGpsLocationState = state;
    }

    public boolean getLowBatteryState() {
        return isLowBatteryState;
    }

    public void setLowBatteryState(boolean isLowBatteryState) {
        this.isLowBatteryState = isLowBatteryState;
    }

    public void setNetworkInfo(NetworkInfo info) {
        mNetworkInfo = info;
    }

    public NetworkInfo getNetworkInfo() {
        return mNetworkInfo;
    }

}
