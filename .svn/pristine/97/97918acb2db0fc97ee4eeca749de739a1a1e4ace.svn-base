package com.sczn.wearlauncher.socket.command.post;

import com.sczn.wearlauncher.location.LocationCallBackHelper;
import com.sczn.wearlauncher.location.bean.LocationInfo;
import com.sczn.wearlauncher.socket.command.CmdCode;
import com.sczn.wearlauncher.socket.command.CommandResultCallback;
import com.sczn.wearlauncher.socket.command.bean.GpsInfo;
import com.sczn.wearlauncher.socket.command.bean.WiFiAndBaseStationInfo;
import com.sczn.wearlauncher.util.GsonHelper;
import com.sczn.wearlauncher.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:上传定位数据
 * Created by Bingo on 2019/5/5.
 */
public class LocationCmd extends BaseCommand {

    private LocationInfo info;

    public LocationCmd(LocationInfo info, CommandResultCallback resultCallback) {
        super(resultCallback);
        this.info = info;
    }

    @Override
    public String send() {
        if (info != null) {
            if (info.getType() == 2) {//gps数据
                List<GpsInfo.DataBean.GpsBean> beans = new ArrayList<>();
                GpsInfo.DataBean.GpsBean bean = new GpsInfo.DataBean.GpsBean();
                bean.setLat(info.getLat());
                bean.setLng(info.getLng());
                bean.setAccuracy(info.getAccuracy());
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
                return GsonHelper.getInstance().getGson().toJson(gpsInfo);
            } else {//基站或者WiFi数据
                WiFiAndBaseStationInfo.DataBean.LbsBean lbs = new WiFiAndBaseStationInfo.DataBean.LbsBean();
                lbs.setSmac("");
                lbs.setServerip("");
                lbs.setImsi("");
                lbs.setNetwork("");

                lbs.setCdma(info.getCdma() == null ? 0 : Integer.parseInt(info.getCdma()));

                lbs.setBts(info.getBts());
                lbs.setNearbts(info.getNearbts());

                lbs.setMmac(info.getMmac());
                lbs.setMacs(info.getMacs());
                lbs.setTimestamp(TimeUtil.getCurrentTimeSec());

                WiFiAndBaseStationInfo.DataBean data = new WiFiAndBaseStationInfo.DataBean();
                data.setLocationType(info.getType());
                data.setOptUserId(LocationCallBackHelper.optUserId);
                data.setLbs(lbs);

                WiFiAndBaseStationInfo info = new WiFiAndBaseStationInfo();
                info.setType(CmdCode.LOCATION);
                info.setTimestamp(TimeUtil.getCurrentTimeSec());
                info.setData(data);
                return GsonHelper.getInstance().getGson().toJson(info);
            }
        }
        return "";
    }
}
