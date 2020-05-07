package com.sczn.wearlauncher.task.util;

import com.sczn.wearlauncher.alert.InClassModelDialog;
import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.task.bean.TaskInfo;
import com.sczn.wearlauncher.util.TimeUtil;

import org.litepal.LitePal;

import java.util.List;

/**
 * Description:
 * Created by Bingo on 2019/3/4.
 */
public class TaskInfoParse {

    private static final String TAG = "TaskInfoParse";

    /**
     * 上课禁用
     *
     * @param data
     */
    public static void logicGprs(String data) {
        if (data.contains(",")) {
            String[] d = data.split(",");
            if (d.length >= 3) {
                LitePal.deleteAll(TaskInfo.class);
                String t1 = d[0];
                String t2 = d[1];
                String t3 = d[2];
                if (t1.contains("-")) {
                    TaskInfo info = new TaskInfo();
                    info.setId(1);
                    info.setStartTime(t1.split("-")[0]);
                    info.setEndTime(t1.split("-")[1]);
                    info.saveOrUpdate("id = ?", "1");
                }
                if (t2.contains("-")) {
                    TaskInfo info = new TaskInfo();
                    info.setId(2);
                    info.setStartTime(t2.split("-")[0]);
                    info.setEndTime(t2.split("-")[1]);
                    info.saveOrUpdate("id = ?", "2");
                }
                if (t3.contains("-")) {
                    TaskInfo info = new TaskInfo();
                    info.setId(3);
                    info.setStartTime(t3.split("-")[0]);
                    info.setEndTime(t3.split("-")[1]);
                    info.saveOrUpdate("id = ?", "3");
                }
            }
        }
        checkIsClassModel(1);
    }

    /**
     * 检查上课禁用模式
     *
     * @param isClass
     */
    public static void checkIsClassModel(int isClass) {
        if (isClass == 1) {
            String mHour = TimeUtil.getTimeHour();
            MxyLog.d(TAG, "上课禁用模式." + mHour);
            List<TaskInfo> taskInfoList = LitePal.findAll(TaskInfo.class);
            if (taskInfoList != null && taskInfoList.size() > 0) {
                boolean isShow = false;
                String mClass = "";
                String mStartTime = "";
                String mEndTime = "";
                for (int i = 0; i < taskInfoList.size(); i++) {
                    TaskInfo info = taskInfoList.get(i);
                    if (info.getStartTime() != null && info.getEndTime() != null) {
                        if (TimeUtil.isEffectiveDate(mHour, info.getStartTime(), info.getEndTime())) {
                            mClass = info.getTask();
                            mStartTime = info.getStartTime();
                            mEndTime = info.getEndTime();
                            isShow = true;
                            break;
                        }
                    }
                }
                if (isShow) {
                    InClassModelDialog.getInstance().showMsgDialog(LauncherApp.getAppContext(), mClass, mStartTime, mEndTime);
                    MxyLog.w(TAG, "执行上课禁用模式");
                } else {
                    MxyLog.d(TAG, "解除上课禁用模式");
                    InClassModelDialog.getInstance().dismissMsgDialog();
                }
            }
        } else {
            MxyLog.d(TAG, "解除上课禁用模式");
            InClassModelDialog.getInstance().dismissMsgDialog();
        }
    }
}
