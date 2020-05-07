package com.sczn.wearlauncher.base.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class StringUtils {

    /**
     * @param str the str
     * @return true, if is empty
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.equals("") || str.trim().equals("")
                || str.trim().equals("null"))
            return true;
        return false;
    }

    public static boolean isEmail(String strEmail) {
        String strPattern = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }

    public static boolean isPhone(String strPhone) {
        String strPattern = "^1[0-9]{10}$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strPhone);
        return m.matches();
    }

    public static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;
        String expression = "(\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$";

        CharSequence inputStr = phoneNumber;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * 字符串拼接,代替+这样的拼接方式
     *
     * @param values
     * @return
     */
    public static String getJoinString(Object... values) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object o : values) {
            stringBuilder.append(o);
        }
        return stringBuilder.toString();
    }

    /**
     * 这个方法是保证时间两位数据显示，如果为1点时，就为01
     * 如果等0，显示00
     *
     * @param c c
     * @return String 
     */
    public static String dataLong(int c) {
        if (c >= 10) {
            return String.valueOf(c);
        } else if (c == 0) {
            return "00";
        } else {
            return "0" + c;
        }
    }
}
