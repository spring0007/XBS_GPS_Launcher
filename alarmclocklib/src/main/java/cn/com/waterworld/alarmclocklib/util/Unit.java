package cn.com.waterworld.alarmclocklib.util;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by k.liang on 2018/4/25 14:24
 */
public class Unit {

    /**
     * 16进制转2进制
     */
    public static String toBinaryString2(String str) {
        int s = Integer.parseInt(str, 16);
        String str2 = Integer.toBinaryString(s);
        if (str2.length() < 8) {
            int length = 8 - str2.length();
            for (int i = 0; i < length; i++) {
                str2 = "0" + str2;
            }
        }
        return str2;
    }

    /**
     * 2进制转16进制
     */
    public static String toHexString16(String str) {
        int xx = Integer.parseInt(str, 2);
        String str2 = Integer.toHexString(xx);
        return str2;
    }

    /**
     * 以时、分、秒，再加上随机的150范围内的数字，组合成ID，其值不超过int的大小；
     */
    public static String getAlarmID() {
        Random random = new Random();
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);
        return random.nextInt(150) + "" + hour + "" + min + "" + sec;
    }
}
