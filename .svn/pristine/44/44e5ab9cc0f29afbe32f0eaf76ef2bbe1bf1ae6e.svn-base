package com.sczn.wearlauncher.status;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.app.AppConfig;
import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.base.absFragment;
import com.sczn.wearlauncher.event.RefreshBatteryEvent;
import com.sczn.wearlauncher.event.RefreshWifiSignalEvent;
import com.sczn.wearlauncher.sp.SysKey;
import com.sczn.wearlauncher.util.NetworkUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Description:title bar
 */
public class PhoneStatusBarFragment extends absFragment {

    public static final String TAG = "PhoneStatusBarFragment";

    public static final int GPS_LOCATION_STATE_BEGIN = 0;
    public static final int GPS_LOCATION_STATE_END = 1;
    public static final int GPS_LOCATION_STATE_SUCCESS = 2;
    public static final int GPS_LOCATION_STATE_FAIL = 3;

    private TelephonyManager mTelephonyManager;
    private PhoneListener phoneListener;
    private ConnectivityManager mConnectivityManager;
    //电量
    private ImageView mBatteryView;
    //sim
    private ImageView mMobileView;
    //wifi信号
    private ImageView mWifiView;
    //是否可同步
    private ImageView mSyncView;
    //Volte
    private ImageView mVolteView;
    //手机信号
    private ImageView mSignalView;
    //手机信号 更改中
    private ImageView mSignalProgressView;
    //GPS 定位状态
    private ImageView mGpsStateView;


    //电池动画图片的资源ID，有两个，充电和低电量
    private int mBatteryAnimResId = -1;
    private AnimationDrawable mBatteryAnimationDrawable;

    private static final int PROGRESS_TYPE_RUNNABLE = 1;
    private static final int PROGRESS_TYPE_4G_2G = 2;
    private static final int PROGRESS_TYPE_SYNC = 3;
    private int mProgressType = 0;

    public static PhoneStatusBarFragment newInstance() {
        return new PhoneStatusBarFragment();
    }


    private Runnable mProgressRunnable = new Runnable() {
        @Override
        public void run() {
            if (getActivity() == null || getActivity().isFinishing() || !isAdded()) {
                return;
            }
            if (AppConfig.getInstance().getSyncState() == 0 ||
                    AppConfig.getInstance().getWTokenState() == 1) {
                MxyLog.d(TAG, "正在同步，继续滚动进度条");
            } else {
                MxyLog.d(TAG, "停止滚动进度条");
                startOrStopProgressAnim(PROGRESS_TYPE_RUNNABLE, false);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        initView();
        initData();
    }

    @Override
    protected int getLayoutResouceId() {
        return R.layout.fragment_phone_status;
    }

    protected void initView() {
        mBatteryView = findViewById(R.id.battery_view);
        mMobileView = findViewById(R.id.mobile_view);
        mWifiView = findViewById(R.id.wifi_view);
        mSyncView = findViewById(R.id.sync_view);
        mVolteView = findViewById(R.id.volte_view);
        mSignalView = findViewById(R.id.signal_view);
        mSignalProgressView = findViewById(R.id.signal_progress_view);
        mGpsStateView = findViewById(R.id.gps_state_view);
    }

    /**
     * 初始化数据状态
     */
    protected void initData() {
        mTelephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        phoneListener = new PhoneListener();
        mConnectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (mConnectivityManager != null) {
            final NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                updateConnectivityView();
                updateWifiSignalView();
            }
        }

        if (NetworkUtils.isHasSimCard(LauncherApp.getAppContext())) {
            final int gsmLevel = NetworkUtils.getSignalLevel(LauncherApp.getAppContext());
            mSignalView.setImageResource(R.drawable.icon_signal_bad);
            setPhoneSignalIcon(gsmLevel);
        } else {
            mSignalView.setImageResource(R.drawable.icon_signal_bad);
        }

        final int batteryLevel = AppConfig.getInstance().getBatteryLevel();
        final int batteryState = AppConfig.getInstance().getBatteryState();
        updateBatteryView(batteryLevel, batteryState);

        final int syncState = AppConfig.getInstance().getSyncState();

        updateSyncState(syncState);

        volteIcon();

        updateGPSLocationState();
    }

    @Override
    public void onResume() {
        super.onResume();
        mTelephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mTelephonyManager != null) {
            mTelephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_NONE);
        }
    }

    /**
     * sim信号监听
     */
    private class PhoneListener extends PhoneStateListener {
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            if (NetworkUtils.isHasSimCard(LauncherApp.getAppContext())) {
                setPhoneSignalIcon(NetworkUtils.getSignalLevel(LauncherApp.getAppContext()));
                updateConnectivityView();
                if (NetworkUtils.is4G(LauncherApp.getAppContext())) {
                    volteIcon();//有volte
                }
            } else {
                mSignalView.setImageResource(R.drawable.icon_signal_bad);
                mMobileView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 显示信号的状态
     */
    public void updateConnectivityView() {
        if (getActivity() == null || getActivity().isFinishing() || !isAdded()) {
            return;
        }
        String net = NetworkUtils.getNetworkType(getActivity());
        if (net != null && !net.isEmpty()) {
            mMobileView.setVisibility(View.VISIBLE);
            if (NetworkUtils.NETWORK_TYPE_4G.equals(net)) {
                mMobileView.setImageResource(R.drawable.icon_4g);
                if (AppConfig.getInstance().getHaveLteEver() == null) {
                    AppConfig.getInstance().setHaveLteEver(true);
                }
                startOrStopProgressAnim(PROGRESS_TYPE_4G_2G, false);
            } else if (NetworkUtils.NETWORK_TYPE_3G.equals(net)) {
                mMobileView.setImageResource(R.drawable.icon_3g);
            } else if (NetworkUtils.NETWORK_TYPE_2G.equals(net)) {
                mMobileView.setImageResource(R.drawable.icon_2g);
            } else {
                mMobileView.setVisibility(View.GONE);
            }
        } else {
            mMobileView.setVisibility(View.GONE);
        }
        final NetworkInfo networkInfoMobile = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        final NetworkInfo networkInfoWifi = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        // MxyLog.i(TAG, "当前网络名称：TYPE_MOBILE : " + networkInfoMobile);
        // MxyLog.i(TAG, "当前网络名称：TYPE_WIFI : " + networkInfoWifi);

        if (networkInfoWifi != null && networkInfoWifi.isAvailable() && networkInfoWifi.getState() == NetworkInfo.State.CONNECTED) {
            mWifiView.setVisibility(View.VISIBLE);
            updateWifiSignalView();
        } else {
            mWifiView.setVisibility(View.GONE);
        }
    }

    /**
     * 刷新wifi信号
     */
    private void updateWifiSignalView() {
        if (getActivity() == null || getActivity().isFinishing() || !isAdded()) {
            return;
        }
        if (NetworkUtils.isWiFi(getActivity())) {
            mWifiView.setVisibility(View.VISIBLE);
            AppConfig.getInstance().setNetworkInfo(mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI));
            int level = NetworkUtils.getWiFiSignal(getActivity());
            Message msg = Message.obtain();
            if (level == 5) {
                msg.what = 5;
                mWifiView.setImageResource(R.drawable.icon_wifi_great);
            } else if (level == 4) {
                msg.what = 4;
                mWifiView.setImageResource(R.drawable.icon_wifi_good);
            } else if (level == 3) {
                msg.what = 3;
                mWifiView.setImageResource(R.drawable.icon_wifi_low);
            } else if (level == 2) {
                msg.what = 2;
                mWifiView.setImageResource(R.drawable.icon_wifi_lowest);
            } else {
                msg.what = 1;
                mWifiView.setImageResource(R.drawable.icon_wifi_bad);
            }
        } else {
            mWifiView.setVisibility(View.GONE);
        }
    }

    /**
     * 设置手机信号
     *
     * @param signalStrength
     */
    public void setPhoneSignalIcon(int signalStrength) {
        if (getActivity() == null || getActivity().isFinishing() || !isAdded()) {
            return;
        }
        switch (signalStrength) {
            case 4:
                mSignalView.setImageResource(R.drawable.icon_signal_great);
                break;
            case 3:
                mSignalView.setImageResource(R.drawable.icon_signal_good);
                break;
            case 2:
                mSignalView.setImageResource(R.drawable.icon_signal_normal);
                break;
            case 1:
                mSignalView.setImageResource(R.drawable.icon_signal_low);
                break;
            default:
                //mSignalView.setImageResource(R.drawable.icon_signal_bad);
                break;
        }
    }

    /**
     * 刷新电池状态
     *
     * @param percent
     * @param state
     */
    private void updateBatteryView(int percent, int state) {
        if (getActivity() == null || getActivity().isFinishing() || !isAdded()) {
            return;
        }
        MxyLog.d(TAG, "update battery state:" + state);
        if (state == BatteryManager.BATTERY_STATUS_CHARGING) {
            //充电状态，开启充电动画
            startBatteryAnim(R.drawable.battery_charging);
        } else {
            stopBatteryAnim();
            //有效化percent范围[0 , 100]
            percent = Math.max(0, Math.min(100, percent));
            //根据电量，设置图标
            switch (percent / 20) {
                case 0:
                    startBatteryAnim(R.drawable.battery_low_power);
                    mBatteryView.setBackgroundResource(R.drawable.icon_cd_0006);
                    break;
                case 1:
                    mBatteryView.setBackgroundResource(R.drawable.icon_cd_0004);
                    break;
                case 2:
                    mBatteryView.setBackgroundResource(R.drawable.icon_cd_0003);
                    break;
                case 3:
                    mBatteryView.setBackgroundResource(R.drawable.icon_cd_0002);
                    break;
                case 4:
                    mBatteryView.setBackgroundResource(R.drawable.icon_cd_0001);
                    break;
                case 5://充满电状态,power full
                    mBatteryView.setBackgroundResource(R.drawable.icon_cd_0001);
                    break;
                default:
                    mBatteryView.setBackgroundResource(R.drawable.icon_cd_0004);
                    break;
            }
        }
    }

    /**
     * 开启电池动画
     *
     * @param resId
     */
    private void startBatteryAnim(int resId) {
        if (mBatteryAnimResId != resId) {
            stopBatteryAnim();
            mBatteryAnimResId = resId;
            mBatteryView.setBackgroundResource(resId);
            mBatteryAnimationDrawable = (AnimationDrawable) mBatteryView.getBackground();
            mBatteryAnimationDrawable.start();
        }
    }

    /**
     * 关闭电池动画
     */
    private void stopBatteryAnim() {
        if (mBatteryAnimationDrawable != null && mBatteryAnimationDrawable.isRunning()) {
            mBatteryAnimationDrawable.stop();
            mBatteryAnimationDrawable = null;
        }
        mBatteryAnimResId = -1;
    }

    private void startOrStopProgressAnim(int progressType, boolean start) {
        if (start) {
            mProgressType = progressType;
            //
            mSignalProgressView.setVisibility(View.VISIBLE);
            //
            Drawable drawable = mSignalProgressView.getDrawable();

            if (drawable instanceof AnimationDrawable) {
                ((AnimationDrawable) drawable).start();
            }
        } else {
            if (progressType != PROGRESS_TYPE_RUNNABLE && mProgressType != progressType) {
                return;
            }

            mSignalProgressView.setVisibility(View.GONE);
            //
            Drawable drawable = mSignalProgressView.getDrawable();

            if (drawable instanceof AnimationDrawable) {
                ((AnimationDrawable) drawable).stop();
            }
        }
    }

    /**
     * 是否可同步
     *
     * @param type 0 同步失败；1 同步中；2 同步成功
     */
    private void updateSyncState(int type) {
        if (getActivity() == null || getActivity().isFinishing() || !isAdded()) {
            return;
        }
        if (type == 0) {
            mSyncView.setVisibility(View.GONE);
            startOrStopProgressAnim(PROGRESS_TYPE_SYNC, false);
        } else if (type == 1) {
            /*ViewGroup.LayoutParams layoutParams = mSyncView.getLayoutParams();

            layoutParams.width = 26;
            //
            mSyncView.setImageResource(R.drawable.lc_icon_sync_g_ing);
            mSyncView.setVisibility(View.VISIBLE);*/

            mSyncView.setVisibility(View.GONE);
            startOrStopProgressAnim(PROGRESS_TYPE_SYNC, true);
        } else if (type == 2) {
            ViewGroup.LayoutParams layoutParams = mSyncView.getLayoutParams();

            layoutParams.width = 20;
            //
            mSyncView.setImageResource(R.drawable.lc_icon_sync_g_ok);
            mSyncView.setVisibility(View.VISIBLE);
            startOrStopProgressAnim(PROGRESS_TYPE_SYNC, false);
        }
    }

    private void volteIcon() {
        boolean volteEnable = isVolteEnable();
        if (volteEnable) {
            mVolteView.setVisibility(View.VISIBLE);
        } else {
            mVolteView.setVisibility(View.GONE);
        }
    }

    private boolean isVolteEnable() {
        if (mTelephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY) {
            int enableInt = Settings.System.getInt(LauncherApp.getAppContext().getContentResolver(), SysKey.KEY_SWITCH_IS_OPEN_VOLTE, 0);
            return enableInt != 0;
        }
        return false;
    }

    private void updateGPSLocationState() {
        if (getActivity() == null || getActivity().isFinishing() || !isAdded()) {
            return;
        }
        final int state = AppConfig.getInstance().getGpsLocationState();

        if (state == GPS_LOCATION_STATE_BEGIN) {
            mGpsStateView.setVisibility(View.VISIBLE);
            //
            mGpsStateView.setImageResource(R.drawable.lc_icon_gps_now);
        } else if (state == GPS_LOCATION_STATE_END) {
            mGpsStateView.setVisibility(View.GONE);
        } else if (state == GPS_LOCATION_STATE_SUCCESS) {
            mGpsStateView.setVisibility(View.VISIBLE);
            //
            mGpsStateView.setImageResource(R.drawable.lc_icon_gps_success);
        } else if (state == GPS_LOCATION_STATE_FAIL) {
            mGpsStateView.setVisibility(View.GONE);
        }
    }

    /***************************************EventBus事件**********************************************/

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshBatteryEvent(RefreshBatteryEvent event) {
        // 刷新电量
        updateBatteryView(event.getBatteryLevel(), event.getState());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshWifiSignalEvent(RefreshWifiSignalEvent event) {
        // 刷新WiFi状态
        updateWifiSignalView();
        volteIcon();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
