package com.sczn.wearlauncher.socket.command.post;

import com.google.gson.Gson;
import com.sczn.wearlauncher.socket.command.CmdCode;
import com.sczn.wearlauncher.socket.command.CommandResultCallback;
import com.sczn.wearlauncher.socket.command.bean.BasePostBean;
import com.sczn.wearlauncher.util.TimeUtil;

/**
 * Description:日程提醒
 * Created by Bingo on 2019/3/12.
 */
public class DayRemindCmd extends BaseCommand {

    public DayRemindCmd(CommandResultCallback resultCallback) {
        super(resultCallback);
    }

    @Override
    public String send() {
        BasePostBean bean = new BasePostBean();
        bean.setA(0);
        bean.setType(CmdCode.DAY_REMIND);
        bean.setTimestamp(TimeUtil.getCurrentTimeSec());
        return new Gson().toJson(bean);
    }
}
