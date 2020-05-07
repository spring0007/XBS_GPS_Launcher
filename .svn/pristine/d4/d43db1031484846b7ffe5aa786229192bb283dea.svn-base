package com.sczn.wearlauncher.setting;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import com.sczn.wearlauncher.BuildConfig;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.base.AbsActivity;
import com.sczn.wearlauncher.ruisocket.Util;
import com.sczn.wearlauncher.util.SystemPermissionUtil;

import java.util.Locale;

/**
 * 关于我们
 */
public class AboutActivity extends AbsActivity implements View.OnClickListener {
    private final static String TAG = "AboutActivity";

    private TextView mCompanyView;
    private TextView mVersionNameView;
    private TextView mImeiView;
    private TextView mAndroidView;

    private final static int START_CODE_VERIFY = 101;
    private final static long DURATION = 3 * 1000;//规定有效时间
    private final static int COUNTS = 12;//点击次数
    private long[] mHits = new long[COUNTS];

    //点击顺序暗码
    private final String TAG_CODE = "123321";
    private StringBuilder codeBuilder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
        initData();
    }

    private void initView() {
        mCompanyView = findViewById(R.id.company_view);
        mVersionNameView = findViewById(R.id.version_name_view);
        mImeiView = findViewById(R.id.imei_view);
        mAndroidView = findViewById(R.id.tv_android);
        mVersionNameView.setOnClickListener(this);
        mImeiView.setOnClickListener(this);
        mAndroidView.setOnClickListener(this);
    }

    private void initData() {
        codeBuilder = new StringBuilder();
        mCompanyView.setText(String.format(Locale.getDefault(), getString(R.string.setting_about_company), "WTWD"));
        mVersionNameView.setText(String.format(Locale.getDefault(), getString(R.string.setting_about_version_name), getSystemVersionName()));
        mImeiView.setText(String.format(Locale.getDefault(), getString(R.string.setting_about_imei), Util.getIMEI(getApplicationContext())));
    }

    private String getSystemVersionName() {
        return Build.DISPLAY;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.version_name_view:
                if (codeBuilder.length() == 0) {
                    mHandler.sendEmptyMessageDelayed(START_CODE_VERIFY, DURATION);
                    MxyLog.d(TAG, "开始计时校验暗码");
                }
                codeBuilder.append("1");
                if (codeBuilder.toString().equals(TAG_CODE)) {
                    doOnVerifyAction();
                }
                break;
            case R.id.imei_view:
                codeBuilder.append("2");
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                if (mHits[0] >= (SystemClock.uptimeMillis() - DURATION)) {
                    SystemPermissionUtil.callButton();
                }
                break;
            case R.id.tv_android:
                codeBuilder.append("3");
                gotoSettings();
                break;
        }
    }

    /**
     *
     */
    private void doOnVerifyAction() {
        startActivity(new Intent(AboutActivity.this, VerifyCodeActivity.class));
        finish();
    }

    /**
     * 清空codeBuilder
     */
    private void clearCode() {
        if (codeBuilder != null && codeBuilder.length() > 0) {
            codeBuilder.delete(0, codeBuilder.length());
        }
    }

    /**
     * 前往系统设置页面
     */
    private void gotoSettings() {
        if (BuildConfig.DEBUG) {
        }
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        if (mHits[0] >= (SystemClock.uptimeMillis() - DURATION)) {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
            finish();
        }
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case START_CODE_VERIFY:
                    clearCode();
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
