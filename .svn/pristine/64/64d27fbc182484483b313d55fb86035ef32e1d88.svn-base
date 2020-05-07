package com.sczn.wearlauncher.socket.command.post;

import com.google.gson.Gson;
import com.sczn.wearlauncher.Config;
import com.sczn.wearlauncher.socket.command.CmdCode;
import com.sczn.wearlauncher.socket.command.CommandResultCallback;
import com.sczn.wearlauncher.socket.command.bean.Reset;
import com.sczn.wearlauncher.util.TimeUtil;

/**
 * Description:
 * Created by Bingo on 2019/3/12.
 */
public class ResetCmd extends BaseCommand {

    public ResetCmd(CommandResultCallback resultCallback) {
        super(resultCallback);
    }

    @Override
    public String send() {
        Reset.DataBean bean = new Reset.DataBean();
        bean.setImei(Config.IMEI);

        Reset reset = new Reset();
        //reset.setType(CmdCode.RESET);
        reset.setTimestamp(TimeUtil.getCurrentTimeSec());
        reset.setData(bean);
        return new Gson().toJson(reset);
    }
}
