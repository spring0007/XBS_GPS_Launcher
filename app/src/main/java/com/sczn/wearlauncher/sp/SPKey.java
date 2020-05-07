package com.sczn.wearlauncher.sp;

/**
 * Description:
 * Created by Bingo on 2019/1/24.
 */
public class SPKey {

    //后台服务器地址
    public static final String IP = "ip";
    public static final String PORT = "port";

    //是否登录,登录状态
    public static final String WATCHID = "watch_id";
    public static final String LOGINSTATUS = "login_status";

    //是否上课关闭
    public static final String ISCLOSEINCLASS = "is_close_in_class";

    //定位信息,并且记录上次定位的经度和纬度、最近定位成功的时间、最近定位成功的内容
    public static final String LOCATION = "location";//定位信息
    public static final String LOCATION_LNG = "location_lng";//上次定位的经度和纬度
    public static final String LOCATION_LAT = "location_lat";//上次定位的经度和纬度
    public static final String LOCATION_LAST_TIME = "location_last_time";//最近定位成功的时间
    public static final String LOCATION_LAST_INFO = "location_last_info";//最近定位成功的内容
    public static final String LOCATION_LAST_UPLOAD_TIME = "location_last_upload_time";//最近上传定位成功的时间
    public static final String LOCATION_GPS_CYCLE_KEY = "location_gps_cycle_key";//定位上传的周期

    //自动关机时间
    public static final String AUTO_SHUTDOWN_TIME = "auto_shutdown_time";
    //禁止关机
    public static final String IS_NO_SHUTDOWN = "is_no_shutdown";

    //app列表的样式
    public static final String APP_MENU_STYLE = "app_menu_style";//0横向,1网格

    //手表音量
    public static final String WATCH_VOLUME = "watch_volume";

    //设置中心号码,通过该手机号码可发送短信指令。同时终端的各种报警短信会发送到该号码的手机上面
    public static final String CENTER_NUMBER = "center_number";
    //设置终端短信控制密码,非中心号码发送短信指令给终端需添加此密码.
    public static final String PW_NUMBER = "pw_number";

    //开关设置
    public static final String LOW_SWITCH = "low_switch";
    public static final String SOS_SMS_SWITCH = "sos_sms_switch";
    public static final String REMOVE_SWITCH = "remove_switch";
    public static final String REMOVE_SMS_SWITCH = "remove_sms_switch";
    public static final String PEDO_SWITCH = "pedo_switch";

    //sos号码
    public static final String SOS_1 = "sos_1";
    public static final String SOS_2 = "sos_2";
    public static final String SOS_3 = "sos_3";
}
