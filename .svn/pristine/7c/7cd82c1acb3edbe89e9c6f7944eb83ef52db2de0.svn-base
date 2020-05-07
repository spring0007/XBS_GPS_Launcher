package cn.com.waterworld.steplib;

import android.content.Context;

import java.lang.ref.WeakReference;

import cn.com.waterworld.steplib.step.utils.DbUtils;

/**
 * Description:
 * Created by Bingo on 2019/4/9.
 */
public class StepApp {

    private static String DB_NAME = "wtwd_wear_step.db";

    private static WeakReference<Context> weakReference;

    public static void init(Context context) {
        weakReference = new WeakReference<>(context);
        DbUtils.createDb(weakReference.get(), DB_NAME);
        DbUtils.getLiteOrm().setDebugged(false);
    }

    public static Context getApplication() {
        return weakReference.get();
    }
}
