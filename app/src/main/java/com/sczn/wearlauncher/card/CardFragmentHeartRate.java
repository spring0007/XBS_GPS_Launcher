package com.sczn.wearlauncher.card;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.absDialogFragment;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.card.heartrate.HeartRateActivity;
import com.sczn.wearlauncher.db.Provider.ColumnsHealthAlarm;
import com.sczn.wearlauncher.db.Provider.ColumnsHeartRate;

import java.util.ArrayList;
import java.util.Calendar;

public class CardFragmentHeartRate extends absDialogFragment {


    ArrayList<Integer> heart = new ArrayList<Integer>();
    // 申请电源锁，禁止休眠
    private WakeLock mWakeLock = null;

    public static CardFragmentHeartRate newInstance() {
        CardFragmentHeartRate fragment = new CardFragmentHeartRate();

        return fragment;

    }

    private Context mContext;
    private ImageView heartRateImageView;
    private TextView mHeartRateTextView, mBeginTest;
    private LinearLayout mHeartRatePart;

    private int mHeartRate = 0;
    private StringBuffer mRecords = new StringBuffer();
    private boolean istest = false;
    private boolean istested = false;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {

                case 1:
                    if (mHeartRate == 0) {
                        Toast.makeText(mContext, R.string.heart_rate_error, Toast.LENGTH_SHORT).show();
                        sendEmptyMessageDelayed(1, 30 * 1000);
                    }
                    break;
            }
        }

        ;
    };

    Runnable mrunnable = new Runnable() {
        @Override
        public void run() {
        }
    };


    @Override
    protected int getLayoutResouceId() {
        // TODO Auto-generated method stub
        mContext = getActivity();
        return R.layout.fragment_card_health;
    }


    protected void initView() {
        // TODO Auto-generated method stub
        heartRateImageView = findViewById(R.id.heart_rate_iv);
        mHeartRateTextView = findViewById(R.id.heart_rate_text);
        mBeginTest = findViewById(R.id.begin_test);
        mHeartRatePart = findViewById(R.id.heart_rate_num_part);
        mBeginTest.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (istest) {
                    return;
                }
                istest = true;
                beginTest();
            }
        });
    }


    protected void initData() {
        // TODO Auto-generated method stub
        mRootView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                // TODO Auto-generated method stub
                try {
                    Intent i = new Intent(mContext, HeartRateActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra(HeartRateActivity.ARG_RECORDS, mRecords.toString());
                    mContext.startActivity(i);
                } catch (Exception e) {
                    // TODO: handle exception
                    MxyLog.e(this, "gotoSleepActivity--" + e.toString());
                }

            }
        });
        mHeartRate = 0;
    }

    @Override
    protected void creatView() {
        // TODO Auto-generated method stub
        initView();
        initData();
        mRecords.setLength(0);
    }


    @Override
    protected void destorytView() {
        // TODO Auto-generated method stub
        istest = false;

        reSetState();
        releaseWakeLock();
    }


    void saveheartRate() {
        if (mHeartRate != 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MILLISECOND, 0);
            long time = calendar.getTimeInMillis();

            ContentValues values = new ContentValues();
            values.put(ColumnsHeartRate.COLUMNS_TIME, time);
            values.put(ColumnsHeartRate.COLUMNS_HEART_RATE, mHeartRate);

            LauncherApp.appContext.getContentResolver().insert(ColumnsHealthAlarm.CONTENT_URI, values);
            Intent heartIt = new Intent("WEARABLE_RECEIVER_HEARTRATE_DATA");
            heartIt.putExtra("heartRate", mHeartRate);
            mContext.sendBroadcast(heartIt);

        }
    }

    private void reSetState() {

        handler.removeMessages(1);
        if (istested) {
        }

        stopPlay();
        heartRateImageView.setBackground(null);
        heartRateImageView.setVisibility(View.VISIBLE);
        mHeartRatePart.setVisibility(View.GONE);
        mBeginTest.setText(R.string.click_test);
        heart.clear();
        mHeartRate = 0;
    }

    private void beginTest() {
        istested = true;
        mBeginTest.setText(R.string.heart_rate_is_testing);
        AnimationDrawable animationDrawable = (AnimationDrawable) getResources().getDrawable(R.drawable.health_heart_anim);
        heartRateImageView.setBackground(animationDrawable);
        animationDrawable.start();
        acquireWakeLock();
        startPlay();
    }

    public void startPlay() {
        if (handler != null) {

            handler.postDelayed(mrunnable, 1000);
        }
    }

    public void stopPlay() {
        if (handler != null) {
            handler.removeCallbacks(mrunnable);
        }
    }

    private void acquireWakeLock() {
        if (null == mWakeLock) {
            PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, this
                    .getClass().getCanonicalName());
            if (null != mWakeLock) {
                mWakeLock.acquire();
            }
        }
    }

    // 释放设备电源锁
    private void releaseWakeLock() {
        if (null != mWakeLock) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }


}
