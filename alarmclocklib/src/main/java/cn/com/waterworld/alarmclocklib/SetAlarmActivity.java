package cn.com.waterworld.alarmclocklib;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import cn.com.waterworld.alarmclocklib.db.AlarmDao;
import cn.com.waterworld.alarmclocklib.model.AlarmBean;
import cn.com.waterworld.alarmclocklib.util.AlarmClock;
import cn.com.waterworld.alarmclocklib.util.Constants;
import cn.com.waterworld.alarmclocklib.util.DateUtil;
import cn.com.waterworld.alarmclocklib.util.Unit;

/**
 * Created by wangfeng on 2018/6/11.
 * 设置闹钟界面
 */

public class SetAlarmActivity extends BaseActivity {
    private RelativeLayout rlTime;
    private TextView tvTime;
    private RelativeLayout rlWeek;
    private TextView tvWeek;
    private ImageView ivCancle;
    private ImageView ivOk;

    private String curTime;
    private int defaultHour;
    private int defaultMinute;
    private String week;
    private String weekValue;
    private AlarmBean bean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        curTime = DateUtil.getHHmmStringDateFromMilli(System.currentTimeMillis());
        Date curDate = new Date();
        curDate.setTime(System.currentTimeMillis());
        defaultHour = curDate.getHours();
        defaultMinute = curDate.getMinutes();
        week = DateUtil.getWeekOfDate(this, curDate);
        int index = DateUtil.getWeekIndex(curDate);
        switch (index) {
            case 1:
                weekValue = "00000001";
                break;
            case 2:
                weekValue = "01000000";
                break;
            case 3:
                weekValue = "00100000";
                break;
            case 4:
                weekValue = "00010000";
                break;
            case 5:
                weekValue = "00001000";
                break;
            case 6:
                weekValue = "00000100";
                break;
            case 7:
                weekValue = "00000010";
                break;
        }
        weekValue = Unit.toHexString16(weekValue);

        bean = new AlarmBean();
        bean.setAlarmTime(curDate.getHours() * 60 + curDate.getMinutes());
        tvTime.setText(curTime);
        tvWeek.setText(week);
        //默认值
        bean.setWeekTip(week);
        bean.setIsOff(0);
        bean.setID(Unit.getAlarmID());
        bean.setFlag(2);
        //根据星期几获取WeekValue
        bean.setWeekValue(weekValue);
    }

    private void initView() {
        rlTime = findViewById(R.id.rl_time);
        tvTime = findViewById(R.id.tv_time);
        tvWeek = findViewById(R.id.tv_week);
        ivCancle = findViewById(R.id.iv_cancle);
        ivOk = findViewById(R.id.iv_ok);
        rlWeek = findViewById(R.id.rl_week);
//        //时间选择器
//        pvTime = new TimePickerBuilder(SetAlarmActivity.this, new OnTimeSelectListener() {
//            @Override
//            public void onTimeSelect(Date date, View v) {
//                bean.setAlarmTime(date.getHours() * 60 + date.getMinutes());
//                curTime = getTime(date);
//                tvTime.setText(curTime);
//            }
//        }).setType(new boolean[]{false, false, false, true, true, false})// 几点几分
//                .setContentTextSize(20)//滚轮文字大小
//                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
//                .build();
    }

    private void initListener() {
        MyOnClickListener onClickListener = new MyOnClickListener();
        rlTime.setOnClickListener(onClickListener);
        rlWeek.setOnClickListener(onClickListener);
        ivCancle.setOnClickListener(onClickListener);
        ivOk.setOnClickListener(onClickListener);
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int i = view.getId();
            if (i == R.id.rl_time) {
                chooseTime();
            } else if (i == R.id.rl_week) {
                chooseWeek();
            } else if (i == R.id.iv_cancle) {
                cancleAlarm();
            } else if (i == R.id.iv_ok) {
                setAlarm();
            }
        }
    }

    //选择时间
    private void chooseTime() {
//        pvTime.show();
        Intent intent = new Intent(SetAlarmActivity.this, NewChooseTimeActivity.class);
        intent.putExtra("defaultHour", defaultHour);
        intent.putExtra("defaultMinute", defaultMinute);
        startActivityForResult(intent, Constants.ALARM_TIME_REQUEST_CODE);
    }

    //选择星期几
    private void chooseWeek() {
        Intent intent = new Intent(SetAlarmActivity.this, ChooseWeekActivity.class);
        intent.putExtra("curTime", curTime);
        intent.putExtra("defaultWeek", bean.getWeekValue());
        startActivityForResult(intent, Constants.ALARM_WEEK_REQUEST_CODE);
    }

    //取消闹钟设置
    private void cancleAlarm() {
        finish();
    }

    //设置闹钟
    private void setAlarm() {
        boolean has = AlarmDao.getInstance().hasByTimeAndFlag(bean.getAlarmTime(), bean.getFlag());
        if (!has) {
            AlarmDao.getInstance().insert(bean);
            setSystemAlarm(bean);
            finish();
        } else {
            Toast.makeText(this, getString(R.string.do_not_setting_clock_tip), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 例子：str = {"",0,1,1,1,1,1,1,1}
     */
    private void setSystemAlarm(AlarmBean bean) {
        if (bean.getFlag() == 0) {
            AlarmClock.setAlarmReceiver(SetAlarmActivity.this, 0, bean.getAlarmTime() / 60, bean.getAlarmTime() % 60,
                    Integer.parseInt(bean.getID()), 0, getString(R.string.str_alarm_tip), 0);
        } else {
            String[] str = Unit.toBinaryString2(bean.getWeekValue()).split("");
            for (int i = 0; i < str.length; i++) {
                if (i >= 2 && str[i].equals("1") && bean.getIsOff() == 0) {
                    AlarmClock.setAlarmReceiver(SetAlarmActivity.this, bean.getFlag(), bean.getAlarmTime() / 60, bean.getAlarmTime() % 60,
                            Integer.parseInt(bean.getID() + "" + (i - 1)), i - 1, getString(R.string.str_alarm_tip), 0);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.ALARM_TIME_REQUEST_CODE && resultCode == Constants.ALARM_TIME_RESULT_CODE) {
            String selectHour = data.getStringExtra("hour");
            String selectMinute = data.getStringExtra("minute");
            bean.setAlarmTime(Integer.parseInt(selectHour) * 60 + Integer.parseInt(selectMinute));
            curTime = selectHour + ":" + selectMinute;
            tvTime.setText(curTime);
        }
        if (requestCode == Constants.ALARM_WEEK_REQUEST_CODE && resultCode == Constants.ALARM_WEEK_RESULT_CODE) {
            week = data.getStringExtra("week");
            String weekTip = data.getStringExtra("weekTip");
            tvWeek.setText(weekTip);
            bean.setWeekValue(week);
            bean.setWeekTip(weekTip);
            bean.setIsOff(0);
            bean.setID(Unit.getAlarmID());
            //确定闹钟类型（一次性，周期性）
            if (weekTip.equals(getString(R.string.str_week_onetime))) {//一次性
                bean.setFlag(0);
            } else {
                bean.setFlag(2);
            }
        }
    }
}
