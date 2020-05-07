package com.sczn.wearlauncher.socket.command;

import android.os.Build;
import android.view.View;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.base.util.StringUtils;
import com.sczn.wearlauncher.chat.model.WechatGroupInfo;
import com.sczn.wearlauncher.chat.model.WechatMessageInfo;
import com.sczn.wearlauncher.chat.util.MsgAlertPlayer;
import com.sczn.wearlauncher.contact.ContactHelper;
import com.sczn.wearlauncher.friend.ObserverManager;
import com.sczn.wearlauncher.location.LocationUtil;
import com.sczn.wearlauncher.player.SoundPoolUtil;
import com.sczn.wearlauncher.setting.util.AudioManagerHelper;
import com.sczn.wearlauncher.socket.WaterSocketManager;
import com.sczn.wearlauncher.socket.command.gprs.post.StringTo;
import com.sczn.wearlauncher.socket.command.remind.GprsAlarmClock;
import com.sczn.wearlauncher.sp.SPKey;
import com.sczn.wearlauncher.sp.SPUtils;
import com.sczn.wearlauncher.task.util.TaskInfoParse;
import com.sczn.wearlauncher.util.DialogUtil;
import com.sczn.wearlauncher.util.FileHelper;
import com.sczn.wearlauncher.util.SystemPermissionUtil;
import com.sczn.wearlauncher.util.TimeUtil;

import java.io.ByteArrayOutputStream;

/**
 * Description:GPRS通信协议
 * Created by Bingo on 2019/5/6.
 */
public class CmdGprsReceive {

    private static final String TAG = "CmdGprsReceive";

    //是否解析语音协议
    private static ByteArrayOutputStream voiceFile;

    /**
     * 解析接收的数据
     *
     * @param result
     * @param buffer
     */
    public static void onResult(String result, byte[] buffer) {
        MxyLog.i("parseData", "接收的数据:" + result);
        if (!StringUtils.isEmpty(result)) {
            if (!result.contains("*")) {
                WaterSocketManager.getInstance().onFail();
                MxyLog.d(TAG, "收到错误数据.数据中没有包含协议必要的*");
                return;
            }
            //去掉头尾的[]符号
            result = result.substring(1, result.length() - 1);
            String[] msg = result.split("\\*");
            if (msg.length < 3) {
                WaterSocketManager.getInstance().onFail();
                MxyLog.d(TAG, "收到错误数据.数据的长度不足.");
                return;
            }
            String flag = msg[3];
            String data = "";
            if (flag.contains(",")) {
                flag = flag.split(",")[0];
                data = msg[3].substring(flag.length() + 1);
            }
            MxyLog.i(TAG, "收到的类型<<<<< = " + flag);
            switch (flag) {
                case CmdCode.LK:
                    break;
                case CmdCode.VERNO:
                    MxyLog.i(TAG, "当前版本号:" + Build.DISPLAY);
                    WaterSocketManager.getInstance().send(new StringTo(CmdCode.VERNO + "," + Build.DISPLAY, null));
                    break;
                case CmdCode.CR:
                    //定位
                    WaterSocketManager.getInstance().send(new StringTo(CmdCode.CR, null));
                    LocationUtil.getInstance().start(LauncherApp.getAppContext());
                    break;
                case CmdCode.UPLOAD:
                    WaterSocketManager.getInstance().send(new StringTo(CmdCode.UPLOAD, null));
                    if (!StringUtils.isEmpty(data)) {
                        MxyLog.i(TAG, "定位间隔:" + data);
                        SPUtils.setParam(SPKey.LOCATION_GPS_CYCLE_KEY, data);
                    }
                    break;
                case CmdCode.PHB2:
                    //收到通讯录
                    MxyLog.d(TAG, "后五个号码");
                    ContactHelper.getInstance().logicGprs(data, 2);
                    WaterSocketManager.getInstance().send(new StringTo(CmdCode.PHB2, null));
                    break;
                case CmdCode.PHB:
                    MxyLog.d(TAG, "前五个号码");
                    ContactHelper.getInstance().logicGprs(data, 1);
                    WaterSocketManager.getInstance().send(new StringTo(CmdCode.PHB, null));
                    break;
                case CmdCode.MONITOR:
                    //监听
                    doCalling(data);
                    WaterSocketManager.getInstance().send(new StringTo(CmdCode.MONITOR, null));
                    break;
                case CmdCode.FIND:
                    //找手表
                    WaterSocketManager.getInstance().send(new StringTo(CmdCode.FIND, null));
                    SoundPoolUtil.getInstance().loadResAndPlay(LauncherApp.getAppContext(), R.raw.schedule_sound, 10);
                    DialogUtil.showMsgDialog(LauncherApp.getAppContext(),
                            LauncherApp.getAppContext().getResources().getString(R.string.find),
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DialogUtil.dismissMsgDialog();
                                    SoundPoolUtil.release();
                                }
                            });
                    break;
                case CmdCode.POWEROFF:
                    //关机
                    WaterSocketManager.getInstance().send(new StringTo(CmdCode.POWEROFF, null));
                    SystemPermissionUtil.shutdown(LauncherApp.getAppContext());
                    break;
                case CmdCode.CENTER:
                    //中心设置
                    WaterSocketManager.getInstance().send(new StringTo(CmdCode.CENTER, null));
                    setCenter(data);
                    break;
                case CmdCode.LOWBAT:
                    //低电短信报警开关
                    WaterSocketManager.getInstance().send(new StringTo(CmdCode.LOWBAT, null));
                    setLowSwitch(data);
                    break;
                case CmdCode.SOSSMS:
                    //SOS短信报警开关
                    WaterSocketManager.getInstance().send(new StringTo(CmdCode.SOSSMS, null));
                    setSosSmsSwitch(data);
                    break;
                case CmdCode.REMOVESMS:
                    //取下手表短信报警开关
                    WaterSocketManager.getInstance().send(new StringTo(CmdCode.REMOVESMS, null));
                    setRemoveSmsSwitch(data);
                    break;
                case CmdCode.REMOVE:
                    //取下手环报警开关
                    WaterSocketManager.getInstance().send(new StringTo(CmdCode.REMOVE, null));
                    setRemoveSwitch(data);
                    break;
                case CmdCode.PEDO:
                    //计步功能开关
                    WaterSocketManager.getInstance().send(new StringTo(CmdCode.PEDO, null));
                    setPedoSwitch(data);
                    break;
                case CmdCode.WALKTIME:
                    //计步时间段设置
                    setWalkTime(data);
                    WaterSocketManager.getInstance().send(new StringTo(CmdCode.WALKTIME, null));
                    break;
                case CmdCode.SOS1:
                    setSosNum(data);
                    WaterSocketManager.getInstance().send(new StringTo(CmdCode.SOS1, null));
                    break;
                case CmdCode.SOS2:
                    setSosNum(data);
                    WaterSocketManager.getInstance().send(new StringTo(CmdCode.SOS2, null));
                    break;
                case CmdCode.SOS3:
                    setSosNum(data);
                    WaterSocketManager.getInstance().send(new StringTo(CmdCode.SOS3, null));
                    break;
                case CmdCode.SOS:
                    setSosNum(data);
                    WaterSocketManager.getInstance().send(new StringTo(CmdCode.SOS, null));
                    break;
                case CmdCode.IP:
                    //IP端口设置
                    setIpReset(data);
                    break;
                case CmdCode.FACTORY:
                    //恢复出厂设置
                    WaterSocketManager.getInstance().send(new StringTo(CmdCode.FACTORY, null));
                    SystemPermissionUtil.reset();
                    break;
                case CmdCode.LZ:
                    //设置语言和时区
                    WaterSocketManager.getInstance().send(new StringTo(CmdCode.LZ, null));
                    setLanguageTimeZone(data);
                    break;
                case CmdCode.RESET:
                    //重启
                    WaterSocketManager.getInstance().send(new StringTo(CmdCode.RESET, null));
                    SystemPermissionUtil.reboot();
                    break;
                case CmdCode.PW:
                    //控制密码设置
                    //设置终端短信控制密码,非中心号码发送短信指令给终端需添加此密码
                    WaterSocketManager.getInstance().send(new StringTo(CmdCode.PW, null));
                    setPw(data);
                    break;
                case CmdCode.CALL:
                    //拨打电话
                    WaterSocketManager.getInstance().send(new StringTo(CmdCode.CALL, null));
                    SystemPermissionUtil.doCalling(data);
                    break;
                case CmdCode.SILENCETIME:
                    //免打扰时间段设置
                    WaterSocketManager.getInstance().send(new StringTo(CmdCode.SILENCETIME, null));
                    TaskInfoParse.logicGprs(data);
                    break;
                case CmdCode.FLOWER:
                    //小红花个数设置指令
                    WaterSocketManager.getInstance().send(new StringTo(CmdCode.FLOWER, null));
                    setFlower(data);
                    break;
                case CmdCode.REMIND:
                    //闹钟设置指令
                    WaterSocketManager.getInstance().send(new StringTo(CmdCode.REMIND, null));
                    GprsAlarmClock.set(data);
                    break;
                case CmdCode.MESSAGE:
                    //短语显示设置指令
                    WaterSocketManager.getInstance().send(new StringTo(CmdCode.MESSAGE, null));
                    showText(data);
                    break;
                case CmdCode.profile:
                    //情景模式
                    WaterSocketManager.getInstance().send(new StringTo(CmdCode.profile, null));
                    setProfile(data);
                    break;
                case CmdCode.TK:
                    saveVoiceFile(buffer);
                    break;
                default:
                    break;
            }
        }
        WaterSocketManager.getInstance().onSuccess(result);
    }

    /**
     * 解析保存接收语音文件
     *
     * @param buffer
     */
    private static void saveVoiceFile(byte[] buffer) {
        try {
            //接收语音文件
            if (voiceFile == null) {
                voiceFile = new ByteArrayOutputStream();
            }
            String originally = CommandHelper.bytesToHexString(buffer);
            if (originally != null) {
                originally = originally
                        .replace("7d01", "7d")
                        .replace("7d02", "5b")
                        .replace("7d03", "5d")
                        .replace("7d04", "2c")
                        .replace("7d05", "2a");
            }
            byte[] newBuffer = CommandHelper.hexStringToBytes(originally);
            if (newBuffer != null && newBuffer.length > 23) {
                byte[] fileData = new byte[newBuffer.length - 2];
                System.arraycopy(newBuffer, 1, fileData, 0, newBuffer.length - 2);
                voiceFile.write(fileData, 0, fileData.length);
                FileHelper.saveVoiceData(voiceFile.toByteArray(), TimeUtil.getTimeToVoiceName());
                voiceFile.reset();
                voiceFile = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置语言
     * 设置系统时区
     *
     * @param flag
     */
    private static void setLanguageTimeZone(String flag) {
        if (flag.contains(",")) {
            String[] d = flag.split(",");
            if (!StringUtils.isEmpty(d[0])) {
                MxyLog.i(TAG, "language:" + d[0]);
                SystemPermissionUtil.setLanguage(d[0]);
            }
            if (d.length >= 3 && !StringUtils.isEmpty(d[1])) {
                MxyLog.i(TAG, "timeZone:" + d[1]);
                SystemPermissionUtil.setTimeZone(d[1]);
            }
        }
    }

    /**
     * 低电短信报警开关
     *
     * @param data
     */
    private static void setLowSwitch(String data) {
        if (!StringUtils.isEmpty(data)) {
            SPUtils.setParam(SPKey.LOW_SWITCH, data);
        }
    }

    /**
     * SOS短信报警开关
     *
     * @param data
     */
    private static void setSosSmsSwitch(String data) {
        if (!StringUtils.isEmpty(data)) {
            SPUtils.setParam(SPKey.SOS_SMS_SWITCH, data);
        }
    }

    /**
     * 取下手表短信报警开关
     *
     * @param data
     */
    private static void setRemoveSmsSwitch(String data) {
        if (!StringUtils.isEmpty(data)) {
            SPUtils.setParam(SPKey.REMOVE_SMS_SWITCH, data);
        }
    }

    /**
     * 取下手环报警开关
     *
     * @param data
     */
    private static void setRemoveSwitch(String data) {
        if (!StringUtils.isEmpty(data)) {
            SPUtils.setParam(SPKey.REMOVE_SWITCH, data);
        }
    }

    /**
     * 计步功能开关
     *
     * @param data
     */
    private static void setPedoSwitch(String data) {
        if (!StringUtils.isEmpty(data)) {
            SPUtils.setParam(SPKey.PEDO_SWITCH, data);
        }
    }

    /**
     * 计步时间段设置
     *
     * @param data
     */
    private static void setWalkTime(String data) {
        if (!StringUtils.isEmpty(data)) {
            MxyLog.d(TAG, "计步时间段设置:" + data);
        }
    }

    /**
     * 设置情景模式
     *
     * @param data
     */
    private static void setProfile(String data) {
        if (!StringUtils.isEmpty(data)) {
            AudioManagerHelper mAudioManagerHelper = new AudioManagerHelper(LauncherApp.getAppContext());
            if (data.equals("1")) {//表示震动加响铃
                mAudioManagerHelper.ringAndVibrate();
            } else if (data.equals("2")) {//表示响铃
                mAudioManagerHelper.ring();
                //默认把音量开到上一次的音量的保存的数值
                //如果上次是静音则开启响铃模式时默认设置为2
                int volumeLevel = (int) SPUtils.getParam(SPKey.WATCH_VOLUME, 0);
                if (volumeLevel == 0) {
                    volumeLevel = 2;
                }
                mAudioManagerHelper.setVolume(volumeLevel);
            } else if (data.equals("3")) {//表示震动
                mAudioManagerHelper.vibrate();
            } else if (data.equals("4")) {//表示静音
                mAudioManagerHelper.silent();
                mAudioManagerHelper.setVolume(0);
            }
        }
    }

    /**
     * 设置ip地址
     *
     * @param data
     */
    private static void setIpReset(String data) {
        if (data.contains(",")) {
            String[] d = data.split(",");
            if (d.length >= 3 && !StringUtils.isEmpty(d[0]) && !StringUtils.isEmpty(d[1])) {
                SPUtils.setParam(SPKey.IP, d[0]);
                SPUtils.setParam(SPKey.PORT, Integer.parseInt(d[1]));
                WaterSocketManager.getInstance().reInitSocket();
            }
        }
    }

    /**
     * 控制密码设置
     *
     * @param data
     */
    private static void setPw(String data) {
        if (!StringUtils.isEmpty(data)) {
            SPUtils.setParam(SPKey.PW_NUMBER, data);
        }
    }

    /**
     * 小红花个数设置指令
     *
     * @param data
     */
    private static void setFlower(String data) {
        if (!StringUtils.isEmpty(data)) {
            MxyLog.d(TAG, "小红花个数:" + data);
            DialogUtil.showMsgDialog(LauncherApp.getAppContext(), "小红花个数:" + data);
        }
    }

    /**
     * 中心号码设置
     *
     * @param data
     */
    private static void setCenter(String data) {
        if (!StringUtils.isEmpty(data)) {
            MxyLog.d(TAG, "中心号码设置:" + data);
            SPUtils.setParam(SPKey.CENTER_NUMBER, data);
        }
    }


    /**
     * 终端收到该指令后会自动回拨给指令中设置的号码.
     *
     * @param data
     */
    private static void doCalling(String data) {
        if (!StringUtils.isEmpty(data)) {
            SystemPermissionUtil.doCalling(data);
        }
    }

    /**
     * 解析SOS号码
     *
     * @param data
     */
    private static void setSosNum(String data) {
        if (data.contains(",")) {
            String[] d = data.split(",");
            if (d.length >= 3) {
                if (!StringUtils.isEmpty(d[0])) {
                    MxyLog.d(TAG, "SOS1:" + d[0]);
                    SPUtils.setParam(SPKey.SOS_1, d[0]);
                } else {
                    SPUtils.setParam(SPKey.SOS_1, "");
                }
                if (!StringUtils.isEmpty(d[1])) {
                    MxyLog.d(TAG, "SOS2:" + d[1]);
                    SPUtils.setParam(SPKey.SOS_2, d[1]);
                } else {
                    SPUtils.setParam(SPKey.SOS_2, "");
                }
                if (!StringUtils.isEmpty(d[2])) {
                    MxyLog.d(TAG, "SOS3:" + d[2]);
                    SPUtils.setParam(SPKey.SOS_3, d[2]);
                } else {
                    SPUtils.setParam(SPKey.SOS_3, "");
                }
            }
        }
    }


    /**
     * 收到消息
     *
     * @param data
     */
    private static void showText(String data) {
        MsgAlertPlayer.getPlayer().playMsgAlert();
        if (!StringUtils.isEmpty(data)) {
            //更新标记当前组,未读
            WechatGroupInfo group = new WechatGroupInfo();
            group.setMsgReadStatus(1);
            group.updateAll("groupId = ?", "0");

            WechatMessageInfo msg = new WechatMessageInfo();
            msg.setGroupId("0");
            msg.setContent(CommandHelper.hexUnicode2String(data));
            msg.setSenderType(0);
            msg.setSenderId("0");
            msg.setSenderName("监护人");
            msg.setCreateTime(TimeUtil.getTime());
            msg.save();

            ObserverManager.getInstance().notification(ObserverManager.CHAT_MSG_OBSERVER, "0");
        }
    }
}
