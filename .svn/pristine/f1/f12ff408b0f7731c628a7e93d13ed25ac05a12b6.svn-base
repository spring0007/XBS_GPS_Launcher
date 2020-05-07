package com.sczn.wearlauncher.ruisocket;


import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;

import com.sczn.wearlauncher.app.MxyLog;

import java.util.List;

public class WifiMgrUtil {
    //定义一个WifiManager对象  
    private WifiManager mWifiManager;
    //定义一个WifiInfo对象  
    private WifiInfo mWifiInfo;
    //扫描出的网络连接列表  
    private List<ScanResult> mWifiList;
    //网络连接列表  
    private List<WifiConfiguration> mWifiConfigurations;
    WifiLock mWifiLock;

    public WifiMgrUtil(Context context) {
        //取得WifiManager对象  
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //取得WifiInfo对象  
        mWifiInfo = mWifiManager.getConnectionInfo();

    }

    //打开wifi  
    public void openWifi() {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
    }

    //关闭wifi  
    public void closeWifi() {
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }

    // 检查当前wifi状态
    public int checkState() {
        return mWifiManager.getWifiState();
    }

    //锁定wifiLock  
    public void acquireWifiLock() {
        mWifiLock.acquire();
    }

    //解锁wifiLock  
    public void releaseWifiLock() {
        //判断是否锁定  
        if (mWifiLock.isHeld()) {
            mWifiLock.acquire();
        }
    }

    //创建一个wifiLock  
    public void createWifiLock() {
        mWifiLock = mWifiManager.createWifiLock("test");
    }

    //得到配置好的网络  
    public List<WifiConfiguration> getConfiguration() {
        return mWifiConfigurations;
    }

    //指定配置好的网络进行连接  
    public void connetionConfiguration(int index) {
        if (index > mWifiConfigurations.size()) {
            return;
        }
        //连接配置好指定ID的网络  
        mWifiManager.enableNetwork(mWifiConfigurations.get(index).networkId, true);
    }

    //指定配置好的网络进行连接
    public void connetionConfigurationnetid(int networkId) {

        //连接配置好指定ID的网络  
        mWifiManager.enableNetwork(networkId, true);
    }

    public void startScan() {
        mWifiManager.startScan();
        //得到扫描结果  
        mWifiList = mWifiManager.getScanResults();
        //得到配置好的网络连接  
        mWifiConfigurations = mWifiManager.getConfiguredNetworks();
    }

    public void logmWifiConfigurations() {
        for (int s = 0; s < mWifiConfigurations.size(); s++) {
            MxyLog.e("mWifiConfigurations", mWifiConfigurations.get(s).SSID);
        }
    }

    //得到网络列表  
    public List<ScanResult> getWifiList() {
        return mWifiList;
    }

    //查看扫描结果  
    public StringBuffer lookUpScan() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mWifiList.size(); i++) {
            sb.append("Index_" + new Integer(i + 1).toString() + ":");
            // 将ScanResult信息转换成一个字符串包
            // 其中把包括：BSSID、SSID、capabilities、frequency、level    
            sb.append((mWifiList.get(i)).toString()).append("\n");
        }
        return sb;
    }

    public String getMacAddress() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
    }

    public String getBSSID() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
    }

    public String getSSID() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getSSID();
    }

    public int getIpAddress() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
    }

    //得到连接的ID  
    public int getNetWordId() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
    }

    //得到wifiInfo的所有信息  
    public String getWifiInfo() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
    }

    //添加一个网络并连接  
    public void addNetWork(WifiConfiguration configuration) {
        int wcgId = mWifiManager.addNetwork(configuration);
        mWifiManager.enableNetwork(wcgId, true);
    }

    //断开指定ID的网络  
    public void disConnectionWifi(int netId) {
        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
    }

    //判定指定WIFI是否已经配置好,依据WIFI的地址BSSID,返回NetId
    public int IsConfiguration(String SSID) {
        mWifiConfigurations = mWifiManager.getConfiguredNetworks();
        for (int i = 0; i < mWifiConfigurations.size(); i++) {

            if (mWifiConfigurations.get(i).SSID.
                    equals("\"" + SSID + "\"")) {//地址相同
                MxyLog.e("mWifiConfigurations.get(i).networkId", " " + mWifiConfigurations.get(i).networkId);
                return mWifiConfigurations.get(i).networkId;
            }
        }
        return -1;
    }

    //添加指定WIFI的配置信息,原列表不存在此SSID
    public int AddWifiConfig(String ssid, String pwd, List<ScanResult> ScanResult) {
        int wifiId = -1;
        mWifiList = ScanResult;
        for (int i = 0; i < mWifiList.size(); i++) {
            ScanResult wifi = mWifiList.get(i);
            if (wifi.SSID.equals(ssid)) {
                MxyLog.i("AddWifiConfig", "equals");
                WifiConfiguration wifiCong = new WifiConfiguration();
                wifiCong.SSID = "\"" + wifi.SSID + "\"";//\"转义字符，代表"
                wifiCong.preSharedKey = "\"" + pwd + "\"";//WPA-PSK密码
                wifiCong.hiddenSSID = false;
                wifiCong.status = WifiConfiguration.Status.ENABLED;
                wifiId = mWifiManager.addNetwork(wifiCong);//将配置好的特定WIFI密码信息添加,添加完成后默认是不激活状态，成功返回ID，否则为-1  
                if (wifiId != -1) {
                    return wifiId;
                }
            }
        }
        return wifiId;
    }

    /**
     * 利用WifiConfiguration.KeyMgmt的管理机制，来判断当前wifi是否需要连接密码
     *
     * @param wifiManager
     * @return true：需要密码连接，false：不需要密码连接
     */
    public boolean checkIsCurrentWifiHasPassword(String capabilities) {
        if (capabilities.contains("WPA")) {
            return true;
        } else {
            return false;
        }


    }

    public void linknolockwifi(String ssid) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + ssid + "\"";
        // 没有密码

        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);


        mWifiManager.enableNetwork(mWifiManager.addNetwork(config), true);

    }
}