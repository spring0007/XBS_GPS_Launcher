package com.sczn.wearlauncher.card.geographic;

public class ModelGeographic {

    public static final String SHARE_KEY_TEMP = "sczn_last_temp";
    public static final String SHARE_KEY_WEATHER = "sczn_last_weather";

    private JsonWeather mWeather;
    private JsonAtmosphere mAtmosphere;
    private JsonLocationInfo mLocationInfo;
    private String cutyZh;


    public String getCutyZh() {
        return cutyZh;
    }

    public void setCutyZh(String cutyZh) {
        this.cutyZh = cutyZh;
    }

    public ModelGeographic(String cutyZh) {
        this.cutyZh = cutyZh;
    }

    public ModelGeographic(JsonWeather mWeather, JsonAtmosphere mAtmosphere, JsonLocationInfo mLocationInfo) {
        super();
        this.mWeather = mWeather;
        this.mAtmosphere = mAtmosphere;
        this.mLocationInfo = mLocationInfo;
    }

    public JsonWeather getmWeather() {
        return mWeather;
    }

    public void setmWeather(JsonWeather mWeather) {
        this.mWeather = mWeather;
    }

    public JsonAtmosphere getmAtmosphere() {
        return mAtmosphere;
    }

    public void setmAtmosphere(JsonAtmosphere mAtmosphere) {
        this.mAtmosphere = mAtmosphere;
    }

    public JsonLocationInfo getmLocationInfo() {
        return mLocationInfo;
    }

    public void setmLocationInfo(JsonLocationInfo mLocationInfo) {
        this.mLocationInfo = mLocationInfo;
    }

}
