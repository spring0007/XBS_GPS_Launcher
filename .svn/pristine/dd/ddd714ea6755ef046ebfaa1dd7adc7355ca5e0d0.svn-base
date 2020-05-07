package com.sczn.wearlauncher.socket.command.post;

import com.google.gson.Gson;
import com.sczn.wearlauncher.socket.command.CmdCode;
import com.sczn.wearlauncher.socket.command.CommandResultCallback;
import com.sczn.wearlauncher.socket.command.bean.Step;
import com.sczn.wearlauncher.util.TimeUtil;

/**
 * Description:
 * Created by Bingo on 2019/2/25.
 */
public class UploadStepsCmd extends BaseCommand {

    private long stepNum;
    private long timestamp;
    private String optUserId;


    /**
     * @param stepNum
     * @param timestamp
     * @param optUserId
     * @param resultCallback
     */
    public UploadStepsCmd(long stepNum, long timestamp, String optUserId, CommandResultCallback resultCallback) {
        super(resultCallback);
        this.stepNum = stepNum;
        this.timestamp = timestamp;
        this.optUserId = optUserId;
    }

    @Override
    public String send() {
        Step.DataBean dataBean = new Step.DataBean();
        dataBean.setStepNumber(stepNum);
        dataBean.setTimestamp(timestamp);
        dataBean.setOptUserId(optUserId);

        Step step = new Step();
        step.setType(CmdCode.UPLOADSTEPS);
        step.setTimestamp(TimeUtil.getCurrentTimeSec());
        step.setData(dataBean);
        return new Gson().toJson(step);
    }
}
