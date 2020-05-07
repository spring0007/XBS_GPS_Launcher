package com.sczn.wearlauncher.status;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.absFragment;
import com.sczn.wearlauncher.status.view.BatteryIcon;
import com.sczn.wearlauncher.status.view.SimIcon;
import com.sczn.wearlauncher.status.view.WifiIcon;

/**
 * 电量WiFi信号等页面
 */
public class StatusFragmentSecond extends absFragment {
    private final static String TAG = StatusFragmentSecond.class.getSimpleName();

    private BatteryIcon mBatteryIcon;
    private SimIcon mSimIcon;
    private WifiIcon mWifiIcon;

    @Override
    protected int getLayoutResouceId() {
        // TODO Auto-generated method stub
        return R.layout.fragment_status_second;
    }

    @Override
    protected void initView() {
        // TODO Auto-generated method stub
        mBatteryIcon = findViewById(R.id.status_battery);
        mSimIcon = findViewById(R.id.status_sim);
        mWifiIcon = findViewById(R.id.status_wifi);
    }

    @Override
    protected void initData() {
        // TODO Auto-generated method stub
    }

    @Override
    protected void startFreshData() {
        // TODO Auto-generated method stub
        super.startFreshData();
        mWifiIcon.startFresh();
        mSimIcon.startFresh();
        mBatteryIcon.startFresh();
    }

    @Override
    protected void endFreshData() {
        // TODO Auto-generated method stub
        super.endFreshData();
        mWifiIcon.stopFresh();
        mSimIcon.stopFresh();
        mBatteryIcon.stopFresh();
    }
}
