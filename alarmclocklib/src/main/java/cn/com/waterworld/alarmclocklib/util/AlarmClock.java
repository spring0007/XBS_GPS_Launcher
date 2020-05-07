package cn.com.waterworld.alarmclocklib.util;

import android.content.Context;

/**
 * Created by wangfeng on 2018/6/11.
 * 闹钟封装类
 */

public class AlarmClock {
    /**
     * 发送设置闹钟广播
     *
     * @param flag            周期性时间间隔的标志,flag = 0 表示一次性的闹钟, flag = 1 表示每天提醒的闹钟(1天的时间间隔),
     *                        flag = 2 表示按周每周提醒的闹钟（一周的周期性时间间隔）
     * @param hour            时
     * @param minute          分
     * @param id              闹钟的id
     * @param week            week=0表示一次性闹钟或者按天的周期性闹钟，非0 的情况下是几就代表以周为周期性的周几的闹钟
     * @param tips            闹钟提示信息
     * @param soundOrVibrator 2表示声音和震动都执行，1表示只有铃声提醒，0表示只有震动提醒
     */
    public static void setAlarmReceiver(Context context, int flag, int hour, int minute, int id, int week, String tips, int soundOrVibrator) {
        LogUtil.d("发送设置闹钟广播" + "hour" + hour + ",minute" + minute + ",msg" + tips + ",id" + id);
        SystemAlarmClockOperation.setAlarm(context, flag, hour, minute, id, week, tips, 1);
    }

    /**
     * 发送删除闹钟广播
     *
     * @param id 闹钟的id
     */
    public static void deleteAlarmReceiver(Context context, int id) {
        LogUtil.i("发送删除闹钟广播" + "id:" + id);
        SystemAlarmClockOperation.cancelAlarm(context, id);
    }
}
