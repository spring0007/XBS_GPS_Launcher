package com.sczn.wearlauncher.socket.command.gprs.post;

import com.sczn.wearlauncher.location.bean.LocationInfo;
import com.sczn.wearlauncher.socket.command.CmdCode;
import com.sczn.wearlauncher.socket.command.CommandResultCallback;
import com.sczn.wearlauncher.socket.command.post.BaseCommand;

/**
 * Description:
 * Created by Bingo on 2019/5/10.
 */
public class PP extends BaseCommand {

    private String content;

    public PP(LocationInfo info, CommandResultCallback resultCallback) {
        super(resultCallback);
        this.content = CmdCode.PP + "," + UD.ud(info);
    }

    @Override
    public String send() {
        return Base.compose(content);
    }
}
