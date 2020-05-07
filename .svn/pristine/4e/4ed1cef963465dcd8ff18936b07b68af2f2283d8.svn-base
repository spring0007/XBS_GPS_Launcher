package com.sczn.wearlauncher.location;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.base.util.StringUtils;
import com.sczn.wearlauncher.location.bean.BaseStationInfo;
import com.sczn.wearlauncher.location.bean.LocationInfo;
import com.sczn.wearlauncher.location.bean.WifiDataBean;
import com.sczn.wearlauncher.location.impl.OnLocationChangeListener;
import com.sczn.wearlauncher.socket.WaterSocketManager;
import com.sczn.wearlauncher.socket.command.CommandResultCallback;
import com.sczn.wearlauncher.socket.command.gprs.post.UD;
import com.sczn.wearlauncher.sp.SPKey;
import com.sczn.wearlauncher.sp.SPUtils;
import com.sczn.wearlauncher.util.GsonHelper;
import com.sczn.wearlauncher.util.MoreFastEvent;
import com.sczn.wearlauncher.util.NetworkUtils;
import com.sczn.wearlauncher.util.SystemPermissionUtil;

import java.util.List;

/**
 * Description:
 * Created by Bingo on 2019/5/10.
 */
public class LocationUtil {
    private final String TAG = "LocationUtil";
    private static LocationUtil helper;

    //定位
    private static final int START_LOAD_LOCATION_START = 10;
    private static final int START_LOAD_LOCATION_END = 11;
    private static final int START_LOAD_LOCATION_ING = 12;
    private static final int START_LOAD_LOCATION_TIME_OUT = 30000;

    //定位信息
    private OnLocationChangeListener<LocationInfo> onLocationChangeListener;
    private LocationInfo locationInfo;

    //标记
    private long uploadEventTime = 0;

    /**
     * 单例
     *
     * @return
     */
    public static LocationUtil getInstance() {
        if (null == helper) {
            synchronized (LocationUtil.class) {
                if (null == helper) {
                    helper = new LocationUtil();
                }
            }
        }
        return helper;
    }

    /**
     * 立即唤醒终端GPS定位功能,连续定位3分钟，按照10秒一次定位数据上传3分钟后关闭
     *
     * @param mContext
     */
    public void start(final Context mContext) {
        startLocation(mContext, new OnLocationChangeListener<LocationInfo>() {
            @Override
            public void success(final LocationInfo info) {
                MoreFastEvent.getInstance().event(10000, uploadEventTime, new MoreFastEvent.onEventCallBackWithTimeListener() {
                    @Override
                    public void onCallback(long eventTime) {
                        uploadEventTime = eventTime;
                        MxyLog.w(TAG, "立即唤醒终端GPS定位功能,连续定位3分钟，按照10秒一次定位数据上传3分钟后关闭");
                        doSendLocationCmd(info);
                    }
                });
            }

            @Override
            public void fail() {
                stopLocation();
            }
        });
    }

    /**
     * 有网络,开始定位
     * <p>
     * 流程为GPS-->>WiFi-->>基站
     *
     * @param onLocationChangeListener
     * @param mContext
     */
    private void startLocation(final Context mContext, OnLocationChangeListener<LocationInfo> onLocationChangeListener) {
        this.onLocationChangeListener = onLocationChangeListener;
        final boolean hasNet = NetworkUtils.isNetWorkConnected(mContext);
        MxyLog.d(TAG, "开始定位,是否有网络:" + hasNet);
        if (!hasNet) {
            /**
             * 没有网络,后期规划,没有网络需要定位GPS数据
             */
            this.onLocationChangeListener.fail();
            this.onLocationChangeListener = null;
            return;
        }
        if (!mHandler.hasMessages(START_LOAD_LOCATION_ING)) {
            mHandler.sendEmptyMessageDelayed(START_LOAD_LOCATION_ING, 180000);
            locationInfo = new LocationInfo();
            readOnGPS();
        } else {
            MxyLog.w(TAG, "还在定位上传位置中,不能重复触发..");
        }
    }

    /**
     * 读取基站信息
     *
     * @param mContext
     */
    private void readOnBaseInfo(Context mContext) {
        BaseStationInfo b = BaseStationHelper.getConnectedCellInfo(mContext);
        List<BaseStationInfo> ns = BaseStationHelper.getCellInfo(mContext);

        locationInfo.setBtsCount(ns == null ? 0 : ns.size() + 1);
        locationInfo.setBts(BaseStationHelper.formatBaseInfo(b, ns));
    }

    /**
     * 读取WiFi信息
     * 并且上传判断是否需要上传WiFi定位
     * YES,执行上传WiFi定位信息
     * NO,执行基站定位
     *
     * @param mContext
     */
    private void readOnWiFiInfo(final Context mContext) {
        WiFiLocationHelper.getInstance().scanWifiData(mContext, new OnLocationChangeListener<List<WifiDataBean>>() {
            @Override
            public void success(List<WifiDataBean> data) {
                String info = WiFiLocationHelper.wifiFormat(data);
                if (StringUtils.isEmpty(info)) {
                    locationInfo.setMacs("");
                } else {
                    locationInfo.setMacs(info);
                }
                MxyLog.d(TAG, "读取到有WiFi信息..准备上传," + info);
                locationInfo.setWifiCount(data.size() + 1);
                if (data.size() > 3) {
                    locationInfo.setType(1);
                    if (onLocationChangeListener != null) {
                        onLocationChangeListener.success(locationInfo);
                    }
                } else {
                    MxyLog.d(TAG, "判断周围的WiFi数量小于3,使用基站定位");
                    locationInfo.setType(0);
                    if (onLocationChangeListener != null) {
                        onLocationChangeListener.success(locationInfo);
                    }
                }
            }

            @Override
            public void fail() {
                MxyLog.i(TAG, "读取失败or没有WiFi信息..开始基站定位");
                locationInfo.setType(0);
                if (onLocationChangeListener != null) {
                    onLocationChangeListener.success(locationInfo);
                }
            }
        });
    }

    /**
     * GPS定位
     */
    private void readOnGPS() {
        if (mHandler.hasMessages(START_LOAD_LOCATION_END)) {
            mHandler.removeMessages(START_LOAD_LOCATION_END);
        }
        SystemPermissionUtil.openGpsSwitch();
        mHandler.sendEmptyMessageDelayed(START_LOAD_LOCATION_END, START_LOAD_LOCATION_TIME_OUT);
        //因为打开gps高精度,需要一点时间缓冲,所以延迟2秒开始定位
        mHandler.sendEmptyMessageDelayed(START_LOAD_LOCATION_START, 2000);
    }

    /**
     * 开始进行GPS定位
     */
    private void doOnStartGPSLocation() {
        MxyLog.d(TAG, "开始进行GPS定位..");
        GPSHelper.getInstance().stopGPS();
        GPSHelper.getInstance().startGPS(LauncherApp.getAppContext(), new OnLocationChangeListener<LocationInfo>() {
            @Override
            public void success(LocationInfo info) {
                MxyLog.d(TAG, "GPS定位成功,读取到GPS信息" + info.toString());
                if (mHandler.hasMessages(START_LOAD_LOCATION_END)) {
                    mHandler.removeMessages(START_LOAD_LOCATION_END);
                }
                locationInfo.setType(2);
                locationInfo.setAccuracy(info.getAccuracy());
                locationInfo.setLat(info.getLat());
                locationInfo.setLng(info.getLng());
                locationInfo.setGpsCount(info.getGpsCount());
                locationInfo.setAltitude(info.getAltitude());
                locationInfo.setBearing(info.getBearing());
                locationInfo.setSpeed(info.getSpeed());

                //定位成功,开始上传数据
                //执行上传gps位置信息
                if (onLocationChangeListener != null) {
                    onLocationChangeListener.success(locationInfo);
                }
            }

            @Override
            public void fail() {
                MxyLog.i(TAG, "GPS定位失败了,开始上传基站定位");
                readGpsFail();
            }
        });
    }

    /**
     * gps获取失败
     */
    private void readGpsFail() {
        if (mHandler.hasMessages(START_LOAD_LOCATION_ING)) {
            mHandler.removeMessages(START_LOAD_LOCATION_ING);
            MxyLog.d(TAG, "WiFi和基站定位就不需要连续上传了.");
        }
        stopLocation();
        //读取基站信息
        readOnBaseInfo(LauncherApp.getAppContext());
        //读取WiFi信息
        readOnWiFiInfo(LauncherApp.getAppContext());
    }

    /**
     * 停止定位全流程
     */
    public void stopLocation() {
        if (mHandler.hasMessages(START_LOAD_LOCATION_END)) {
            mHandler.removeMessages(START_LOAD_LOCATION_END);
        }
        GPSHelper.getInstance().stopGPS();
        WiFiLocationHelper.getInstance().stopScanWifi();
        SystemPermissionUtil.closeGpsSwitch();
    }

    /**
     * 开始上传定位信息
     * 注:需要注意onLocationChangeListener是否为空的判断
     *
     * @param info
     */
    public void doSendLocationCmd(final LocationInfo info) {
        //定位成功,记录定位的时间和信息
        SPUtils.setParam(SPKey.LOCATION_LAST_TIME, System.currentTimeMillis());
        SPUtils.setParam(SPKey.LOCATION_LAST_INFO, GsonHelper.getInstance().getGson().toJson(info));
        SPUtils.setParam(SPKey.LOCATION_LAST_UPLOAD_TIME, System.currentTimeMillis());
        WaterSocketManager.getInstance().send(new UD(info, new CommandResultCallback() {
            @Override
            public void onSuccess(String baseObtain) {
                MxyLog.i(TAG, "上传定位成功.");
            }

            @Override
            public void onFail() {
                MxyLog.w(TAG, "上传定位失败.");
            }
        }));
    }

    /**
     *
     */
    private Handler mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case START_LOAD_LOCATION_END:
                    MxyLog.d(TAG, "GPS定位超时了,开始走WiFi或者基站定位");
                    readGpsFail();
                    break;
                case START_LOAD_LOCATION_START:
                    doOnStartGPSLocation();
                    break;
                case START_LOAD_LOCATION_ING:
                    MxyLog.d(TAG, "三分钟已到,需要停止上传位置.");
                    stopLocation();
                    break;
            }
            return false;
        }
    });
}
