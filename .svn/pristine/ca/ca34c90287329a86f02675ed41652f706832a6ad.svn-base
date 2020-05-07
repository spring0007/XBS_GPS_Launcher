package com.sczn.wearlauncher.location;

import android.content.Context;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.location.bean.LocationInfo;
import com.sczn.wearlauncher.location.impl.OnLocationChangeListener;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

/**
 * Description:GPS定位
 * Created by Bingo on 2019/1/15.
 */
public class GPSHelper {

    private final String TAG = "GPSHelper";
    private static GPSHelper helper;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private LocationInfo locationInfo;

    /**
     * 是否正在定位
     */
    private boolean isLocation = false;

    /**
     * 单例
     *
     * @return
     */
    public static GPSHelper getInstance() {
        if (null == helper) {
            synchronized (GPSHelper.class) {
                if (null == helper) {
                    helper = new GPSHelper();
                }
            }
        }
        return helper;
    }

    /**
     * 初始化location服务
     * 开始GPS定位
     * 绑定监听
     * 参数1，设备：有GPS_PROVIDER和NETWORK_PROVIDER两种，前者是GPS,后者是GPRS以及WIFI定位
     * 参数2，位置信息更新周期.单位是毫秒
     * 参数3，位置变化最小距离：当位置距离变化超过此值时，将更新位置信息
     * 参数4，监听
     * 备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新
     *
     * @param mContext
     */
    public void startGPS(final Context mContext, final OnLocationChangeListener<LocationInfo> onLocationChangeListener) {
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        locationInfo = new LocationInfo();
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                MxyLog.i(TAG, "onLocationChanged: 位置信息变化时触发");
                if (location != null) {
                    if (location.getAccuracy() > 300) {
                        MxyLog.d(TAG, "定位的精确度:" + location.getAccuracy());
                    }
                    getLocationInfo(location, onLocationChangeListener);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                switch (status) {
                    case LocationProvider.AVAILABLE:
                        MxyLog.i(TAG, "当前GPS状态为可见状态");
                        break;
                    case LocationProvider.OUT_OF_SERVICE:
                        MxyLog.i(TAG, "当前GPS状态为服务区外状态");
                        break;
                    case LocationProvider.TEMPORARILY_UNAVAILABLE:
                        MxyLog.i(TAG, "当前GPS状态为暂停服务状态");
                        break;
                    default:
                        MxyLog.i(TAG, "当前GPS状态为" + status);
                        break;
                }
            }

            @Override
            public void onProviderEnabled(String provider) {
                MxyLog.i(TAG, "GPS开启时触发");
            }

            @Override
            public void onProviderDisabled(String provider) {
                MxyLog.i(TAG, "GPS禁用时触发");
            }
        };
        if (locationManager != null) {
            String locationProvider;
            List<String> providers = locationManager.getProviders(true);//获取所有可用的位置提供器
            if (providers.contains(LocationManager.GPS_PROVIDER)) {
                MxyLog.d(TAG, "GPS定位");
                locationProvider = LocationManager.GPS_PROVIDER;
                startRequestLocationUpdates(locationProvider, 0, 0);
            } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
                MxyLog.d(TAG, "Network定位");
                locationProvider = LocationManager.NETWORK_PROVIDER;
                if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER) && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    startRequestLocationUpdates(locationProvider, 0, 0);
                }
            } else {
                onLocationChangeListener.fail();
                MxyLog.d(TAG, "没有可用的位置提供器");
                return;
            }
            //上一次的定位信息
            // Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
            // if (lastKnownLocation != null) {
            //     // 不为空,显示地理位置经纬度
            //     startRequestLocationUpdates("gps", 3000, 1);
            //     double Latitude = lastKnownLocation.getLatitude();
            //     double Longitude = lastKnownLocation.getLongitude();
            //     MxyLog.i(TAG, "lastKnownLocation: Latitude = " + Latitude);
            //     MxyLog.i(TAG, "lastKnownLocation: Longitude = " + Longitude);
            //     getLocationInfo(lastKnownLocation, onLocationChangeListener);
            // }
            if (locationManager != null) {
                locationManager.removeGpsStatusListener(mGpsStatusListener);
                locationManager.addGpsStatusListener(mGpsStatusListener);
            }
        }
    }

    /**
     * 开始请求gps定位
     *
     * @param locationProvider
     * @param minTime
     * @param minDistance
     */
    private void startRequestLocationUpdates(String locationProvider, long minTime, float minDistance) {
        locationManager.requestLocationUpdates(locationProvider, minTime, minDistance, locationListener);
    }


    /**
     * 得到城市的信息
     *
     * @param info
     */
    private void getLocationInfo(Location info, OnLocationChangeListener<LocationInfo> onLocationChangeListener) {
        if (info != null) {
            BigDecimal b = new BigDecimal(info.getLatitude());
            double lat = b.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();

            BigDecimal b1 = new BigDecimal(info.getLongitude());
            double lng = b1.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();

            locationInfo.setAccuracy(info.getAccuracy());
            locationInfo.setLat(lat);
            locationInfo.setLng(lng);
            locationInfo.setAltitude(info.getAltitude());
            locationInfo.setBearing(info.getBearing());
            locationInfo.setSpeed(info.getSpeed());
            onLocationChangeListener.success(locationInfo);
        } else {
            onLocationChangeListener.fail();
        }
    }

    /**
     * 返回查询条件
     *
     * @return
     */
    private static Criteria getCriteria() {
        Criteria criteria = new Criteria();
        // 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(false);
        // 设置是否需要方位信息
        criteria.setBearingRequired(false);
        // 设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }

    /**
     * 卫星状态监听
     *
     * @return
     */
    private GpsStatus.Listener mGpsStatusListener = new GpsStatus.Listener() {
        @Override
        public void onGpsStatusChanged(int event) {
            switch (event) {
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    MxyLog.i(TAG, "第一次定位");
                    break;
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    MxyLog.i(TAG, "卫星状态改变");
                    if (locationManager != null) {
                        GpsStatus gpsStatus = locationManager.getGpsStatus(null);
                        // 获取卫星颗数的默认最大值
                        int maxSatellites = gpsStatus.getMaxSatellites();
                        // 创建一个迭代器保存所有卫星
                        Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
                        int count = 0;
                        while (iters.hasNext() && count <= maxSatellites) {
                            GpsSatellite s = iters.next();
                            count++;
                        }
                        locationInfo.setGpsCount(count);
                        MxyLog.i(TAG, "搜索到：" + count + "颗卫星");
                    }
                    break;
                case GpsStatus.GPS_EVENT_STARTED:
                    MxyLog.i(TAG, "定位启动");
                    break;
                case GpsStatus.GPS_EVENT_STOPPED:
                    MxyLog.i(TAG, "定位结束");
                    break;
            }
        }
    };

    /**
     * 停止GPS定位
     */
    public void stopGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
            locationManager.removeGpsStatusListener(mGpsStatusListener);
            locationManager = null;
        }
        if (locationListener != null) {
            locationListener = null;
        }
        if (mGpsStatusListener != null) {
            mGpsStatusListener = null;
        }
    }
}
