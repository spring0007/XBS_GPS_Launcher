package com.sczn.wearlauncher.card.healthalarm;

import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.player.PlayManager;

public class ActivityAlarm extends HealthAlarmActivity implements OnClickListener {

    public static void startActivity(Context context) {
        Intent i = new Intent(context, ActivityAlarm.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public static final int TIME_AUTO_DISMISS = 50 * 1000;

    private Handler mHandler;
    private TextView mAlarmContent;
    private TextView mAlarmOk;

    private PowerManager mPowerManager;
    private WakeLock mWakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getSimpleName());
        mWakeLock.acquire();

        mHandler = new Handler();
        //mHandler.postDelayed(disMissSelf, TIME_AUTO_DISMISS);
        startRing();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        setIntent(intent);

        mHandler.removeCallbacks(disMissSelf);
        //mHandler.postDelayed(disMissSelf, TIME_AUTO_DISMISS);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mHandler.removeCallbacks(disMissSelf);
        mHandler.postDelayed(disMissSelf, TIME_AUTO_DISMISS);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mHandler.postDelayed(disMissSelf, 1000);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        if (mWakeLock != null && mWakeLock.isHeld()) {
            mWakeLock.release();
        }
        mHandler.removeCallbacks(disMissSelf);
        stopRing();
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.activity_card_healthalarm_alarm;
    }

    @Override
    protected void initView() {
        // TODO Auto-generated method stub
        mAlarmContent = (TextView) findViewById(R.id.alarm_content);
        mAlarmOk = (TextView) findViewById(R.id.alarm_ok);
    }

    @Override
    protected void initData() {
        // TODO Auto-generated method stub
        mAlarmOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.alarm_ok:
                finish();
                break;
            default:
                break;
        }
    }

    private Runnable disMissSelf = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            ActivityAlarm.this.finish();
        }
    };

    private void startRing() {
        // 获取alarm uri
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        PlayManager.getInstance().play(alert);
    }

    private void stopRing() {
        PlayManager.getInstance().stop();
    }
}
