package cn.com.waterworld.alarmclocklib;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import cn.com.waterworld.alarmclocklib.util.Constants;
import cn.com.waterworld.alarmclocklib.util.DateUtil;
import cn.com.waterworld.alarmclocklib.util.StringUtils;
import cn.com.waterworld.alarmclocklib.util.Unit;

/**
 * Created by wangfeng on 2018/6/11.
 * 选择星期几
 */

public class ChooseWeekActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvCurTime;
    private CheckBox cbOneTime;
    private CheckBox cbWeek1;
    private CheckBox cbWeek2;
    private CheckBox cbWeek3;
    private CheckBox cbWeek4;
    private CheckBox cbWeek5;
    private CheckBox cbWeek6;
    private CheckBox cbWeek7;
    private ImageView ivCancle;
    private ImageView ivOk;
    private String week;
    private String weekTip;
    private String curTime;
    private String defaultWeek;

    private String[] WEEK_VALUE = new String[]{"0", "0", "0", "0", "0", "0", "0", "0"};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_week);

        initData();
        initView();
        initListener();
    }

    private void initData() {
        Intent intent = getIntent();
        if (null != intent) {
            curTime = intent.getStringExtra("curTime");
            defaultWeek = intent.getStringExtra("defaultWeek");
        }
    }

    private void initView() {
        tvCurTime = (TextView) findViewById(R.id.tv_time_tip);
        cbOneTime = (CheckBox) findViewById(R.id.cb_onetime);
        cbWeek1 = (CheckBox) findViewById(R.id.cb_week1);
        cbWeek2 = (CheckBox) findViewById(R.id.cb_week2);
        cbWeek3 = (CheckBox) findViewById(R.id.cb_week3);
        cbWeek4 = (CheckBox) findViewById(R.id.cb_week4);
        cbWeek5 = (CheckBox) findViewById(R.id.cb_week5);
        cbWeek6 = (CheckBox) findViewById(R.id.cb_week6);
        cbWeek7 = (CheckBox) findViewById(R.id.cb_week7);
        ivCancle = (ImageView) findViewById(R.id.iv_cancle);
        ivOk = (ImageView) findViewById(R.id.iv_ok);
        if (!StringUtils.isEmpty(curTime)) {
            tvCurTime.setText(curTime);
        }
    }

    private void initListener() {
        MyCheckedChangeListener myCheckedChangeListener = new MyCheckedChangeListener();
        cbOneTime.setOnCheckedChangeListener(myCheckedChangeListener);
        cbWeek1.setOnCheckedChangeListener(myCheckedChangeListener);
        cbWeek2.setOnCheckedChangeListener(myCheckedChangeListener);
        cbWeek3.setOnCheckedChangeListener(myCheckedChangeListener);
        cbWeek4.setOnCheckedChangeListener(myCheckedChangeListener);
        cbWeek5.setOnCheckedChangeListener(myCheckedChangeListener);
        cbWeek6.setOnCheckedChangeListener(myCheckedChangeListener);
        cbWeek7.setOnCheckedChangeListener(myCheckedChangeListener);
        ivCancle.setOnClickListener(this);
        ivOk.setOnClickListener(this);
        //设置默认显示周
        if (!StringUtils.isEmpty(defaultWeek)) {
            String[] str = Unit.toBinaryString2(defaultWeek).split("");
            if (defaultWeek.equals("0")) {//单次
                cbOneTime.setChecked(true);
            } else {
                for (int i = 0; i < str.length; i++) {
                    if (i >= 2 && str[i].equals("1")) {
                        if (2 == i) {
                            cbWeek1.setChecked(true);
                        } else if (3 == i) {
                            cbWeek2.setChecked(true);
                        } else if (4 == i) {
                            cbWeek3.setChecked(true);
                        } else if (5 == i) {
                            cbWeek4.setChecked(true);
                        } else if (6 == i) {
                            cbWeek5.setChecked(true);
                        } else if (7 == i) {
                            cbWeek6.setChecked(true);
                        } else if (8 == i) {
                            cbWeek7.setChecked(true);
                        }
                    }
                }
            }
        }

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_cancle) {
            finish();

        } else if (i == R.id.iv_ok) {
            if (!hasOneChecked()) {
                Toast.makeText(getApplicationContext(), getString(R.string.str_no_week_tip), Toast.LENGTH_SHORT).show();
                return;
            }
            if (!cbOneTime.isChecked()) {
                setWeekValue(1, cbWeek1);
                setWeekValue(2, cbWeek2);
                setWeekValue(3, cbWeek3);
                setWeekValue(4, cbWeek4);
                setWeekValue(5, cbWeek5);
                setWeekValue(6, cbWeek6);
                setWeekValue(7, cbWeek7);
                week = WEEK_VALUE[0] + WEEK_VALUE[1] + WEEK_VALUE[2] + WEEK_VALUE[3] + WEEK_VALUE[4] +
                        WEEK_VALUE[5] + WEEK_VALUE[6] + WEEK_VALUE[7];
                if (week.equals("01111111")) {
                    weekTip = getString(R.string.str_everyday);
                } else {
                    if (WEEK_VALUE[1].equals("1")) {
                        weekTip = getString(R.string.str_week1);
                    } else {
                        weekTip = "";
                    }
                    if (WEEK_VALUE[2].equals("1")) {
                        if (StringUtils.isEmpty(weekTip)) {
                            weekTip += getString(R.string.str_week2);
                        } else {
                            weekTip += "," + getString(R.string.str_week2);
                        }
                    } else {
                        weekTip += "";
                    }
                    if (WEEK_VALUE[3].equals("1")) {
                        if (StringUtils.isEmpty(weekTip)) {
                            weekTip += getString(R.string.str_week3);
                        } else {
                            weekTip += "," + getString(R.string.str_week3);
                        }
                    } else {
                        weekTip += "";
                    }
                    if (WEEK_VALUE[4].equals("1")) {
                        if (StringUtils.isEmpty(weekTip)) {
                            weekTip += getString(R.string.str_week4);
                        } else {
                            weekTip += "," + getString(R.string.str_week4);
                        }
                    } else {
                        weekTip += "";
                    }
                    if (WEEK_VALUE[5].equals("1")) {
                        if (StringUtils.isEmpty(weekTip)) {
                            weekTip += getString(R.string.str_week5);
                        } else {
                            weekTip += "," + getString(R.string.str_week5);
                        }
                    } else {
                        weekTip += "";
                    }
                    if (WEEK_VALUE[6].equals("1")) {
                        if (StringUtils.isEmpty(weekTip)) {
                            weekTip += getString(R.string.str_week6);
                        } else {
                            weekTip += "," + getString(R.string.str_week6);
                        }
                    } else {
                        weekTip += "";
                    }
                    if (WEEK_VALUE[7].equals("1")) {
                        if (StringUtils.isEmpty(weekTip)) {
                            weekTip += getString(R.string.str_week7);
                        } else {
                            weekTip += "," + getString(R.string.str_week7);
                        }
                    } else {
                        weekTip += "";
                    }
                }
            } else {
                week = "00000000";
                weekTip = getString(R.string.str_week_onetime);
            }
            startIntent(week);

        }
    }

    private class MyCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
            int i = compoundButton.getId();
            if (i == R.id.cb_onetime) {
                cbOneTime.setChecked(checked);
                Date curDate = new Date();
                curDate.setTime(System.currentTimeMillis());
                int weekIndex = DateUtil.getWeekIndex(curDate);
                switch (weekIndex) {
                    case 1:
                        week = "0,0,0,0,0,0,0,1";
                        break;
                    case 2:
                        week = "0,1,0,0,0,0,0,0";
                        break;
                    case 3:
                        week = "0,0,1,0,0,0,0,0";
                        break;
                    case 4:
                        week = "0,0,0,1,0,0,0,0";
                        break;
                    case 5:
                        week = "0,0,0,0,1,0,0,0";
                        break;
                    case 6:
                        week = "0,0,0,0,0,1,0,0";
                        break;
                    case 7:
                        week = "0,0,0,0,0,0,1,0";
                        break;
                }

                if (checked) {
                    cbWeek1.setChecked(false);
                    cbWeek2.setChecked(false);
                    cbWeek3.setChecked(false);
                    cbWeek4.setChecked(false);
                    cbWeek5.setChecked(false);
                    cbWeek6.setChecked(false);
                    cbWeek7.setChecked(false);
                }

            } else if (i == R.id.cb_week1) {
                cbWeek1.setChecked(checked);
                if (checked) {
                    cbOneTime.setChecked(false);
                }

            } else if (i == R.id.cb_week2) {
                cbWeek2.setChecked(checked);
                if (checked) {
                    cbOneTime.setChecked(false);
                }

            } else if (i == R.id.cb_week3) {
                cbWeek3.setChecked(checked);
                if (checked) {
                    cbOneTime.setChecked(false);
                }

            } else if (i == R.id.cb_week4) {
                cbWeek4.setChecked(checked);
                if (checked) {
                    cbOneTime.setChecked(false);
                }

            } else if (i == R.id.cb_week5) {
                cbWeek5.setChecked(checked);
                if (checked) {
                    cbOneTime.setChecked(false);
                }

            } else if (i == R.id.cb_week6) {
                cbWeek6.setChecked(checked);
                if (checked) {
                    cbOneTime.setChecked(false);
                }

            } else if (i == R.id.cb_week7) {
                cbWeek7.setChecked(checked);
                if (checked) {
                    cbOneTime.setChecked(false);
                }

            }
        }
    }

    private boolean hasOneChecked() {
        if (cbOneTime.isChecked() || cbWeek1.isChecked() || cbWeek2.isChecked() || cbWeek3.isChecked() || cbWeek4.isChecked() || cbWeek5.isChecked() || cbWeek6.isChecked() || cbWeek7.isChecked()) {
            return true;
        }
        return false;
    }

    private void setWeekValue(int i, CheckBox checkBox) {
        if (checkBox.isChecked()) {
            WEEK_VALUE[i] = "1";
        } else {
            WEEK_VALUE[i] = "0";
        }
    }

    private void startIntent(String week) {
        Intent intent = new Intent();
        week = Unit.toHexString16(week);
        intent.putExtra("week", week);
        intent.putExtra("weekTip", weekTip);
        setResult(Constants.ALARM_WEEK_RESULT_CODE, intent);
        finish();
    }
}
