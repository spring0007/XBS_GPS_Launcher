package cn.com.waterworld.alarmclocklib.util;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.com.waterworld.alarmclocklib.R;

/**
 * Created by wangfeng on 2018/6/11.
 */

public class DateUtil {
    public static SimpleDateFormat HHmm = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat DEFAULT_SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public static String getHHmmStringDateFromMilli(long milliseconds) {
        if (milliseconds == 0) {
            return "";
        }
        String string = "";
        try {
            Date date = new Date(milliseconds);
            string = HHmm.format(date);
        } catch (Exception e) {
        }
        return string;
    }

    public static String getStringDateFromMilli(long milliseconds) {
        if (milliseconds == 0) {
            return "";
        }
        String string = "";
        try {
            Date date = new Date(milliseconds);
            string = DEFAULT_SDF.format(date);
        } catch (Exception e) {
        }
        return string;
    }

    /**
     * 获取指定日期是星期几
     * 参数为null时表示获取当前日期是星期几
     *
     * @param date
     * @return
     */
    public static String getWeekOfDate(Context context, Date date) {
        String[] weekOfDays = {context.getString(R.string.str_week7),
                context.getString(R.string.str_week1), context.getString(R.string.str_week2),
                context.getString(R.string.str_week3), context.getString(R.string.str_week4),
                context.getString(R.string.str_week5), context.getString(R.string.str_week6)};
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekOfDays[w];
    }

    /**
     * 获取星期
     *
     * @param time Date类型时间
     * @return 星期
     */
    public static String getWeek(Date time) {
        return new SimpleDateFormat("EEEE", Locale.getDefault()).format(time);
    }

    /**
     * 获取星期
     * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @return 星期
     */
    public static String getWeek(String time) {
        return new SimpleDateFormat("EEEE", Locale.getDefault()).format(string2Date(time));
    }

    /**
     * 将时间字符串转为Date类型
     * <p>格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @return Date类型
     */
    public static Date string2Date(String time) {
        return string2Date(time, DEFAULT_SDF);
    }

    /**
     * 将时间字符串转为Date类型
     * <p>格式为用户自定义</p>
     *
     * @param time   时间字符串
     * @param format 时间格式
     * @return Date类型
     */
    public static Date string2Date(String time, SimpleDateFormat format) {
        return new Date(string2Milliseconds(time, format));
    }

    /**
     * 将时间字符串转为时间戳
     * <p>格式为用户自定义</p>
     *
     * @param time   时间字符串
     * @param format 时间格式
     * @return 毫秒时间戳
     */
    public static long string2Milliseconds(String time, SimpleDateFormat format) {
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取星期
     * <p>注意：周日的Index才是1，周六为7</p>
     *
     * @param time Date类型时间
     * @return 1...7
     */
    public static int getWeekIndex(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        return cal.get(Calendar.DAY_OF_WEEK);
    }
}
