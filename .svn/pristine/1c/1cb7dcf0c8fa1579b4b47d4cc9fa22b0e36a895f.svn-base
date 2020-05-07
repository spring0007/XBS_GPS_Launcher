package com.sczn.wearlauncher.socket.command.post;

import com.google.gson.Gson;
import com.sczn.wearlauncher.location.LocationCallBackHelper;
import com.sczn.wearlauncher.socket.command.CmdCode;
import com.sczn.wearlauncher.socket.command.CommandResultCallback;
import com.sczn.wearlauncher.socket.command.bean.GpsInfo;
import com.sczn.wearlauncher.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Created by Bingo on 2019/3/1.
 */
public class GpsCmd extends BaseCommand {

    private double mLatitude;
    private double mLongitude;
    private float accuracy;

    public GpsCmd(double mLatitude, double mLongitude, float accuracy, CommandResultCallback resultCallback) {
        super(resultCallback);
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.accuracy = accuracy;
    }

    @Override
    public String send() {
        List<GpsInfo.DataBean.GpsBean> beans = new ArrayList<>();
        GpsInfo.DataBean.GpsBean bean = new GpsInfo.DataBean.GpsBean();
        bean.setLat(mLatitude);
        bean.setLng(mLongitude);
        bean.setAccuracy(accuracy);
        bean.setTimestamp(TimeUtil.getCurrentTimeSec());
        beans.add(bean);

        GpsInfo.DataBean data = new GpsInfo.DataBean();
        data.setGps(beans);
        data.setLocationType(2);
        data.setOptUserId(LocationCallBackHelper.optUserId);

        GpsInfo gpsInfo = new GpsInfo();
        gpsInfo.setTimestamp(TimeUtil.getCurrentTimeSec());
        gpsInfo.setType(CmdCode.LOCATION);
        gpsInfo.setData(data);
        return new Gson().toJson(gpsInfo);
    }
}
