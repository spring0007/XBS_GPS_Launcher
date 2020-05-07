package com.sczn.wearlauncher.clock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Xml;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.sp.SPUtils;
import com.sczn.wearlauncher.card.geographic.ModelGeographic;
import com.sczn.wearlauncher.card.sport.StepInfoToday;
import com.sczn.wearlauncher.clock.ClockSkinUtil.ClockSkinConst;
import com.sczn.wearlauncher.util.TimeUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by wuzhiyi on 2016/1/28.
 */
public class ClockSkinParse extends ModelClockSkin {
    private final static String TAG = ClockSkinParse.class.getSimpleName();

    private final static boolean IS_EXSTORAGE = false;
    private static final String FILE_NAME = "clock_skin.xml";
    private static final String ROOT_PATH = "/system/ClockSkin/";

    private Drawable mDrawBattery;
    private Drawable mDrawBatteryGray;
    private ModelDrawableInfo myDrawable;
    private ArrayList<ModelDrawableInfo> myDrawables = null;
    private long startAnimationTime = -1;
    private long animationTimeCount = 0;
    private boolean mChanged;

    static {
        // myDrawable = null;
        //startAnimationTime = 0;
    }

    public ClockSkinParse() {
        this(SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    public ClockSkinParse(int clockWidth, int clockHeight) {
        this.mClockWidth = clockWidth;
        this.mClockHeight = clockHeight;

        myDrawable = null;
        startAnimationTime = 0;
    }


    private void loadDrawableArray(Context paramContext, ModelDrawableInfo paramDrawableInfo, String paramString1, String paramString2) {
        int eventType = -1;
        String itemName = null;
        ArrayList<Drawable> drawables = null;
        ArrayList<Integer> durations = null;
        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            InputStream inputStream = null;
            if (IS_EXSTORAGE) {
                inputStream = new FileInputStream(new File(ROOT_PATH + paramString1 + File.separator + paramString2));
                xmlPullParser.setInput(inputStream, "UTF-8");
            } else {
                inputStream = paramContext.getAssets().open(ClockSkinUtil.getClockSkinDetail(paramString1, paramString2));
                xmlPullParser.setInput(inputStream, "UTF-8");
            }
            eventType = xmlPullParser.getEventType();
            boolean isDone = false;
            while (!isDone) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        if (drawables == null) {
                            drawables = new ArrayList<Drawable>();
                        }
                        if (durations == null) {
                            durations = new ArrayList<Integer>();
                        }
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        isDone = true;
                        paramDrawableInfo.setDrawableArrays(drawables);
                        paramDrawableInfo.setDurationArrays(durations);
                        break;
                    case XmlPullParser.START_TAG:
                        itemName = xmlPullParser.getName();
                        if (ClockSkinConst.TAG_ANIMATION_ITEMS.equals(itemName)) {
                            startAnimationTime = 0;
                            animationTimeCount = 0;
                        } else if (ClockSkinConst.TAG_IMAGE.equals(itemName)) {
                            Bitmap bmp = null;

                            if (IS_EXSTORAGE) {
                                String ss = ROOT_PATH + paramString1 + "/" + xmlPullParser.nextText();
                                MxyLog.d("wuzhiyi", ss);
                                bmp = BitmapFactory.decodeFile(ss);
                            } else {
                                InputStream in = paramContext.getAssets().open(
                                        ClockSkinUtil.getClockSkinDetail(paramString1, xmlPullParser.nextText()));
                                bmp = BitmapFactory.decodeStream(in);

                            }

                            DisplayMetrics dm = paramContext.getResources().getDisplayMetrics();
                            bmp.setDensity(dm.densityDpi);
                            BitmapDrawable bitmapDrawable = new BitmapDrawable(paramContext.getResources(), bmp);
                            drawables.add(bitmapDrawable);
                        } else if (ClockSkinConst.TAG_NAME.equals(itemName)) {
                            Bitmap bmp = null;

                            if (IS_EXSTORAGE) {
                                String ss = ROOT_PATH + paramString1 + "/" + xmlPullParser.nextText();
                                MxyLog.d("wuzhiyi", ss);
                                bmp = BitmapFactory.decodeFile(ss);
                            } else {
                                InputStream in = paramContext.getAssets().open(
                                        ClockSkinUtil.getClockSkinDetail(paramString1, xmlPullParser.nextText()));
                                bmp = BitmapFactory.decodeStream(in);

                            }
                            DisplayMetrics dm = paramContext.getResources().getDisplayMetrics();
                            bmp.setDensity(dm.densityDpi);
                            BitmapDrawable bitmapDrawable = new BitmapDrawable(paramContext.getResources(), bmp);
                            drawables.add(bitmapDrawable);
                        } else if (ClockSkinConst.TAG_DURATION.equals(itemName)) {
                            int i = Integer.valueOf(xmlPullParser.nextText().toString()).intValue();
                            animationTimeCount += i;
                            durations.add(i);
                        }
                        break;
                }
                eventType = xmlPullParser.next();
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载assets下ClockSkin时钟的皮肤
     *
     * @param paramContext
     * @param paramString
     */
    private void loadClockSkinByPath(Context paramContext, String paramString) {
        MxyLog.d("paramString = ", paramString);
        XmlPullParser xmlPullParser;
        InputStream inputStream;
        try {
            if (IS_EXSTORAGE) {
                File file = new File(ROOT_PATH + paramString + "/" + FILE_NAME);
                inputStream = new FileInputStream(file);
            } else {
                inputStream = paramContext.getAssets().open(
                        ClockSkinUtil.getClockSkinXmlFile(paramString));

                //MxyLog.d(TAG, "loadClockSkinByPath--inputStream=" + inputStream);
            }
            xmlPullParser = Xml.newPullParser();
            xmlPullParser.setInput(inputStream, "UTF-8");

            int eventType = xmlPullParser.getEventType();
            boolean isDone = false;
            String localName = null;
            while (!isDone) {
                localName = null;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        if (myDrawables == null) {
                            myDrawables = new ArrayList();
                        } else {
                            myDrawables.clear();
                        }
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        isDone = true;
                        break;
                    case XmlPullParser.START_TAG: {
                        localName = xmlPullParser.getName();
                        if (ClockSkinConst.TAG_DRAWABLE.equals(localName)) {
                            myDrawable = new ModelDrawableInfo(mClockWidth, mClockHeight);

                        } else if (myDrawable != null) {
                            if (ClockSkinConst.TAG_NAME.equals(localName)) {
                                localName = xmlPullParser.nextText();
                                int index = localName.lastIndexOf(".");
                                if (index > 0) {
                                    if (ClockSkinConst.TAG_DRAWABLE_FILE_TYPE.equalsIgnoreCase(localName.substring(index + 1))) {
                                        loadDrawableArray(paramContext, myDrawable, paramString, localName);
                                    } else if (ClockSkinConst.TAG_DRAWABLE_TYPE.equalsIgnoreCase(localName.substring(index + 1))) {

                                        Bitmap bmp = null;
                                        if (IS_EXSTORAGE) {
                                            String ss = ROOT_PATH + paramString + "/" + localName;
                                            MxyLog.d("wuzhiyi", ss);
                                            bmp = BitmapFactory.decodeFile(ss);
                                        } else {
                                            InputStream in = paramContext.getAssets().open(
                                                    ClockSkinUtil.getClockSkinDetail(paramString, localName));
                                            //MxyLog.d(TAG, "loadClockSkinByPath--localName=" + localName + "--in=" + in);
                                            bmp = BitmapFactory.decodeStream(in);

                                        }
                                        DisplayMetrics dm = paramContext.getResources().getDisplayMetrics();
                                        try {
                                            bmp.setDensity(dm.densityDpi);
                                        } catch (RuntimeException e) {
                                            e.printStackTrace();
                                        }
                                        BitmapDrawable bitmapDrawable = new BitmapDrawable(paramContext.getResources(), bmp);
                                        myDrawable.setDrawable(bitmapDrawable);
                                    }
                                }
                            } else if (ClockSkinConst.TAG_CENTERX.equals(localName)) {
                                myDrawable.setCenterX(Integer.valueOf(xmlPullParser.nextText()));
                            } else if (ClockSkinConst.TAG_CENTERY.equals(localName)) {
                                myDrawable.setCenterY(Integer.valueOf(xmlPullParser.nextText()));
                            } else if (ClockSkinConst.TAG_ROTATE.equals(localName)) {
                                myDrawable.setRotate(Integer.valueOf(xmlPullParser.nextText()));
                            } else if (ClockSkinConst.TAG_MUL_ROTATE.equals(localName)) {
                                myDrawable.setMulRotate(Integer.valueOf(xmlPullParser.nextText()));
                            } else if (ClockSkinConst.TAG_OFFSET_ANGLE.equals(localName)) {
                                myDrawable.setAngle(Integer.valueOf(xmlPullParser.nextText()));
                            } else if (ClockSkinConst.TAG_ARRAY_TYPE.equals(localName)) {
                                myDrawable.setArrayType(Integer.valueOf(xmlPullParser.nextText()));
                            } else if (ClockSkinConst.TAG_COLOR.equals(localName)) {
                                myDrawable.setColor(Integer.valueOf(xmlPullParser.nextText()));
                            } else if (ClockSkinConst.TAG_START_ANGLE.equals(localName)) {
                                myDrawable.setStartAngle(Integer.valueOf(xmlPullParser.nextText()));
                            } else if (ClockSkinConst.TAG_DIRECTION.equals(localName)) {
                                myDrawable.setDirection(Integer.valueOf(xmlPullParser.nextText()));
                            } else if (ClockSkinConst.TAG_TEXT_SIZE.equals(localName)) {
                                myDrawable.setTextsize(Integer.valueOf(xmlPullParser.nextText()));
                            } else if (ClockSkinConst.TAG_COLOR_ARRAY.equals(localName)) {
                                myDrawable.setColorArray(xmlPullParser.nextText());
                            } else if (ClockSkinConst.TAG_COUNT.equals(localName)) {
                                startAnimationTime = 0;
                                int count = Integer.valueOf(xmlPullParser.nextText());
                                ArrayList<ModelSnowInfo> snowInfos = new ArrayList<ModelSnowInfo>();
                                Random random = new Random();
                                for (int i = 0; i < count; i++) {
                                    ModelSnowInfo snowInfo = new ModelSnowInfo();
                                    float scale = adjustResolutionFloat(random.nextFloat());
                                    if (scale < 0.1f) {
                                        scale = 0.1f;
                                    }
                                    snowInfo.setDrawable(zoomDrawable(myDrawable.getDrawable(), scale));
                                    snowInfo.setX(random.nextInt(mClockWidth));
                                    snowInfo.setY(random.nextInt(mClockHeight));
                                    snowInfo.setSpeed(random.nextFloat() / 2);
                                    snowInfo.setScale(random.nextFloat());
                                    snowInfos.add(snowInfo);
                                }
                                myDrawable.setSnowInfos(snowInfos);
                            } else if (ClockSkinConst.TAG_DURATION.equals(localName)) {
                                startAnimationTime = 0;
                                int i = Integer.valueOf(xmlPullParser.nextText());
                                myDrawable.setDuration(i);
                            }
                        }
                    }
                    break;
                    case XmlPullParser.END_TAG: {
                        localName = xmlPullParser.getName();
                        if (ClockSkinConst.TAG_DRAWABLE.equals(localName)) {
                            if (myDrawable.getArrayType() == ClockSkinConst.ARRAY_ROTATE_ANIMATION) {
                                myDrawable.setCurrentAngle(myDrawable.getStartAngle());
                                if (myDrawable.getDuration() != 0) {
                                    myDrawable.setRotateSpeed(myDrawable.getAngle() * 1.0f / myDrawable.getDuration());
                                }
                                myDrawable.setDirection(1);
                            }
                            if (myDrawable != null) {
                                myDrawables.add(myDrawable);
                                myDrawable = null;
                            }
                        }
                    }
                    break;
                }
                eventType = xmlPullParser.next();
            }
            inputStream.close();
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据不同的array type绘制不同的背景
     *
     * @param paramContext
     * @param paramCanvas
     * @param localDrawableInfo
     */
    private void drawClockArray(Context paramContext, Canvas paramCanvas, ModelDrawableInfo localDrawableInfo) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        float milliSecond = calendar.get(Calendar.MILLISECOND);
        boolean is24TimeFormat = isTime24Format(paramContext);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        switch (localDrawableInfo.getArrayType()) {
            case ClockSkinConst.ARRAY_YEARMONTHDAY:
                if ((localDrawableInfo.getDrawableArrays() != null) && (localDrawableInfo.getDrawableArrays().size() > 0)) {
                    drawDigitalYearMonthDay(paramCanvas, localDrawableInfo.getDrawableArrays(), calendar,
                            localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY(), true);
                }
                break;
            case ClockSkinConst.ARRAY_MONTHDAY:
                if ((localDrawableInfo.getDrawableArrays() != null) && (localDrawableInfo.getDrawableArrays().size() > 0)) {
                    drawDigitalMonthAndDay(paramCanvas, localDrawableInfo.getDrawableArrays().get(month / 10),
                            localDrawableInfo.getDrawableArrays().get(month % 10), localDrawableInfo.getDrawableArrays().get(10),
                            localDrawableInfo.getDrawableArrays().get(day / 10),
                            localDrawableInfo.getDrawableArrays().get(day % 10), localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY(), true);
                }
                break;

            case ClockSkinConst.ARRAY_MONTH:
                if ((localDrawableInfo.getDrawableArrays() != null) && localDrawableInfo.getDrawableArrays().size() > (month - 1)) {
                    drawDigitalOnePicture(paramCanvas, localDrawableInfo.getDrawableArrays().get(month - 1),
                            localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY(), true);
                }
                break;
            case ClockSkinConst.ARRAY_DAY:
                if ((localDrawableInfo.getDrawableArrays() != null) && localDrawableInfo.getDrawableArrays().size() > 0) {
                    drawDigitalTwoPicture(paramCanvas, localDrawableInfo.getDrawableArrays().get(day / 10),
                            localDrawableInfo.getDrawableArrays().get(day % 10),
                            localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY(), true);
                }
                break;
            case ClockSkinConst.ARRAY_WEEKDAY:
                if ((localDrawableInfo.getDrawableArrays() != null) && localDrawableInfo.getDrawableArrays().size() >= dayOfWeek - 1) {
                    drawClockQuietPicture(paramCanvas, localDrawableInfo.getDrawableArrays().get(dayOfWeek - 1),
                            localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY(), true);
                }
                break;
            case ClockSkinConst.ARRAY_HOURMINUTE:
                if ((localDrawableInfo.getDrawableArrays() != null) && (localDrawableInfo.getDrawableArrays().size() > 0)) {
                    Drawable amPm = null;
                    if (!is24TimeFormat && localDrawableInfo.getDrawableArrays().size() == 13) {
                        amPm = localDrawableInfo.getDrawableArrays().get(hour >= 12 ? 12 : 11);
                        hour = hour % 12;
                        if (hour == 0) {
                            hour = 12;
                        }
                    }
                    Drawable hour1 = localDrawableInfo.getDrawableArrays().get(hour / 10);
                    Drawable hour2 = localDrawableInfo.getDrawableArrays().get(hour % 10);
                    Drawable colon = localDrawableInfo.getDrawableArrays().get(10);
                    Drawable minute1 = localDrawableInfo.getDrawableArrays().get(minute / 10);
                    Drawable minute2 = localDrawableInfo.getDrawableArrays().get(minute % 10);
                    drawDigitalHourAndMinute(paramCanvas, hour1, hour2, colon, minute1, minute2, amPm,
                            localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY(), second, true);
                }
                break;
            case ClockSkinConst.ARRAY_HOUR:
                if ((localDrawableInfo.getDrawableArrays() != null) && (localDrawableInfo.getDrawableArrays().size() > 0)) {
                    drawDigitalTwoPicture(paramCanvas, localDrawableInfo.getDrawableArrays().get(hour / 10),
                            localDrawableInfo.getDrawableArrays().get(hour % 10), localDrawableInfo.getCenterX(),
                            localDrawableInfo.getCenterY(), true);
                }
                break;
            case ClockSkinConst.ARRAY_MINUTE: {
                if ((localDrawableInfo.getDrawableArrays() != null) && (localDrawableInfo.getDrawableArrays().size() > 0)) {
                    drawDigitalTwoPicture(paramCanvas, localDrawableInfo.getDrawableArrays().get(minute / 10),
                            localDrawableInfo.getDrawableArrays().get(minute % 10), localDrawableInfo.getCenterX(),
                            localDrawableInfo.getCenterY(), true);
                }
            }
            break;
            case ClockSkinConst.ARRAY_SECOND: {
                if ((localDrawableInfo.getDrawableArrays() != null) && (localDrawableInfo.getDrawableArrays().size() > 0)) {
                    drawDigitalTwoPicture(paramCanvas, localDrawableInfo.getDrawableArrays().get(second / 10),
                            localDrawableInfo.getDrawableArrays().get(second % 10), localDrawableInfo.getCenterX(),
                            localDrawableInfo.getCenterY(), true);
                }
            }
            break;
            case ClockSkinConst.ARRAY_WEATHER: {
                if ((localDrawableInfo.getDrawableArrays() != null) && (localDrawableInfo.getDrawableArrays().size() > 0)) {
                    int currentWeather = SPUtils.getIntParam(paramContext, WeatherConstant.TODAY_WEATHER, 0);
                    drawClockQuietPicture(paramCanvas, localDrawableInfo.getDrawableArrays().get(currentWeather), localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY(), true);
                }
            }
            break;
            case ClockSkinConst.ARRAY_TEMPERATURE: {
                if ((localDrawableInfo.getDrawableArrays() != null) && (localDrawableInfo.getDrawableArrays().size() > 0)) {
                    int temp = SPUtils.getIntParam(paramContext, WeatherConstant.TODAY_TEMP, 0);
                    Drawable minus = localDrawableInfo.getDrawableArrays().get(10);
                    Drawable temp1 = localDrawableInfo.getDrawableArrays().get(Math.abs(temp / 10));
                    Drawable temp2 = localDrawableInfo.getDrawableArrays().get(Math.abs(temp % 10));
                    Drawable tempUnit = localDrawableInfo.getDrawableArrays().get(11);
                    drawTemperature(paramCanvas, minus, temp1, temp2, tempUnit, localDrawableInfo.getCenterX(),
                            localDrawableInfo.getCenterY(), temp < 0 ? true : false, true);
                }
            }
            break;
            case ClockSkinConst.ARRAY_STEPS: {
                if ((localDrawableInfo.getDrawableArrays() != null) && (localDrawableInfo.getDrawableArrays().size() > 0)) {
                    int step = (int) SPUtils.getIntParam(paramContext, SportStepsConstant.CURRENT_DAY_STEP_COUNT, 0);
                    drawStepsPicture(paramCanvas, localDrawableInfo.getDrawableArrays(), localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY(), step, true);
                }
            }
            break;
            case ClockSkinConst.ARRAY_HEARTRATE: {
                if ((localDrawableInfo.getDrawableArrays() != null) && (localDrawableInfo.getDrawableArrays().size() > 0)) {
                    int heartRate = SPUtils.getIntParam(paramContext, "current_heart", 0);
                    drawHeartRatePicture(paramCanvas, localDrawableInfo.getDrawableArrays(), localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY(), heartRate, true);
                }
            }
            break;
            case ClockSkinConst.ARRAY_BATTERY: {
                if ((localDrawableInfo.getDrawableArrays() != null) && (localDrawableInfo.getDrawableArrays().size() > 0)) {
                    int batteryLevel = getBatteryLevel(paramContext);
                    drawBatteryPicture(paramCanvas, localDrawableInfo.getDrawableArrays(),
                            localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY(), batteryLevel, true);
                }
            }
            break;
            case ClockSkinConst.ARRAY_SPECIAL_SECOND:
                if (localDrawableInfo.getColorArray() != null) {
                    drawSpecialSecond(paramCanvas, localDrawableInfo.getColorArray(), minute, second);
                }
                break;
            case ClockSkinConst.ARRAY_YEAR:
                if ((localDrawableInfo.getDrawableArrays() != null) && (localDrawableInfo.getDrawableArrays().size() > 0)) {
                    drawDigitalYear(paramCanvas, localDrawableInfo.getDrawableArrays(), year, localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY(), true);
                }
                break;
            case ClockSkinConst.ARRAY_BATTERY_WITH_CIRCLE:
                if ((localDrawableInfo.getDrawableArrays() != null) && (localDrawableInfo.getDrawableArrays().size() > 0)
                        && localDrawableInfo.getColorArray() != null) {
                    int batteryLevel = getBatteryLevel(paramContext);
                    drawBatteryPictureWithCircle(paramCanvas, localDrawableInfo.getDrawableArrays(),
                            localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY(), batteryLevel,
                            localDrawableInfo.getColorArray(), true);
                }
                break;
            case ClockSkinConst.ARRAY_BATTERY_WITH_CIRCLE_PIC:
                if ((localDrawableInfo.getDrawableArrays() != null) && (localDrawableInfo.getDrawableArrays().size() > 0)
                        ) {
                    int batteryLevel = getBatteryLevel(paramContext);
                    drawBatteryPictureWithCirclePic(paramCanvas, localDrawableInfo.getDrawableArrays(),
                            localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY(), batteryLevel, true);
                }
                break;
            case ClockSkinConst.ARRAY_STEPS_WITH_CIRCLE:
                if ((localDrawableInfo.getDrawableArrays() != null) &&
                        (localDrawableInfo.getDrawableArrays().size() > 0) && (localDrawableInfo.getColorArray() != null)) {
                    int step = SPUtils.getIntParam(paramContext, SportStepsConstant.CURRENT_DAY_STEP_COUNT, 0);
                    drawStepsPictureWithCircle(paramContext, paramCanvas, localDrawableInfo.getDrawableArrays(), localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY(),
                            step, localDrawableInfo.getColorArray(), true);
                }
                break;
            case ClockSkinConst.ARRAY_MOON_PHASE:
                if ((localDrawableInfo.getDrawableArrays() != null) && (localDrawableInfo.getDrawableArrays().size() > 0)) {
                    drawClockQuietPicture(paramCanvas, localDrawableInfo.getDrawableArrays().get(0), localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY(), true);
                }
                break;
            case ClockSkinConst.ARRAY_AM_PM:
                if ((localDrawableInfo.getDrawableArrays() != null) && (localDrawableInfo.getDrawableArrays().size() > 0)) {
                    int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                    if (!is24TimeFormat) {
                        int index = (currentHour >= 12) ? 1 : 0;
                        drawClockQuietPicture(paramCanvas, localDrawableInfo.getDrawableArrays().get(index), localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY(), true);
                    }
                }
                break;
            case ClockSkinConst.ARRAY_FRAME_ANIMATION:
                if (startAnimationTime <= 0) {
                    startAnimationTime = calendar.getTimeInMillis();
                }
                if (localDrawableInfo.getDrawableArrays() != null && (localDrawableInfo.getDrawableArrays().size() > 0)) {
                    if (localDrawableInfo.getDurationArrays() != null && (localDrawableInfo.getDurationArrays().size() > 0)) {
                        if (animationTimeCount > 0) {
                            long diff = calendar.getTimeInMillis() - startAnimationTime;
                            diff = diff % animationTimeCount;
                            for (int i = 0; i < localDrawableInfo.getDurationArrays().size(); i++) {
                                if (diff < localDrawableInfo.getDurationArrays().get(i)) {
                                    drawClockQuietPicture(paramCanvas, localDrawableInfo.getDrawableArrays().get(i), localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY(), true);
                                    break;
                                }
                                diff -= localDrawableInfo.getDurationArrays().get(i);
                            }
                        }
                    }
                }
                break;
            case ClockSkinConst.ARRAY_ROTATE_ANIMATION:
                if (startAnimationTime <= 0) {
                    startAnimationTime = calendar.getTimeInMillis();
                }
                if (localDrawableInfo.getDirection() == ClockSkinConst.ROTATE_CLOCKWISE) {
                    localDrawableInfo.setCurrentAngle(localDrawableInfo.getCurrentAngle() + localDrawableInfo.getRotateSpeed());
                } else {
                    localDrawableInfo.setCurrentAngle(localDrawableInfo.getCurrentAngle() - localDrawableInfo.getRotateSpeed());
                }
                if ((localDrawableInfo.getCurrentAngle() >= localDrawableInfo.getStartAngle() + localDrawableInfo.getAngle()) ||
                        localDrawableInfo.getCurrentAngle() <= localDrawableInfo.getStartAngle()) {
                    if (localDrawableInfo.getDirection() == ClockSkinConst.ROTATE_CLOCKWISE) {
                        localDrawableInfo.setDirection(ClockSkinConst.ANTI_ROTATE_CLOCKWISE);
                    } else {
                        localDrawableInfo.setDirection(ClockSkinConst.ROTATE_CLOCKWISE);
                    }
                }
                if (localDrawableInfo.getDrawable() != null) {
                    MxyLog.d("wuzhiyi", "rotate=" + localDrawableInfo.getCurrentAngle());
                    drawClockRotatePicture(paramCanvas, localDrawableInfo.getDrawable(), localDrawableInfo.getCenterX(),
                            localDrawableInfo.getCenterY(), localDrawableInfo.getCurrentAngle(), true);

                }
                break;
            case ClockSkinConst.ARRAY_SNOW_ANIMATION:
                if (localDrawableInfo.getDrawable() != null && localDrawableInfo.getSnowInfos() != null &&
                        localDrawableInfo.getSnowInfos().size() > 0) {
                    if (startAnimationTime <= 0) {
                        startAnimationTime = calendar.getTimeInMillis();
                    }
                    for (ModelSnowInfo snowInfo : localDrawableInfo.getSnowInfos()) {
                        float y = snowInfo.getY() + snowInfo.getSpeed();
                        if (y > mClockHeight) {
                            Random random = new Random();
                            y = random.nextInt(mClockHeight / 2);
                        }
                        snowInfo.setY(y);
                        int diffX = Math.abs(snowInfo.getX() - mClockWidth / 2);
                        int diffY = Math.abs((int) snowInfo.getY() - mClockWidth / 2);
                        if (diffX * diffX + diffY * diffY <= (mClockHeight * mClockWidth / 4)) {
                            drawClockQuietPicture(paramCanvas, snowInfo.getDrawable(),
                                    snowInfo.getX(), (int) snowInfo.getY(), true);
                        }
                    }
                }
                break;
            case ClockSkinConst.ARRAY_TEXT_PEDOMETER: {
                int step = SPUtils.getIntParam(paramContext, SportStepsConstant.CURRENT_DAY_STEP_COUNT, 0);
                drawPedometer(paramContext, paramCanvas, localDrawableInfo.getStartAngle(),
                        localDrawableInfo.getDirection(), localDrawableInfo.getTextsize(), step);
            }
            break;
            case ClockSkinConst.ARRAY_CHARGING: {
                if (isChargingNow(paramContext)) {
                    int textColor = Color.WHITE;
                    Drawable chargingDrawable = mDrawBattery;
                    if (localDrawableInfo.getColor() == 1) {
                        chargingDrawable = mDrawBatteryGray;
                        textColor = Color.BLACK;
                    }
                    drawChargingInfo(paramContext, paramCanvas, chargingDrawable, localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY(), textColor, true);
                }
            }
            break;
            case ClockSkinConst.ARRAY_TEXT_MON_DAY_WEEK:
                drawMonthAndDay(paramCanvas, month, day, hour, minute, second, localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY());
                break;
        }
    }

    @Override
    public void drawClock(Canvas paramCanvas, Context paramContext) {
        if (myDrawables != null && myDrawables.size() > 0) {
            if (this.mChanged) {
                this.mChanged = false;
                mDrawBattery = paramContext.getResources().getDrawable(R.drawable.clock_battery_panel);
                mDrawBatteryGray = paramContext.getResources().getDrawable(R.drawable.clock_battery_panel_gray);
            }
            ModelDrawableInfo drawable = myDrawables.get(0);
            if (drawable != null && drawable.getDrawable() != null) {
                int width = drawable.getDrawable().getIntrinsicWidth();
                int height = drawable.getDrawable().getIntrinsicHeight();
                width = Math.min(width, height);
                if (width != getScaleWidth()) {
                    setScaleWidth(width);
                }
            }
            Calendar calendar = Calendar.getInstance();
            /**
             * 循环画时间的动画,时间的变化
             */
            for (ModelDrawableInfo drawableInfo : myDrawables) {
                switch (drawableInfo.getRotate()) {
                    case ClockSkinConst.ROTATE_NONE: {
                        if (drawableInfo.getArrayType() > 0) {
                            drawClockArray(paramContext, paramCanvas, drawableInfo);
                        } else {
                            if (drawableInfo.getDrawable() != null) {
                                drawClockQuietPicture(paramCanvas, drawableInfo.getDrawable(),
                                        drawableInfo.getCenterX(), drawableInfo.getCenterY(), true);
                            }
                        }
                    }
                    break;
                    case ClockSkinConst.ROTATE_HOUR: {
                        float hourAngle = calendar.get(Calendar.HOUR_OF_DAY) / 12.0f * 360.0f;
                        int minute = calendar.get(Calendar.MINUTE);
                        hourAngle += (minute * 30 / 60);
                        drawClockRotatePicture(paramCanvas, drawableInfo.getDrawable(), drawableInfo.getCenterX(),
                                drawableInfo.getCenterY(), hourAngle + drawableInfo.getAngle(), true);
                    }
                    break;
                    case ClockSkinConst.ROTATE_MINUTE: {
                        drawClockRotatePicture(paramCanvas, drawableInfo.getDrawable(), drawableInfo.getCenterX(),
                                drawableInfo.getCenterY(), calendar.get(Calendar.MINUTE) / 60.0f * 360.0f, true);
                    }
                    break;
                    case ClockSkinConst.ROTATE_SECOND: {
                        int second = calendar.get(Calendar.SECOND);
                        int milliSecond = calendar.get(Calendar.MILLISECOND);
                        float secondAngle = (second * 1000 + milliSecond) * 6.0f / 1000.0f;
                        if (drawableInfo.getMulRotate() > 0) {
                            secondAngle *= drawableInfo.getMulRotate();
                        } else if (drawableInfo.getMulRotate() < 0) {
                            secondAngle = secondAngle / (Math.abs(drawableInfo.getMulRotate()));
                        }
                        drawClockRotatePicture(paramCanvas, drawableInfo.getDrawable(), drawableInfo.getCenterX(),
                                drawableInfo.getCenterY(), secondAngle + drawableInfo.getAngle(), true);
                    }
                    break;
                    case ClockSkinConst.ROTATE_MONTH:
                        if (drawableInfo.getDrawable() != null) {
                            int maxDay = TimeUtil.getDaysByYearMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
                            float angle = (calendar.get(Calendar.MONTH) + 1) * 30.0F + calendar.get(Calendar.DAY_OF_MONTH) * 30.0F / maxDay;
                            drawClockRotatePicture(paramCanvas, drawableInfo.getDrawable(), drawableInfo.getCenterX(),
                                    drawableInfo.getCenterY(), angle + drawableInfo.getAngle(), true);
                        }
                        break;
                    case ClockSkinConst.ROTATE_WEEK:
                        if (drawableInfo.getDrawable() != null) {
                            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
                            float angle = (weekDay + calendar.get(Calendar.HOUR_OF_DAY) * 1.0F / 24) * 360.0F / 7;
                            drawClockRotatePicture(paramCanvas, drawableInfo.getDrawable(), drawableInfo.getCenterX(),
                                    drawableInfo.getCenterY(), angle + drawableInfo.getAngle(), true);
                        }
                        break;
                    case ClockSkinConst.ROTATE_BATTERY:
                        if (drawableInfo.getDrawable() != null) {
                            int batteryLevel = getBatteryLevel(paramContext);
                            int direction = drawableInfo.getDirection();
                            float angle = batteryLevel * 180.0F / 100;
                            drawClockRotatePicture(paramCanvas, drawableInfo.getDrawable(), drawableInfo.getCenterX(),
                                    drawableInfo.getCenterY(), angle * ((direction == 1) ? 1 : -1) + drawableInfo.getAngle(), true);
                        }
                        break;
                    case ClockSkinConst.ROTATE_DAYNIGHT: {
                        if (drawableInfo.getDrawable() != null) {
                            float angle = calendar.get(Calendar.HOUR_OF_DAY) * 15.0F + calendar.get(Calendar.MINUTE) / 60.0F * 15.0F;
                            drawClockRotatePicture(paramCanvas, drawableInfo.getDrawable(), drawableInfo.getCenterX(),
                                    drawableInfo.getCenterY(), angle + drawableInfo.getAngle(), true);
                        }
                    }
                    break;
                    case ClockSkinConst.ROTATE_HOUR_BG: {
                        float hourAngle = calendar.get(Calendar.HOUR_OF_DAY) / 24.0f * 360.0f;
                        int minute = calendar.get(Calendar.MINUTE);
                        hourAngle += (minute * 30 / 60);
                        drawClockRotatePicture(paramCanvas, drawableInfo.getDrawable(), drawableInfo.getCenterX(),
                                drawableInfo.getCenterY(), hourAngle + drawableInfo.getAngle(), true);
                    }
                    break;
                }
            }
        }
    }

    /**
     * drawable转换成bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);//建立对应bitmap
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);//把drawable内容画到画布
        return bitmap;
    }

    private Drawable zoomDrawable(Drawable drawable, float scale) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);
        return new BitmapDrawable(newbmp);
    }

    @Override
    public ModelClockSkin getChildSkinByPosition(Context paramContext, int paramInt) {
        this.mChanged = true;
        if (IS_EXSTORAGE) {
            File file = new File(ROOT_PATH);
            if (file.isDirectory()) {
                loadClockSkinByPath(paramContext, file.list()[paramInt]);
            }
            return this;
        } else {
            final String name = ClockSkinUtil.getClockSkinByPosition(paramContext, paramInt);
            if (name != null) {
                loadClockSkinByPath(paramContext, name);
            } else {
                return null;
            }
            return this;
        }
    }

    @Override
    public List<ModelClockSkin> getSkins(Context paramContext) {
        ArrayList<ModelClockSkin> clockSkins = new ArrayList();
        File file = new File(ROOT_PATH);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            int i = 0;
            while (i < files.length) {
                File str = files[i];
                String fileName = str.getAbsolutePath() + "/" + "clock_skin_model.png";
                ClockSkinParse clockSkinParse = new ClockSkinParse();
                clockSkinParse.mPreview = fileName;
                clockSkinParse.position = i;
                clockSkins.add(clockSkinParse);
                i += 1;
            }
        }
        return clockSkins;
    }

    @Override
    public void recycleDrawable() {
        if ((myDrawables == null) || (myDrawables.size() <= 0))
            return;
        for (ModelDrawableInfo drawableInfo : myDrawables) {
            if (drawableInfo.getDrawable() != null) {
                drawableInfo.getDrawable().setCallback(null);
            }
            if (drawableInfo.getDrawableArrays() != null) {
                for (Drawable drawable : drawableInfo.getDrawableArrays()) {
                    drawable.setCallback(null);
                }
            }
        }
    }

    public class WeatherConstant {
        public static final String TODAY_TEMP = ModelGeographic.SHARE_KEY_TEMP;
        public static final String TODAY_WEATHER = ModelGeographic.SHARE_KEY_WEATHER;
    }

    public class SportStepsConstant {
        public static final String CURRENT_DAY_STEP_COUNT = StepInfoToday.SHARE_KEY_STEP_TODAY;
    }
}
