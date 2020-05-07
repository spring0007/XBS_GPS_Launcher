package com.sczn.wearlauncher.socket.command.post;

import com.google.gson.Gson;
import com.sczn.wearlauncher.location.BaseStationHelper;
import com.sczn.wearlauncher.location.LocationCallBackHelper;
import com.sczn.wearlauncher.location.bean.BaseStationInfo;
import com.sczn.wearlauncher.socket.command.CmdCode;
import com.sczn.wearlauncher.socket.command.CommandResultCallback;
import com.sczn.wearlauncher.socket.command.bean.WiFiAndBaseStationInfo;
import com.sczn.wearlauncher.util.TimeUtil;

import java.util.List;

/**
 * Description:基站和WiFi定位
 * Created by Bingo on 2019/3/1.
 */
public class WiFiAndBaseStationCmd extends BaseCommand {

    private int locationType;
    private BaseStationInfo cbs;
    private List<BaseStationInfo> nearbyCbs;
    private String wifi;
    private String nearbyWifi;

    public WiFiAndBaseStationCmd(int locationType, BaseStationInfo cbs,
                                 List<BaseStationInfo> nearbyCbs, String wifi, String nearbyWifi,
                                 CommandResultCallback resultCallback) {
        super(resultCallback);
        this.locationType = locationType;
        this.cbs = cbs;
        this.nearbyCbs = nearbyCbs;
        this.wifi = wifi;
        this.nearbyWifi = nearbyWifi;
    }

    @Override
    public String send() {
        WiFiAndBaseStationInfo.DataBean.LbsBean lbs = new WiFiAndBaseStationInfo.DataBean.LbsBean();
        lbs.setSmac("");
        lbs.setServerip("");
        lbs.setImsi("");
        lbs.setNetwork("");
        if (cbs != null) {
            lbs.setCdma(cbs.getBaseType());
        }
        lbs.setBts(BaseStationHelper.formatBaseInfo(cbs));
        lbs.setNearbts(BaseStationHelper.formatNearbyBaseInfo(nearbyCbs));

        lbs.setMmac(wifi == null ? "" : wifi);
        lbs.setMacs(nearbyWifi == null ? "" : nearbyWifi);
        lbs.setTimestamp(TimeUtil.getCurrentTimeSec());

        WiFiAndBaseStationInfo.DataBean data = new WiFiAndBaseStationInfo.DataBean();
        data.setLocationType(locationType);
        data.setOptUserId(LocationCallBackHelper.optUserId);
        data.setLbs(lbs);

        WiFiAndBaseStationInfo info = new WiFiAndBaseStationInfo();
        info.setType(CmdCode.LOCATION);
        info.setTimestamp(TimeUtil.getCurrentTimeSec());
        info.setData(data);
        return new Gson().toJson(info);
    }
}
