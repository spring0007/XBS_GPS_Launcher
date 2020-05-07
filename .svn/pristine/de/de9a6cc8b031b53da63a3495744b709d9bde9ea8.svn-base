package com.sczn.wearlauncher.setting.adapetr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.location.bean.WiFiScanBean;
import com.sczn.wearlauncher.setting.WLANInputActivity;
import com.sczn.wearlauncher.setting.bean.WifiAssistantBean;

import java.util.List;

public class WifiResultAdapter extends RecyclerView.Adapter<WifiResultAdapter.MyViewHolder> {

    private final Context mContext;

    private WifiManager wifiManager;

    private WifiAssistantBean mWifiAssistantBean;
    private int[] wifiStateIconArray = {R.drawable.img_wifi_state_0, R.drawable.img_wifi_state_1,
            R.drawable.img_wifi_state_2, R.drawable.img_wifi_state_3, R.drawable.img_wifi_state_4};

    private List<WiFiScanBean> mDataList;
    private WiFiScanBean scanResult = null;

    private Handler gotoInputPassWord = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            try {
                if (mWifiAssistantBean != null) {
                    NetworkInfo.State wifiNetworkState = mWifiAssistantBean.getWifiNetworkState();
                    if (scanResult != null && (wifiNetworkState != NetworkInfo.State.CONNECTED)) {
                        List<WifiConfiguration> wifiConfigurationList = wifiManager.getConfiguredNetworks();
                        if (wifiConfigurationList != null) {
                            for (WifiConfiguration wifiConfiguration : wifiConfigurationList) {
                                if (wifiConfiguration.SSID.equals("\"" + scanResult.getSsid() + "\"")) {
                                    wifiManager.removeNetwork(wifiConfiguration.networkId);
                                    if (mContext != null) {
                                        Toast.makeText(mContext, "连接网络失败，请重试~",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    });

    public WifiResultAdapter(Context context, List<WiFiScanBean> list) {
        this.mContext = context;
        this.mDataList = list;
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    /**
     * @param list
     */
    public void setData(List<WiFiScanBean> list) {
        if (list != null) {
            this.mDataList = list;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = View.inflate(mContext, R.layout.item_wifi_result, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final WiFiScanBean bean = mDataList.get(position);
        holder.nameView.setText(bean.getSsid());

        if (mWifiAssistantBean != null) {
            final WifiInfo wifiInfo = mWifiAssistantBean.getWifiInfo();

            if (wifiInfo != null) {
                final String ssidConnected = wifiInfo.getSSID();

                if (TextUtils.equals(ssidConnected, "\"" + bean.getSsid() + "\"")) {
                    holder.connectedView.setVisibility(View.VISIBLE);

                    final NetworkInfo.State wifiNetworkState = mWifiAssistantBean.getWifiNetworkState();

                    if (wifiNetworkState != null) {
                        if (wifiNetworkState == NetworkInfo.State.CONNECTED) {
                            // WiFi已连接
                            holder.connectedView.setImageResource(wifiStateIconArray[getWifiPower(bean)]);
                            holder.nameView.setTextColor(Color.parseColor("#0062f7"));
                            holder.tvWifiState.setText(mContext.getString(R.string.wifi_state_connected));
                            holder.tvWifiState.setTextColor(Color.parseColor("#0062f7"));
                        } else if (wifiNetworkState == NetworkInfo.State.CONNECTING) {
                            // WiFi正在连接
                            final AnimationDrawable drawable = (AnimationDrawable) ActivityCompat.getDrawable(mContext, R.drawable.setting_wifi_connecting);
                            holder.connectedView.setImageDrawable(drawable);
                            drawable.start();
                            holder.nameView.setTextColor(Color.parseColor("#000000"));
                            holder.tvWifiState.setText(mContext.getString(R.string.wifi_state_connecting));
                            holder.tvWifiState.setTextColor(Color.parseColor("#000000"));
                        }
                    } else {
                        // WiFi未连接
                        holder.connectedView.setImageResource(wifiStateIconArray[getWifiPower(bean)]);
                        holder.nameView.setTextColor(Color.parseColor("#000000"));
                        holder.tvWifiState.setText(mContext.getString(R.string.wifi_state_unconnect));
                        holder.tvWifiState.setTextColor(Color.parseColor("#000000"));
                    }
                } else {
                    // WiFi未连接
                    holder.connectedView.setImageResource(wifiStateIconArray[getWifiPower(bean)]);
                    holder.nameView.setTextColor(Color.parseColor("#000000"));
                    holder.tvWifiState.setText(mContext.getString(R.string.wifi_state_unconnect));
                    holder.tvWifiState.setTextColor(Color.parseColor("#000000"));
                }
            } else {
                // WiFi未连接
                holder.connectedView.setImageResource(wifiStateIconArray[getWifiPower(bean)]);
                holder.nameView.setTextColor(Color.parseColor("#000000"));
                holder.tvWifiState.setText(mContext.getString(R.string.wifi_state_unconnect));
                holder.tvWifiState.setTextColor(Color.parseColor("#000000"));
            }
        } else {
            // WiFi未连接
            holder.connectedView.setImageResource(wifiStateIconArray[0]);
            holder.nameView.setTextColor(Color.parseColor("#000000"));
            holder.tvWifiState.setText(mContext.getString(R.string.wifi_state_unconnect));
            holder.tvWifiState.setTextColor(Color.parseColor("#000000"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOnConnectWiFi(bean);
            }
        });
    }

    /**
     * 执行点击连接WiFi的流程
     *
     * @param bean
     */
    private void doOnConnectWiFi(WiFiScanBean bean) {
        // FIXME: 2017/9/21 这里新增加一个逻辑 如果当前的wifi是之前已经连接的wifi 那么就不用再输入密码
        boolean isConfigure = false;
        WifiConfiguration wifiConfiguration = null;
        List<WifiConfiguration> wifiConfigurationList = wifiManager.getConfiguredNetworks();
        if (wifiConfigurationList != null) {
            for (WifiConfiguration configuration : wifiConfigurationList) {
                if (configuration.SSID.equals("\"" + bean.getSsid() + "\"")) {
                    isConfigure = true;
                    wifiConfiguration = configuration;
                }
            }
        }
        if (isConfigure) {
            // FIXME: 2017/9/21 如果当前的网络已经配置 那么就执行自动连接热点的逻辑
            WifiResultAdapter.this.scanResult = bean;
            boolean isConnect = connect(wifiConfiguration);
            if (!isConnect) {
                // FIXME: 2017/9/21 如果当前的网络没有进行配置 跳转到密码的输入的界面
                //可以自动连接
                final Intent intent = new Intent(mContext, WLANInputActivity.class);

                intent.putExtra(WLANInputActivity.SCAN_RESULT_TAG, bean);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                mContext.startActivity(intent);
            }
        } else {
            // FIXME: 2017/9/21 如果当前的网络没有进行配置 跳转到密码的输入的界面
            //可以自动连接
            final Intent intent = new Intent(mContext, WLANInputActivity.class);

            intent.putExtra(WLANInputActivity.SCAN_RESULT_TAG, bean);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            mContext.startActivity(intent);
        }
    }

    /**
     * 已有配置链接
     *
     * @param wf
     * @return
     */
    public boolean connect(WifiConfiguration wf) {
        // 状态变成WIFI_STATE_ENABLED的时候才能执行下面的语句，即当状态为WIFI_STATE_ENABLING时，让程序在while里面跑
        while (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
            try {
                // 为了避免程序一直while循环，让它睡个100毫秒在检测……
                Thread.currentThread();
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
        boolean bRet = wifiManager.enableNetwork(wf.networkId, true);
        wifiManager.saveConfiguration();
        gotoInputPassWord.sendEmptyMessageDelayed(0, 8000);
        return bRet;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void setWifiAssistant(WifiAssistantBean wifiAssistantBean) {
        this.mWifiAssistantBean = wifiAssistantBean;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameView;
        ImageView connectedView;
        TextView tvWifiState;

        public MyViewHolder(View view) {
            super(view);
            nameView = view.findViewById(R.id.name_view);
            connectedView = view.findViewById(R.id.connected_view);
            tvWifiState = view.findViewById(R.id.tv_wifi_state);
        }
    }

    /**
     * 通过当前的wifi的对象获取当前的wifi的信号的强度
     *
     * @param info 当前的wifi的对象
     * @return 当前的wifi的信号的强度
     */
    public int getWifiPower(WiFiScanBean info) {
        int strength;
        strength = WifiManager.calculateSignalLevel(info.getRssi(), 5);
        return strength;
    }
}
