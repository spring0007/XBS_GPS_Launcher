package com.sczn.wearlauncher.ruisocket;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.sczn.wearlauncher.app.MxyLog;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    private static int PowerState = 0;

    public static boolean isPowerAlarm(int powerScale) {
        int powerle = 0;
        if (powerScale > 20) {
            powerle = 2;
        } else if (powerScale > 10 && powerScale < 20) {
            powerle = 1;
        }
        if (powerle < PowerState) {
            return true;
        }
        PowerState = powerle;
        return false;
    }

    public static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;
        /**
         * valid phone number format;
         */
        String expression1 = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$";
        /**
         * valid phone number format;
         */
        String expression2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{4})[-0 ]?(\\d{4})$";
        CharSequence inputStr = phoneNumber;
        Pattern pattern1 = Pattern.compile(expression1);
        Matcher matcher1 = pattern1.matcher(inputStr);

        Pattern pattern2 = Pattern.compile(expression2);
        Matcher matcher2 = pattern2.matcher(inputStr);
        if (matcher1.matches() || matcher2.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * 字符串转换unicode
     */
    public static String string2Unicode(String string) {
        StringBuilder unicode = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            // 取出每一个字符
            char c = string.charAt(i);
            String hexB = Integer.toHexString(c).toUpperCase();
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            // 转换为unicode
            unicode.append("\\u").append(hexB);
        }
        return unicode.toString();
    }

    /**
     * 字符串转换unicode
     */
    public static String string2UnicodeU(String string) {
        StringBuilder unicode = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            // 取出每一个字符
            char c = string.charAt(i);
            String hexB = Integer.toHexString(c).toUpperCase();
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            // 转换为unicode
            unicode.append(hexB);
        }
        StringBuilder unicode1 = new StringBuilder(set64(unicode.toString()));
        for (int i = 0; i < 16; i++) {
            String e = unicode1.substring(4 * i, 4 * (i + 1));
            if (!e.equals("0000")) {
                e = e.substring(2, 4) + e.substring(0, 2);
                unicode1.append(e);
            }
        }
        return unicode1.toString();
    }

    /**
     * unicode 转字符串
     */
    public static String unicode2String(String unicode) {
        StringBuilder string = new StringBuilder();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);
            // 追加成string
            string.append((char) data);
        }
        return string.toString();
    }

    /**
     * unicode 转字符串
     */
    public static String unicode2String2(String unicode1) {
        StringBuilder unicode = new StringBuilder();
        for (int i = 0; i < (unicode1.length() / 4); i++) {
            String e = unicode1.substring(4 * i, 4 * (i + 1));
            if (!e.equals("0000")) {
                e = "\\u" + e.substring(2, 4) + e.substring(0, 2);
                unicode.append(e);
            }
        }
        StringBuilder string = new StringBuilder();
        String[] hex = unicode.toString().split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);
            // 追加成string
            string.append((char) data);
        }
        return string.toString();
    } //计算字符串字节长度

    public static int getWordCount(String s) {
        int length = 0;
        for (int i = 0; i < s.length(); i++) {
            int ascii = Character.codePointAt(s, i);
            if (ascii >= 0 && ascii <= 255)
                length++;
            else
                length += 2;
        }
        return length;

    }//设置4位数长度字符串

    public static String setFour(String a) {
        if (TextUtils.isEmpty(a)) {
            return "0000";
        }
        if (a.length() < 4) {
            a = "0" + a;
            if (a.length() == 4) {
                return a;
            } else {
                a = setFour(a);
            }
        } else if (a.length() > 4) {
            a = a.substring(a.length() - 4, a.length());

        }

        return a;
    }//设置4位数长度字符串

    public static String setEight(String a) {
        if (a.length() < 8) {
            a = "0" + a;
            if (a.length() == 8) {
                return a;
            } else {
                a = setEight(a);
            }
        } else if (a.length() > 8) {
            a = a.substring(a.length() - 8, a.length());

        }

        return a;
    }

    //设置2位数长度字符串
    public static String setTwo(String a) {
        if (TextUtils.isEmpty(a)) {
            return "00";
        }
        if (a.length() < 2) {
            a = "0" + a;
            if (a.length() == 2) {
                return a;
            } else {
                a = setTwo(a);
            }
        } else if (a.length() > 2) {
            a = a.substring(a.length() - 2, a.length());

        }

        return a;
    }

    public static String set64(String a) {
        if (a.length() < 64) {
            a = a + "0";
            if (a.length() == 64) {
                return a;
            } else {
                a = set64(a);
            }
        } else if (a.length() > 64) {
            a = a.substring(a.length() - 64, a.length());

        }

        return a;
    } //设置6位数长度字符串

    public static String setSix(String a) {
        if (a.length() < 6) {
            a = "0" + a;
            if (a.length() == 6) {
                return a;
            } else {
                a = setSix(a);
            }
        } else if (a.length() > 6) {
            a = a.substring(a.length() - 6, a.length());

        }

        return a;
    }

    public static String setThree(String a) {
        if (a.length() < 3) {
            a = "0" + a;
            if (a.length() == 3) {
                return a;
            } else {
                a = setThree(a);
            }
        } else if (a.length() > 3) {
            a = a.substring(a.length() - 3, a.length());

        }

        return a;
    }

    public static String getTel(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String tel = telephonyManager.getLine1Number();
        if (tel != null) {
            return tel;
        } else {
            return "00000000000";
        }

    }

    /**
     * 获取手机IMEI号
     */
    @SuppressLint("HardwareIds")
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            return null;
        }
        return telephonyManager.getDeviceId();
    }

    /**
     * 获取手机IMSI号
     */
    @SuppressLint("HardwareIds")
    public static String getIMSI(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephonyMgr == null) {
            return null;
        }
        return mTelephonyMgr.getSubscriberId();
    }

    /**
     * 获取mac地址
     */
    public static String getMacAddress() {
        String str = "";
        String macSerial = "";
        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
            if ("".equals(macSerial)) {
                return loadFileAsString("/sys/class/net/eth0/address").toUpperCase().substring(0, 17);
            }
        } catch (Exception ex) {
            MxyLog.e("----->", "getMacAddress:" + ex.toString());
        }
        return macSerial.toUpperCase();
    }

    private static String loadFileAsString(String fileName) throws Exception {
        FileReader reader = new FileReader(fileName);
        String text = loadReaderAsString(reader);
        reader.close();
        return text;
    }

    private static String loadReaderAsString(Reader reader) throws Exception {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[4096];
        int readLength = reader.read(buffer);
        while (readLength >= 0) {
            builder.append(buffer, 0, readLength);
            readLength = reader.read(buffer);
        }
        return builder.toString();
    }

    public static String timeToOx() {
        Calendar cal = Calendar.getInstance();
        String year = Integer.toHexString(cal.get(Calendar.YEAR) - 2000);// 获取年份
        String month = Integer.toHexString(cal.get(Calendar.MONTH) + 1);// 获取月份
        String day = Integer.toHexString(cal.get(Calendar.DATE));// 获取日
        String hour = Integer.toHexString(cal.get(Calendar.HOUR_OF_DAY));// 小时
        String minute = Integer.toHexString(cal.get(Calendar.MINUTE));// 分
        String second = Integer.toHexString(cal.get(Calendar.SECOND));// 秒
        System.out.println(year + month + day + hour + minute + second);

        if (year.length() == 1) {
            year = "0" + year;
        }
        if (month.length() == 1) {
            month = "0" + month;
        }
        if (day.length() == 1) {
            day = "0" + day;
        }
        if (hour.length() == 1) {
            hour = "0" + hour;
        }
        if (minute.length() == 1) {
            minute = "0" + minute;
        }
        if (second.length() == 1) {
            second = "0" + second;
        }

        return year + month + day + hour + minute + second;

    }

    /**
     * 将16进制字符串转换为byte[]
     *
     * @param str
     * @return
     */
    public static byte[] toBytes(String str) {
        if (str == null || str.trim().equals("")) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
    }

    public static String hexString(byte[] b) {
        String sMsg = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;

            }
            sMsg += hex;
        }
        return sMsg.toUpperCase();
    }

    /**
     * 字符串转换为Ascii
     *
     * @param value
     * @return
     */
    public static String stringToAscii(String value) {
        StringBuffer sbu = new StringBuffer();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i != chars.length - 1) {
                sbu.append((int) chars[i]);
            } else {
                sbu.append((int) chars[i]);
            }
        }
        return sbu.toString();
    }
}
