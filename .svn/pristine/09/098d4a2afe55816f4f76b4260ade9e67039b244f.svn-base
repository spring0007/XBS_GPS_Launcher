package com.sczn.wearlauncher.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.view.WindowManager;
import android.widget.TextView;

import com.sczn.wearlauncher.Config;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.AbsActivity;
import com.sczn.wearlauncher.player.PlayManager;
import com.sczn.wearlauncher.player.SoundPoolUtil;

/**
 * Description:闹钟响铃类
 * Created by Bingo on 2019/1/24.
 */
public class AlarmClockRingActivity extends AbsActivity {

    private final static int MSG_STOP_RINGING = 0x100;
    // 停止响铃
    private final static int MSG_ALARM_RINGING = 0x101;
    // 响铃时间
    private final static int MSG_ALARM_RINGING_TIME_OUT = 10000;

    private TextView tips_msg = null;
    protected TextView tips_time = null;

    private PowerManager pm = null;
    private PowerManager.WakeLock mWakelock = null;


    private String content = null;
    private boolean isLocked = false;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_ALARM_RINGING:
                    stopPlay();
                    finish();
                    break;
                case MSG_STOP_RINGING:
                    SoundPoolUtil.release();
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initView();
        initDate();
        initPower();
    }

    private void initView() {
        tips_msg = findViewById(R.id.tips_msg);
        tips_time = findViewById(R.id.tips_time);
    }

    private void initDate() {
        //根据意图获取提醒的内容和提醒的时间
        Intent intent = getIntent();
        content = intent.getStringExtra("content");
        int type = intent.getIntExtra("type", 0);
        int hour = intent.getIntExtra("hour", 0);
        int minute = intent.getIntExtra("minute", 0);

        String hourTip = String.valueOf(hour);
        String minuteTip = String.valueOf(minute);
        if (hour / 10 == 0) {//0-9
            hourTip = "0" + hour;
        }
        if (minute / 10 == 0) {//0-9
            minuteTip = "0" + minute;
        }
        tips_msg.setText(content);
        tips_time.setText(hourTip + ":" + minuteTip);
        initPlaySound();
    }

    @SuppressLint("InvalidWakeLockTag")
    private void initPower() {
        pm = (PowerManager) getSystemService(POWER_SERVICE);
        if (pm == null) {
            return;
        }
        mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "SimpleTimer");
        mWakelock.acquire(10 * 60 * 1000L /*10 minutes*/);//点亮
        isLocked = true;
    }

    /**
     * 初始化声音播放数据
     */
    private void initPlaySound() {
        //停止其他语音
        PlayManager.getInstance().stop();
        /**
         * 电话响起了繁忙中,闹钟的铃声不需要响
         */
        if (!Config.isPhoneRingState) {
            SoundPoolUtil.getInstance().loadResAndPlay(this, R.raw.schedule_sound, 10);
        }
        handler.sendEmptyMessageDelayed(MSG_ALARM_RINGING, MSG_ALARM_RINGING_TIME_OUT);
    }

    private void stopPlay() {
        if (handler.hasMessages(MSG_ALARM_RINGING)) {
            handler.removeMessages(MSG_ALARM_RINGING);
        }
        SoundPoolUtil.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isLocked) {
            mWakelock.release();//释放锁
            isLocked = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlay();
    }
}
