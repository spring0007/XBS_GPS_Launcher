package com.sczn.wearlauncher.socket.command.remind;

import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.base.util.StringUtils;

import cn.com.waterworld.alarmclocklib.db.AlarmDao;
import cn.com.waterworld.alarmclocklib.model.AlarmBean;
import cn.com.waterworld.alarmclocklib.util.Unit;

/**
 * Description:
 * Created by Bingo on 2019/5/9.
 */
public class GprsAlarmClock {

    private static final String TAG = "GprsAlarmClock";

    /**
     * 设置闹钟
     * <p>
     * 说明:闹铃格式为：时间-开关-频率(1：一次；2:每天;3：自定义)
     * 08:10-1-1：闹钟时间 8:10，打开，响铃一次
     * 08:10-1-2：闹钟时间 8:10，打开，每天响铃
     * 08:10-1-3-0111110：闹钟时间 8:10，打开，自定义周一至周五打开
     *
     * @param data
     */
    public static void set(String data) {
        if (!data.contains(",")) {
            MxyLog.w(TAG, "设置闹钟数据错误.");
            return;
        }
        String[] alarms = data.split(",");
        for (String alarm : alarms) {
            if (!StringUtils.isEmpty(alarm)) {
                MxyLog.w(TAG, "设置闹钟数据:" + alarm);
                if (alarm.contains("-")) {
                    AlarmBean alarmBean = new AlarmBean();
                    String[] info = alarm.split("-");
                    int t = 0;
                    String time;
                    String open = "";
                    String flag = "";
                    String week = "";
                    if (info.length >= 2) {
                        time = info[0];
                        if (time.contains(":")) {
                            t = Integer.parseInt(time.split(":")[0]) * 60 + Integer.parseInt(time.split(":")[1]);
                        }
                        open = info[1];
                    }
                    if (info.length >= 3) {
                        flag = info[2];
                    }
                    if (info.length == 4) {
                        week = info[3];
                        //把第一个字符移动添加到后面
                        if (!week.isEmpty()) {
                            week = week.substring(1) + week.substring(0, 1);
                        }
                    }
                    //时间当id,服务器时间不可以相等
                    alarmBean.setID(String.valueOf(t));
                    alarmBean.setAlarmTime(t);
                    if (open.equals("0")) {
                        alarmBean.setIsOff(1);
                    } else {
                        alarmBean.setIsOff(0);
                    }
                    //设置周期
                    if (!StringUtils.isEmpty(flag)) {
                        int f = Integer.parseInt(flag);
                        if (f == 2) {
                            week = "01111111";
                        }
                        alarmBean.setFlag(f - 1);
                    }

                    //设置自定义周期
                    AlarmBean a = AlarmDao.getInstance().query(alarmBean.getID());
                    if (a != null) {
                        MxyLog.e(TAG, ">>" + a.toString());
                        if (week.isEmpty()) {
                            week = Unit.toBinaryString2(a.getWeekValue());
                        }
                        if (flag.isEmpty()) {
                            alarmBean.setFlag(a.getFlag());
                        }
                    }
                    if (week.isEmpty()) {
                        week = "00000000";
                    }
                    week = Unit.toHexString16(week);
                    alarmBean.setWeekValue(week);

                    if (alarmBean.getIsOff() == 0) {
                        AlarmDao.getInstance().insert(alarmBean);
                        DayRemindHelper.setSystemAlarm(alarmBean);
                    } else {
                        AlarmDao.getInstance().updata(alarmBean);
                        DayRemindHelper.deleteIDAlarm(alarmBean);
                    }
                }
            }
        }
    }
}
