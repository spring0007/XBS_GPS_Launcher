package com.sczn.wearlauncher.socket.command.post;

import com.google.gson.Gson;
import com.sczn.wearlauncher.socket.command.CmdCode;
import com.sczn.wearlauncher.socket.command.CommandResultCallback;
import com.sczn.wearlauncher.socket.command.bean.FenceAlarm;
import com.sczn.wearlauncher.util.TimeUtil;

/**
 * Description:上传安全围栏警告
 * Created by Bingo on 2019/3/7.
 */
public class FenceAlarmCmd extends BaseCommand {

    private String fenceName;
    private double lng;
    private double lat;

    public FenceAlarmCmd(String fenceName, double lng, double lat, CommandResultCallback resultCallback) {
        super(resultCallback);
        this.fenceName = fenceName;
        this.lng = lng;
        this.lat = lat;
    }

    @Override
    public String send() {
        FenceAlarm.DataBean.ContentBean bean = new FenceAlarm.DataBean.ContentBean();
        bean.setFenceName(fenceName);
        bean.setLng(lng);
        bean.setLat(lat);

        FenceAlarm.DataBean data = new FenceAlarm.DataBean();
        data.setContent(bean);

        FenceAlarm alarm = new FenceAlarm();
        alarm.setType(CmdCode.FENCE_ALARM);
        alarm.setTimestamp(TimeUtil.getCurrentTimeSec());
        alarm.setData(data);
        return new Gson().toJson(alarm);
    }
}
