package com.sczn.wearlauncher.setting.util;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import com.sczn.wearlauncher.app.MxyLog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class WifiAdminBiz {

    private final static String TAG = WifiAdminBiz.class.getSimpleName();

    private static WifiAdminBiz wifiAdminBiz;
    private WifiManager mWifiManager;

    private boolean isConnected = false;

    private String lastSsidFromPush;

    private String pwdFromPush = "";

    private String ssidFromPush = "";

    private WifiChangeReceiver receiver = null;

    private WeakReference<Context> mContext;

    public static WifiAdminBiz getInstance(Context context) {
        if (null == wifiAdminBiz) {
            synchronized (WifiAdminBiz.class) {
                if (null == wifiAdminBiz) {
                    wifiAdminBiz = new WifiAdminBiz(context);
                }
            }
        }
        return wifiAdminBiz;
    }

    public WifiAdminBiz(Context context) {
        mContext = new WeakReference<>(context);
        this.mWifiManager = (WifiManager) mContext.get().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public WifiManager getWifiManager() {
        return mWifiManager;
    }


    public void openWifi() {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
    }


    public void closeWifi() {
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }


    public void disconnect() {
        if (mWifiManager != null) {
            mWifiManager.disconnect();
        }
    }


    public boolean isConnected() {
        if (!mWifiManager.isWifiEnabled()) {
            isConnected = false;
        }
        return isConnected;
    }


    @SuppressLint("WrongConstant")
    public void checkNetCardState() {
        if (mWifiManager.getWifiState() == 0) {
            MxyLog.i("tag_taoyunwatch", "网卡正在关闭");
        } else if (mWifiManager.getWifiState() == 1) {
            MxyLog.i("tag_taoyunwatch", "网卡已经关闭");
        } else if (mWifiManager.getWifiState() == 2) {
            MxyLog.i("tag_taoyunwatch", "网卡正在打开");
        } else if (mWifiManager.getWifiState() == 3) {
            MxyLog.i("tag_taoyunwatch", "网卡已经打开");
        } else {
            MxyLog.i("tag_taoyunwatch", "..没有获取到状态---_---");
        }
    }


    public void scan() {
        mWifiManager.startScan();
    }


    public List<ScanResult> getScanResult() {
        return mWifiManager.getScanResults();
    }


    public int addNetwork(WifiConfiguration wcg) {
        int wcgID = mWifiManager.addNetwork(wcg);
        mWifiManager.saveConfiguration();
        boolean flag = mWifiManager.enableNetwork(wcgID, true);
        isConnected = flag;

        MxyLog.i(TAG, "connect success? " + flag + "    wcgID =" + wcgID);

        return wcgID;
    }


    public void connectWifi(String ssid, String pwd) {
        if (pwd == null) {
            pwd = "";
        }

        List<ScanResult> mWifiList = mWifiManager.getScanResults();
        this.ssidFromPush = ssid;
        this.pwdFromPush = pwd;

        MxyLog.d(TAG, "wifiBiz.isConnected() =" + isConnected());
        MxyLog.d(TAG, "wifiBiz.lastSsidFromPush =" + lastSsidFromPush);

        onReceiveNewNetworks(mWifiList, ssidFromPush, pwdFromPush);
        lastSsidFromPush = ssidFromPush;
    }


    private void onReceiveNewNetworks(List<ScanResult> mWifiList, String ssidFromPush, String pwdFromPush) {
        List<String> passableWifiSSID = new ArrayList<>();
        MxyLog.d(TAG, "============onReceiveNewNetworks============");

        if (ssidFromPush == null || pwdFromPush == null)
            return;
        for (ScanResult result : mWifiList) {
            MxyLog.d(TAG, "result.SSID : " + result.SSID);
            if ((result.SSID).contains(ssidFromPush))
                passableWifiSSID.add(result.SSID);
        }

        if (mWifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLED) {
            registerReceiver();
            mWifiManager.setWifiEnabled(true);
        } else {
            synchronized (this) {
                MxyLog.d(TAG, "============onReceiveNewNetworks============" + ssidFromPush + "/" + pwdFromPush);
                WifiConfiguration config = WifiConfigurationAdmin.createWifiWpaInfo(ssidFromPush, pwdFromPush);
                addNetwork(config);
            }
        }
    }

    private void registerReceiver() {
        if (mContext != null && mContext.get() != null) {
            this.receiver = new WifiChangeReceiver();
            IntentFilter filter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
            mContext.get().registerReceiver(receiver, filter);
        }
    }

    private void unregisterReceiver() {
        if (mContext != null && mContext.get() != null && receiver != null) {
            mContext.get().unregisterReceiver(receiver);
            mContext = null;
            receiver = null;
        }
    }

    /**
     * WIFI状态变化,触发
     */
    private class WifiChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                if (WifiManager.WIFI_STATE_ENABLED == wifiState) {
                    synchronized (this) {
                        WifiConfiguration config = WifiConfigurationAdmin.createWifiWpaInfo(ssidFromPush, pwdFromPush);
                        MxyLog.d(TAG, "ssid = " + ssidFromPush + ",pwd = " + pwdFromPush);
                        addNetwork(config);
                    }
                    unregisterReceiver();
                }
            }
        }
    }
}
