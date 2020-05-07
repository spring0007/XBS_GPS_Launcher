package com.sczn.wearlauncher.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.PowerManager;
import android.provider.Settings;

import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.base.util.MxyToast;
import com.sczn.wearlauncher.base.util.StringUtils;
import com.sczn.wearlauncher.event.RefreshBatteryEvent;
import com.toycloud.tcwatchlib.TcWatchDevice;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Description:需要系统配合处理的方法
 * Created by Bingo on 2019/3/13.
 */
public class SystemPermissionUtil {

    /**
     * 恢复出厂设置
     */
    public static void reset() {
        MxyToast.showShort(LauncherApp.getAppContext(), "恢复出厂设置");
    }

    /**
     * 重启
     */
    public static void reboot() {
        try {
            PowerManager pm = (PowerManager) LauncherApp.getAppContext().getSystemService(Context.POWER_SERVICE);
            if (pm != null) {
                pm.reboot("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开volte开关
     */
    public static void openVolte() {
        MxyToast.showShort(LauncherApp.getAppContext(), "打开volte开关");
    }

    /**
     * 关机
     *
     * @param mContext
     */
    public static void shutdown(Context mContext) {
        /*Intent intent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
        intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);*/
        TcWatchDevice.shutdown(mContext);
    }

    /**
     * 打开系统的gps高精度定位开关,高耗能
     */
    public static void openGpsSwitch() {
        try {
            LocationMode(LauncherApp.getAppContext(), Settings.Secure.LOCATION_MODE_HIGH_ACCURACY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭系统的gps高精度定位开关.
     */
    public static void closeGpsSwitch() {
        try {
            LocationMode(LauncherApp.getAppContext(), Settings.Secure.LOCATION_MODE_OFF);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * GPS模式更改
     *
     * @param context
     * @param mode
     */
    private static void LocationMode(Context context, int mode) {
        Intent intent = new Intent("com.android.settings.location.MODE_CHANGING");
        int currentMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_OFF);
        intent.putExtra("CURRENT_MODE", currentMode);
        intent.putExtra("NEW_MODE", mode);
        context.sendBroadcast(intent, android.Manifest.permission.WRITE_SECURE_SETTINGS);
        Settings.Secure.putInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE, mode);
    }

    /**
     * 跳转到拨号盘
     */
    public static void callButton() {
        Intent dialIntent = new Intent(Intent.ACTION_CALL_BUTTON);//跳转到拨号界面
        dialIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        LauncherApp.getAppContext().startActivity(dialIntent);
    }

    /**
     * 直接调用短信接口发短信
     *
     * @param phoneNumber
     * @param message
     */
    public static void sendSMS(String phoneNumber, String message) {
        if (!NetworkUtils.isHasSimCard(LauncherApp.getAppContext())) {
            MxyLog.e(MxyLog.TAG, "no has sim card");
            return;
        }
        // 获取短信管理器
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        // 拆分短信内容（手机短信长度限制）
        List<String> divideContents = smsManager.divideMessage(message);
        try {
            for (String text : divideContents) {
                smsManager.sendTextMessage(phoneNumber, null, "" + text, null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行拨打电话
     *
     * @param number
     */
    public static void doCalling(String number) {
        MxyLog.d("doCalling", "call to number.." + number);
        MxyToast.showShort(LauncherApp.getAppContext(), "拨打" + number);
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri data = Uri.parse("tel:" + number);
        intent.setData(data);
        LauncherApp.getAppContext().startActivity(intent);
    }

    /**
     * 单项聆听,静默拨打电话出去.
     *
     * @param number
     */
    public static void oneWayDoCalling(String number) {
        MxyLog.d("oneWayDoCalling", "call to number.." + number);
        MxyToast.showShort(LauncherApp.getAppContext(), "单项聆听");

        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri data = Uri.parse("tel:" + number);
        intent.setData(data);
        LauncherApp.getAppContext().startActivity(intent);
    }

    /**
     * 保存白名单数据
     *
     * @param data 白名单: 1501347107,1501347107,1501347107,1501347107
     */
    public static void saveWhitelistData(String data) {
        // MxyToast.showShort(LauncherApp.getAppContext(), "保存白名单数据");
    }

    /**
     * 紧急通话
     *
     * @param data 紧急通话: 1501347107,1501347107,1501347107,1501347107
     */
    public static void saveSosData(String data) {
        // MxyToast.showShort(LauncherApp.getAppContext(), "紧急通话");
    }

    /**
     * 保存单向聆听
     *
     * @param data 单向聆听: 1501347107,1501347107,1501347107,1501347107
     */
    public static void saveOneWayListenerData(String data) {
        // MxyToast.showShort(LauncherApp.getAppContext(), "保存单向聆听");
    }

    /**
     * 设置系统时区,需要关闭自动更新时区
     *
     * @param timeZone
     */
    public static void setTimeZone(String timeZone) {
        try {
            //开启自动更新时区
            //Settings.Global.putInt(LauncherApp.getAppContext().getContentResolver(), Settings.Global.AUTO_TIME_ZONE, 1);

            //关闭自动更新时区
            Settings.Global.putInt(LauncherApp.getAppContext().getContentResolver(), Settings.Global.AUTO_TIME_ZONE, 0);
            final Calendar now = Calendar.getInstance();
            TimeZone tz = TimeZone.getTimeZone(timeZone);
            now.setTimeZone(tz);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置语言
     * <p>
     * 0:英文
     * 1:简体中文
     * 3:葡萄牙
     * 4:西班牙
     * 5:德文
     * 8:越南语
     * 7.土耳其语
     * 9.俄罗斯语
     * 10.法语
     * 注意：不是所以项目都支持着 10 种语言
     * <p>
     * 需要系统签名
     *
     * @param language
     */
    public static void setLanguage(String language) {
        //目前只支持中文和英文
        Locale locale;
        if (!StringUtils.isEmpty(language) && language.equals("1")) {
            locale = Locale.CHINA;//中文
        } else {
            locale = Locale.ENGLISH;//英文
        }
        Class amnClass;
        try {
            amnClass = Class.forName("android.app.ActivityManagerNative");
            Object amn;
            Configuration config;

            // amn = ActivityManagerNative.getDefault();
            Method methodGetDefault = amnClass.getMethod("getDefault");
            methodGetDefault.setAccessible(true);
            amn = methodGetDefault.invoke(amnClass);

            // config = amn.getConfiguration();
            Method methodGetConfiguration = amnClass.getMethod("getConfiguration");
            methodGetConfiguration.setAccessible(true);
            config = (Configuration) methodGetConfiguration.invoke(amn);

            // config.userSetLocale = true;
            Class configClass = config.getClass();
            Field f = configClass.getField("userSetLocale");
            f.setBoolean(config, true);

            // set the locale to the new value
            config.locale = locale;

            // amn.updateConfiguration(config);
            Method methodUpdateConfiguration = amnClass.getMethod("updateConfiguration", Configuration.class);
            methodUpdateConfiguration.setAccessible(true);
            methodUpdateConfiguration.invoke(amn, config);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取电池状态
     */
    @SuppressLint("NewApi")
    public static void getBtyState() {
        int status = -1;
        int percent = 1;
        boolean isCharging = false;

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = LauncherApp.getAppContext().registerReceiver(null, filter);
        if (batteryStatus != null) {
            status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;

            int rawLevel = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1); //获得当前电量
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1); //获得总电量
            if (rawLevel >= 0 && scale > 0) {
                percent = (int) ((rawLevel * 100f) / scale);
            }
        }
        MxyLog.d(MxyLog.TAG, "isCharging:" + isCharging);
        MxyLog.d(MxyLog.TAG, "status:" + status);
        MxyLog.d(MxyLog.TAG, "percent:" + percent);

        EventBus.getDefault().post(new RefreshBatteryEvent(percent, status));
    }
}
