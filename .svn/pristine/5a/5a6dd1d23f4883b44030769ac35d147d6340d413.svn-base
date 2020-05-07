package com.sczn.wearlauncher.socket.command.post;

import com.sczn.wearlauncher.socket.command.CommandResultCallback;

/**
 * Description:直接以JSON的格式发送
 * Created by Bingo on 2019/4/29.
 */
public class JsonCmd extends BaseCommand {

    private String json;

    public JsonCmd(String json, CommandResultCallback resultCallback) {
        super(resultCallback);
        this.json = json;
    }

    @Override
    public String send() {
        return json;
    }
}
