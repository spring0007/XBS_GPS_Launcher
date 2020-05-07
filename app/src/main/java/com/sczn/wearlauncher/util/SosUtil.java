package com.sczn.wearlauncher.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.base.util.StringUtils;

/**
 * SOS拨打流程
 */
public class SosUtil {

    private final String TAG = "SosUtil";
    private final String ACTION_CALL_HANGUP = "com.toycloud.tcwatchlib.ACTION_CALL_HANGUP";
    private final String ACTION_CALL_CONNECTED = "com.toycloud.tcwatchlib.ACTION_CALL_CONNECTED";

    private PhoneStatusReceiver receiver;
    private boolean isRegister = false;
    private String number;
    private int callCount = 0;
    private final int reCallMaxCount = 2;//默认3次

    private static SosUtil sosUtil;

    public static SosUtil getInstance() {
        if (null == sosUtil) {
            synchronized (SosUtil.class) {
                if (null == sosUtil) {
                    sosUtil = new SosUtil();
                }
            }
        }
        return sosUtil;
    }

    /**
     * 拨打sos回调
     */
    private OnSosCallBack onSosCallBack;

    public interface OnSosCallBack {

        void success();

        void fail();
    }

    /**
     * 开始拨打sos号码流程
     *
     * @param number
     * @param onSosCallBack
     */
    public void call(String number, OnSosCallBack onSosCallBack) {
        this.onSosCallBack = onSosCallBack;
        this.number = number;
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_CALL_HANGUP);
        filter.addAction(ACTION_CALL_CONNECTED);
        receiver = new PhoneStatusReceiver();
        if (!isRegister) {
            LauncherApp.getAppContext().registerReceiver(receiver, filter);
            isRegister = true;
        }
        callCount = 0;
        reCall();
    }

    /**
     * 结束sos流程
     */
    public void end() {
        MxyLog.i(TAG, "sos call end.");
        if (receiver != null && isRegister) {
            try {
                LauncherApp.getAppContext().unregisterReceiver(receiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
            receiver = null;
            isRegister = false;
        }
        callCount = 0;
        onSosCallBack = null;
    }

    /**
     * 重播累计
     */
    private void reCall() {
        MxyLog.i(TAG, "reCall callCount:" + callCount);
        if (callCount > reCallMaxCount) {//重拨的max最大次数
            callCount = 0;
            if (onSosCallBack != null) {
                onSosCallBack.fail();
            }
            return;
        }
        if (!StringUtils.isEmpty(number)) {
            SystemPermissionUtil.doCalling(number);
            callCount += 1;
        }
    }

    private class PhoneStatusReceiver extends BroadcastReceiver {
        public static final String EXTRA_IS_CALLER_OR_CALLEE = "isCallerOrCallee";
        public static final String EXTRA_CALLER_OR_CALLEE_HANG_UP = "callerOrCalleeHangUp";
        public static final String EXTRA_RINGING_OR_CONNECTED = "ringingOrConnected";

        private boolean isCallerOrCallee = false;

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            isCallerOrCallee = intent.getBooleanExtra(EXTRA_IS_CALLER_OR_CALLEE, false);
            if (action != null && action.equals(ACTION_CALL_CONNECTED)) {
                MxyLog.w(TAG, "CallConnectedStateBroadCast Receiver"); //电话接通
                dealConnected();
            } else if (action != null && action.equals(ACTION_CALL_HANGUP)) {
                MxyLog.w(TAG, "CallHANGUPStateBroadCast Receiver"); //电话挂断
                dealHangup(intent);
            }
        }

        private void dealConnected() {
            MxyLog.w(TAG, "CallConnectedStateBroadCast Receiver isCallerOrCallee=" + isCallerOrCallee); //电话接通
        }

        private void dealHangup(Intent intent) {
            boolean isCallerOrCalleeHangUp = intent.getBooleanExtra(EXTRA_CALLER_OR_CALLEE_HANG_UP, false);
            boolean isRingingOrConnected = intent.getBooleanExtra(EXTRA_RINGING_OR_CONNECTED, false);
            if (!isRingingOrConnected) {
                MxyLog.i(TAG, "sos接通..sos重拨流程结束");
                if (onSosCallBack != null) {
                    onSosCallBack.success();
                    return;
                }
            }
            if (!isCallerOrCalleeHangUp && isRingingOrConnected) {
                MxyLog.i(TAG, "sos执行重拨...");
                reCall();
            } else {
                MxyLog.i(TAG, "接通或者主动挂断...");
                if (isCallerOrCalleeHangUp && isRingingOrConnected && isCallerOrCallee) {
                    //取消拨号
                    MxyLog.i(TAG, "取消拨号");
                }
                end();
            }
        }
    }
}
