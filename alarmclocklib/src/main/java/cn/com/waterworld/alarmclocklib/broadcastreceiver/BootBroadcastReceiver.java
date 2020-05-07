package cn.com.waterworld.alarmclocklib.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import cn.com.waterworld.alarmclocklib.R;
import cn.com.waterworld.alarmclocklib.db.AlarmDao;
import cn.com.waterworld.alarmclocklib.model.AlarmBean;
import cn.com.waterworld.alarmclocklib.util.AlarmClock;
import cn.com.waterworld.alarmclocklib.util.ListUtil;
import cn.com.waterworld.alarmclocklib.util.LogUtil;
import cn.com.waterworld.alarmclocklib.util.Unit;

/**
 * Created by wangfeng on 2018/6/26.
 * 手表重启监听广播
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.e("MyAlarm" + "收到通知重新设置");
        List<AlarmBean> alarmList = AlarmDao.getInstance().queryAll();
        if (!ListUtil.isEmpty(alarmList)) {
            for (AlarmBean bean : alarmList) {
                if (bean.getIsOff() == 0) {
                    setSystemAlarm(context, bean);
                }
            }
        }
    }

    /**
     * 例子：str = {"",0,1,1,1,1,1,1,1}
     */
    private void setSystemAlarm(Context context, AlarmBean bean) {
        LogUtil.e("MyAlarm" + "重新设置闹钟");
        if (bean.getFlag() == 0) {
            AlarmClock.setAlarmReceiver(context, 0, bean.getAlarmTime() / 60, bean.getAlarmTime() % 60,
                    Integer.parseInt(bean.getID()), 0, context.getString(R.string.str_alarm_tip), 0);
        } else {
            String[] str = Unit.toBinaryString2(bean.getWeekValue()).split("");
            for (int i = 0; i < str.length; i++) {
                if (i >= 2 && str[i].equals("1") && bean.getIsOff() == 0) {
                    AlarmClock.setAlarmReceiver(context, bean.getFlag(), bean.getAlarmTime() / 60, bean.getAlarmTime() % 60,
                            Integer.parseInt(bean.getID() + "" + (i - 1)), i - 1, context.getString(R.string.str_alarm_tip), 0);
                }
            }
        }
    }
}
