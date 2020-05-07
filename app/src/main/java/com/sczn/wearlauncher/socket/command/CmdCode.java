package com.sczn.wearlauncher.socket.command;

/**
 * Description:通信协议号码
 * Created by Bingo on 2019/2/27.
 */
public class CmdCode {
    public static final int HEART = 0;              //心跳
    public static final int LOGIN = 1;              //登录
    public static final int LOCATION = 2;           //定位
    public static final int UPLOADSTEPS = 3;        //计步
    public static final int SHAKE = 5;              //摇一摇
    public static final int LINKMAN = 11;           //通讯录
    public static final int TASK = 12;              //课程表
    public static final int INTIMATE_SETTING = 13;  //贴心设置
    public static final int FENCE_INFO = 14;        //安全信息
    public static final int DAY_REMIND = 15;        //日程提醒
    public static final int SHUTDOWN = 21;          //远程关机
    public static final int TAKE_PHOTO = 22;        //远程拍照
    public static final int ONE_WAY_LISTENING = 23; //单向聆听

    public static final int SHUTDOWN_NTY = 100; //手表关机通知
    public static final int LOW_POWER = 103;    //手表低电压提醒
    public static final int FENCE_ALARM = 102;  //安全围栏
    public static final int SIM_CHANGE = 104;   //SIM卡拔出提醒
    public static final int CHAT_MSG = 108;     //消息通知


    //----------------------GPRS通信协议----------------------//
    public static final String START = "[";             //头
    public static final String END = "]";               //尾
    public static final String CUSTOMER_CODE = "CS";    //厂商

    public static final String AL = "AL";               //报警数据上报
    public static final String LK = "LK";               //链路保持
    public static final String UD = "UD";               //位置数据上报
    public static final String UD2 = "UD2";             //盲点补传数据

    public static final String UPLOAD = "UPLOAD";       //数据上传间隔设置
    public static final String CENTER = "CENTER";       //中心号码设置
    public static final String PW = "PW";               //控制密码设置
    public static final String CALL = "CALL";           //拨打电话
    public static final String MONITOR = "MONITOR";     //监听
    public static final String SOS1 = "SOS1";           //第1个SOS号码设置
    public static final String SOS2 = "SOS2";           //第2个SOS号码设置
    public static final String SOS3 = "SOS3";           //第3个SOS号码设置
    public static final String SOS = "SOS";             //3个SOS号码同时设置
    public static final String IP = "IP";               //IP端口设置
    public static final String FACTORY = "FACTORY";     //恢复出厂设置
    public static final String LZ = "LZ";               //设置语言和时区
    public static final String SOSSMS = "SOSSMS";       //SOS短信报警开关
    public static final String LOWBAT = "LOWBAT";       //低电短信报警开关
    public static final String VERNO = "VERNO";         //版本查询
    public static final String RESET = "RESET";         //重启
    public static final String CR = "CR";               //定位指令
    public static final String POWEROFF = "POWEROFF";   //关机指令
    public static final String REMOVE = "REMOVE";       //取下手环报警开关
    public static final String REMOVESMS = "REMOVESMS"; //取下手表短信报警开关
    public static final String PEDO = "PEDO";           //计步功能开关
    public static final String WALKTIME = "WALKTIME";   //计步时间段设置
    public static final String SLEEPTIME = "SLEEPTIME"; //翻转检测时间段设置
    public static final String SILENCETIME = "SILENCETIME";//免打扰时间段设置
    public static final String FIND = "FIND";           //找手表指令
    public static final String FLOWER = "FLOWER";       //小红花个数设置指令
    public static final String REMIND = "REMIND";       //闹钟设置指令
    public static final String TK = "TK";               //对讲功能
    public static final String TKQ = "TKQ";             //终端请求录音下发
    public static final String TKQ2 = "TKQ2";           //终端请求好友录音下发
    public static final String MESSAGE = "MESSAGE";     //短语显示设置指令
    public static final String PHB = "PHB";             //设置电话本,PHB为前5个号码,phb2为后5个号码
    public static final String PHB2 = "PHB2";           //设置电话本,PHB为前5个号码,phb2为后5个号码
    public static final String PP = "PP";               //碰碰交单个好友
    public static final String PPR = "PPR";             //解除单个好友
    public static final String profile = "profile";     //情景模式
    public static final String hrtstart = "hrtstart";   //心率协议
    public static final String heart = "heart";         //终端心率上传
}
