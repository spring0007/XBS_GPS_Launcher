package com.sczn.wearlauncher;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class OperationService extends IntentService {

    public static final String AGR_OPRATION_TYPE = "opration";

    //sensor data save operation
    public static final int TYPE_INIT_SENSOR = 0;
    public static final int TYPE_SAVA_SENSOR = 1;
    public static final int TYPE_RESET_DATA = 2;

    //notification operation
    public static final int NTF_ADD_PHONE = 11;
    public static final int NTF_ADD_WATCH = 12;
    public static final int NTF_USB_PLUGOUT = 13;

    //global value
    public static final int GLOBAL_INIT_STEP_TODAY = 21;

    public static final String EXTRA_NTF_PHONE = "ntf_phone";
    public static final String EXTRA_NTF_WATCH = "ntf_watch";


    private PowerManager mPowerManager;
    private WakeLock mWakeLock;

    public OperationService() {
        super("DbOprationService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getSimpleName());
        mWakeLock.acquire(10 * 1000);//10秒
    }

    @Override
    public void onDestroy() {
        if (mWakeLock != null && mWakeLock.isHeld()) {
            mWakeLock.release();
        }
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
