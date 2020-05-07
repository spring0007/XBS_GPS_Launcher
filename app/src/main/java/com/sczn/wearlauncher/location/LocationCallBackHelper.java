package com.sczn.wearlauncher.location;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.gson.JsonSyntaxException;
import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.base.util.StringUtils;
import com.sczn.wearlauncher.event.LocationEvent;
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

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Description:定位,回调call back info
 * 注意区分定位成功和定位上传成功的区别
 * 定位成功指:获取到WiFi或者GPS或者基站信息
 * 定位上传成功则是指获取到的定位信息上传和服务器交互成功
 * <p>
 * 新的修改注意多测试2019/05/05
 * <p>
 * Created by Bingo on 2019/05/05.
 */
public class LocationCallBackHelper {

    private final String TAG = "LocationCallBackHelper";
    private static LocationCallBackHelper helper;

    //optUserId
    public static String optUserId;

    //定位
    private static final int START_LOAD_LOCATION_START = 10;
    private static final int START_LOAD_LOCATION_END = 11;
    private static final int START_LOAD_LOCATION_TIME_OUT = 30000;

    //定位信息
    private OnLocationChangeListener<LocationInfo> onLocationChangeListener;
    private LocationInfo locationInfo;

    /**
     * 单例
     *
     * @return
     */
    public static LocationCallBackHelper getInstance() {
        if (null == helper) {
            synchronized (LocationCallBackHelper.class) {
                if (null == helper) {
                    helper = new LocationCallBackHelper();
                }
            }
        }
        return helper;
    }

    /**
     * 有网络,开始定位
     * <p>
     * 流程为WiFi-->>GPS-->>基站
     *
     * @param onLocationChangeListener
     * @param mContext
     */
    public void startLocation(final Context mContext, OnLocationChangeListener<LocationInfo> onLocationChangeListener) {
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
        locationInfo = new LocationInfo();
        long current = System.currentTimeMillis();
        long lastTime = (long) SPUtils.getParam(SPKey.LOCATION_LAST_TIME, 0L);
        String info = (String) SPUtils.getParam(SPKey.LOCATION_LAST_INFO, "");
        if (info == null || info.isEmpty() || current - lastTime >= 60000) {
            //读取基站信息
            doOnBaseInfo(mContext);
            //读取WiFi信息
            doOnWiFiInfo(mContext);
        } else {
            MxyLog.d(TAG, "两次请求定位的时间,小于1分钟,直接使用本地上次记录的位置信息:" + info);
            try {
                locationInfo = GsonHelper.getInstance().getGson().fromJson(info, LocationInfo.class);
                this.onLocationChangeListener.success(locationInfo);
                this.onLocationChangeListener = null;
            } catch (JsonSyntaxException e) {
                this.onLocationChangeListener.fail();
                this.onLocationChangeListener = null;
                MxyLog.d(TAG, e.toString());
            }
        }
    }

    /**
     * 获取位置信息,默认执行上传位置信息
     * <p>
     * 防止多次请求数据,目前定义7秒不能重复触发
     *
     * @param mContext
     */
    public void startLocationAndUpload(final Context mContext) {
        MoreFastEvent.getInstance().event(7000, new MoreFastEvent.onEventCallBackListener() {
            @Override
            public void onCallback() {
                startLocation(mContext, new OnLocationChangeListener<LocationInfo>() {
                    @Override
                    public void success(LocationInfo info) {
                        doSendLocationCmd(info);
                    }

                    @Override
                    public void fail() {

                    }
                });
            }
        });
    }

    /**
     * 直接获取基站信息
     *
     * @param mContext
     */
    public void startBS(Context mContext, OnLocationChangeListener<LocationInfo> listener) {
        final boolean hasNet = NetworkUtils.isNetWorkConnected(mContext);
        if (!hasNet) {
            listener.fail();
            return;
        }
        locationInfo = new LocationInfo();
        //读取基站信息
        doOnBaseInfo(mContext);
        locationInfo.setType(0);
        locationInfo.setCdma("");
        locationInfo.setMmac("");
        locationInfo.setMacs("");
        listener.success(locationInfo);
    }

    /**
     * 读取基站信息
     *
     * @param mContext
     */
    private void doOnBaseInfo(Context mContext) {
        BaseStationInfo b = BaseStationHelper.getConnectedCellInfo(mContext);
        List<BaseStationInfo> ns = BaseStationHelper.getCellInfo(mContext);

        locationInfo.setBtsCount(ns == null ? 0 : ns.size() + 1);
        locationInfo.setBts(BaseStationHelper.formatBaseInfo(b, ns));
    }

    /**
     * 读取WiFi信息
     * 并且上传判断是否需要上传WiFi定位
     * YES,执行上传WiFi定位信息
     * NO,执行GPS定位
     *
     * @param mContext
     */
    private void doOnWiFiInfo(final Context mContext) {
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
                        onLocationChangeListener = null;
                    }
                } else {
                    MxyLog.d(TAG, "判断周围的WiFi数量小于3,使用GPS定位");
                    doOnGPS();
                }
            }

            @Override
            public void fail() {
                MxyLog.i(TAG, "读取失败or没有WiFi信息..开始GPS定位");
                doOnGPS();
            }
        });
    }

    /**
     * GPS定位
     */
    private void doOnGPS() {
        if (mHandler.hasMessages(START_LOAD_LOCATION_END)) {
            mHandler.removeMessages(START_LOAD_LOCATION_END);
        }
        SystemPermissionUtil.openGpsSwitch();
        GPSHelper.getInstance().stopGPS();
        mHandler.sendEmptyMessageDelayed(START_LOAD_LOCATION_END, START_LOAD_LOCATION_TIME_OUT);
        //因为打开gps高精度,需要一点时间缓冲,所以延迟2秒开始定位
        mHandler.sendEmptyMessageDelayed(START_LOAD_LOCATION_START, 2000);
    }

    /**
     * 开始进行定位
     * 需要进行纠偏处理
     * 定位成功,第一次需要先上传一次位置.20秒后,再计算上次的位置点和本次定位点,如果偏差50米,以最后一次位置点为准.
     */
    private void doOnStartGPSLocation() {
        MxyLog.d(TAG, "开始进行定位..");
        GPSHelper.getInstance().startGPS(LauncherApp.getAppContext(), new OnLocationChangeListener<LocationInfo>() {
            @Override
            public void success(LocationInfo info) {
                MxyLog.d(TAG, "GPS定位成功,读取到GPS信息" + info.toString());
                GPSHelper.getInstance().stopGPS();
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
                    onLocationChangeListener = null;
                }
            }

            @Override
            public void fail() {
                stopLocation();
                MxyLog.i(TAG, "GPS定位失败了,开始上传基站定位");
                locationInfo.setType(0);
                if (onLocationChangeListener != null) {
                    onLocationChangeListener.success(locationInfo);
                    onLocationChangeListener = null;
                }
            }
        });
    }


    /**
     * do定位上传成功
     *
     * @param json
     */
    private void locationUploadSuccessful(String json) {
    }

    /**
     * 定位上传失败~
     */
    private void locationUploadFail() {
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
        EventBus.getDefault().post(new LocationEvent(1));
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
                locationUploadSuccessful(baseObtain);
            }

            @Override
            public void onFail() {
                MxyLog.w(TAG, "上传定位失败.");
                locationUploadFail();
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
                    MxyLog.d(TAG, "GPS定位超时了,开始上传基站定位");
                    stopLocation();
                    locationInfo.setType(0);
                    if (onLocationChangeListener != null) {
                        onLocationChangeListener.success(locationInfo);
                        onLocationChangeListener = null;
                    }
                    break;
                case START_LOAD_LOCATION_START:
                    doOnStartGPSLocation();
                    break;
            }
            return false;
        }
    });
}
