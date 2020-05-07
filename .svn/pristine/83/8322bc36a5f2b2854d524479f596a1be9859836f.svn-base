package com.sczn.wearlauncher.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.base.absFragment;
import com.sczn.wearlauncher.base.util.DateFormatUtil;
import com.sczn.wearlauncher.card.geographic.JsonWeather.TempCondition;
import com.sczn.wearlauncher.card.geographic.ModelGeographic;
import com.sczn.wearlauncher.card.geographic.UtilGeographic.IWeatherListen;
import com.sczn.wearlauncher.event.LocationEvent;
import com.sczn.wearlauncher.sp.SPKey;
import com.sczn.wearlauncher.sp.SPUtils;
import com.sczn.wearlauncher.util.NetworkUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Locale;

/**
 * 包含天气预报,电量的页面
 */
public class HomeFragment extends absFragment implements IWeatherListen {

    public static final String TAG = "HomeFragment";
    public static final int STATE_LOADING = 0;
    public static final int STATE_LOAD_SUCCESS = 1;
    public static final int STATE_LOAD_FAILED = 2;

    private TextView mTime;
    private TextView mCity;
    private TextView mWeatherInfo;
    private TextView mWeatherTemp;
    private ImageView mWeatherIcon;

    //刷新显示的时间
    private static final int SET_TIME = 2;

    //位置信息
    private String location = "";

    /**
     *
     */
    private Handler mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SET_TIME:
                    mTime.setText(DateFormatUtil.getCurrTimeString(DateFormatUtil.YYYY_MM_DD_HM));
                    mHandler.sendEmptyMessageDelayed(SET_TIME, 1000);
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    /**
     * @return
     */
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResouceId() {
        return R.layout.fragment_card_weather;
    }

    @Override
    protected void initView() {
        mTime = findViewById(R.id.card_weather_time);
        mCity = findViewById(R.id.card_weather_city);
        mWeatherInfo = findViewById(R.id.card_weather_info);
        mWeatherTemp = findViewById(R.id.card_weather_temp);
        mWeatherIcon = findViewById(R.id.card_weather_icon);
    }

    @Override
    protected void initData() {
        mTime.setText(DateFormatUtil.getCurrTimeString(DateFormatUtil.YYYY_MM_DD_HM));
        mCity.setText(R.string.pressure_bad_signal);

        mWeatherInfo.setText(R.string.Unkown);
        mWeatherIcon.setImageResource(R.drawable.weather_sunny_today);
        mWeatherTemp.setText(String.format(getString(R.string.weather_temp), "0"));

        location = (String) SPUtils.getParam(SPKey.LOCATION, "");
    }

    /**
     * 定位的状态
     *
     * @param state
     */
    private void setState(int state) {
        switch (state) {
            case STATE_LOADING:
                mCity.setText(location);
                break;
            case STATE_LOAD_FAILED:
                mCity.setText(R.string.pressure_bad_signal);
                break;
            case STATE_LOAD_SUCCESS:
                mCity.setText(location);
                break;
            default:
                break;
        }
    }

    @Override
    protected void startFreshData() {
        super.startFreshData();
        EventBus.getDefault().register(this);
        mHandler.sendEmptyMessage(SET_TIME);//显示时间`
        if (NetworkUtils.isNetWorkConnected(LauncherApp.getAppContext())) {
            setState(STATE_LOADING);
            //UtilGeographic.getInstance().startGetWeather(HomeFragment.this);
            //LocationCallBackHelper.getInstance().startLocationAndUpload(LauncherApp.getAppContext());
        } else {
            setState(STATE_LOAD_FAILED);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void endFreshData() {
        super.endFreshData();
        EventBus.getDefault().unregister(this);
        //UtilGeographic.getInstance().stopGetWeather();
        //LocationCallBackHelper.getInstance().stopLocation();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onFailed() {
        setState(STATE_LOAD_FAILED);
    }

    @Override
    public void onSuccess(ModelGeographic info) {
        freshWeather(info);
    }

    /**
     * 刷新定位状态
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshLocationEvent(LocationEvent event) {
        if (event != null && getActivity() != null && !getActivity().isFinishing() && !getActivity().isDestroyed()) {
            if (event.getState() == 0) {
                // location = event.getAddress() + event.getLng() + "," + event.getLat();
                location = event.getAddress();
                setState(STATE_LOAD_SUCCESS);
            } else {
                setState(STATE_LOAD_FAILED);
            }
        }
    }

    /**
     * 更新城市和天气
     *
     * @param info
     */
    private void freshWeather(ModelGeographic info) {
        setState(STATE_LOAD_SUCCESS);

        if ("TW".equals(mActivity.getResources().getConfiguration().locale.getCountry())
                || "CN".equals(mActivity.getResources().getConfiguration().locale.getCountry())) {
            if (info.getCutyZh() != null && !info.getCutyZh().isEmpty()) {
                mCity.setText(info.getCutyZh());
            }
        } else {
            if (info.getmLocationInfo() != null && info.getmLocationInfo().getCity() != null && !info.getmLocationInfo().getCity().isEmpty()) {
                mCity.setText(info.getmLocationInfo().getCity());
            }
        }
        if (info.getmWeather() != null) {
            final TempCondition temp = info.getmWeather().getCondition();
            mWeatherTemp.setText(WeatherParse.transFormData(temp.getTemp(), 176));

            switch (WeatherParse.getWeatherType(temp.getText())) {
                case WeatherParse.WEATHER_TYPE_CLOUDY:
                    mWeatherInfo.setText(R.string.weather_cloudy);
                    mWeatherIcon.setImageResource(R.drawable.weather_cloudy_today);
                    break;
                case WeatherParse.WEATHER_TYPE_RAIN:
                    mWeatherInfo.setText(R.string.weather_rain);
                    mWeatherIcon.setImageResource(R.drawable.weather_rain_today);
                    break;
                case WeatherParse.WEATHER_TYPE_SNOW:
                    mWeatherInfo.setText(R.string.weather_snow);
                    mWeatherIcon.setImageResource(R.drawable.weather_snow_today);
                    break;
                case WeatherParse.WEATHER_TYPE_SUNNY:
                    mWeatherInfo.setText(R.string.weather_sunny);
                    mWeatherIcon.setImageResource(R.drawable.weather_sunny_today);
                    break;
                case WeatherParse.WEATHER_TYPE_WINDY:
                    mWeatherInfo.setText(R.string.weather_windy);
                    mWeatherIcon.setImageResource(R.drawable.weather_windy_today);
                    break;
                case WeatherParse.WEATHER_TYPE_UNKNOW:
                default:
                    mWeatherInfo.setText(R.string.Unkown);
                    mWeatherIcon.setImageResource(R.drawable.weather_sunny_today);
                    break;
            }
        }
    }

    public static class WeatherParse {
        public static final int WEATHER_TYPE_UNKNOW = 0;
        public static final int WEATHER_TYPE_SUNNY = 1;
        public static final int WEATHER_TYPE_RAIN = 2;
        public static final int WEATHER_TYPE_CLOUDY = 3;
        public static final int WEATHER_TYPE_WINDY = 4;
        public static final int WEATHER_TYPE_SNOW = 5;

        public static int getWeatherType(String info) {

            final String weather = info.toLowerCase(Locale.getDefault());
            if (weather.equals("Sunny".toLowerCase(Locale.getDefault()))
                    || weather.equals("Mostly Sunny".toLowerCase(Locale.getDefault()))
                    || weather.equals("Partly Sunny".toLowerCase(Locale.getDefault()))
                    || weather.equals("Clear".toLowerCase(Locale.getDefault()))
                    || weather.equals("Mostly Clear".toLowerCase(Locale.getDefault()))) {
                return WEATHER_TYPE_SUNNY;
            }
            if (weather.equals("Cloudy".toLowerCase(Locale.getDefault()))
                    || weather.equals("Partly Cloudy".toLowerCase(Locale.getDefault()))
                    || weather.equals("Mostly Cloudy".toLowerCase(Locale.getDefault()))) {
                return WEATHER_TYPE_CLOUDY;
            }
            if (weather.equals("Thunderstorms".toLowerCase(Locale.getDefault()))
                    || weather.equals("Rain".toLowerCase(Locale.getDefault()))
                    || weather.equals("Showers".toLowerCase(Locale.getDefault()))
                    || weather.equals("Scattered Thunderstorms".toLowerCase(Locale.getDefault()))
                    || weather.equals("Scattered Showers".toLowerCase(Locale.getDefault()))) {
                return WEATHER_TYPE_RAIN;
            }
            if (weather.equals("Windy".toLowerCase(Locale.getDefault()))
                    || weather.equals("Partly Windy".toLowerCase(Locale.getDefault()))
                    || weather.equals("Mostly Windy".toLowerCase(Locale.getDefault()))
                    || weather.equals("Breezy".toLowerCase(Locale.getDefault()))) {
                return WEATHER_TYPE_WINDY;
            }
            if (weather.equals("Snow".toLowerCase(Locale.getDefault()))
                    || weather.equals("Partly Snow".toLowerCase(Locale.getDefault()))
                    || weather.equals("Mostly Snow".toLowerCase(Locale.getDefault()))) {
                return WEATHER_TYPE_SNOW;
            }

            MxyLog.e("WeatherParse", "getWeatherType--info=" + weather + "--not define");
            return WEATHER_TYPE_UNKNOW;
        }

        public static String transFormData(String temp) {
            int formerTemp = Integer.parseInt(temp);
            int newTemp = (int) ((formerTemp - 32) / 1.8);
            return String.valueOf(newTemp);
        }

        public static String transFormData(String temp, int count) {
            int formerTemp = Integer.parseInt(temp);
            int newTemp = (int) ((formerTemp - 32) / 1.8);
            char symbol = (char) count;
            return String.valueOf(newTemp) + String.valueOf(symbol);
        }
    }
}
