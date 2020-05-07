package com.sczn.wearlauncher.socket.command.gprs.post;

import com.sczn.wearlauncher.base.util.StringUtils;
import com.sczn.wearlauncher.socket.command.CmdCode;
import com.sczn.wearlauncher.socket.command.CommandResultCallback;
import com.sczn.wearlauncher.socket.command.post.BaseCommand;

/**
 * Description:链路保持
 * Created by Bingo on 2019/5/7.
 */
public class LK extends BaseCommand {

    private String content;

    /**
     * 说明:链路保持数据每5分钟发一次,若终端未收到服务器的回复,则会一分钟重连一次，
     * 重连5次后一直连接不上服务器，终端就会重启.
     *
     * @param resultCallback
     */
    public LK(CommandResultCallback resultCallback) {
        super(resultCallback);
        this.content = CmdCode.LK;
    }

    /**
     * 说明:链路保持数据每8分钟发一次,若终端未收到服务器的回复,则会一分钟重连一次，
     * 重连5次后一直连接不上服务器，终端就会重启.
     *
     * @param step
     * @param count
     * @param battery
     * @param resultCallback
     */
    public LK(int step, int count, int battery, CommandResultCallback resultCallback) {
        super(resultCallback);
        this.content = StringUtils.getJoinString(CmdCode.LK, ",", step, ",", count, ",", battery);
    }

    @Override
    public String send() {
        return Base.compose(content);
    }
}
