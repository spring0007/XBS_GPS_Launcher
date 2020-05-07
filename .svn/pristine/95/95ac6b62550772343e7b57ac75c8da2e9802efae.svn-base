package com.sczn.wearlauncher.setting.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

import com.sczn.wearlauncher.app.MxyLog;


public class BrightnessHelper {
    private final Context mContext;

    private static final int LEVEL_1 = 20;
    private static final int LEVEL_2 = 160;
    private static final int LEVEL_3 = 255;

    public BrightnessHelper(Context context) {
        this.mContext = context.getApplicationContext();
    }

    /**
     * 设置当前页面屏幕亮度
     *
     * @param brightness
     */
    public void setWindowBrightness(Activity activity, int brightness) {
        MxyLog.d("brightness", "brightness = " + brightness);
        int p;
        if (brightness == 1) {
            p = BrightnessHelper.LEVEL_1;
        } else if (brightness == 2) {
            p = BrightnessHelper.LEVEL_2;
        } else {
            p = BrightnessHelper.LEVEL_3;
        }
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = p / 255.0f;
        window.setAttributes(lp);
    }

    /**
     * 系统设置屏幕亮度
     *
     * @param activity
     * @param brightness
     */
    public static void saveBrightness(Activity activity, int brightness) {
        int p;
        if (brightness == 1) {
            p = BrightnessHelper.LEVEL_1;
        } else if (brightness == 2) {
            p = BrightnessHelper.LEVEL_2;
        } else {
            p = BrightnessHelper.LEVEL_3;
        }
        Uri uri = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
        ContentResolver contentResolver = activity.getContentResolver();
        Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, p);
        activity.getContentResolver().notifyChange(uri, null);
    }

    public int getScreenBrightnessLevel() {
        final int screenBrightness = getScreenBrightness();
        if (screenBrightness > 0 && screenBrightness <= LEVEL_1) {
            return 1;
        } else if (screenBrightness > LEVEL_1 && screenBrightness <= LEVEL_2) {
            return 2;
        } else if (screenBrightness > LEVEL_2 && screenBrightness <= LEVEL_3) {
            return 3;
        }
        return LEVEL_1;
    }

    private int getScreenBrightness() {
        try {
            return Settings.System.getInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
