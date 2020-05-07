package com.sczn.wearlauncher.socket.command;

/**
 * Description:
 * Created by Bingo on 2019/1/16.
 */
public class CommandHelper {

    private static CommandHelper helper;

    /**
     * @return
     */
    public static CommandHelper getInstance() {
        if (null == helper) {
            synchronized (CommandHelper.class) {
                if (null == helper) {
                    helper = new CommandHelper();
                }
            }
        }
        return helper;
    }


    /**
     * 解析数据
     *
     * @param byteArray
     * @return
     */
    public String hexString(byte[] byteArray) {
        String str = "";
        try {
            if (byteArray == null) {
                return null;
            }
            str = new String(byteArray, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 解析发送数据
     *
     * @param str
     * @return
     */
    public byte[] stringToBytes(String str) {
        byte[] byteArray = null;
        try {
            if (str == null) {
                return null;
            }
            byteArray = str.getBytes("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteArray;
    }

    public static String hexUnicode2String(String src) {
        return hexString2String(src, "Unicode", "UTF-8");
    }

    private static String hexString2String(String src, String oldchartype, String chartype) {
        byte[] bts = hexString2Bytes(src);
        try {
            if (oldchartype.equals(chartype))
                return new String(bts, oldchartype);
            else
                return new String(new String(bts, oldchartype).getBytes(), chartype);
        } catch (Exception e) {
            return "";
        }
    }

    protected static byte[] hexString2Bytes(String src) {
        int l = src.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            ret[i] = Integer.valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();
        }
        return ret;
    }

    /**
     * int转16进制,不足4位数,前面补0
     *
     * @param var 1
     * @return 0001
     */
    public static String toHexString(int var) {
        String hex = Integer.toHexString(var).toUpperCase();
        if (hex.length() > 0 && hex.length() <= 4) {
            if (hex.length() == 1) {
                hex = "000" + hex;
            } else if (hex.length() == 2) {
                hex = "00" + hex;
            } else if (hex.length() == 3) {
                hex = "0" + hex;
            }
        }
        return hex;
    }

    /**
     * 16进制转10进制
     *
     * @param hex 000A
     * @return 10
     */
    public static int toInt(String hex) {
        return Integer.parseInt(hex, 16);
    }


    /**
     * Convert byte[] to hex string.
     * <p>
     * 将byte[] 数组转换为16进制字符串
     * <p>
     * 这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
     *
     * @param src byte[] data
     * @return hex string
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    /**
     * Convert hex string to byte[]
     * <p>
     * 16进制字符串转换为byte[]数组
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
