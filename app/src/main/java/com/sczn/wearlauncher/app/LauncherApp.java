package com.sczn.wearlauncher.app;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.L;
import com.sczn.wearlauncher.clock.ClockSkinUtil;
import com.sczn.wearlauncher.db.DbMgr;
import com.sczn.wearlauncher.socket.WaterSocketManager;

import org.litepal.LitePal;

import cn.com.waterworld.alarmclocklib.app.AlarmClockApp;

public class LauncherApp extends Application {

    public static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        appInit();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        L.writeLogs(false);
        ImageLoader.getInstance().init(config);

        LitePal.initialize(this);
        AlarmClockApp.init(this);

        DbMgr.init(appContext);
        WaterSocketManager.getInstance();

        /**
         * 本地crash log打印
         */
        LauncherCrashHandler.getInstance().init(appContext);
    }

    public static Context getAppContext() {
        return appContext;
    }

    private void appInit() {
        ClockSkinUtil.initAllClockIndex();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        // WaterSocketManager.getInstance().unInitSocketService(appContext);
    }
}
