package cn.com.waterworld.alarmclocklib.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;

/**
 * Description:设置系统闹钟和删除系统闹钟
 */
public class SystemAlarmClockOperation {

    private static final String TAG = "SystemAlarmClockOperation";
    private static final String ALARM_ACTION = "cn.com.waterworld.alarm.clock";

    /**
     * 取消闹钟
     *
     * @param context
     * @param id
     */
    public static void cancelAlarm(Context context, int id) {
        LogUtil.e(TAG + "取消闹钟:" + "id" + id);
        Intent intent = new Intent(ALARM_ACTION);
        // 通常cancel之前可以先用PendingIntent.FLAG_NO_CREATE来判断之前是不是设定了这个alarm，假如没设定，那就不要去调用cancel
        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_NO_CREATE);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am != null) {
            LogUtil.e(TAG + "cancel alarm");
            am.cancel(pi);
        } else {
            LogUtil.e(TAG + "am == null");
        }
    }

    /**
     * @param flag            周期性时间间隔的标志,flag = 0 表示一次性的闹钟, flag = 1 表示每天提醒的闹钟(1天的时间间隔),flag = 2
     *                        表示按周每周提醒的闹钟（一周的周期性时间间隔）
     * @param hour            时
     * @param minute          分
     * @param id              闹钟的id
     * @param week            week=0表示一次性闹钟或者按天的周期性闹钟，非0 的情况下是几就代表以周为周期性的周几的闹钟
     * @param tips            闹钟提示信息
     * @param soundOrVibrator 2表示声音和震动都执行，1表示只有铃声提醒，0表示只有震动提醒
     */
    public static void setAlarm(Context context, int flag, int hour, int minute, int id, int week, String tips, int soundOrVibrator) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am == null) {
            LogUtil.e(TAG + "am == null");
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), hour, minute, 0);

        long intervalMillis = 0;
        if (flag == 0) {
            intervalMillis = 0;
        } else if (flag == 1) {
            intervalMillis = 24 * 3600 * 1000;
        } else if (flag == 2) {
            intervalMillis = 24 * 3600 * 1000 * 7;
        }
        Intent intent = new Intent(ALARM_ACTION);
        intent.putExtra("intervalMillis", intervalMillis);
        intent.putExtra("hour", hour);
        intent.putExtra("minute", minute);
        intent.putExtra("msg", tips);
        intent.putExtra("id", id);
        intent.putExtra("week", week);
        PendingIntent sender = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        //     am.setWindow(AlarmManager.RTC_WAKEUP, calMethod(week, calendar.getTimeInMillis()), intervalMillis, sender);
        // } else {
        //     if (flag == 0) {
        //         am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        //     } else {
        //         am.setRepeating(AlarmManager.RTC_WAKEUP, calMethod(week, calendar.getTimeInMillis()), intervalMillis, sender);
        //     }
        // }

        // 修改使用准确性闹钟，当前版本为19（4.4）或以上使用精准闹钟
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, calMethod(week, calendar.getTimeInMillis()), sender);
        } else {
            am.set(AlarmManager.RTC_WAKEUP, calMethod(week, calendar.getTimeInMillis()), sender);
        }
        String alarmtime = DateUtil.getStringDateFromMilli(calMethod(week, calendar.getTimeInMillis()));
        LogUtil.e(TAG + "设置闹钟:" + "week" + week + ",hour" + hour + ",minute" + minute + ",id" + id + ",闹铃时间：" + alarmtime);
    }

    /**
     * 设置闹钟
     *
     * @param context  context
     * @param flag     周期性时间间隔的标志
     *                 flag = 0 表示一次性的闹钟,
     *                 flag = 1 表示每天提醒的闹钟(1天的时间间隔)
     *                 flag = 2 表示按周每周提醒的闹钟（一周的周期性时间间隔）
     * @param calendar 时间
     * @param id       闹钟的 id
     * @param week     week=0表示一次性闹钟或者按天的周期性闹钟，非0的情况下是几就代表以周为周期性的周几的闹钟
     * @param tips     闹钟提示信息
     */
    public static void setAlarm(Context context, int flag, Calendar calendar, int id, int week, String tips) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am == null) {
            LogUtil.e(TAG + "am == null");
            return;
        }
        long intervalMillis = 0;
        if (flag == 1) {
            intervalMillis = 24 * 3600 * 1000;
        } else if (flag == 2) {
            intervalMillis = 24 * 3600 * 1000 * 7;
        }
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        Intent intent = new Intent(ALARM_ACTION);
        intent.putExtra("intervalMillis", intervalMillis);
        intent.putExtra("hour", hour);
        intent.putExtra("minute", minute);
        intent.putExtra("msg", tips);
        intent.putExtra("id", id);
        intent.putExtra("week", week);
        PendingIntent sender = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        // 修改使用准确性闹钟，当前版本为19（4.4）或以上使用精准闹钟
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, calMethod(week, calendar.getTimeInMillis()), sender);
        } else {
            am.set(AlarmManager.RTC_WAKEUP, calMethod(week, calendar.getTimeInMillis()), sender);
        }
        String alarmtime = DateUtil.getStringDateFromMilli(calMethod(week, calendar.getTimeInMillis()));
        LogUtil.d(TAG + "设置闹钟:" + "week" + week + ",hour" + hour + ",minute" + minute + ",id" + id + ",闹铃时间：" + alarmtime);
    }

    public static void setAlarmAgain(Context context, int flag, int hour, int minute, int id, int week, String tips) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am == null) {
            LogUtil.e(TAG + "am == null");
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), hour, minute, 0);

        long intervalMillis = 0;
        if (flag == 0) {
            intervalMillis = 0;
        } else if (flag == 1) {
            intervalMillis = 24 * 3600 * 1000;
        } else if (flag == 2) {
            intervalMillis = 24 * 3600 * 1000 * 7;
        }
        Intent intent = new Intent(ALARM_ACTION);
        intent.putExtra("intervalMillis", intervalMillis);
        intent.putExtra("hour", hour);
        intent.putExtra("minute", minute);
        intent.putExtra("msg", tips);
        intent.putExtra("id", id);
        intent.putExtra("week", week);
        PendingIntent sender = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // 修改使用准确性闹钟，当前版本为19（4.4）或以上使用精准闹钟
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, calMethod(week, calendar.getTimeInMillis()), sender);
        } else {
            am.set(AlarmManager.RTC_WAKEUP, calMethod(week, calendar.getTimeInMillis()), sender);
        }
        String alarmtime = DateUtil.getStringDateFromMilli(calMethod(week, calendar.getTimeInMillis()));
        LogUtil.e(TAG + "重复设置闹钟:" + "week" + week + ",hour" + hour + ",minute" + minute + ",id" + id + ",闹铃时间：" + alarmtime);
    }

    /**
     * @param weekflag 传入的是周几
     * @param dateTime 传入的是时间戳（设置当天的年月日+从选择框拿来的时分秒）
     * @return 返回起始闹钟时间的时间戳
     */
    private static long calMethod(int weekflag, long dateTime) {
        long time = 0;
        //weekflag == 0表示是按天为周期性的时间间隔或者是一次行的，weekfalg非0时表示每周几的闹钟并以周为时间间隔
        if (weekflag != 0) {
            Calendar c = Calendar.getInstance();
            int week = c.get(Calendar.DAY_OF_WEEK);
            if (1 == week) {
                week = 7;
            } else if (2 == week) {
                week = 1;
            } else if (3 == week) {
                week = 2;
            } else if (4 == week) {
                week = 3;
            } else if (5 == week) {
                week = 4;
            } else if (6 == week) {
                week = 5;
            } else if (7 == week) {
                week = 6;
            }
            if (weekflag == week) {
                if (dateTime > System.currentTimeMillis()) {
                    time = dateTime;
                } else {
                    time = dateTime + 7 * 24 * 3600 * 1000;
                }
            } else if (weekflag > week) {
                time = dateTime + (weekflag - week) * 24 * 3600 * 1000;
            } else if (weekflag < week) {
                time = dateTime + (weekflag - week + 7) * 24 * 3600 * 1000;
            }
        } else {
            if (dateTime > System.currentTimeMillis()) {
                time = dateTime;
            } else {
                time = dateTime + 24 * 3600 * 1000;
            }
        }
        return time;
    }

    public static void updateAlarm(Context context, int id) {
        // Intent intent = new Intent(UPDATE_ALARM_ACTION);
        // intent.putExtra("id", String.valueOf(id));
        // LogUtil.e(TAG + "通知闹钟状态更新广播id" + id);
        // context.sendBroadcast(intent);
    }
}
