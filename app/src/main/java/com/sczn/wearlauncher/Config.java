package com.sczn.wearlauncher;

public class Config {

    /**
     * 电话状态
     */
    public static boolean isPhoneRingState = false;

    /**
     * 电量
     */
    public static int battery;

    /**
     * 信号强度
     */
    public static int gmsSignal;

    /**
     * 步数
     */
    public static int step;

    /**
     * 设备状态
     */
    public static String deviceState = "00000000";

    /**
     * IMEI
     */
    public final static String IMEI = /*Util.getIMEI(LauncherApp.getAppContext())*/"3920923009";


    //最后一次闹钟响起时间记录
    public static long alarm_ring_last_time = 0;
    //低电量上传,定义10分钟内不需要重复上传
    public static long upload_battery_last_time = 0;
    //网络变化去重
    public static long net_change_time = 0;
    //亮屏变化
    public static long screen_on_change_time = 0;
    //心跳包协议
    public static long lk_time = 0;
}
