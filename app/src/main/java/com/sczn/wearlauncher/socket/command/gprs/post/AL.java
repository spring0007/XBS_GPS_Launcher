package com.sczn.wearlauncher.socket.command.gprs.post;

import com.sczn.wearlauncher.location.bean.LocationInfo;
import com.sczn.wearlauncher.socket.command.CmdCode;
import com.sczn.wearlauncher.socket.command.CommandResultCallback;
import com.sczn.wearlauncher.socket.command.post.BaseCommand;

/**
 * Description:报警数据上报
 * Created by Bingo on 2019/5/7.
 */
public class AL extends BaseCommand {

    private String content;

    public AL(LocationInfo info, String status, CommandResultCallback resultCallback) {
        super(resultCallback);
        this.content = CmdCode.AL + "," + UD.ud(info, status);
    }

    @Override
    public String send() {
        return Base.compose(content);
    }
}
