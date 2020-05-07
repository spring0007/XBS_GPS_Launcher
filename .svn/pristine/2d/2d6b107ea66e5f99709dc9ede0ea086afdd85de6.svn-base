package cn.com.waterworld.alarmclocklib;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Date;

import cn.com.waterworld.alarmclocklib.util.Constants;
import cn.com.waterworld.alarmclocklib.view.WheelView;

/**
 * Created by wangfeng on 2018/6/11.
 * 选择时间
 */

public class ChooseTimeActivity extends BaseActivity implements View.OnClickListener {
    private WheelView hourView;
    private WheelView minuteView;
    private ImageView ivCancle;
    private ImageView ivOk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_time);

        initView();
    }

    private void initView() {
        hourView = (WheelView) findViewById(R.id.hourView);
        minuteView = (WheelView) findViewById(R.id.minuteView);
        ivCancle = (ImageView) findViewById(R.id.iv_cancle);
        ivOk = (ImageView) findViewById(R.id.iv_ok);
        hourView.setData(getHourData());
        minuteView.setData(getMinuteData());
        //得到当前时间：时，分
        Date curDate = new Date();
        curDate.setTime(System.currentTimeMillis());
        int hour = curDate.getHours();
        int minute = curDate.getMinutes();
        hourView.setDefault(hour);
        minuteView.setDefault(minute);

        ivCancle.setOnClickListener(this);
        ivOk.setOnClickListener(this);
    }


    private ArrayList<String> getHourData() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i <= 23; i++) {
            if (i < 10) {
                list.add("0" + String.valueOf(i));
            } else {
                list.add(String.valueOf(i));
            }
        }
        return list;
    }

    private ArrayList<String> getMinuteData() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i <= 59; i++) {
            if (i < 10) {
                list.add("0" + String.valueOf(i));
            } else {
                list.add(String.valueOf(i));
            }
        }
        return list;
    }

    public String getHour() {
        if (hourView == null) {
            return null;
        }
        return hourView.getSelectedText();
    }

    public String getMinute() {
        if (minuteView == null) {
            return null;
        }
        return minuteView.getSelectedText();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_cancle) {
            finish();

        } else if (i == R.id.iv_ok) {
            startIntent();
        }
    }

    private void startIntent() {
        Intent intent = new Intent();
        intent.putExtra("hour", getHour());
        intent.putExtra("minute", getMinute());
        setResult(Constants.ALARM_TIME_RESULT_CODE, intent);
        finish();
    }
}
