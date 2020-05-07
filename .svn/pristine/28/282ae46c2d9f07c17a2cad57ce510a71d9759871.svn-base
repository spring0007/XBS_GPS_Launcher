package com.sczn.wearlauncher.setting;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.kyleduo.switchbutton.SwitchButton;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.AbsActivity;
import com.sczn.wearlauncher.menu.activity.StyleChooseActivity;

/**
 * Description:设置页面
 * Created by Bingo on 2019/1/19.
 */
public class SettingActivity extends AbsActivity implements View.OnClickListener {

    private final String TAG = "SettingActivity";

    private WifiManager mWifiManager;

    private SwitchButton mWlanEnableView;
    private View mClockStyleLayout;
    private View mThemeStyleLayout;
    private View mBindLayout;
    private View mVolumeLayout;
    private View mProfileLayout;
    private View mAboutLayout;
    private View mFotaLayout;
    private View mShutdownLayout;
    private View mBrightnessLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        initView();
        initData();
    }

    private void initView() {
        mWlanEnableView = findViewById(R.id.wlan_enable_view);
        mWlanEnableView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final boolean checked = mWlanEnableView.isChecked();
                mWifiManager.setWifiEnabled(checked);
            }
        });
        mClockStyleLayout = findViewById(R.id.clock_style_layout);
        mThemeStyleLayout = findViewById(R.id.theme_style_layout);
        mBindLayout = findViewById(R.id.bind_layout);
        mVolumeLayout = findViewById(R.id.volume_layout);
        mBrightnessLayout = findViewById(R.id.brightness_layout);
        mProfileLayout = findViewById(R.id.profile_layout);
        mAboutLayout = findViewById(R.id.about_layout);
        mFotaLayout = findViewById(R.id.fota_layout);
        mShutdownLayout = findViewById(R.id.shutdown_layout);
        findViewById(R.id.wlan_layout).setOnClickListener(this);
        mClockStyleLayout.setOnClickListener(this);
        mThemeStyleLayout.setOnClickListener(this);
        mBindLayout.setOnClickListener(this);
        mVolumeLayout.setOnClickListener(this);
        mProfileLayout.setOnClickListener(this);
        mAboutLayout.setOnClickListener(this);
        mFotaLayout.setOnClickListener(this);
        mShutdownLayout.setOnClickListener(this);
        mBrightnessLayout.setOnClickListener(this);
    }

    private void initData() {
        final boolean wifiEnabled = mWifiManager.isWifiEnabled();
        mWlanEnableView.setCheckedImmediatelyNoEvent(wifiEnabled);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wlan_layout:
                startActivity(new Intent(SettingActivity.this, WLANListActivity.class));
                break;
            case R.id.clock_style_layout:
                /**
                 * 选择不同的表盘
                 */
                Intent intent = new Intent(SettingActivity.this, SelectIndexSkinActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.theme_style_layout:
                startActivity(new Intent(SettingActivity.this, StyleChooseActivity.class));
                break;
            case R.id.bind_layout:
                startActivity(new Intent(SettingActivity.this, BindDeviceActivity.class));
                break;
            case R.id.volume_layout:
                startActivity(new Intent(SettingActivity.this, VolumeActivity.class));
                break;
            case R.id.brightness_layout:
                startActivity(new Intent(SettingActivity.this, BrightnessActivity.class));
                break;
            case R.id.profile_layout:
                startActivity(new Intent(SettingActivity.this, RingerModeActivity.class));
                break;
            case R.id.about_layout:
                startActivity(new Intent(SettingActivity.this, AboutActivity.class));
                break;
            case R.id.fota_layout:
                showButtonTip(getString(R.string.already_upgrade), 2000);
                break;
            case R.id.shutdown_layout:
                startActivity(new Intent(SettingActivity.this, ShutDownActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
