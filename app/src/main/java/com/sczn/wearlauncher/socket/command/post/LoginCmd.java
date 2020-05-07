package com.sczn.wearlauncher.socket.command.post;

import com.google.gson.Gson;
import com.sczn.wearlauncher.Config;
import com.sczn.wearlauncher.app.AppConfig;
import com.sczn.wearlauncher.socket.command.CmdCode;
import com.sczn.wearlauncher.socket.command.CommandResultCallback;
import com.sczn.wearlauncher.socket.command.bean.Login;
import com.sczn.wearlauncher.util.TimeUtil;

/**
 * Description:
 * Created by Bingo on 2019/2/25.
 */
public class LoginCmd extends BaseCommand {
    public LoginCmd(CommandResultCallback resultCallback) {
        super(resultCallback);
    }

    @Override
    public String send() {
        Login.DataBean dataBean = new Login.DataBean();
        dataBean.setImei(Config.IMEI);
        dataBean.setPower(AppConfig.getInstance().getBatteryLevel());

        Login login = new Login();
        login.setType(CmdCode.LOGIN);
        login.setTimestamp(TimeUtil.getCurrentTimeSec());
        login.setData(dataBean);
        return new Gson().toJson(login);
    }
}
