package com.sczn.wearlauncher.socket.command.post;

import com.sczn.wearlauncher.location.LocationCallBackHelper;
import com.sczn.wearlauncher.location.bean.LocationInfo;
import com.sczn.wearlauncher.socket.command.CommandResultCallback;
import com.sczn.wearlauncher.util.TimeUtil;

/**
 * Description:消息通知,本条协议需要上传位置信息
 * Created by Bingo on 2019/4/30.
 */
public class NotifyMsgJsonCmd extends BaseCommand {

    private int type;
    private String content;

    public NotifyMsgJsonCmd(int type, LocationInfo info, CommandResultCallback resultCallback) {
        super(resultCallback);
        this.type = type;
        /**
         * GPS定位
         */
        if (info.getType() == 2) {
            this.content = "\"locationType\": " + info.getType() + "," +
                    "\"gps\": [{\"lat\":\"" + info.getLat() + "\",\"lng\":\"" + info.getLng() + "\"," +
                    "\"accuracy\":" + info.getAccuracy() + ",\"timestamp\": " + TimeUtil.getCurrentTimeSec() + "}]";
        } else {
            this.content = "\"optUserId\":" + LocationCallBackHelper.optUserId + ",\"locationType\":" + info.getType() + "," +
                    "\"lbs\":{\"smac\":\"\",\"serverip\":\"\",\"cdma\":\"" + info.getCdma() + "\",\"imsi\":\"\",\"network\":\"\"," +
                    "\"bts\":\"" + info.getBts() + "\"," +
                    "\"nearbts\":\"" + info.getNearbts() + "\"," +
                    "\"mmac\":\"" + info.getMmac() + "\"," +
                    "\"macs\": \"" + info.getMacs() + "\"}," +
                    "\"timestamp\":" + TimeUtil.getCurrentTimeSec() + "";
        }
    }

    @Override
    public String send() {
        return "{\"type\":" + type + ",\"timestamp\":" + TimeUtil.getCurrentTimeSec() + ",\"data\":{" + content + "}}";
    }
}
