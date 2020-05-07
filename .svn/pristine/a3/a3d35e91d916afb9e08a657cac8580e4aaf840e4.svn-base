package com.sczn.wearlauncher.socket.command.gprs.post;

import com.sczn.wearlauncher.Config;
import com.sczn.wearlauncher.base.util.StringUtils;
import com.sczn.wearlauncher.socket.command.CmdCode;
import com.sczn.wearlauncher.socket.command.CommandHelper;
import com.sczn.wearlauncher.socket.command.CommandResultCallback;
import com.sczn.wearlauncher.socket.command.post.BaseCommand;

/**
 * Description:
 * Created by Bingo on 2019/5/13.
 */
public class TK extends BaseCommand {

    private String content;

    public TK(byte[] data, CommandResultCallback resultCallback) {
        super(CmdCode.TK, resultCallback);
        this.content = CmdCode.TK + ",";
        byte[] aa = CommandHelper.getInstance().stringToBytes(
                StringUtils.getJoinString(
                        CmdCode.START, CmdCode.CUSTOMER_CODE,
                        "*",
                        Config.IMEI,
                        "*",
                        CommandHelper.toHexString(content.length() + data.length),
                        "*",
                        content, CmdCode.END)
        );
        byte[] bytes = new byte[aa.length + data.length];
        bytes[bytes.length - 1] = aa[aa.length - 1];
        //合并两个数组
        System.arraycopy(aa, 0, bytes, 0, aa.length - 1);
        System.arraycopy(data, 0, bytes, aa.length - 1, data.length);
        setData(bytes);
    }

    @Override
    public String send() {
        return content;
    }
}
