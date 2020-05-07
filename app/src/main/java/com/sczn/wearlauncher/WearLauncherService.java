package com.sczn.wearlauncher;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;

import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.base.AbsBroadcastReceiver;
import com.sczn.wearlauncher.base.util.NetWorkUtil;
import com.sczn.wearlauncher.base.util.SysServices;
import com.sczn.wearlauncher.card.healthalarm.UtilHealthAlarm;
import com.sczn.wearlauncher.notification.UtilNotification;
import com.sczn.wearlauncher.sp.SPUtils;
import com.sczn.wearlauncher.status.util.RaiseScreenUtil;

import java.util.Calendar;

public class WearLauncherService extends Service {

    private final static String TAG = WearLauncherService.class.getSimpleName();

    public static void startInstance(Context context) {
        Intent i = new Intent(NotificationListenerService.SERVICE_INTERFACE);
        i.setPackage(context.getPackageName());
        context.startService(i);
    }

    private LauncherServiceReceiver mLauncherServiceReceiver;
    // private SportMgrUtil mSensorMgrUtil;
    private UtilHealthAlarm mUtilHealthAlarm;
    private RaiseScreenUtil mRaiseScreenUtil;
    private UtilNotification mUtilNotification;

    @Override
    public void onCreate() {
        MxyLog.d(TAG, "onCreate--" + Calendar.getInstance().getTime() + "--" + Calendar.getInstance().getTimeInMillis());
        super.onCreate();

        SPUtils.setParam(LauncherApp.appContext, NetWorkUtil.SHARE_KEY_RESETAPN, false);
        mLauncherServiceReceiver = new LauncherServiceReceiver();
        mLauncherServiceReceiver.register(this);

        // mSensorMgrUtil = SportMgrUtil.getInstances();
        // mSensorMgrUtil.startMgr(this);

        mRaiseScreenUtil = RaiseScreenUtil.getInstance();
        mRaiseScreenUtil.startMgr();

        mUtilHealthAlarm = UtilHealthAlarm.getInstance();
        mUtilHealthAlarm.startMgr(this);

        mUtilNotification = UtilNotification.getInstance();
        mUtilNotification.startMgr(this);

        MxyLog.d(TAG, "onCreate----finish--" + Calendar.getInstance().getTime() + "--" + Calendar.getInstance().getTimeInMillis());
    }

    @Override
    public void onDestroy() {
        MxyLog.d(TAG, "onDestroy--" + Calendar.getInstance().getTime() + "--" + Calendar.getInstance().getTimeInMillis());

        mLauncherServiceReceiver.unRegister(this);

        // mSensorMgrUtil.stopMgr(this);

        mRaiseScreenUtil.stopMgr();

        mUtilHealthAlarm.stopMgr(this);

        mUtilNotification.stopMgr(this);

        MxyLog.d(TAG, "onDestroy----finish--" + Calendar.getInstance().getTime() + "--" + Calendar.getInstance().getTimeInMillis());
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private class LauncherServiceReceiver extends AbsBroadcastReceiver {

        public static final String SETTING_KEY_WIFI_STATE = "sczn_wifi_state";
        public static final String SETTING_KEY_WIFI_OFF_BY_SCREEN = "destroy_screen_without_internet";

        @Override
        public IntentFilter getIntentFilter() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            return filter;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                MxyLog.d(TAG, "action is " + action);
                if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                    //stopSensorListener();
                    //gotoHome();
                    storeWifiState(context);
                } else if (action.equals(Intent.ACTION_SCREEN_ON)) {
                    //openSensorHandle.sendEmptyMessageDelayed(0, 5 * 1000);
                    restoreWifiState(context);
                } else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
                    NetWorkUtil.checkNetWorkMobile();
                }
            }
        }

        /**
         * @param context
         */
        private void storeWifiState(Context context) {
            if (0 == (Settings.System.getInt(context.getContentResolver(), SETTING_KEY_WIFI_OFF_BY_SCREEN, 0))) {
                return;
            }
            final WifiManager wm = SysServices.getWfMgr(context);
            switch (wm.getWifiState()) {
                case WifiManager.WIFI_STATE_ENABLED:
                case WifiManager.WIFI_STATE_ENABLING:
                    Settings.System.putInt(context.getContentResolver(), SETTING_KEY_WIFI_STATE, WifiManager.WIFI_STATE_ENABLED);
                    wm.setWifiEnabled(false);
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                case WifiManager.WIFI_STATE_DISABLED:
                case WifiManager.WIFI_STATE_UNKNOWN:
                default:
                    Settings.System.putInt(context.getContentResolver(), SETTING_KEY_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
                    break;
            }
        }

        /**
         * @param context
         */
        private void restoreWifiState(Context context) {
            if (0 == (Settings.System.getInt(context.getContentResolver(), SETTING_KEY_WIFI_OFF_BY_SCREEN, 0))) {
                return;
            }
            if (WifiManager.WIFI_STATE_ENABLED == Settings.System.getInt(context.getContentResolver()
                    , SETTING_KEY_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED)) {
                SysServices.getWfMgr(context).setWifiEnabled(true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
