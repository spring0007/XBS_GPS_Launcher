package com.sczn.wearlauncher.socket.command.gprs.post;

import com.sczn.wearlauncher.Config;
import com.sczn.wearlauncher.base.util.StringUtils;
import com.sczn.wearlauncher.socket.command.CmdCode;
import com.sczn.wearlauncher.socket.command.CommandHelper;

/**
 * Description:
 * Created by Bingo on 2019/5/8.
 */
public class Base {

    /**
     * 组合协议
     *
     * @param content
     * @return
     */
    public static String compose(String content) {
        return StringUtils.getJoinString(
                CmdCode.START, CmdCode.CUSTOMER_CODE,
                "*",
                Config.IMEI,
                "*",
                CommandHelper.toHexString(content.length()),
                "*",
                content, CmdCode.END);
    }
}
