package com.sczn.wearlauncher.card;


import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.absFragment;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.sp.SPUtils;
import com.sczn.wearlauncher.card.geographic.CompassView;
import com.sczn.wearlauncher.card.geographic.JsonAtmosphere;
import com.sczn.wearlauncher.card.geographic.JsonLocationInfo;
import com.sczn.wearlauncher.card.geographic.JsonWeather;
import com.sczn.wearlauncher.card.geographic.WeatherHttpTask;
import com.sczn.wearlauncher.card.geographic.WeatherHttpTask.WeatherCallBack;

public class CardFragmentPressure extends absFragment {

    private Context mContext;
    private CompassView mPressurePoint;
    private TextView mPressureTextView, mTempTextView, mBadSignal;
    private float mTargetDirection = 0.0f, mCurrentDirection = 0.0f;
    private float mPressure = 0.0f;
    private LinearLayout mPressurePart, mTempPart;
    private String mLastCity;
    private ProgressBar progressBar;


    private Handler handler = new Handler();
    private Runnable mPressureBarThread = new Runnable() {
        @Override
        public void run() {
            if (mPressure > 0) {
                if (Math.abs(mTargetDirection - mCurrentDirection) > 0.5) {
                    if (mCurrentDirection < mTargetDirection) {
                        mCurrentDirection += 0.3;
                    }
                    if (mCurrentDirection > mTargetDirection) {
                        mCurrentDirection -= 0.3;
                    }

                    mPressurePoint.updateDirection(mCurrentDirection);
                }
            }
            handler.postDelayed(mPressureBarThread, 30);
        }
    };

    @Override
    protected int getLayoutResouceId() {
        mContext = getActivity();
        return R.layout.fragment_card_pressure;
    }

    @Override
    protected void initView() {
        mRootView = findViewById(R.id.pressure_root);
        mPressurePoint = findViewById(R.id.pressure_point);
        mPressureTextView = findViewById(R.id.pressure_text);
        mTempTextView = findViewById(R.id.temp_text);
        mPressurePart = findViewById(R.id.pressure_part);
        mTempPart = findViewById(R.id.temp_part);
        mPressurePart.setVisibility(View.GONE);
        mTempPart.setVisibility(View.GONE);
        mBadSignal = findViewById(R.id.bad_signal);
        progressBar = findViewById(R.id.progressBar1);
    }

    @Override
    protected void initData() {
        locateSetting();
    }

    @Override
    protected void startFreshData() {
        super.startFreshData();
        progressBar.setVisibility(View.VISIBLE);
        initData2();
        locateSetting();
        getYahooWeather(mLastCity);
    }

    @Override
    protected void endFreshData() {
        super.endFreshData();
        initData2();
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void initData2() {
        mTempPart.setVisibility(View.GONE);
        mPressurePart.setVisibility(View.GONE);
        mBadSignal.setVisibility(View.INVISIBLE);
        mTargetDirection = 0.0f;
        mCurrentDirection = 0.0f;
        mPressure = 0.0f;
        mPressurePoint.updateDirection(mCurrentDirection);

    }

    private void locateSetting() {
        //初始化城市
        mLastCity = (String) SPUtils.getParam(mContext, "last_city", getString(R.string.city));
    }

    private void getYahooWeather(String city) {
        MxyLog.e("city", city);
        WeatherHttpTask.getInstance().getWeatherInfo(city, new WeatherCallBack() {

            @Override
            public void onSuccess(JsonWeather weather, JsonLocationInfo cityInfo,
                                  JsonAtmosphere atmosphere) {
                String pressure = atmosphere.getPressure();
                String temp = transFormData(weather.getCondition().getTemp());
                mPressureTextView.setText(pressure);
                mTempTextView.setText(temp);
                mPressure = Float.valueOf(pressure);
                //旋转角度
                mTargetDirection = (float) ((mPressure - 1010) * 2.8);
                mBadSignal.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                mPressurePart.setVisibility(View.VISIBLE);
                mTempPart.setVisibility(View.VISIBLE);

                handler.postDelayed(mPressureBarThread, 1000);
            }

            @Override
            public void onError(String msg) {
                MxyLog.e("pre", "error:" + msg);
                progressBar.setVisibility(View.INVISIBLE);
                mBadSignal.setVisibility(View.VISIBLE);
            }
        });
    }

    //华氏度转摄氏度
    private String transFormData(String temp) {
        int formerTemp = Integer.parseInt(temp);
        int newTemp = (int) ((formerTemp - 32) / 1.8);
        char symbol = (char) 176;//176为°的符号字符
        return String.valueOf(newTemp) + String.valueOf(symbol);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
