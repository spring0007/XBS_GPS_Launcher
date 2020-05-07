package com.sczn.wearlauncher.setting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.base.AbsActivity;
import com.sczn.wearlauncher.base.view.MyRecyclerView;
import com.sczn.wearlauncher.location.WiFiLocationHelper;
import com.sczn.wearlauncher.location.bean.WiFiScanBean;
import com.sczn.wearlauncher.setting.adapetr.WifiResultAdapter;
import com.sczn.wearlauncher.setting.bean.WifiAssistantBean;
import com.sczn.wearlauncher.setting.decoration.WlanDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:显示WiFi列表页面
 */
public class WLANListActivity extends AbsActivity {

    private final String TAG = "WLANListActivity";

    private MyRecyclerView recyclerView;
    private TextView mNoDataView;

    private WifiResultAdapter mDataAdapter;
    private final List<WiFiScanBean> mDataList = new ArrayList<>();
    private WifiAssistantBean mWifiAssistantBean;

    private WifiManager mWifiManager;
    private ConnectivityManager mConnectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wlan_list);
        initView();
        initWiFi();
        registerReceiver();
    }

    private void registerReceiver() {
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(WifiManager.RSSI_CHANGED_ACTION);
        filter1.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter1.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        filter1.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(mReceiver, filter1);
    }

    private void initView() {
        mNoDataView = findViewById(R.id.no_data_view);
        recyclerView = findViewById(R.id.rv_wifi);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        final WlanDecoration decoration = new WlanDecoration();
        recyclerView.addItemDecoration(decoration);
        recyclerView.setEmpty(mNoDataView);
    }

    private void initWiFi() {
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mConnectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        final boolean wifiEnabled = mWifiManager.isWifiEnabled();
        if (wifiEnabled) {
            mWifiManager.startScan();
            final List<ScanResult> scanResults = mWifiManager.getScanResults();
            inflateWifi(scanResults);
        } else {
            if (isFinishing()) {
                return;
            }
            mNoDataView.setText(getString(R.string.setting_wlan_no_enable));
        }
    }

    /***
     * 展示WiFi扫描的结果
     * @param list
     */
    private void inflateWifi(List<ScanResult> list) {
        final WifiAssistantBean wifiAssistantBean = getWifiAssistantBean();

        final WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        final NetworkInfo networkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final int wifiState = mWifiManager.getWifiState();

        if (networkInfo != null) {
            wifiAssistantBean.setWifiNetworkState(networkInfo.getState());
        }

        wifiAssistantBean.setWifiInfo(wifiInfo);
        wifiAssistantBean.setWifiState(wifiState);

        if (mDataAdapter != null) {
            mDataAdapter.setWifiAssistant(wifiAssistantBean);
        }

        String connectedSsid = null;
        if (wifiInfo != null) {
            connectedSsid = wifiInfo.getSSID();
        }
        mDataList.clear();
        WiFiScanBean connectWiFi = null;
        WiFiScanBean scanBean;
        if (list != null && !list.isEmpty()) {
            for (ScanResult result : list) {
                if (!TextUtils.isEmpty(result.SSID)) {
                    if (connectedSsid != null) {
                        if (TextUtils.equals(connectedSsid, "\"" + result.SSID + "\"")) {
                            connectWiFi = new WiFiScanBean();
                            connectWiFi.setSsid(result.SSID);
                            connectWiFi.setRssi(result.level);
                        } else {
                            scanBean = new WiFiScanBean();
                            scanBean.setSsid(result.SSID);
                            scanBean.setRssi(result.level);
                            mDataList.add(scanBean);
                        }
                    } else {
                        scanBean = new WiFiScanBean();
                        scanBean.setSsid(result.SSID);
                        scanBean.setRssi(result.level);
                        mDataList.add(scanBean);
                    }
                }
            }
            /**
             * 当前连接的设备
             */
            if (connectWiFi != null) {
                mDataList.add(0, connectWiFi);
            }
        }

        if (mDataAdapter == null) {
            mDataAdapter = new WifiResultAdapter(getApplicationContext(), mDataList);
            recyclerView.setAdapter(mDataAdapter);
        } else {
            mDataAdapter.notifyDataSetChanged();
        }

        if (mDataList.isEmpty()) {
            if (isFinishing()) {
                return;
            }
            final boolean wifiEnabled = mWifiManager.isWifiEnabled();
            if (wifiEnabled) {
                if (isFinishing()) {
                    return;
                }
                mNoDataView.setText(getString(R.string.setting_wlan_no_result));
            } else {
                if (isFinishing()) {
                    return;
                }
                mNoDataView.setText(getString(R.string.setting_wlan_no_enable));
            }
        }
    }

    private WifiAssistantBean getWifiAssistantBean() {
        if (mWifiAssistantBean == null) {
            synchronized (this) {
                mWifiAssistantBean = new WifiAssistantBean();
            }
        }
        return mWifiAssistantBean;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            MxyLog.d(TAG, "onReceive action | " + action);

            if (WifiManager.RSSI_CHANGED_ACTION.equals(intent.getAction())
                    || WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())
                    || WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {

                MxyLog.d(TAG, "onReceive 刷新 Wifi ");

                final List<ScanResult> scanResults = mWifiManager.getScanResults();

                if (mWifiManager.isWifiEnabled()) {
                    if (scanResults != null && !scanResults.isEmpty()) {
                        inflateWifi(scanResults);
                    }
                } else {
                    inflateWifi(null);
                }

            } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                MxyLog.d(TAG, "onReceive Wifi 网络状态变化");

                Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (null != parcelableExtra) {
                    NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                    NetworkInfo.State state = networkInfo.getState();

                    final WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
                    final WifiAssistantBean wifiAssistantBean = getWifiAssistantBean();
                    wifiAssistantBean.setWifiInfo(wifiInfo);
                    wifiAssistantBean.setWifiNetworkState(state);

                    /**
                     * 扫描连接到WiFi需要将这WiFi置顶
                     */
                    if (mDataAdapter != null) {
                        mDataAdapter.setWifiAssistant(wifiAssistantBean);
                        int pos = 0;
                        WiFiScanBean bean = WiFiLocationHelper.getInstance().getConnectedWifiName(LauncherApp.getAppContext());
                        if (mDataList.size() > 0) {
                            for (int i = 0; i < mDataList.size(); i++) {
                                if (bean != null && bean.getSsid() != null && bean.getSsid().equals(mDataList.get(i).getSsid())) {
                                    pos = i;
                                    break;
                                }
                            }
                            mDataList.remove(pos);
                            mDataList.add(0, bean);
                            mDataAdapter.setData(mDataList);
                            mDataAdapter.notifyDataSetChanged();

                            if (state == NetworkInfo.State.CONNECTED) {
                                if (recyclerView != null) {
                                    recyclerView.scrollToPosition(0);
                                }
                            }
                        }
                    }
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
