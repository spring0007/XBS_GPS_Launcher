package cn.com.waterworld.alarmclocklib.util;

import android.util.Log;

/**
 * @author Bingo
 * <p>
 * LogUtil Log工具类
 */
public class LogUtil {

    private static final String TAG = "alarm_clock";

    private static final int VERBOSE = 0;
    private static final int DEBUG = 1;
    private static final int INFO = 2;
    private static final int WARN = 3;
    private static final int ERROR = 4;
    private static final int NOTHING = 5;

    // 上线的时候将level指定成NOTHING
    private static int level = VERBOSE;

    /**
     *
     * @param tag
     * @param msg
     */
    public static void v(String tag, String msg) {
        if (level <= VERBOSE) {
            Log.v(tag, msg);
        }
    }

    /**
     *
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if (level <= DEBUG) {
            Log.d(tag, msg);
        }
    }

    /**
     *
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        if (level <= INFO) {
            Log.i(tag, msg);
        }
    }

    /**
     *
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg) {
        if (level <= WARN) {
            Log.w(tag, msg);
        }
    }

    /**
     *
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (level <= ERROR) {
            Log.e(tag, msg);
        }
    }

    /**
     *
     * @param msg
     */
    //---------------------------------------log传入不需要tag--------------------------------------//
    public static void v(String msg) {
        if (level <= VERBOSE) {
            Log.v(TAG, msg);
        }
    }

    /**
     *
     * @param msg
     */
    public static void d(String msg) {
        if (level <= DEBUG) {
            Log.d(TAG, msg);
        }
    }

    /**
     *
     * @param msg
     */
    public static void i(String msg) {
        if (level <= INFO) {
            Log.i(TAG, msg);
        }
    }

    /**
     *
     * @param msg
     */
    public static void w(String msg) {
        if (level <= WARN) {
            Log.w(TAG, msg);
        }
    }

    /**
     *
     * @param msg
     */
    public static void e(String msg) {
        if (level <= ERROR) {
            Log.e(TAG, msg);
        }
    }
}
