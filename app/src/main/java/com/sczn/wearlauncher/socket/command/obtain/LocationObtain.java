package com.sczn.wearlauncher.socket.command.obtain;

/**
 * Description:请求服务器,返回的定位信息
 * Created by Bingo on 2019/3/4.
 */
public class LocationObtain {
    private String address;
    private double lng;
    private double lat;
    private String optUserId;//app用户id

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getOptUserId() {
        return optUserId;
    }

    public void setOptUserId(String optUserId) {
        this.optUserId = optUserId;
    }
}
