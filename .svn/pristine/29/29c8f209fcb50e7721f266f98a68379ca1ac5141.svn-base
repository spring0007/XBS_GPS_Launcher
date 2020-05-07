package com.sczn.wearlauncher.socket.command.post;

import com.sczn.wearlauncher.app.AppConfig;
import com.sczn.wearlauncher.socket.command.CmdCode;
import com.sczn.wearlauncher.socket.command.CommandResultCallback;
import com.sczn.wearlauncher.socket.command.bean.Heart;
import com.sczn.wearlauncher.util.GsonHelper;
import com.sczn.wearlauncher.util.TimeUtil;

/**
 * Description:发送一个指令维持心跳
 * Created by Bingo on 2019/2/27.
 */
public class HeatRateCmd extends BaseCommand {

    public HeatRateCmd(CommandResultCallback resultCallback) {
        super(resultCallback);
    }

    @Override
    public String send() {
        Heart.DataBean bean = new Heart.DataBean();
        bean.setPower(AppConfig.getInstance().getBatteryLevel());

        Heart heart = new Heart();
        heart.setType(CmdCode.HEART);
        heart.setTimestamp(TimeUtil.getCurrentTimeSec());
        heart.setData(bean);
        return GsonHelper.getInstance().getGson().toJson(heart);
        // return "{\"type\":0,\"timestamp\":" + TimeUtil.getCurrentTimeSec() + ",\"data\":{\"power\":\"" + AppConfig.getInstance().getBatteryLevel() + "\"}}";
    }
}
