package cn.com.waterworld.alarmclocklib;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TimePicker;

import java.util.Date;

import cn.com.waterworld.alarmclocklib.util.Constants;

/**
 * Created by wangfeng on 2018/6/11.
 * 选择时间
 */

public class NewChooseTimeActivity extends BaseActivity implements View.OnClickListener {
    private TimePicker timePicker;
    private ImageView ivCancle;
    private ImageView ivOk;
    private int chooseHour;
    private int chooseMinute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_time_new);

        initData();
        initView();
    }

    private void initData() {
        Intent intent = getIntent();
        if (null == intent) {
            return;
        }
        Date curDate = new Date();
        curDate.setTime(System.currentTimeMillis());
        chooseHour = intent.getIntExtra("defaultHour", curDate.getHours());
        chooseMinute = intent.getIntExtra("defaultMinute", curDate.getMinutes());
    }

    private void initView() {
        timePicker = (TimePicker) findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        ivCancle = (ImageView) findViewById(R.id.iv_cancle);
        ivOk = (ImageView) findViewById(R.id.iv_ok);
        //得到当前时间：时，分
//        Date curDate = new Date();
//        curDate.setTime(System.currentTimeMillis());
//        chooseHour=curDate.getHours();
//        chooseMinute=curDate.getMinutes();
        timePicker.setCurrentHour(chooseHour);
        timePicker.setCurrentMinute(chooseMinute);

        ivCancle.setOnClickListener(this);
        ivOk.setOnClickListener(this);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                chooseHour = hourOfDay;
                chooseMinute = minute;
            }

        });
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
        String houtTip = String.valueOf(chooseHour);
        String minuteTip = String.valueOf(chooseMinute);
        if (chooseHour < 10) {
            houtTip = "0" + houtTip;
        }
        if (chooseMinute < 10) {
            minuteTip = "0" + minuteTip;
        }
        intent.putExtra("hour", houtTip);
        intent.putExtra("minute", minuteTip);
        setResult(Constants.ALARM_TIME_RESULT_CODE, intent);
        finish();
    }
}
