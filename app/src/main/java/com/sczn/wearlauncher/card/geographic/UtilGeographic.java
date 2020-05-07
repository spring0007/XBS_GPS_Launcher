package com.sczn.wearlauncher.card.geographic;

import android.os.SystemClock;

import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.sp.SPUtils;
import com.sczn.wearlauncher.card.CardFragmentWeather.WeatherParse;
import com.sczn.wearlauncher.card.geographic.JsonWeather.TempCondition;
import com.sczn.wearlauncher.util.NetworkUtils;

import java.text.DecimalFormat;

public class UtilGeographic {

    private static class Holder {
        private static UtilGeographic instance = new UtilGeographic();
    }

    public static UtilGeographic getInstance() {
        return Holder.instance;
    }

    public static final int FRESH_INTERVAL_MIN = 5 * 1000;
    private IWeatherListen mWeatherListen;
    private IGeographicListen mAltitedeListen;
    private long lastFreshTime = Long.MIN_VALUE;
    private ModelGeographic mGeographicInfo;

    public void startGetWeather(IWeatherListen listen) {
        this.mWeatherListen = listen;

        if (!NetworkUtils.isNetWorkConnected(LauncherApp.getAppContext())) {
            //mWeatherListen.onFailed();
            //return;
        }

        final long currTime = SystemClock.currentThreadTimeMillis();
        if ((currTime - lastFreshTime) < FRESH_INTERVAL_MIN && mGeographicInfo != null) {
            mWeatherListen.onSuccess(mGeographicInfo);
            return;
        }
    }

    public void stopGetWeather() {
        mWeatherListen = null;
    }

    public void startGetAltitude(IGeographicListen listen) {
        this.mAltitedeListen = listen;

        if (!NetworkUtils.isNetWorkConnected(LauncherApp.getAppContext())) {
            //mWeatherListen.onFailed();
            //return;
        }

        final long currTime = SystemClock.currentThreadTimeMillis();
        if ((currTime - lastFreshTime) < FRESH_INTERVAL_MIN && mGeographicInfo != null) {
            mAltitedeListen.onAtmosphere(mGeographicInfo);
        }
    }

    public void stopGetAltitude() {
        mAltitedeListen = null;
    }

    private void freshAltitude(double Altitude) {
        if (mAltitedeListen != null) {
            mAltitedeListen.onAltitude(Altitude);
        }
    }

    private void freshWeather(ModelGeographic info) {
        mGeographicInfo = info;
        lastFreshTime = SystemClock.currentThreadTimeMillis();

        if (info.getmWeather() != null) {
            final TempCondition temp = info.getmWeather().getCondition();
            SPUtils.setParam(LauncherApp.appContext, ModelGeographic.SHARE_KEY_TEMP,
                    Integer.parseInt(WeatherParse.transFormData(temp.getTemp())));
            SPUtils.setParam(LauncherApp.appContext, ModelGeographic.SHARE_KEY_WEATHER,
                    WeatherParse.getWeatherType(temp.getText()));

            if (mAltitedeListen != null) {
                mAltitedeListen.onAtmosphere(info);

                DecimalFormat df = new DecimalFormat("0.00");
                df.getRoundingMode();
                mAltitedeListen.onAltitude(44330000 * (1 - (Math.pow((Double.parseDouble(df.format(Double.parseDouble(info.getmAtmosphere().getPressure()))) / 1013.25),
                        (float) 1.0 / 5255.0))));
            }
        }
        if (mWeatherListen != null) {
            mWeatherListen.onSuccess(info);
        }
    }

    /**
     * 根据城市查询当前的天气
     *
     * @param city
     */
    private void getHttpWeather(final String city) {
        MxyLog.d("getHttpWeather", "city = " + city);
        if (mWeatherListen != null) {
            mGeographicInfo = new ModelGeographic(city);
            mWeatherListen.onSuccess(mGeographicInfo);
        }
        WeatherHttpTask.getInstance().getWeatherInfo(city, new WeatherHttpTask.WeatherCallBack() {
            @Override
            public void onSuccess(JsonWeather weather, JsonLocationInfo cityInfo, JsonAtmosphere atmosphere) {
                mGeographicInfo = new ModelGeographic(weather, atmosphere, cityInfo);
                mGeographicInfo.setCutyZh(city);
                freshWeather(mGeographicInfo);
            }

            @Override
            public void onError(String msg) {
                if (mWeatherListen != null) {
                    mWeatherListen.onFailed();
                }
                MxyLog.d(UtilGeographic.this, "getHttpWeather--error=" + msg);
            }
        });
    }

    public interface IWeatherListen {
        void onFailed();

        void onSuccess(ModelGeographic info);
    }

    public interface IGeographicListen {
        void onAtmosphereFailed();

        void onAltitudeFailed();

        void onAtmosphere(ModelGeographic info);

        void onAltitude(double altitude);
    }
}
