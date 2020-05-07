package com.sczn.wearlauncher.socket.command.post;

import com.google.gson.Gson;
import com.sczn.wearlauncher.socket.command.CmdCode;
import com.sczn.wearlauncher.socket.command.CommandResultCallback;
import com.sczn.wearlauncher.socket.command.bean.BasePostBean;
import com.sczn.wearlauncher.util.TimeUtil;

/**
 * Description:
 * Created by Bingo on 2019/3/7.
 */
public class GetFenceCmd extends BaseCommand {

    public GetFenceCmd(CommandResultCallback resultCallback) {
        super(resultCallback);
    }

    @Override
    public String send() {
        BasePostBean bean = new BasePostBean();
        bean.setA(0);
        bean.setType(CmdCode.FENCE_INFO);
        bean.setTimestamp(TimeUtil.getCurrentTimeSec());
        return new Gson().toJson(bean);
    }
}
