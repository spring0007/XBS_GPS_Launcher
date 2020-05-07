package com.sczn.wearlauncher.card.sport;

import android.content.Context;

import com.sczn.wearlauncher.Config;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.socket.WaterSocketManager;
import com.sczn.wearlauncher.socket.command.CommandResultCallback;
import com.sczn.wearlauncher.socket.command.post.UploadStepsCmd;
import com.sczn.wearlauncher.util.TimeUtil;
import com.toycloud.tcwal.TcWatchDevice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Description:
 * Created by Bingo on 2019/1/18.
 */
public class SportHelper {
    private final String TAG = "SportHelper";

    private static SportHelper helper;

    /**
     * @return
     */
    public static SportHelper getHelper() {
        if (null == helper) {
            synchronized (SportHelper.class) {
                if (null == helper) {
                    helper = new SportHelper();
                }
            }
        }
        return helper;
    }

    /**
     * 上传运动步数
     *
     * @param step
     * @param optUserId
     * @throws NumberFormatException
     */
    public void uploadStep(int step, String optUserId) throws NumberFormatException {
        //上传数据
        UploadStepsCmd stepsCmd = new UploadStepsCmd(step, TimeUtil.getCurrentTimeSec(), optUserId, new CommandResultCallback() {
            @Override
            public void onSuccess(String baseObtain) {
                MxyLog.d(TAG, "上传运动数据成功.");
            }

            @Override
            public void onFail() {
                MxyLog.d(TAG, "上传运动数据失败.");
            }
        });
        WaterSocketManager.getInstance().send(stepsCmd);
    }

    /**
     * 获取一周的运动步数
     *
     * @param mContext
     * @return
     */
    public List<Sport> getWeekStepData(Context mContext) {
        List<Sport> list = new ArrayList<>();
        /*String recentStepData = TcWatchDevice.getWeekStepData(mContext);
        try {
            if (recentStepData != null && !"".equals(recentStepData) && recentStepData.contains(":")) {
                String[] strArray = recentStepData.trim().split(":");
                for (String aStrArray : strArray) {
                    if (aStrArray != null && !aStrArray.isEmpty() && aStrArray.contains(",")) {
                        Sport sport = new Sport();
                        String[] dayArray = aStrArray.split(",");
                        if (dayArray.length == 8) {
                            sport.setNumber(Integer.parseInt(dayArray[0]));
                            sport.setYear(Integer.parseInt(dayArray[1]));
                            sport.setMonth(Integer.parseInt(dayArray[2]));
                            sport.setDay(Integer.parseInt(dayArray[3]));
                            sport.setHour(Integer.parseInt(dayArray[4]));
                            sport.setMinute(Integer.parseInt(dayArray[5]));
                            sport.setMinute(Integer.parseInt(dayArray[6]));
                            sport.setStep(Integer.parseInt(dayArray[7]));
                            sport.setTime(dayArray[1] + "-" + dayArray[2] + "-" + dayArray[3] + ":" + dayArray[4] + ":" + dayArray[5]);

                            MxyLog.d(TAG, "-->>" + sport.toString());

                            list.add(sport);
                        }
                    }
                }
                MxyLog.w(TAG, "共有" + list.size() + "条数据");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return list;
    }

    /**
     * 根据时间获取当前总步数
     *
     * @param mContext
     * @return
     */
    public int getCurrentStepData(Context mContext) {
        int step = 0;
        List<Sport> list = getWeekStepData(mContext);
        if (list != null && list.size() > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int year = calendar.get(Calendar.YEAR);
            int month = (calendar.get(Calendar.MONTH) + 1);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            // 累加同一天的步数
            for (Sport sport : list) {
                if (year == sport.getYear() && month == sport.getMonth() && day == sport.getDay()) {
                    step = step + sport.getStep();
                }
            }
        }
        Config.step = step;
        return step;
    }

}
