package com.sczn.wearlauncher.friend.activity;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.base.AbsActivity;
import com.sczn.wearlauncher.friend.utils.Constant;
import com.sczn.wearlauncher.location.LocationCallBackHelper;
import com.sczn.wearlauncher.location.bean.LocationInfo;
import com.sczn.wearlauncher.location.impl.OnLocationChangeListener;
import com.sczn.wearlauncher.player.SoundPoolUtil;
import com.sczn.wearlauncher.socket.WaterSocketManager;
import com.sczn.wearlauncher.socket.command.CommandResultCallback;
import com.sczn.wearlauncher.socket.command.gprs.post.PP;
import com.sczn.wearlauncher.sp.SPUtils;
import com.sczn.wearlauncher.util.MoreFastEvent;
import com.sczn.wearlauncher.util.NetworkUtils;

import java.lang.ref.WeakReference;

/**
 * Created by k.liang on 2018/4/23 14:34
 */

public class ShakeActivity extends AbsActivity implements SensorEventListener {

    private final String TAG = "ShakeActivity";

    private ImageView btn_setting;
    private LinearLayout linear_top;
    private ImageView shake_img_top_line;
    private LinearLayout linear_bottom;
    private ImageView shake_img_bottom_line;

    //loading
    private ProgressBar loading_progressbar;
    private TextView tv_search;

    private SensorManager mSensorManager;  //sensor管理器
    private Sensor mAccelerometerSensor;
    private Vibrator mVibrator;   //震动
    private boolean isShake = false;  //记录摇动状态
    private ShakeHandler mHandler;

    private static final int START_SHAKE = 0x1;
    private static final int AGAIN_SHAKE = 0x2;
    private static final int END_SHAKE = 0x3;

    private long shakeTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);
        initView();
        initData();
    }

    public void initView() {
        btn_setting = findViewById(R.id.btn_setting);
        btn_setting.setOnClickListener(listener);

        linear_top = findViewById(R.id.linear_top);
        linear_top.setOnClickListener(listener);

        linear_bottom = findViewById(R.id.linear_bottom);
        shake_img_top_line = findViewById(R.id.shake_img_top_line);
        shake_img_bottom_line = findViewById(R.id.shake_img_bottom_line);

        loading_progressbar = findViewById(R.id.loading_progressbar);
        tv_search = findViewById(R.id.tv_search);

        shake_img_top_line.setVisibility(View.GONE);
        shake_img_bottom_line.setVisibility(View.GONE);

        mHandler = new ShakeHandler(this);
    }

    public void initData() {
        //获取Vibrator震动服务
        mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
    }

    public View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_setting:
                    Intent intent = new Intent(ShakeActivity.this, SettingActivity.class);
                    startActivity(intent);
                    break;
                case R.id.linear_top:
                    //测试使用
                    //shake();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onSensorChanged(SensorEvent event) {
        int type = event.sensor.getType();
        if (type == Sensor.TYPE_ACCELEROMETER) {
            //获取三个方向值
            float[] values = event.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];
            if ((Math.abs(x) > 17 || Math.abs(y) > 17 || Math.abs(z) > 17) && !isShake) {
                shake();
            }
        }
    }

    /**
     * 摇一摇
     * 定义2秒内不能重复触发
     */
    private void shake() {
        MoreFastEvent.getInstance().event(2000, shakeTime, new MoreFastEvent.onEventCallBackWithTimeListener() {
            @Override
            public void onCallback(long eventTime) {
                shakeTime = eventTime;
                if (!NetworkUtils.isNetworkAvailable(LauncherApp.getAppContext())) {
                    showButtonTip(getString(R.string.bad_network));
                    return;
                }
                isShake = true;
                //实现摇动逻辑, 摇动后进行震动
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            //开始震动 发出提示音 展示动画效果
                            mHandler.obtainMessage(START_SHAKE).sendToTarget();
                            Thread.sleep(500);
                            //再来一次震动提示
                            mHandler.obtainMessage(AGAIN_SHAKE).sendToTarget();
                            Thread.sleep(500);
                            mHandler.obtainMessage(END_SHAKE).sendToTarget();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
                //开始寻找朋友
                doOnSearchFriends();
            }
        });
    }

    /**
     * 开始寻找朋友
     * 1.先定位
     */
    private void doOnSearchFriends() {
        LocationCallBackHelper.getInstance().startBS(LauncherApp.getAppContext(), new OnLocationChangeListener<LocationInfo>() {
            @Override
            public void success(LocationInfo info) {
                PP pp = new PP(info, new CommandResultCallback() {
                    @Override
                    public void onSuccess(String baseObtain) {
                        endShake(true);
                    }

                    @Override
                    public void onFail() {
                        endShake(false);
                    }
                });
                WaterSocketManager.getInstance().send(pp);
            }

            @Override
            public void fail() {
                endShake(false);
            }
        });
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * 开启 摇一摇动画
     *
     * @param isBack 是否是返回初识状态
     */
    private void startAnimation(boolean isBack) {
        //动画坐标移动的位置的类型是相对自己的
        int type = Animation.RELATIVE_TO_SELF;
        float topFromY;
        float topToY;
        float bottomFromY;
        float bottomToY;
        if (isBack) {
            topFromY = -0.5f;
            topToY = 0;
            bottomFromY = 0.5f;
            bottomToY = 0;
        } else {
            topFromY = 0;
            topToY = -0.5f;
            bottomFromY = 0;
            bottomToY = 0.5f;
        }
        //上面图片的动画效果
        TranslateAnimation topAnim = new TranslateAnimation(type, 0, type, 0, type, topFromY, type, topToY
        );
        topAnim.setDuration(200);
        topAnim.setFillAfter(true);
        TranslateAnimation bottomAnim = new TranslateAnimation(type, 0, type, 0, type, bottomFromY, type, bottomToY
        );
        bottomAnim.setDuration(200);
        bottomAnim.setFillAfter(true);
        if (isBack) {
            bottomAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {//当动画结束后,将中间两条线GONE掉,不让其占位
                    shake_img_top_line.setVisibility(View.GONE);
                    shake_img_bottom_line.setVisibility(View.GONE);
                    loading_progressbar.setVisibility(View.VISIBLE);
                    tv_search.setText(getString(R.string.search_friends));
                }
            });
        } else {
            loading_progressbar.setVisibility(View.VISIBLE);
            tv_search.setText(getString(R.string.search_friends));
        }
        //设置动画
        linear_top.startAnimation(topAnim);
        linear_bottom.startAnimation(bottomAnim);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //获取 SensorManager 负责管理传感器
        mSensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
        if (mSensorManager != null) {
            //获取加速度传感器
            mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (mAccelerometerSensor != null) {
                mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_UI);
            }
        }
    }

    /**
     * 没有好友.结束动画
     */
    private void endShake(final boolean hasFriends) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                shake_img_top_line.setVisibility(View.GONE);
                shake_img_bottom_line.setVisibility(View.GONE);
                loading_progressbar.setVisibility(View.GONE);
                if (hasFriends) {
                    tv_search.setText(getString(R.string.already_add_group));
                } else {
                    tv_search.setText(getString(R.string.not_search_friends));
                }
            }
        });
    }

    @Override
    protected void onPause() {
        // 务必要在pause中注销 mSensorManager
        // 否则会造成界面退出后摇一摇依旧生效的bug
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
        super.onPause();
    }

    private class ShakeHandler extends Handler {
        private WeakReference<ShakeActivity> mReference;
        private ShakeActivity activity;

        public ShakeHandler(ShakeActivity mmActivity) {
            super();
            mReference = new WeakReference<>(mmActivity);
            activity = mReference.get();
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START_SHAKE:
                    if (getVibrationSwithOn()) {
                        activity.mVibrator.vibrate(300);
                    }
                    if (getVoiceSwithOn()) {
                        SoundPoolUtil.getInstance().loadResAndPlay(R.raw.shake, activity);
                    }
                    activity.shake_img_top_line.setVisibility(View.VISIBLE);
                    activity.shake_img_bottom_line.setVisibility(View.VISIBLE);
                    activity.startAnimation(false);
                    break;
                case AGAIN_SHAKE:
                    if (getVibrationSwithOn()) {
                        activity.mVibrator.vibrate(300);
                    }
                    break;
                case END_SHAKE:
                    activity.isShake = false;
                    activity.startAnimation(true);
                    break;
            }
        }
    }

    private boolean getVoiceSwithOn() {
        return (boolean) SPUtils.getParam(ShakeActivity.this, Constant.VOICE_SWITCH_TYPE, false);
    }

    private boolean getVibrationSwithOn() {
        return (boolean) SPUtils.getParam(ShakeActivity.this, Constant.VIBRATION_SWITCH_TYPE, false);
    }
}
