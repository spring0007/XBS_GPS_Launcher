package com.sczn.wearlauncher.base.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.app.MxyLog;

import java.net.HttpURLConnection;
import java.net.URL;

public class NetWorkUtil {
    private static final String TAG = NetWorkUtil.class.getSimpleName();

    private static final int NETWORK_NONE = 0;
    private static final int NETWORK_MOBILE = 1;
    private static final int NETWORK_WIFI = 2;

    private static final Uri CURRENT_APN_URI = Uri.parse("content://telephony/carriers/preferapn");

    public static final String SHARE_KEY_RESETAPN = "resetapn_already";

    public static void checkNetWorkMobile() {
        if (NETWORK_MOBILE != getNetWorkState(LauncherApp.appContext)) {
            return;
        }
        isNetWorkAvailableOfGet("http://www.baidu.com", new Comparable<Boolean>() {
            @Override
            public int compareTo(@NonNull Boolean available) {
                if (available) {
                    // TODO 设备访问Internet正常
                } else {
                    // TODO 设备无法访问Internet
                    resetAPN(LauncherApp.appContext);
                }
                return 0;
            }
        });
    }

    /**
     * @param context
     * @return
     */
    private static int getNetWorkState(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return NETWORK_NONE;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                return NETWORK_WIFI;
            } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                return NETWORK_MOBILE;
            }
        } else {
            return NETWORK_NONE;
        }
        return NETWORK_NONE;
    }

    /**
     * 检查互联网地址是否可以访问-使用get请求
     *
     * @param urlStr   要检查的url
     * @param callback 检查结果回调（是否可以get请求成功）{@see java.lang.Comparable<T>}
     */
    private static void isNetWorkAvailableOfGet(final String urlStr, final Comparable<Boolean> callback) {
        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (callback != null) {
                    callback.compareTo(msg.arg1 == 0);
                }
                return false;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    Connection conn = new Connection(urlStr);
                    Thread thread = new Thread(conn);
                    thread.start();
                    thread.join(3 * 1000); // 设置等待DNS解析线程响应时间为3秒
                    int resCode = conn.get(); // 获取get请求responseCode
                    msg.arg1 = resCode == 200 ? 0 : -1;
                } catch (Exception e) {
                    msg.arg1 = -1;
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }

        }).start();
    }

    /**
     * HttpURLConnection请求线程
     */
    private static class Connection implements Runnable {
        private String urlStr;
        private int responseCode;

        public Connection(String urlStr) {
            this.urlStr = urlStr;
        }

        public void run() {
            try {
                MxyLog.d(TAG, DateFormatUtil.getCurrTimeString(DateFormatUtil.HMS));
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                MxyLog.d(TAG, DateFormatUtil.getCurrTimeString(DateFormatUtil.HMS) + "--" + conn.getResponseCode());
                set(conn.getResponseCode());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public synchronized void set(int responseCode) {
            this.responseCode = responseCode;
        }

        public synchronized int get() {
            return responseCode;
        }
    }

    /**
     * No permission to write APN settings
     *
     * @param context
     */
    private static void resetAPN(Context context) {
        // ContentResolver resolver = context.getContentResolver();
        // ContentValues values = new ContentValues();
        // values.put("apn_id", 0);
        // resolver.update(CURRENT_APN_URI, values, null, null);
        // SPUtils.setParam(LauncherApp.appContext, SHARE_KEY_RESETAPN, true);
    }
}  
  
