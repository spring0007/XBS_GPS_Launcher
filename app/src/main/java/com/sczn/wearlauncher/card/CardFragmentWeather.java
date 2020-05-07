package com.sczn.wearlauncher.card;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.absDialogFragment;
import com.sczn.wearlauncher.base.util.DateFormatUtil;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.card.geographic.JsonWeather.TempCondition;
import com.sczn.wearlauncher.card.geographic.ModelGeographic;
import com.sczn.wearlauncher.card.geographic.UtilGeographic;
import com.sczn.wearlauncher.card.geographic.UtilGeographic.IWeatherListen;

import java.util.Locale;

public class CardFragmentWeather extends absDialogFragment implements IWeatherListen {

    public static final int STATE_LOADING = 0;
    public static final int STATE_LOAD_SUCCESS = 1;
    public static final int STATE_LOAD_FAILED = 2;

    private TextView mTime;
    private TextView mCity;
    private TextView mWeatherInfo;
    private TextView mWeatherTemp;
    private ImageView mWeatherIcon;
    private Context mActivity;
    private Handler mHandler = new Handler();

    public static CardFragmentWeather newInstance() {
        CardFragmentWeather fragment = new CardFragmentWeather();

        return fragment;

    }

    @Override
    protected int getLayoutResouceId() {
        // TODO Auto-generated method stub
        mActivity = getActivity();
        return R.layout.fragment_card_weather;
    }


    protected void initView() {
        // TODO Auto-generated method stub
        mTime = findViewById(R.id.card_weather_time);
        mCity = findViewById(R.id.card_weather_city);
        mWeatherInfo = findViewById(R.id.card_weather_info);
        mWeatherTemp = findViewById(R.id.card_weather_temp);
        mWeatherIcon = findViewById(R.id.card_weather_icon);

    }

    @Override
    protected void creatView() {
        // TODO Auto-generated method stub

        initView();
        initData();
        mHandler.postDelayed(new Runnable() {

            public void run() {
                mHandler.removeCallbacksAndMessages(null);
                mTime.setText(DateFormatUtil.getCurrTimeString(DateFormatUtil.YYYY_MM_DD_HM));
                mHandler.postDelayed(this, 1000);
            }

        }, 1000);
        mHandler.post(freshWeather);
    }

    protected void initData() {
        mTime.setText(DateFormatUtil.getCurrTimeString(DateFormatUtil.YYYY_MM_DD_HM));
        mCity.setText(R.string.pressure_bad_signal);

        mWeatherInfo.setText(R.string.Unkown);
        mWeatherIcon.setImageResource(R.drawable.weather_sunny_today);
        mWeatherTemp.setText(String.format(getString(R.string.weather_temp), 0));

        setState(STATE_LOADING);
    }

    /**
     * 定位的状态
     *
     * @param state
     */
    private void setState(int state) {
        switch (state) {
            case STATE_LOADING:
                mCity.setVisibility(View.GONE);
                break;
            case STATE_LOAD_FAILED:
                mCity.setVisibility(View.VISIBLE);
                // mCity.setText(R.string.pressure_bad_signal);
                break;
            case STATE_LOAD_SUCCESS:
                mCity.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

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


    @Override
    protected void destorytView() {
        // TODO Auto-generated method stub
        UtilGeographic.getInstance().stopGetWeather();
        mHandler.removeCallbacksAndMessages(null);
    }


    private Runnable freshWeather = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            UtilGeographic.getInstance().startGetWeather(CardFragmentWeather.this);
            mHandler.postDelayed(this, UtilGeographic.FRESH_INTERVAL_MIN);
        }
    };

    @Override
    public void onFailed() {
        setState(STATE_LOAD_FAILED);
    }

    @Override
    public void onSuccess(ModelGeographic info) {
        freshWeather(info);
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
