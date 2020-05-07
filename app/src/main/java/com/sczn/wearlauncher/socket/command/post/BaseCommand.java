package com.sczn.wearlauncher.socket.command.post;

import com.sczn.wearlauncher.socket.command.CommandResultCallback;

/**
 * Description:
 * Created by Bingo on 2019/2/25.
 */
public abstract class BaseCommand {

    /**
     * cmd code
     */
    private String cmd;
    private byte[] data;
    /**
     * 默认需要回调
     */
    private boolean needCallBack = true;
    private CommandResultCallback resultCallback;


    public BaseCommand(CommandResultCallback resultCallback) {
        this.resultCallback = resultCallback;
    }

    public BaseCommand(String cmd, CommandResultCallback resultCallback) {
        this.cmd = cmd;
        this.resultCallback = resultCallback;
    }

    public BaseCommand(CommandResultCallback commandCallBack, boolean needCallBack) {
        this.needCallBack = needCallBack;
        this.resultCallback = commandCallBack;
    }


    public boolean getNeedCallBack() {
        return needCallBack;
    }

    public void setNeedCallBack(boolean needCallBack) {
        this.needCallBack = needCallBack;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getCmd() {
        return cmd;
    }

    public abstract String send();

    public void onFail() {
        if (this.resultCallback != null) {
            this.resultCallback.onFail();
        }
    }

    public void onSuccess(String baseObtain) {
        if (this.resultCallback != null) {
            this.resultCallback.onSuccess(baseObtain);
        }
    }
}
