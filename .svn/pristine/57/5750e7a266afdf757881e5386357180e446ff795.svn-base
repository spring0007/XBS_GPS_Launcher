package com.sczn.wearlauncher.socket.command.post;

import com.sczn.wearlauncher.socket.command.CmdCode;
import com.sczn.wearlauncher.socket.command.CommandResultCallback;
import com.sczn.wearlauncher.socket.command.bean.Shake;
import com.sczn.wearlauncher.util.GsonHelper;
import com.sczn.wearlauncher.util.TimeUtil;

/**
 * Description:
 * Created by Bingo on 2019/3/26.
 */
public class ShakeCmd extends BaseCommand {

    private double lat;
    private double lng;

    public ShakeCmd(double lat, double lng, CommandResultCallback resultCallback) {
        super(resultCallback);
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public String send() {
        Shake.DataBean bean = new Shake.DataBean();
        bean.setLat(lat);
        bean.setLng(lng);

        Shake shake = new Shake();
        shake.setData(bean);
        shake.setType(CmdCode.SHAKE);
        shake.setTimestamp(TimeUtil.getCurrentTimeSec());
        return GsonHelper.getInstance().getGson().toJson(shake);
        // return "{\"a\": 0,\"type\": 5,\"timestamp\": 1501123709,\"data\": {\"lat\": \"" + lat + "\",\"lng\": \"" + lng + "\"}}";
    }
}
