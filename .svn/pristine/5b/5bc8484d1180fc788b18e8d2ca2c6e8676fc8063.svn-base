package com.sczn.wearlauncher.sp;

import android.content.Context;
import android.content.SharedPreferences;

import com.sczn.wearlauncher.app.LauncherApp;

/**
 * Created by mxy on 2016/12/26.
 */
public class SPUtils {

    /**
     * 保存在手机里面的文件
     */
    private static final String FILE_NAME = "shared_preferences_date";

    /**
     * 保存数据的方法，我们要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param object
     */
    public static void setParam(String key, Object object) {
        SharedPreferences sp = LauncherApp.getAppContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (object == null) {
            editor.remove(key);
        } else {
            String type = object.getClass().getSimpleName();
            if ("String".equals(type)) {
                editor.putString(key, (String) object);
            } else if ("Integer".equals(type)) {
                editor.putInt(key, (Integer) object);
            } else if ("Boolean".equals(type)) {
                editor.putBoolean(key, (Boolean) object);
            } else if ("Float".equals(type)) {
                editor.putFloat(key, (Float) object);
            } else if ("Long".equals(type)) {
                editor.putLong(key, (Long) object);
            }
        }
        editor.apply();
    }

    /**
     * 保存数据的方法，我们要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void setParam(Context context, String key, Object object) {
        if (context != null) {
            SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            if (object == null) {
                editor.remove(key);
            } else {
                String type = object.getClass().getSimpleName();
                if ("String".equals(type)) {
                    editor.putString(key, (String) object);
                } else if ("Integer".equals(type)) {
                    editor.putInt(key, (Integer) object);
                } else if ("Boolean".equals(type)) {
                    editor.putBoolean(key, (Boolean) object);
                } else if ("Float".equals(type)) {
                    editor.putFloat(key, (Float) object);
                } else if ("Long".equals(type)) {
                    editor.putLong(key, (Long) object);
                }
            }
            editor.apply();
        }
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取
     *
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(String key, Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = LauncherApp.getAppContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        if ("String".equals(type)) {
            return sp.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return sp.getLong(key, (Long) defaultObject);
        }
        return null;
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(Context context, String key, Object defaultObject) {
        if (context != null) {
            String type = defaultObject.getClass().getSimpleName();
            SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

            if ("String".equals(type)) {
                return sp.getString(key, (String) defaultObject);
            } else if ("Integer".equals(type)) {
                return sp.getInt(key, (Integer) defaultObject);
            } else if ("Boolean".equals(type)) {
                return sp.getBoolean(key, (Boolean) defaultObject);
            } else if ("Float".equals(type)) {
                return sp.getFloat(key, (Float) defaultObject);
            } else if ("Long".equals(type)) {
                return sp.getLong(key, (Long) defaultObject);
            }
        }
        return null;
    }

    /**
     * 获取int类型的数据
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getIntParam(Context context, String key, int defaultValue) {
        if (context != null) {
            SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            return sp.getInt(key, defaultValue);
        }
        return defaultValue;
    }
}
