package com.sczn.wearlauncher.location;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.location.bean.WiFiScanBean;
import com.sczn.wearlauncher.location.bean.WifiDataBean;
import com.sczn.wearlauncher.location.impl.OnLocationChangeListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Description:WiFi定位
 * Created by Bingo on 2019/1/15.
 */
public class WiFiLocationHelper {
    private final String TAG = "WiFiLocationHelper";
    private static WiFiLocationHelper helper;

    private Timer wifiScanTimer;

    /**
     * 单例
     *
     * @return
     */
    public static WiFiLocationHelper getInstance() {
        if (null == helper) {
            synchronized (WiFiLocationHelper.class) {
                if (null == helper) {
                    helper = new WiFiLocationHelper();
                }
            }
        }
        return helper;
    }

    /**
     * 获取当前已连接的WiFi信息
     *
     * @param mContext
     * @return
     */
    public WiFiScanBean getConnectedWifiName(final Context mContext) {
        WiFiScanBean bean = null;
        WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
            MxyLog.i(TAG, "getConnectedWifiData: wifiManager = null");
            return null;
        }
        WifiInfo info = wifiManager.getConnectionInfo();
        if (info != null) {
            bean = new WiFiScanBean();
            bean.setSsid(verifySSID(info.getSSID()));
            bean.setRssi(info.getRssi());
        }
        return bean;
    }

    /**
     * 获取当前已连接的WiFi信息
     *
     * @param mContext
     * @return
     */
    public WifiInfo getConnectedWifiData(final Context mContext) {
        WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
            MxyLog.i(TAG, "getConnectedWifiData: wifiManager = null");
            return null;
        }
        WifiInfo info = wifiManager.getConnectionInfo();
        if (info != null) {
            return info;
        }
        return null;
    }

    /**
     * 获取WiFi信息
     *
     * @param mContext
     * @return
     */
    private void getWifiData(final Context mContext, OnLocationChangeListener<List<WifiDataBean>> onLocationChangeListener) {
        WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
            MxyLog.i(TAG, "getWifiData: wifiManager = null");
            onLocationChangeListener.fail();
            return;
        }
        List<ScanResult> list = wifiManager.getScanResults();
        List<WifiDataBean> data = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                WifiDataBean bean = new WifiDataBean();
                bean.setSignalStrength(list.get(i).level);
                bean.setMacAddress(list.get(i).BSSID);
                bean.setWifiName(list.get(i).SSID);
                data.add(bean);
            }
            Collections.sort(data);
            if (data.size() > 0) {
                onLocationChangeListener.success(data);
            } else {
                onLocationChangeListener.fail();
            }
        } else {
            onLocationChangeListener.fail();
        }
    }

    /**
     * 扫描并获取WiFi信息
     * 扫描WiFi4秒才去获取WiFi列表信息
     *
     * @param mContext
     * @param onLocationChangeListener
     */
    public void scanWifiData(final Context mContext, final OnLocationChangeListener<List<WifiDataBean>> onLocationChangeListener) {
        final WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
            MxyLog.i(TAG, "scan: wifiManager = null");
            onLocationChangeListener.fail();
            return;
        }
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        wifiManager.startScan();
        if (wifiScanTimer != null) {
            wifiScanTimer.cancel();
            wifiScanTimer = null;
        }
        wifiScanTimer = new Timer();
        wifiScanTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                getWifiData(mContext, onLocationChangeListener);
            }
        }, 4000);
    }

    /**
     *
     */
    public void stopScanWifi() {
        if (wifiScanTimer != null) {
            wifiScanTimer.cancel();
            wifiScanTimer = null;
        }
    }

    /**
     * 去掉WiFi名称的""号
     *
     * @param mSsid
     * @return
     */
    private static String verifySSID(String mSsid) {
        String ssid = mSsid;
        if (ssid != null && !ssid.isEmpty() && ssid.contains("\"")) {
            ssid = ssid.replace("\"", "");
        }
        return ssid;
    }

    /**
     * 解析成合适服务器上传的格式
     *
     * @param mac
     * @param rssi
     * @param name
     * @return
     */
    public static String wifiFormat(String mac, int rssi, String name) {
        return mac + "," + rssi + "," + verifySSID(name);
    }

    /**
     * gprs协议解析成合适服务器上传的格式
     *
     * @param data
     * @return
     */
    public static String wifiFormat(List<WifiDataBean> data) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < (data.size() >= 5 ? 5 : data.size()); i++) {
            builder.append(data.get(i).getWifiName()).append(",")
                    .append(data.get(i).getMacAddress()).append(",")
                    .append(data.get(i).getSignalStrength()).append(",");
        }
        if (builder.length() > 1) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }
}
