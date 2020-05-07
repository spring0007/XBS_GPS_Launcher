package cn.com.waterworld.alarmclocklib.app;

import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * Created by wangfeng on 2018/4/11.
 */
public class AlarmClockApp {

    private static WeakReference<Context> weakReference;

    public static void init(Context context) {
        weakReference = new WeakReference<>(context);
    }

    public static Context getApplication() {
        return weakReference.get();
    }
}
