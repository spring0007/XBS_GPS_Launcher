package com.sczn.wearlauncher.socket.command.post;

import com.google.gson.Gson;
import com.sczn.wearlauncher.socket.command.CommandResultCallback;
import com.sczn.wearlauncher.socket.command.bean.NotifyMsg;
import com.sczn.wearlauncher.util.TimeUtil;

/**
 * Description:
 * Created by Bingo on 2019/2/25.
 */
public class NotifyMsgCmd extends BaseCommand {

    private int type;
    private String content;

    public NotifyMsgCmd(int type, String content) {
        super(null);
        this.type = type;
        this.content = content;
    }

    public NotifyMsgCmd(int type, String content, CommandResultCallback resultCallback) {
        super(resultCallback);
        this.type = type;
        this.content = content;
    }

    @Override
    public String send() {
        NotifyMsg.DataBean dataBean = new NotifyMsg.DataBean();
        dataBean.setContent(content);

        NotifyMsg msg = new NotifyMsg();
        msg.setType(type);
        msg.setTimestamp(TimeUtil.getCurrentTimeSec());
        msg.setData(dataBean);
        return new Gson().toJson(msg);
    }
}
