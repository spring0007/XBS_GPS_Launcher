package com.sczn.wearlauncher.setting.util;

import android.net.wifi.WifiConfiguration;
import android.text.TextUtils;

import com.sczn.wearlauncher.app.MxyLog;


public class WifiConfigurationAdmin {

    private static String TAG = WifiConfigurationAdmin.class.getSimpleName();


    public static WifiConfiguration createWifiNoPassInfo(String SSID, String password) {

        MxyLog.d(TAG, "into nopass  SSID = " + SSID + "  Password = " + password + " Type = ");
        WifiConfiguration config = new WifiConfiguration();
        config = createWifiInfo(config, SSID, password);

        config.wepKeys[0] = "\"" + "" + "\"";
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        config.wepTxKeyIndex = 0;
        config.status = WifiConfiguration.Status.ENABLED;
        MxyLog.d(TAG, "out nopass  SSID = " + SSID + "  Password = " + password + " Type = ");
        return config;
    }


    public static WifiConfiguration createWifiWepInfo(String SSID, String password) {

        MxyLog.d(TAG, "into WIFICIPHER_WEP   SSID = " + SSID + "  Password = " + password);
        WifiConfiguration config = new WifiConfiguration();
        config = createWifiInfo(config, SSID, password);
        config.hiddenSSID = true;
        config.wepKeys[0] = "\"" + password + "\"";
        config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        config.wepTxKeyIndex = 0;
        MxyLog.d(TAG, "out WIFICIPHER_WEP   SSID = " + SSID + "  Password = " + password);
        return config;

    }


    public static WifiConfiguration createWifiWpaInfo(String SSID, String password) {
        if (TextUtils.isEmpty(password)) {

            MxyLog.d(TAG, "密码为空");
            MxyLog.d(TAG, "createWifiWpaInfo SSID = " + SSID + " | Password = " + password);

            WifiConfiguration config = new WifiConfiguration();

            config.allowedAuthAlgorithms.clear();
            config.allowedGroupCiphers.clear();
            config.allowedKeyManagement.clear();
            config.allowedPairwiseCiphers.clear();
            config.allowedProtocols.clear();
            config.SSID = "\"" + SSID + "\"";

            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

            return config;
        } else {

            MxyLog.d(TAG, "密码不为空");
            MxyLog.d(TAG, "createWifiWpaInfo SSID = " + SSID + " | Password = " + password);

            WifiConfiguration config = new WifiConfiguration();

            config = createWifiInfo(config, SSID, password);

            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;

            MxyLog.d(TAG, "out WIFICIPHER_WPA SSID = " + SSID + " | Password = " + password);

            return config;
        }
    }


    private static WifiConfiguration createWifiInfo(WifiConfiguration config, String SSID, String password) {
        MxyLog.d(TAG, "into wifi热点连接配置   SSID = " + SSID + "  Password = " + password);

        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";
        config.priority = 0;

        MxyLog.d(TAG, "into wifi热点连接配置   config.SSID = " + config.SSID + "  Password = " + password);

        return config;
    }
}
