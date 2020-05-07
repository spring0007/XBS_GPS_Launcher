package com.sczn.wearlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.sczn.wearlauncher.Config;
import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.event.RefreshWifiSignalEvent;
import com.sczn.wearlauncher.player.PlayManager;
import com.sczn.wearlauncher.player.SoundPoolUtil;
import com.sczn.wearlauncher.services.SosService;
import com.sczn.wearlauncher.socket.WaterSocketManager;
import com.sczn.wearlauncher.socket.command.CommandResultCallback;
import com.sczn.wearlauncher.socket.command.gprs.post.LK;
import com.sczn.wearlauncher.util.MoreFastEvent;
import com.sczn.wearlauncher.util.NetworkUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Description:网络变化广播
 * Created by Bingo on 2019/1/17.
 */
public class NetWorkReceiver extends BroadcastReceiver {

    private static final String TAG = "NetWorkReceiver";
    public static final String ACTION_KEY_EVENT = "com.toycloud.tcwatchlib.ACTION_KEY_EVENT";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case ConnectivityManager.CONNECTIVITY_ACTION:
                    if (NetworkUtils.isNetworkAvailable(LauncherApp.getAppContext())) {
                        MxyLog.w(TAG, "网络变化广播:当前网络可用");
                        MoreFastEvent.getInstance().event(10000, Config.net_change_time, new MoreFastEvent.onEventCallBackWithTimeListener() {
                            @Override
                            public void onCallback(long eventTime) {
                                Config.net_change_time = eventTime;
                                lk();
                            }
                        });
                    }
                    break;
                case WifiManager.RSSI_CHANGED_ACTION:
                case WifiManager.WIFI_STATE_CHANGED_ACTION:
                case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION:
                    EventBus.getDefault().postSticky(new RefreshWifiSignalEvent());
                    break;
                case TelephonyManager.ACTION_PHONE_STATE_CHANGED:
                    /**
                     * 电话状态监听
                     */
                    String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                    if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                        MxyLog.d(TAG, ">>EXTRA_STATE_IDLE");
                        Config.isPhoneRingState = false;
                    } else if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                        MxyLog.d(TAG, ">>EXTRA_STATE_RINGING");
                        Config.isPhoneRingState = true;
                        PlayManager.getInstance().stop();
                        SoundPoolUtil.release();
                    } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                        MxyLog.d(TAG, ">>EXTRA_STATE_OFFHOOK");
                        Config.isPhoneRingState = true;
                    }
                    break;
                case Intent.ACTION_SCREEN_ON:
                    MxyLog.w(TAG, "ACTION_SCREEN_ON:ACTION_SCREEN_ON");
                    MoreFastEvent.getInstance().event(10000, Config.screen_on_change_time, new MoreFastEvent.onEventCallBackWithTimeListener() {
                        @Override
                        public void onCallback(long eventTime) {
                            Config.screen_on_change_time = eventTime;
                            lk();
                        }
                    });
                    break;
                case ACTION_KEY_EVENT:
                    MxyLog.d(TAG, "ACTION_KEY_EVENT:ACTION_KEY_EVENT");
                    Intent sosIntent = new Intent(LauncherApp.getAppContext(), SosService.class);
                    sosIntent.putExtra("sosType", "hardware_sos");
                    LauncherApp.getAppContext().startService(sosIntent);
                    break;
            }
        }
    }

    /**
     * 心跳检测
     */
    public static void lk() {
        MoreFastEvent.getInstance().event(30000, Config.lk_time, new MoreFastEvent.onEventCallBackWithTimeListener() {
            @Override
            public void onCallback(long eventTime) {
                Config.lk_time = eventTime;
                WaterSocketManager.getInstance().send(new LK(Config.step, 0, Config.battery, new CommandResultCallback() {
                    @Override
                    public void onSuccess(String baseObtain) {
                        MxyLog.i(TAG, "login>>心跳包检测onSuccess");
                    }

                    @Override
                    public void onFail() {
                        MxyLog.i(TAG, "login>>心跳包检测onFail");
                    }
                }));
            }
        });
    }
}
