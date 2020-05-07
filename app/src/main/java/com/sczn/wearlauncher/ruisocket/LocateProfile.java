package com.sczn.wearlauncher.ruisocket;

public class LocateProfile {
    public LocateProfile() {
        // TODO Auto-generated constructor stub
    }

    private String Longitude;//经    度
    private String Latitude;//纬    度
    private String Provider;//提供者gbs、lbs

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getProvider() {
        return Provider;
    }

    public void setProvider(int provider) {
        Provider = "0" + provider;
    }


    @Override
    public String toString() {
        // TODO Auto-generated method stub
        String profile = null;

        profile = Util.timeToOx() + getProvider() + getLatitude() + getLongitude();
        return profile;
    }

}
