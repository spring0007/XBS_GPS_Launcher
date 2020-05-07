package com.sczn.wearlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.base.util.LogFile;
import com.sczn.wearlauncher.location.LocationCallBackHelper;
import com.sczn.wearlauncher.sp.SPKey;
import com.sczn.wearlauncher.sp.SPUtils;
import com.sczn.wearlauncher.task.util.TaskInfoParse;

/**
 * Description:每分钟系统时间监听
 * Created by Bingo on 2019/3/2.
 */
public class TimeChangeReceiver extends BroadcastReceiver {
    private final String TAG = getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals("refresh_background_service")) {
            MxyLog.d(TAG, "refresh_background_service");
            /**
             * 自动上传位置信息
             */
            checkLocation();

            /**
             * 检测上课模式
             */
            TaskInfoParse.checkIsClassModel(1);
        }
    }

    /**
     * 自动上传位置信息
     */
    private void checkLocation() {
        try {
            long current = System.currentTimeMillis();
            long lastTime = (long) SPUtils.getParam(SPKey.LOCATION_LAST_UPLOAD_TIME, 0L);
            String c = (String) SPUtils.getParam(SPKey.LOCATION_GPS_CYCLE_KEY, "3600");//默认一小时定位一次
            int cycle = Integer.parseInt(c);
            long intervalTime = cycle * 1000;
            MxyLog.d(TAG, "定位上传位置:" + intervalTime);
            if (current - lastTime >= intervalTime) {
                LogFile.logCatWithTimeWithThread(">>>>>>>>>默认一小时需要定位上传位置一次.");
                LocationCallBackHelper.getInstance().startLocationAndUpload(LauncherApp.getAppContext());
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
}
