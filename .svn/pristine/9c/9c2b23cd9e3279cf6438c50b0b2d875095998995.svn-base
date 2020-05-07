package cn.com.waterworld.alarmclocklib;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.com.waterworld.alarmclocklib.db.AlarmDao;
import cn.com.waterworld.alarmclocklib.model.AlarmBean;
import cn.com.waterworld.alarmclocklib.util.AlarmClock;
import cn.com.waterworld.alarmclocklib.util.Constants;
import cn.com.waterworld.alarmclocklib.util.Unit;

import static java.lang.Integer.parseInt;

/**
 * Created by wangfeng on 2018/6/12.
 * 闹钟详情
 */

public class AlarmDetailActivity extends BaseActivity {
    private RelativeLayout rlTime;
    private TextView tvTime;
    private RelativeLayout rlWeek;
    private TextView tvWeek;
    private ImageView ivDelete;
    private ImageView ivOk;

    private String curTime;
    private int defaultHour;
    private int defaultMinute;
    private String week;
    private String weekTip;
    private AlarmBean bean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_detail);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        rlTime = findViewById(R.id.rl_time);
        tvTime = findViewById(R.id.tv_time);
        rlWeek = findViewById(R.id.rl_week);
        tvWeek = findViewById(R.id.tv_week);
        ivDelete = findViewById(R.id.iv_delete);
        ivOk = findViewById(R.id.iv_ok);
        //时间选择器
        /*pvTime = new TimePickerBuilder(AlarmDetailActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                bean.setAlarmTime(date.getHours() * 60 + date.getMinutes());
                curTime = getTime(date);
                tvTime.setText(curTime);
            }
        }).setType(new boolean[]{false, false, false, true, true, false})
                .setContentTextSize(20)//滚轮文字大小
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .build();*/
    }

    private void initData() {
        Intent intent = getIntent();
        if (null == intent) {
            return;
        }
        Bundle bundle = intent.getExtras();
        if (bundle != null && bundle.containsKey("AlarmBean")) {
            bean = (AlarmBean) bundle.getSerializable("AlarmBean");
        }
        if (null != bean) {
            int tMin = bean.getAlarmTime();
            String hour;
            String min;
            if (tMin != 0) {
                hour = tMin / 60 < 10 ? "0" + (tMin / 60) : tMin / 60 + "";
                min = tMin % 60 < 10 ? "0" + (tMin % 60) : tMin % 60 + "";
                curTime = hour + ":" + min;
                defaultHour = Integer.parseInt(hour);
                defaultMinute = Integer.parseInt(min);
            } else {
                curTime = "--";
            }
            weekTip = bean.getWeekTip();

            int flag = bean.getFlag();
            if (flag == 0) {
                tvWeek.setText(getString(R.string.str_week_onetime));
            } else if (flag == 1) {
                tvWeek.setText(getString(R.string.str_everyday));
            } else {
                if (bean.getWeekValue() != null && !bean.getWeekValue().isEmpty()) {
                    if (bean.getWeekValue().equals("7f")) {
                        tvWeek.setText(getString(R.string.str_everyday));
                    } else {
                        String[] str = Unit.toBinaryString2(bean.getWeekValue()).split("");
                        if (str.length >= 9) {
                            StringBuilder tip = new StringBuilder();
                            if (str[2].equals("1")) {
                                tip.append(getString(R.string.str_week1)).append(",");
                            }
                            if (str[3].equals("1")) {
                                tip.append(getString(R.string.str_week2)).append(",");
                            }
                            if (str[4].equals("1")) {
                                tip.append(getString(R.string.str_week3)).append(",");
                            }
                            if (str[5].equals("1")) {
                                tip.append(getString(R.string.str_week4)).append(",");
                            }
                            if (str[6].equals("1")) {
                                tip.append(getString(R.string.str_week5)).append(",");
                            }
                            if (str[7].equals("1")) {
                                tip.append(getString(R.string.str_week6)).append(",");
                            }
                            if (str[8].equals("1")) {
                                tip.append(getString(R.string.str_week7)).append(",");
                            }
                            tvWeek.setText(tip.deleteCharAt(tip.length() - 1).toString());
                        }
                    }
                }
            }
        }
        tvTime.setText(curTime);
    }

    private void initListener() {
        MyOnClickListener onClickListener = new MyOnClickListener();
        rlTime.setOnClickListener(onClickListener);
        rlWeek.setOnClickListener(onClickListener);
        ivDelete.setOnClickListener(onClickListener);
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
            } else if (i == R.id.iv_delete) {
                deleteAlarm();
            } else if (i == R.id.iv_ok) {
                setAlarm();
            }
        }
    }

    //选择时间
    private void chooseTime() {
//        pvTime.show();
        Intent intent = new Intent(AlarmDetailActivity.this, NewChooseTimeActivity.class);
        intent.putExtra("defaultHour", defaultHour);
        intent.putExtra("defaultMinute", defaultMinute);
        startActivityForResult(intent, Constants.ALARM_TIME_REQUEST_CODE);
    }

    //选择星期几
    private void chooseWeek() {
        Intent intent = new Intent(AlarmDetailActivity.this, ChooseWeekActivity.class);
        intent.putExtra("curTime", curTime);
        intent.putExtra("defaultWeek", bean.getWeekValue());
        startActivityForResult(intent, Constants.ALARM_WEEK_REQUEST_CODE);
    }

    //删除闹钟
    private void deleteAlarm() {
        //1.删除数据库对应闹钟，2.删除原先这个id对应的闹钟
        AlarmDao.getInstance().delete(bean.getID());
        deleteIDAlarm(bean);
        finish();
    }

    //设置闹钟
    private void setAlarm() {
        //1.更新数据库，2.删除原先这个id对应的闹钟，3重新设置闹钟
        updateDB(bean);
        deleteIDAlarm(bean);
        setSystemAlarm(bean);
        finish();
    }

    private void updateDB(AlarmBean bean) {
        AlarmDao.getInstance().updata(bean);
    }

    private void deleteIDAlarm(AlarmBean bean) {
        if (bean.getFlag() == 0) {
            AlarmClock.deleteAlarmReceiver(this, Integer.parseInt(bean.getID()));
        } else {
            String[] str = Unit.toBinaryString2(bean.getWeekValue()).split("");
            for (int i = 0; i < str.length; i++) {
                if (i >= 2 && str[i].equals("1") && bean.getIsOff() == 0) {
                    AlarmClock.deleteAlarmReceiver(AlarmDetailActivity.this,
                            parseInt(bean.getID() + "" + (i - 1)));
                }
            }
        }
    }

    /**
     * 例子：str = {"",0,1,1,1,1,1,1,1}
     */
    private void setSystemAlarm(AlarmBean bean) {
        if (bean.getFlag() == 0) {
            AlarmClock.setAlarmReceiver(AlarmDetailActivity.this, 0, bean.getAlarmTime() / 60, bean.getAlarmTime() % 60,
                    Integer.parseInt(bean.getID()), 0, getString(R.string.str_alarm_tip), 0);
        } else {
            String[] str = Unit.toBinaryString2(bean.getWeekValue()).split("");
            for (int i = 0; i < str.length; i++) {
                if (i >= 2 && str[i].equals("1") && bean.getIsOff() == 0) {
                    AlarmClock.setAlarmReceiver(AlarmDetailActivity.this, bean.getFlag(), bean.getAlarmTime() / 60, bean.getAlarmTime() % 60,
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
            bean.setWeekTip(weekTip);
            bean.setWeekValue(week);
            //确定闹钟类型（一次性，周期性）
            if (weekTip.equals(getString(R.string.str_week_onetime))) {//一次性
                bean.setFlag(0);
            } else {
                bean.setFlag(2);
            }
        }
    }
}
