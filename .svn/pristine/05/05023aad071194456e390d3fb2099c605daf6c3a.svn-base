package com.sczn.wearlauncher.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.sczn.wearlauncher.Config;
import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.base.util.StringUtils;
import com.sczn.wearlauncher.location.LocationCallBackHelper;
import com.sczn.wearlauncher.location.bean.LocationInfo;
import com.sczn.wearlauncher.location.impl.OnLocationChangeListener;
import com.sczn.wearlauncher.socket.WaterSocketManager;
import com.sczn.wearlauncher.socket.command.CommandResultCallback;
import com.sczn.wearlauncher.socket.command.gprs.post.AL;
import com.sczn.wearlauncher.sp.SPKey;
import com.sczn.wearlauncher.sp.SPUtils;
import com.sczn.wearlauncher.util.MoreFastEvent;
import com.sczn.wearlauncher.util.NetworkUtils;
import com.sczn.wearlauncher.util.SosUtil;
import com.sczn.wearlauncher.util.SystemPermissionUtil;

/**
 * SOS呼叫,包含重播的机制
 */
public class SosService extends Service {

    private static final String TAG = SosService.class.getSimpleName();

    private long more_event_time;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String notificationType = intent.getStringExtra("sosType");
            if (notificationType != null && notificationType.equals("hardware_sos")) {
                //SOS报警
                MoreFastEvent.getInstance().event(5000, more_event_time, new MoreFastEvent.onEventCallBackWithTimeListener() {
                    @Override
                    public void onCallback(long eventTime) {
                        more_event_time = eventTime;
                        sendSosMsgToServer();
                        doOnExecuteSos();
                    }
                });
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 向服务器发送sos报警消息
     */
    private void sendSosMsgToServer() {
        LocationCallBackHelper.getInstance().startBS(LauncherApp.getAppContext(), new OnLocationChangeListener<LocationInfo>() {
            @Override
            public void success(LocationInfo info) {
                AL al = new AL(info, "00010000", new CommandResultCallback() {
                    @Override
                    public void onSuccess(String baseObtain) {
                        MxyLog.d(TAG, "SOS报警onSuccess");
                    }

                    @Override
                    public void onFail() {
                        MxyLog.d(TAG, "SOS报警onFail");
                    }
                });
                WaterSocketManager.getInstance().send(al);
            }

            @Override
            public void fail() {
                MxyLog.w(TAG, "SOS报警,获取定位信息失败了~");
            }
        });
    }

    /**
     * 执行SOS流程
     */
    private void doOnExecuteSos() {
        if (!NetworkUtils.isHasSimCard(this)) {
            MxyLog.d(TAG, "没有SIM卡");
            return;
        }
        String sos1 = (String) SPUtils.getParam(SPKey.SOS_1, "");
        if (StringUtils.isEmpty(sos1)) {
            MxyLog.d(TAG, "没有设置SOS号码.");
            return;
        }
        String sos2 = (String) SPUtils.getParam(SPKey.SOS_2, "");
        String sos3 = (String) SPUtils.getParam(SPKey.SOS_3, "");

        /**
         * 判断是否需要发送SOS短信
         */
        String sos_switch = (String) SPUtils.getParam(SPKey.SOS_SMS_SWITCH, "0");
        if (sos_switch != null && sos_switch.equals("1")) {
            SystemPermissionUtil.sendSMS(sos1, Config.IMEI + "触发了SOS报警~");
        }
        doCallSOS1(sos1, sos2, sos3);
    }

    /**
     * SOS轮播流程
     * 开始拨打第一个号码，如果失败了,判断则判断拨打第二个号码,或者第三个号码
     *
     * @param sos1
     * @param sos2
     * @param sos3
     */
    private void doCallSOS1(final String sos1, final String sos2, final String sos3) {
        MxyLog.d(TAG, "start call first number :" + sos1);
        SosUtil.getInstance().call(sos1, new SosUtil.OnSosCallBack() {
            @Override
            public void success() {
                SosUtil.getInstance().end();
            }

            @Override
            public void fail() {
                if (!StringUtils.isEmpty(sos2)) {
                    doCallSOS2(sos2, sos3);
                } else {
                    doCallSOS3(sos3);
                }
            }
        });
    }

    /**
     * SOS轮播流程
     * 开始拨打第二个号码，如果失败了,判断则判断拨打第三个号码,或者结束流程
     *
     * @param sos2
     * @param sos3
     */
    private void doCallSOS2(final String sos2, final String sos3) {
        MxyLog.d(TAG, "start call second number :" + sos2);
        SosUtil.getInstance().call(sos2, new SosUtil.OnSosCallBack() {
            @Override
            public void success() {
                SosUtil.getInstance().end();
            }

            @Override
            public void fail() {
                if (!StringUtils.isEmpty(sos3)) {
                    doCallSOS3(sos3);
                } else {
                    SosUtil.getInstance().end();
                }
            }
        });
    }

    /**
     * SOS轮播流程
     * 开始拨打第三个号码，如果失败了结束流程
     *
     * @param sos3
     */
    private void doCallSOS3(String sos3) {
        MxyLog.d(TAG, "start call third number :" + sos3);
        SosUtil.getInstance().call(sos3, new SosUtil.OnSosCallBack() {
            @Override
            public void success() {
                SosUtil.getInstance().end();
            }

            @Override
            public void fail() {
                SosUtil.getInstance().end();
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SosUtil.getInstance().end();
    }
}
