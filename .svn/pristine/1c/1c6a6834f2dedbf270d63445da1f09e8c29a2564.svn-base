package com.sczn.wearlauncher.socket.command.remind;

import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.socket.command.bean.DayRemind;
import com.sczn.wearlauncher.util.TimeUtil;

import org.litepal.LitePal;

import java.util.List;

import cn.com.waterworld.alarmclocklib.model.AlarmBean;
import cn.com.waterworld.alarmclocklib.util.AlarmClock;
import cn.com.waterworld.alarmclocklib.util.SystemAlarmClockOperation;
import cn.com.waterworld.alarmclocklib.util.Unit;

import static java.lang.Integer.parseInt;

/**
 * Description:日程提醒工具
 * Created by Bingo on 2019/4/1.
 */
public class DayRemindHelper {

    /**
     * 设置提醒
     * 1.更新数据库
     * 2.删除原先这个id对应的闹钟
     * 3.重新设置闹钟
     *
     * @param list
     */
    public static void setup(List<DayRemind> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        for (DayRemind d : list) {
            String week = "";
            AlarmBean alarmBean = new AlarmBean();
            if (d.getIsEnable()) {
                alarmBean.setID(d.getId() + "");
                String time = d.getRemindTime();
                if (time != null && time.contains(":")) {
                    int t = Integer.parseInt(time.split(":")[0]) * 60 + Integer.parseInt(time.split(":")[1]);
                    alarmBean.setAlarmTime(t);
                }
                alarmBean.setIsOff(d.getIsEnable() ? 0 : 1);
                alarmBean.setWeekTip(d.getRemindContent());
                if (d.getRemindType() == 1) {//日期
                    alarmBean.setFlag(0);
                    week = "00000000";
                    alarmBean.setWeekValue(week);
                } else {
                    if (d.getRemindDays().contains("1,2,3,4,5,6,7")) {
                        alarmBean.setFlag(1);//每天
                        week = "01111111";
                        alarmBean.setWeekValue(week);
                    } else {
                        StringBuilder builder = new StringBuilder();
                        builder.append("0");
                        alarmBean.setFlag(2);//每周
                        if (d.getRemindDays().contains("1")) {
                            builder.append("1");
                        } else {
                            builder.append("0");
                        }
                        if (d.getRemindDays().contains("2")) {
                            builder.append("1");
                        } else {
                            builder.append("0");
                        }
                        if (d.getRemindDays().contains("3")) {
                            builder.append("1");
                        } else {
                            builder.append("0");
                        }
                        if (d.getRemindDays().contains("4")) {
                            builder.append("1");
                        } else {
                            builder.append("0");
                        }
                        if (d.getRemindDays().contains("5")) {
                            builder.append("1");
                        } else {
                            builder.append("0");
                        }
                        if (d.getRemindDays().contains("6")) {
                            builder.append("1");
                        } else {
                            builder.append("0");
                        }
                        if (d.getRemindDays().contains("7")) {
                            builder.append("1");
                        } else {
                            builder.append("0");
                        }
                        week = builder.toString();
                        MxyLog.d("setup", "week:" + week);
                        alarmBean.setWeekValue(week);
                    }
                }

            }
            d.setWeek(week);
            d.saveOrUpdate("id = ?", String.valueOf(d.getId()));
            cancelAlarm(d);
            setSystemAlarm(d, alarmBean);
        }
    }

    /**
     * 删除全部日程提醒
     * 1.删除数据库
     * 2.删除系统设置的提醒
     */
    public static void deleteAll() {
        List<DayRemind> ds = LitePal.findAll(DayRemind.class);
        if (ds != null && ds.size() > 0) {
            for (DayRemind d : ds) {
                d.delete();
                cancelAlarm(d);
            }
        }
    }

    /**
     * 设置系统闹钟
     *
     * @param d
     * @param bean
     */
    private static void setSystemAlarm(DayRemind d, AlarmBean bean) {
        if (bean.getFlag() == 0) {
            SystemAlarmClockOperation.setAlarm(LauncherApp.getAppContext(), 0,
                    TimeUtil.stringToCalendar(d.getRemindDays(), d.getRemindTime()), d.getId(), 0, d.getRemindContent());
        } else {
            String[] str = bean.getWeekValue().split("");
            for (int i = 0; i < str.length; i++) {
                if (i >= 2 && str[i].equals("1") && bean.getIsOff() == 0) {
                    AlarmClock.setAlarmReceiver(LauncherApp.getAppContext(), bean.getFlag(), bean.getAlarmTime() / 60, bean.getAlarmTime() % 60,
                            Integer.parseInt(d.getId() + "" + (i - 1)), i - 1, bean.getWeekTip(), 0);
                }
            }
        }
    }

    /**
     * 删除系统闹钟
     *
     * @param bean
     */
    private static void cancelAlarm(DayRemind bean) {
        if (bean.getRemindType() == 1) {
            AlarmClock.deleteAlarmReceiver(LauncherApp.getAppContext(), bean.getId());
        } else {
            String[] str = bean.getWeek().split("");
            for (int i = 0; i < str.length; i++) {
                if (i >= 2 && str[i].equals("1")) {
                    AlarmClock.deleteAlarmReceiver(LauncherApp.getAppContext(), Integer.parseInt(bean.getId() + "" + (i - 1)));
                }
            }
        }
    }

    /**
     * @param bean
     */
    public static void setSystemAlarm(AlarmBean bean) {
        if (bean.getFlag() == 0) {
            AlarmClock.setAlarmReceiver(LauncherApp.getAppContext(), 0, bean.getAlarmTime() / 60, bean.getAlarmTime() % 60,
                    Integer.parseInt(bean.getID()), 0, bean.getWeekTip(), 0);
        } else {
            String[] str = Unit.toBinaryString2(bean.getWeekValue()).split("");
            for (int i = 0; i < str.length; i++) {
                if (i >= 2 && str[i].equals("1") && bean.getIsOff() == 0) {
                    AlarmClock.setAlarmReceiver(LauncherApp.getAppContext(), bean.getFlag(), bean.getAlarmTime() / 60, bean.getAlarmTime() % 60,
                            Integer.parseInt(bean.getID() + "" + (i - 1)), i - 1, bean.getWeekTip(), 0);
                }
            }
        }
    }

    /**
     * @param bean
     */
    public static void deleteIDAlarm(AlarmBean bean) {
        if (bean.getFlag() == 0) {
            AlarmClock.deleteAlarmReceiver(LauncherApp.getAppContext(), Integer.parseInt(bean.getID()));
        } else {
            String v = Unit.toBinaryString2(bean.getWeekValue());
            String[] str = v.split("");
            for (int i = 0; i < str.length; i++) {
                if (i >= 2 && str[i].equals("1")) {
                    AlarmClock.deleteAlarmReceiver(LauncherApp.getAppContext(), parseInt(bean.getID() + "" + (i - 1)));
                }
            }
        }
    }
}
