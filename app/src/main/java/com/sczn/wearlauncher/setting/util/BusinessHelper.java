package com.sczn.wearlauncher.setting.util;

import android.content.Context;
import android.media.AudioManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.sp.SysKey;


public class BusinessHelper {
    private static final String TAG = BusinessHelper.class.getSimpleName();

    public static boolean canShutdown(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final int simState = tm.getSimState();
        if (simState == TelephonyManager.SIM_STATE_ABSENT || simState == TelephonyManager.SIM_STATE_UNKNOWN) {
            return true;
        }
        final int shutdownEnable = Settings.System.getInt(context.getContentResolver(), SysKey.WATCH_DISABLE_POWEROFF_MODE, 0);
        if (shutdownEnable == 1) {
            return false;
        } else {
            return true;
        }
    }

    public static void requestAudioFocus(Context context) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        int resultMusic = mAudioManager.requestAudioFocus(null,
                android.media.AudioManager.STREAM_MUSIC, android.media.AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

        int resultAlarm = mAudioManager.requestAudioFocus(null,
                android.media.AudioManager.STREAM_ALARM, android.media.AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

        if (resultMusic == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            MxyLog.d(TAG, "resultMusic 申请到权限了！！");
        } else {
            MxyLog.d(TAG, "resultMusic 没有申请到权限了！！ result : " + resultMusic);
        }

        if (resultAlarm == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            MxyLog.d(TAG, "resultAlarm 申请到权限了！！");
        } else {
            MxyLog.d(TAG, "resultAlarm 没有申请到权限了！！ result : " + resultAlarm);
        }
    }
}
