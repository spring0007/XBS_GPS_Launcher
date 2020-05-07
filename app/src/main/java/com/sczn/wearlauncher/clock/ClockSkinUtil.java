package com.sczn.wearlauncher.clock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.provider.Settings;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.app.MxyLog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 表盘皮肤
 */
public class ClockSkinUtil {
    private static final String TAG = ClockSkinUtil.class.getSimpleName();

    private static final String CLOCK_ROOT = "ClockSkin";
    private static final String CLOCK_XML_NAME = "clock_skin.xml";
    private static final String CLOCK_MODEL_NAME = "clock_skin_model.png";

    public static final int CLOCK_SKIN_STATE_UNREADY = 0;
    public static final int CLOCK_SKIN_STATE_READYING = 1;
    public static final int CLOCK_SKIN_STATE_READY = 2;

    public static void initAllClockIndex() {
        new ClockIndexLoader().execute();
    }

    /**
     * 首页表盘的皮肤选择
     *
     * @param context
     * @return
     */
    public static String[] getAllClockSkins(Context context) {
        try {
            return context.getAssets().list(CLOCK_ROOT);
        } catch (IOException e) {
            MxyLog.e(TAG, "getClockSkins error=" + e.toString());
            return null;
        }
    }

    /**
     * 首页表盘的皮肤选择
     *
     * @param context
     * @param position 根据index
     * @return
     */
    public static String getClockSkinByPosition(Context context, int position) {
        try {
            String[] files = context.getAssets().list(CLOCK_ROOT);
            if (files != null && files.length > position) {
                return files[position];
            }
        } catch (IOException e) {
            e.printStackTrace();
            MxyLog.e(TAG, "getClockSkins error=" + e.toString());
        }
        return null;
    }

    /**
     * @param context
     * @param skinName
     * @return
     */
    public static Drawable getClockSkinModelByName(Context context, String skinName) {
        InputStream in;
        Bitmap bitmap = null;
        try {
            in = context.getAssets().open(getClockSkinModelFile(skinName));
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (IOException e) {
            MxyLog.e(TAG, "getDrawable" + "--IOException=" + e.toString());
        }
        if (bitmap == null) {
            return context.getResources().getDrawable(R.drawable.alt_bg);
        }
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    public static String getClockSkinXmlFile(String skinName) {
        return CLOCK_ROOT + File.separator + skinName + File.separator + CLOCK_XML_NAME;
    }

    public static String getClockSkinDetail(String skinName, String pngName) {
        return CLOCK_ROOT + File.separator + skinName + File.separator + pngName;
    }

    private static String getClockSkinModelFile(String skinName) {
        return CLOCK_ROOT + File.separator + skinName + File.separator + CLOCK_MODEL_NAME;
    }

    private static class ClockIndexLoader extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            final String[] clocks = getAllClockSkins(LauncherApp.appContext);
            if (LauncherApp.appContext != null && clocks != null) {
                try {
                    final StringBuilder indexString = new StringBuilder();
                    for (String clock : clocks) {
                        final String[] index = clock.split("_");
                        if (index.length == 2) {
                            indexString.append(index[1]).append("#");
                        } else {
                            indexString.append(index[0]).append("#");
                        }
                    }
                    if (indexString.length() > 1) {
                        return indexString.substring(0, indexString.length() - 1);
                    }
                } catch (Exception e) {
                    cancel(true);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!isCancelled() && result != null && LauncherApp.appContext != null) {
                Settings.System.putString(LauncherApp.appContext.getContentResolver(),
                        "clock_index", result);
            }
        }
    }

    public class ClockSkinConst {
        public static final int ROTATE_CLOCKWISE = 1;
        public static final int ANTI_ROTATE_CLOCKWISE = 2;
        public static final int ROTATE_NONE = 0;
        public static final int ROTATE_HOUR = 1;
        public static final int ROTATE_MINUTE = 2;
        public static final int ROTATE_SECOND = 3;
        public static final int ROTATE_MONTH = 4;
        public static final int ROTATE_WEEK = 5;
        public static final int ROTATE_BATTERY = 6;
        public static final int ROTATE_DAYNIGHT = 7;
        public static final int ROTATE_HOUR_BG = 8;
        public static final int ROTATE_MINUTE_BG = 9;
        public static final int ROTATE_SECOND_BG = 10;

        public static final int ARRAY_YEARMONTHDAY = 1;
        public static final int ARRAY_MONTHDAY = 2;
        public static final int ARRAY_MONTH = 3;
        public static final int ARRAY_DAY = 4;
        public static final int ARRAY_WEEKDAY = 5;
        public static final int ARRAY_HOURMINUTE = 6;
        public static final int ARRAY_HOUR = 7;
        public static final int ARRAY_MINUTE = 8;
        public static final int ARRAY_SECOND = 9;
        public static final int ARRAY_WEATHER = 10;
        public static final int ARRAY_TEMPERATURE = 11;
        public static final int ARRAY_STEPS = 12;
        public static final int ARRAY_HEARTRATE = 13;
        public static final int ARRAY_BATTERY = 14;
        public static final int ARRAY_SPECIAL_SECOND = 15;
        public static final int ARRAY_YEAR = 16;
        public static final int ARRAY_BATTERY_WITH_CIRCLE = 17;
        public static final int ARRAY_STEPS_WITH_CIRCLE = 18;
        public static final int ARRAY_MOON_PHASE = 19;
        public static final int ARRAY_AM_PM = 20;
        public static final int ARRAY_FRAME_ANIMATION = 21;
        public static final int ARRAY_ROTATE_ANIMATION = 22;
        public static final int ARRAY_SNOW_ANIMATION = 23;
        public static final int ARRAY_BATTERY_WITH_CIRCLE_PIC = 24;
        public static final int ARRAY_TEXT_PEDOMETER = 97;
        public static final int ARRAY_TEXT_HEARTRATE = 98;
        public static final int ARRAY_CHARGING = 99;
        public static final int ARRAY_TEXT_MON_DAY_WEEK = 100;
        public static final String TAG_ARRAY_TYPE = "arraytype";
        public static final String TAG_CENTERX = "centerX";
        public static final String TAG_CENTERY = "centerY";
        public static final String TAG_COLOR = "color";
        public static final String TAG_COLOR_ARRAY = "colorarray";
        public static final String TAG_DIRECTION = "direction";
        public static final String TAG_DRAWABLE = "drawable";
        public static final String TAG_DRAWABLES = "drawables";
        public static final String TAG_IMAGE = "image";
        public static final String TAG_MUL_ROTATE = "mulrotate";
        public static final String TAG_NAME = "name";
        public static final String TAG_OFFSET_ANGLE = "angle";
        public static final String TAG_ROTATE = "rotate";
        public static final String TAG_START_ANGLE = "startAngle";
        public static final String TAG_TEXT_SIZE = "textsize";
        public static final String TAG_DRAWABLE_FILE_TYPE = "xml";
        public static final String TAG_DRAWABLE_TYPE = "png";
        public static final String TAG_ANIMATION_ITEMS = "animationItems";
        public static final String TAG_ANIMATION_ITEM = "animationItem";
        public static final String TAG_DURATION = "duration";
        public static final String TAG_COUNT = "count";
    }
}
