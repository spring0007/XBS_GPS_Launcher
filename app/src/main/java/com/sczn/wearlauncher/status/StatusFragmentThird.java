package com.sczn.wearlauncher.status;

import android.os.Bundle;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.absFragment;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.status.fragment.BrightnessFragment;
import com.sczn.wearlauncher.status.fragment.VolumeFragment;
import com.sczn.wearlauncher.status.view.BrightnessIcon;
import com.sczn.wearlauncher.status.view.BrightnessIcon.IBrightnessClick;
import com.sczn.wearlauncher.status.view.RaiseScreen;
import com.sczn.wearlauncher.status.view.VolumeIcon;
import com.sczn.wearlauncher.status.view.VolumeIcon.IVolumeClick;

public class StatusFragmentThird extends absFragment implements IVolumeClick, IBrightnessClick {
    private static final String TAG = StatusFragmentThird.class.getSimpleName();

    public static final String FRAGMENT_TAG_BRIGHTNESS = "status_tag_brightness";
    public static final String FRAGMENT_TAG_VOLUME = "status_tag_volume";

    public static final String ARG_IS_TMP = "is_tmp";

    public static StatusFragmentThird newInstance(boolean isTmp) {
        StatusFragmentThird fragment = new StatusFragmentThird();
        Bundle bdl = new Bundle();
        bdl.putBoolean(ARG_IS_TMP, isTmp);
        fragment.setArguments(bdl);
        return fragment;

    }

    private boolean isTmp = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Bundle bdl = getArguments();
        if (bdl != null) {
            isTmp = bdl.getBoolean(ARG_IS_TMP, false);
        }
    }

    private VolumeIcon mVolumeIcon;
    private BrightnessIcon mBrightnessIcon;
    private RaiseScreen mRaiseScreen;

    @Override
    protected int getLayoutResouceId() {
        // TODO Auto-generated method stub
        return R.layout.fragment_status_third;
    }

    @Override
    protected void initView() {
        // TODO Auto-generated method stub
        mVolumeIcon = findViewById(R.id.status_volume);
        mBrightnessIcon = findViewById(R.id.status_brightness);
        mRaiseScreen = findViewById(R.id.status_raisescreen);
    }

    @Override
    protected void initData() {
        // TODO Auto-generated method stub
    }

    @Override
    protected void startFreshData() {
        // TODO Auto-generated method stub
        super.startFreshData();
        mVolumeIcon.setClickCb(this);
        mBrightnessIcon.setClickCb(this);
        if (!isTmp) {
            mRaiseScreen.startFresh();
        }
    }

    @Override
    protected void endFreshData() {
        // TODO Auto-generated method stub
        super.endFreshData();
        mVolumeIcon.setClickCb(null);
        mBrightnessIcon.setClickCb(null);
        if (!isTmp) {
            mRaiseScreen.stopFresh();
        }
    }

    @Override
    public void onBrightnessClick() {
        // TODO Auto-generated method stub
        final BrightnessFragment fragmet = (BrightnessFragment) getChildFragmentManager().findFragmentByTag(FRAGMENT_TAG_BRIGHTNESS);
        if (fragmet != null) {
            MxyLog.e(TAG, "showVolumeFragment--fragmet != null");
        } else {
            BrightnessFragment.getInstance().show(getChildFragmentManager(), FRAGMENT_TAG_BRIGHTNESS);
        }
    }

    @Override
    public void onVolumeClick() {
        // TODO Auto-generated method stub
        final VolumeFragment fragmet = (VolumeFragment) getChildFragmentManager().findFragmentByTag(FRAGMENT_TAG_VOLUME);
        if (fragmet != null) {
            MxyLog.e(TAG, "showVolumeFragment--fragmet != null");
        } else {
            VolumeFragment.getInstance().show(getChildFragmentManager(), FRAGMENT_TAG_VOLUME);
        }
    }

}
