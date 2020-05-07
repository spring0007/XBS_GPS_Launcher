package com.sczn.wearlauncher.location.util;

import com.google.gson.reflect.TypeToken;
import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.location.LocationCallBackHelper;
import com.sczn.wearlauncher.location.bean.LocationInfo;
import com.sczn.wearlauncher.location.impl.OnLocationChangeListener;
import com.sczn.wearlauncher.socket.WaterSocketManager;
import com.sczn.wearlauncher.socket.command.CommandResultCallback;
import com.sczn.wearlauncher.socket.command.gprs.post.AL;
import com.sczn.wearlauncher.socket.command.obtain.BaseObtain;
import com.sczn.wearlauncher.socket.command.obtain.FenceObtain;
import com.sczn.wearlauncher.socket.command.post.FenceAlarmCmd;
import com.sczn.wearlauncher.sp.SPKey;
import com.sczn.wearlauncher.sp.SPUtils;
import com.sczn.wearlauncher.util.GsonHelper;
import com.sczn.wearlauncher.util.TimeUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Created by Bingo on 2019/3/7.
 */
public class FenceHelper {

    private static final String TAG = "FenceHelper";
    public static List<String> fenceRemindTimeList;

    /**
     * 解析电子围栏信息
     *
     * @param json
     */
    public static void logic(String json) {
        BaseObtain<List<FenceObtain>> fences = GsonHelper.getInstance().getGson().fromJson(json, new TypeToken<BaseObtain<List<FenceObtain>>>() {
        }.getType());
        if (fences == null || fences.getData() == null || fences.getData().size() <= 0) {
            return;
        }

        List<FenceObtain> fence = fences.getData();
        if (fence == null || fence.size() <= 0) {
            return;
        }
        MxyLog.d(TAG, "共有" + fence.size() + "个电子围栏");
        LitePal.deleteAll(FenceObtain.class);
        LitePal.saveAll(fence);

        float lng = (float) SPUtils.getParam(SPKey.LOCATION_LNG, 0f);
        float lat = (float) SPUtils.getParam(SPKey.LOCATION_LAT, 0f);
        if (lng != 0 && lat != 0) {
            checkFence(lng, lat);
        }
    }

    /**
     * 检查围栏是否需要提醒
     *
     * @param lng
     * @param lat
     */
    public static void checkFence(double lng, double lat) {
        MxyLog.i(TAG, "checkFence:lng=" + lng + ",lat=" + lat);
        if (fenceRemindTimeList == null) {
            fenceRemindTimeList = new ArrayList<>();
        }
        fenceRemindTimeList.clear();
        List<FenceObtain> fences = LitePal.findAll(FenceObtain.class);
        if (fences != null && fences.size() > 0) {
            double fLng;
            double fLat;
            double fRange;
            double distance;
            for (FenceObtain f : fences) {
                fLng = f.getFenceLng();
                fLat = f.getFenceLat();
                fRange = f.getFenceRange();
                distance = MapUtils.lineDistanceGD(fLng, fLat, lng, lat);
                if (f.getRemindWay() == 1) {
                    if (distance <= fRange) {
                        MxyLog.d(TAG, "提示,当前已进入" + f.getName() + "围栏.");
                        if (f.getIsIsEnable() && isRemindDate(f.getRemindDays())) {
                            fenceAlarm(f.getName(), f.getFenceLng(), f.getFenceLat());
                        }
                    }
                } else if (f.getRemindWay() == 2) {
                    if (distance > fRange) {
                        MxyLog.d(TAG, "当前的位置,超出了" + f.getName() + "围栏.");
                        if (f.getIsIsEnable() && isRemindDate(f.getRemindDays())) {
                            fenceAlarm(f.getName(), f.getFenceLng(), f.getFenceLat());
                        }
                    } else {
                        MxyLog.d(TAG, "正常范围.");
                    }
                } else if (f.getRemindWay() == 3) {
                    MxyLog.d(TAG, "在哪个时间段,没有进入该区域." + f.getRemindTime());
                    if (distance > fRange) {
                        if (f.getIsIsEnable() && isRemindDate(f.getRemindDays())) {
                            //需要进行提醒
                            fenceRemindTimeList.add(f.getRemindTime());
                            fenceAlarm(f.getName(), f.getFenceLng(), f.getFenceLat());
                        }
                    } else {
                        MxyLog.d(TAG, "进入该正常范围区域.");
                    }
                }
            }
        } else {
            MxyLog.d(TAG, "暂时没有任务围栏信息.");
        }
    }

    /**
     * 上传电子围栏警告
     *
     * @param fenceName
     * @param lng
     * @param lat
     */
    private static void fenceAlarm(String fenceName, double lng, double lat) {
        FenceAlarmCmd alarmCmd = new FenceAlarmCmd(fenceName, lng, lat, new CommandResultCallback() {
            @Override
            public void onSuccess(String baseObtain) {
                MxyLog.d(TAG, "上传电子围栏警告onSuccess");
            }

            @Override
            public void onFail() {
                MxyLog.d(TAG, "上传电子围栏警告onFail");
            }
        });
        WaterSocketManager.getInstance().send(alarmCmd);
    }

    /**
     * 检查日期是否需要发送围栏推送
     * 检查提醒的周期是否包含需要提醒周期
     *
     * @param remindDays
     * @return
     */
    private static boolean isRemindDate(String remindDays) {
        if (remindDays == null || remindDays.isEmpty()) {
            return true;//为空默认不需要检查日期
        }
        int week = TimeUtil.getWeek();//当前为周几
        if (week == 2 && remindDays.contains("1")) {
            return true;
        }
        if (week == 3 && remindDays.contains("2")) {
            return true;
        }
        if (week == 4 && remindDays.contains("3")) {
            return true;
        }
        if (week == 5 && remindDays.contains("4")) {
            return true;
        }
        if (week == 6 && remindDays.contains("5")) {
            return true;
        }
        if (week == 7 && remindDays.contains("6")) {
            return true;
        }
        if (week == 1 && remindDays.contains("7")) {
            return true;
        }
        return false;
    }

    /**
     * 围栏
     * <p>
     * 00000002---出围栏状态
     * 00000004---进围栏状态
     * 00040000---出围栏报警
     * 00080000---进围栏报警
     *
     * @param status
     */
    public static void fenceAlarm(final String status) {
        LocationCallBackHelper.getInstance().startBS(LauncherApp.getAppContext(), new OnLocationChangeListener<LocationInfo>() {
            @Override
            public void success(LocationInfo info) {
                AL al = new AL(info, status, new CommandResultCallback() {
                    @Override
                    public void onSuccess(String baseObtain) {
                        MxyLog.d(TAG, "上传围栏状态.onSuccess");
                    }

                    @Override
                    public void onFail() {
                        MxyLog.d(TAG, "上传围栏状态.onFail");
                    }
                });
                WaterSocketManager.getInstance().send(al);
            }

            @Override
            public void fail() {
                MxyLog.d(TAG, "上传围栏状态.获取位置信息失败.");
            }
        });
    }
}
