package com.sczn.wearlauncher.socket.command.gprs.post;

import com.sczn.wearlauncher.Config;
import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.base.util.StringUtils;
import com.sczn.wearlauncher.card.sport.SportHelper;
import com.sczn.wearlauncher.location.bean.LocationInfo;
import com.sczn.wearlauncher.socket.command.CmdCode;
import com.sczn.wearlauncher.socket.command.CommandResultCallback;
import com.sczn.wearlauncher.socket.command.post.BaseCommand;
import com.sczn.wearlauncher.util.TimeUtil;

/**
 * Description:位置数据上报
 * UD           ,命令号
 * 180916       ,日期
 * 025723       ,时间
 * A            ,gps定位有效
 * 22.570733,   ,纬度
 * N            ,纬度表示
 * 113.8626083, ,经度
 * E            ,经度表示
 * 0.00         ,速度
 * 249.5        ,方向
 * 0.0          ,海拔
 * 6            ,卫星个数
 * 100          ,gsm 信号强度
 * 60           ,电量
 * 0            ,计步数
 * 0            ,翻转次数
 * 00000010,终端状态，数据为16进制，解析成二进制为 0000 0000 0000 0000 0000 0000 0001 0000 前面4个字节表示状态，后面4个字节表示报警，数据中第4位为1，对照文档最后的数据解析，表示手表静止状态。详细可参看文档最后部分。
 * 7            ,基站个数
 * 255,460,1,9529,21809,158,9529,63555,133,9529,63554,129,9529,21405,126,9529,21242,124, 9529,21151,120,9529,63556,119,基站信息
 * 0            ,WiFi个数
 * 40.7         ,定位精度，单位为米
 * <p>
 * <p>
 * <p>
 * 说明:终端按照设定间隔上报位置和状态信息,不需要平台回复.
 * Created by Bingo on 2019/5/7.
 */
public class UD extends BaseCommand {

    private String content;

    /**
     * 封装上传的定位数据格式
     *
     * @param info
     * @return
     */
    public static String ud(LocationInfo info) {
        return StringUtils.getJoinString(TimeUtil.getDate(), ",",
                TimeUtil.getTimes(), ",",
                info.getType() == 2 ? "A" : "V", ",",
                info.getLat(), ",",
                "N", ",",
                info.getLng(), ",",
                "E", ",",
                info.getSpeed(), ",",
                info.getBearing(), ",",
                info.getAltitude(), ",",
                info.getGpsCount(), ",",
                Math.abs(Config.gmsSignal), ",",
                Config.battery, ",",
                SportHelper.getHelper().getCurrentStepData(LauncherApp.getAppContext()), ",",
                0, ",",
                Config.deviceState, ",",
                info.getBtsCount(), ",",
                info.getBts(), ",",
                info.getWifiCount(), ",",
                info.getMacs() == null ? "" : info.getMacs(), ",",
                info.getAccuracy()
        );
    }

    /**
     * 封装上传的定位数据格式,需要传入设备的通知状态
     *
     * @param info
     * @param status
     * @return
     */
    public static String ud(LocationInfo info, String status) {
        return StringUtils.getJoinString(TimeUtil.getDate(), ",",
                TimeUtil.getTimes(), ",",
                info.getType() == 2 ? "A" : "V", ",",
                info.getLat(), ",",
                "N", ",",
                info.getLng(), ",",
                "E", ",",
                info.getSpeed(), ",",
                info.getBearing(), ",",
                info.getAltitude(), ",",
                info.getGpsCount(), ",",
                Math.abs(Config.gmsSignal), ",",
                Config.battery, ",",
                SportHelper.getHelper().getCurrentStepData(LauncherApp.getAppContext()), ",",
                0, ",",
                status, ",",
                info.getBtsCount(), ",",
                info.getBts(), ",",
                info.getWifiCount(), ",",
                info.getMacs() == null ? "" : info.getMacs(), ",",
                info.getAccuracy()
        );
    }

    public UD(LocationInfo info, CommandResultCallback resultCallback) {
        super(resultCallback, false);
        this.content = CmdCode.UD + "," + ud(info);
    }

    @Override
    public String send() {
        return Base.compose(content);
    }
}
