package com.sczn.wearlauncher.socket.command.gprs.post;

import com.sczn.wearlauncher.location.bean.LocationInfo;
import com.sczn.wearlauncher.socket.command.CmdCode;
import com.sczn.wearlauncher.socket.command.CommandResultCallback;
import com.sczn.wearlauncher.socket.command.post.BaseCommand;

/**
 * Description:盲点补传数据
 * 说明:补传未登陆平台是产生的上报数据.
 * Created by Bingo on 2019/5/7.
 */
public class UD2 extends BaseCommand {

    private String content;

    public UD2(LocationInfo info, CommandResultCallback resultCallback) {
        super(resultCallback);
        this.content = CmdCode.UD2 + "," + UD.ud(info);
    }

    @Override
    public String send() {
        return Base.compose(content);
    }
}
