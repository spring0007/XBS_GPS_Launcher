package com.sczn.wearlauncher.socket.command.gprs.post;

import com.sczn.wearlauncher.socket.command.CommandResultCallback;
import com.sczn.wearlauncher.socket.command.post.BaseCommand;

/**
 * Description:
 * Created by Bingo on 2019/5/8.
 */
public class StringTo extends BaseCommand {

    private String content;

    /**
     * 不需要回调
     *
     * @param content
     * @param commandCallBack
     */
    public StringTo(String content, CommandResultCallback commandCallBack) {
        super(commandCallBack, false);
        this.content = content;
    }

    /**
     * @param needCallBack
     * @param content
     * @param commandCallBack
     */
    public StringTo(boolean needCallBack, String content, CommandResultCallback commandCallBack) {
        super(commandCallBack, needCallBack);
        this.content = content;
    }

    @Override
    public String send() {
        return Base.compose(content);
    }
}
