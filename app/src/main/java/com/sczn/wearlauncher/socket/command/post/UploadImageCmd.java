package com.sczn.wearlauncher.socket.command.post;

import com.sczn.wearlauncher.socket.command.CommandResultCallback;

/**
 * Description:
 * Created by Bingo on 2019/3/8.
 */
public class UploadImageCmd extends BaseCommand {

    private String base64Img;

    public UploadImageCmd(String base64Img, CommandResultCallback resultCallback) {
        super(resultCallback);
        this.base64Img = base64Img;
    }

    @Override
    public String send() {
        return "{a:0,type:22,data:{img:" + base64Img + ",timestamp:156988}}";
    }
}
