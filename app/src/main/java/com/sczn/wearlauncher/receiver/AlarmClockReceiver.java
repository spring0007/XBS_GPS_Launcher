package com.sczn.wearlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sczn.wearlauncher.Config;
import com.sczn.wearlauncher.activity.AlarmClockRingActivity;
import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.util.MoreFastEvent;
import com.sczn.wearlauncher.util.TimeUtil;

import java.util.List;

import cn.com.waterworld.alarmclocklib.db.AlarmDao;
import cn.com.waterworld.alarmclocklib.model.AlarmBean;
import cn.com.waterworld.alarmclocklib.util.AlarmClock;

/**
 * Description:
 * Created by Bingo on 2019/1/24.
 */
public class AlarmClockReceiver extends BroadcastReceiver {

    private final String TAG = "AlarmClockReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        int time = TimeUtil.getCurrentTotalMin();
        MxyLog.d(TAG, "-------闹钟响铃--------" + time);

        List<AlarmBean> list = AlarmDao.getInstance().queryList(String.valueOf(time));
        if (list != null && list.size() > 0) {
            for (AlarmBean bean : list) {
                if (bean.getFlag() == 0 && bean.getIsOff() == 0) {//开
                    bean.setIsOff(1);
                    //修改单次闹钟的开关状态,(还需要需要删除系统的闹钟)
                    AlarmDao.getInstance().updata(bean);
                    AlarmClock.deleteAlarmReceiver(context, Integer.parseInt(bean.getID()));

                    //测试反馈希望删除单次闹钟；发送消息同步更新闹钟列表
                    //AlarmDao.getInstance().delete(bean.getID());
                }
            }
            doOnRing(intent);
        }
    }

    private void doOnRing(final Intent intent) {
        /**
         * 20秒钟,防止闹钟重复响铃
         */
        MoreFastEvent.getInstance().event(20 * 1000, Config.alarm_ring_last_time, new MoreFastEvent.onEventCallBackWithTimeListener() {
            @Override
            public void onCallback(long eventTime) {
                Config.alarm_ring_last_time = eventTime;

                int type = intent.getIntExtra("type", 0x11);
                String content = intent.getStringExtra("msg");
                int hour = intent.getIntExtra("hour", 0);
                int minute = intent.getIntExtra("minute", 0);

                Intent scheduleIntent = new Intent();
                scheduleIntent.setClass(LauncherApp.getAppContext(), AlarmClockRingActivity.class);
                scheduleIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

                scheduleIntent.putExtra("content", content);
                scheduleIntent.putExtra("type", type);
                scheduleIntent.putExtra("hour", hour);
                scheduleIntent.putExtra("minute", minute);
                LauncherApp.getAppContext().startActivity(scheduleIntent);
            }
        });
    }
}
