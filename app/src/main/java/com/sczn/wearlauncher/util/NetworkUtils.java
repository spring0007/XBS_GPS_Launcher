package com.sczn.wearlauncher.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.sczn.wearlauncher.Config;
import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.app.MxyLog;

import java.io.IOException;
import java.util.List;

import static android.telephony.TelephonyManager.NETWORK_TYPE_LTE;

/**
 * @Bingo 网络查询类
 */
public class NetworkUtils {

    private static final String TAG = "NetworkUtils";

    public static final String NETWORK_TYPE_WIFI = "WIFI";
    public static final String NETWORK_TYPE_2G = "2G";
    public static final String NETWORK_TYPE_3G = "3G";
    public static final String NETWORK_TYPE_4G = "4G";
    public static final String NETWORK_TYPE_5G = "5G";
    public static final String NETWORK_TYPE_UNKNOWN = "UNKNOWN";

    /**
     * 判断是4G还是其他网络
     *
     * @param context
     * @return
     */
    public static boolean is4G(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null) {
            switch (networkINfo.getSubtype()) {
                case NETWORK_TYPE_LTE:
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    /**
     * @param mContext
     * @return
     */
    public static NetworkInfo getNetworkInfo(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return null;
        }
        return cm.getActiveNetworkInfo();
    }

    /**
     * 判断当前是否在WIFI网络下
     *
     * @param mContext
     * @return
     */
    public static boolean isWiFi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info != null && info.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 检测网络是否连接
     *
     * @param mContext
     * @return
     */
    public static boolean isNetWorkConnected(Context mContext) {
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        NetworkInfo mNetworkInfo = manager.getActiveNetworkInfo();
        return mNetworkInfo != null && mNetworkInfo.isAvailable();
    }

    /**
     * 检测网络连通性(是否能访问网络)
     *
     * @return
     */
    public static boolean isNetworkOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("ping -c 3 www.baidu.com");
            int exitValue = ipProcess.waitFor();
            MxyLog.i(TAG, "Process:" + exitValue);
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 网络是否可用
     *
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mConnectivityManager == null) {
            return false;
        }
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            if (mNetworkInfo.isAvailable()) {
                // 获取网络类型
                int netWorkType = mNetworkInfo.getType();
                if (netWorkType == ConnectivityManager.TYPE_WIFI) {
                    return true;
                } else if (netWorkType == ConnectivityManager.TYPE_MOBILE) {
                    return true;
                } else {
                    return true;
                }

            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 判断当前网络是否可用(6.0以上版本)
     * 实时
     *
     * @param context
     * @return
     */
    public static boolean isNetSystemUsable(Context context) {
        boolean isNetUsable = false;
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            NetworkCapabilities networkCapabilities =
                    manager.getNetworkCapabilities(manager.getActiveNetwork());
            if (networkCapabilities == null) {
                return false;
            }
            isNetUsable = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        }
        return isNetUsable;
    }

    /**
     * 获取连接的WiFi速度及信号强度
     *
     * @param context
     * @return
     */
    public static int getWiFiSignal(Context context) {
        int signal = 0;
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
            return signal;
        }
        WifiInfo info = wifiManager.getConnectionInfo();
        if (info.getBSSID() != null) {
            // 链接信号强度，5为获取的信号强度值在5以内
            // strength = WifiManager.calculateSignalLevel(info.getRssi(), 5);
            // 链接速度
            // int speed = info.getLinkSpeed();
            // 链接速度单位
            // String units = WifiInfo.LINK_SPEED_UNITS;
            // Wifi源名称
            // String ssid = info.getSSID();

            // 获得信号强度值(定义信号等级为5级)
            int level = info.getRssi();
            if (level < 0 && level >= -50) {
                signal = 5;                               // 信号最好
            } else if (level < -50 && level >= -70) {
                signal = 4;                               // 信号偏差
            } else if (level < -70 && level >= -80) {
                signal = 3;                               // 信号最差
            } else if (level < -80 && level >= -100) {
                signal = 2;                               // 信号最差
            } else {
                signal = 1;                               // 信号最差
            }
        }
        Log.d(TAG, "WiFi强度-->>: " + signal);
        return signal;
    }

    /**
     * 获取当前4G卡的信号强度
     *
     * @param context
     * @return
     */
    public synchronized static int getMobileSignal(Context context) {
        int signal = 0;
        int dbm = 0;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return signal;
        }
        List<CellInfo> cellInfoList;
        cellInfoList = tm.getAllCellInfo();
        if (null != cellInfoList) {
            for (CellInfo cellInfo : cellInfoList) {
                if (cellInfo instanceof CellInfoGsm) {
                    CellSignalStrengthGsm cellSignalStrengthGsm = ((CellInfoGsm) cellInfo).getCellSignalStrength();
                    dbm = cellSignalStrengthGsm.getDbm();
                } else if (cellInfo instanceof CellInfoCdma) {
                    CellSignalStrengthCdma cellSignalStrengthCdma = ((CellInfoCdma) cellInfo).getCellSignalStrength();
                    dbm = cellSignalStrengthCdma.getDbm();
                } else if (cellInfo instanceof CellInfoWcdma) {
                    CellSignalStrengthWcdma cellSignalStrengthWcdma = ((CellInfoWcdma) cellInfo).getCellSignalStrength();
                    dbm = cellSignalStrengthWcdma.getDbm();
                } else if (cellInfo instanceof CellInfoLte) {
                    CellSignalStrengthLte cellSignalStrengthLte = ((CellInfoLte) cellInfo).getCellSignalStrength();
                    dbm = cellSignalStrengthLte.getDbm();
                }
            }
            // 定义信号等级为
            if (dbm < 0 && dbm >= -85) {
                signal = 4;                               // 信号满格4格
            } else if (dbm < -85 && dbm >= -95) {
                signal = 3;                               // 信号偏差3格
            } else if (dbm < -95 && dbm >= -105) {
                signal = 2;                               // 信号最差2格
            } else if (dbm < -105 && dbm >= -115) {
                signal = 1;                               // 信号最差1格
            } else {
                signal = 0;                               // 信号最差0个或者无法获取
            }
        }
        Config.gmsSignal = dbm;
        Log.d(TAG, "SIM强度-->>: dbm = " + dbm + "信号格" + signal);
        return signal;
    }

    /**
     * 获取信号等级
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static int getSignalLevel(Context context) {
        int signal = 0;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return signal;
        }
        List<CellInfo> cellInfoList;
        cellInfoList = tm.getAllCellInfo();
        if (null != cellInfoList) {
            for (CellInfo cellInfo : cellInfoList) {
                if (cellInfo instanceof CellInfoGsm) {
                    CellSignalStrengthGsm cellSignalStrengthGsm = ((CellInfoGsm) cellInfo).getCellSignalStrength();
                    signal = cellSignalStrengthGsm.getLevel();
                } else if (cellInfo instanceof CellInfoCdma) {
                    CellSignalStrengthCdma cellSignalStrengthCdma =
                            ((CellInfoCdma) cellInfo).getCellSignalStrength();
                    signal = cellSignalStrengthCdma.getLevel();
                } else if (cellInfo instanceof CellInfoWcdma) {
                    CellSignalStrengthWcdma cellSignalStrengthWcdma =
                            ((CellInfoWcdma) cellInfo).getCellSignalStrength();
                    signal = cellSignalStrengthWcdma.getLevel();
                } else if (cellInfo instanceof CellInfoLte) {
                    CellSignalStrengthLte cellSignalStrengthLte = ((CellInfoLte) cellInfo).getCellSignalStrength();
                    signal = cellSignalStrengthLte.getLevel();
                }
            }
        }
        return signal;
    }

    /**
     *
     */
    public static void clearWifi() {
        WifiManager wifiManager = (WifiManager) LauncherApp.getAppContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
            return;
        }
        List<WifiConfiguration> wifiConfigs = wifiManager.getConfiguredNetworks();
        //忘记所有wifi密码
        for (WifiConfiguration wifiConfig : wifiConfigs) {
            MxyLog.e(TAG, "SSID = " + wifiConfig.SSID + " netId = " + String.valueOf(wifiConfig.networkId));
            wifiManager.removeNetwork(wifiConfig.networkId);
        }
        wifiManager.saveConfiguration();
    }

    /**
     * @param mContext
     * @return
     */
    public static String getNetworkType(Context mContext) {
        String strNetworkType = "";
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return strNetworkType;
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = NETWORK_TYPE_WIFI;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();
                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = NETWORK_TYPE_2G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = NETWORK_TYPE_3G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = NETWORK_TYPE_4G;
                        break;
                    default:
                        // TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = NETWORK_TYPE_3G;
                        } else {
                            strNetworkType = _strSubTypeName.toUpperCase();
                        }
                        break;
                }
            }
        }
        return strNetworkType;
    }

    /**
     * 判断是否包含SIM卡
     *
     * @return 状态
     */
    public static boolean isHasSimCard(Context context) {
        TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telMgr == null) {
            return false;
        }
        int simState = telMgr.getSimState();
        boolean result = true;
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                result = false; // 没有SIM卡
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                result = false;
                break;
        }
        return result;
    }
}
