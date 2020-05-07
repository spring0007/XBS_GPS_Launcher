package com.sczn.wearlauncher.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.sczn.wearlauncher.MainActivity;

import java.util.List;

/**
 * Activity检查类
 */
public class ActivityUtils {

    /**
     * 回到显示当前应用的主界面(MainActivity)
     */
    public static void gotoHome(Context mContext, String extraKey, String value) {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        i.putExtra(extraKey, true);
        i.putExtra(MainActivity.ARG_IS_VALUE, value);
        mContext.startActivity(i);
    }

    /**
     * 判断某activity是否处于栈顶
     *
     * @param cls
     * @param context
     * @return true在栈顶 false不在栈顶
     */
    public static boolean isActivityTop(Class cls, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        if (manager.getRunningTasks(1) == null) {
            return false;
        }
        if (manager.getRunningTasks(1).get(0) == null) {
            return false;
        }
        if (manager.getRunningTasks(1).get(0).topActivity == null) {
            return false;
        }
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(cls.getName());
    }

    /**
     * 获得栈中最顶层的Activity
     *
     * @param context
     * @return
     */
    public static String getTopActivity(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager == null) {
            return null;
        }
        List<ActivityManager.RunningTaskInfo> runningTaskInfo = manager.getRunningTasks(1);
        if (runningTaskInfo != null) {
            return (runningTaskInfo.get(0).topActivity).toString();
        } else {
            return null;
        }
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context   Context
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        List<ActivityManager.RunningTaskInfo> list = manager.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            return className.equals(cpn.getClassName());
        }
        return false;
    }
}
