package com.sczn.wearlauncher.util;

import android.annotation.SuppressLint;
import android.content.Context;

import com.sczn.wearlauncher.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Description:时间工具
 * Created by Bingo on 2019/1/7.
 */
public class TimeUtil {


    /**
     * 获取日期
     *
     * @return 190507
     */
    public static String getDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd", Locale.getDefault());
        return simpleDateFormat.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 获取时间
     *
     * @return 152428
     */
    public static String getTimes() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HHmmss", Locale.getDefault());
        return simpleDateFormat.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 获取当前的时间
     *
     * @return 小时:分钟
     */
    public static String getTimeHour() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return simpleDateFormat.format(new Date(System.currentTimeMillis()));
    }

    /**
     * @return
     */
    public static String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return simpleDateFormat.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 设置语音文件的名字
     *
     * @return
     */
    public static String getTimeToVoiceName() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss", Locale.getDefault());
        return simpleDateFormat.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 获取当天日期
     *
     * @return
     */
    public static String getTodayDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(date);
    }

    /**
     * String 转化Calendar
     *
     * @param days
     * @param time
     * @return
     */
    public static Calendar stringToCalendar(String days, String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        Date date;
        try {
            date = sdf.parse(days + " " + time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param mNowTime   当前时间
     * @param mStartTime 开始时间
     * @param mEndTime   结束时间
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static boolean isEffectiveDate(String mNowTime, String mStartTime, String mEndTime) {
        String format = "HH:mm";
        try {
            Date nowTime = new SimpleDateFormat(format).parse(mNowTime);
            Date startTime = new SimpleDateFormat(format).parse(mStartTime);
            Date endTime = new SimpleDateFormat(format).parse(mEndTime);
            if (nowTime.getTime() == startTime.getTime()
                    || nowTime.getTime() == endTime.getTime()) {
                return true;
            }
            Calendar date = Calendar.getInstance();
            date.setTime(nowTime);

            Calendar begin = Calendar.getInstance();
            begin.setTime(startTime);

            Calendar end = Calendar.getInstance();
            end.setTime(endTime);

            if (date.after(begin) && date.before(end)) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据年月获取对应的月的天数
     *
     * @param year
     * @param month
     * @return
     */
    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        return a.get(Calendar.DATE);
    }

    /**
     * 获取当前时间,精确秒
     *
     * @return
     */
    public static long getCurrentTimeSec() {
        return System.currentTimeMillis() / 1000;
    }


    /**
     * 将时间戳转为时间字符串
     * <p>格式为用户自定义</p>
     *
     * @param milliseconds 毫秒时间戳
     * @param format       时间格式
     * @return 时间字符串
     */
    public static String milliseconds2String(long milliseconds, SimpleDateFormat format) {
        return format.format(new Date(milliseconds));
    }

    /**
     * 获取周几
     * 原数据:1-2-3-4-5-6-7  代表周日----周六
     *
     * @return
     */
    public static int getWeek() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * @param mContext
     * @return
     */
    public static String getWeekString(Context mContext) {
        Calendar calendar = Calendar.getInstance();
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                return mContext.getResources().getString(R.string.weekday_sun);
            case 2:
                return mContext.getResources().getString(R.string.weekday_mon);
            case 3:
                return mContext.getResources().getString(R.string.weekday_tue);
            case 4:
                return mContext.getResources().getString(R.string.weekday_wed);
            case 5:
                return mContext.getResources().getString(R.string.weekday_thu);
            case 6:
                return mContext.getResources().getString(R.string.weekday_fri);
            case 7:
                return mContext.getResources().getString(R.string.weekday_sat);
            default:
                return "";
        }
    }

    /**
     * 获取当前总分钟
     *
     * @return
     */
    public static int getCurrentTotalMin() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        return hour * 60 + min;
    }
}
