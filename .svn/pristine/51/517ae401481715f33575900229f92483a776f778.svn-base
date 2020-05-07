package com.sczn.wearlauncher.location.util;

/**
 * Created by zdl on 2019/3/5
 */
public class MapUtils {
    /**
     * 高德地图经纬度算直线距离
     *
     * @param lngA
     * @param latA
     * @param lngB
     * @param latB
     * @return
     */
    public static double lineDistanceGD(double lngA, double latA, double lngB, double latB) {
        double d1 = 0.01745329251994329D;
        double d2 = lngA;
        double d3 = latA;
        double d4 = lngB;
        double d5 = latB;
        d2 *= d1;
        d3 *= d1;
        d4 *= d1;
        d5 *= d1;
        double d6 = Math.sin(d2);
        double d7 = Math.sin(d3);
        double d8 = Math.cos(d2);
        double d9 = Math.cos(d3);
        double d10 = Math.sin(d4);
        double d11 = Math.sin(d5);
        double d12 = Math.cos(d4);
        double d13 = Math.cos(d5);
        double[] arrayOfDouble1 = new double[3];
        double[] arrayOfDouble2 = new double[3];
        arrayOfDouble1[0] = (d9 * d8);
        arrayOfDouble1[1] = (d9 * d6);
        arrayOfDouble1[2] = d7;
        arrayOfDouble2[0] = (d13 * d12);
        arrayOfDouble2[1] = (d13 * d10);
        arrayOfDouble2[2] = d11;
        double d14 = Math.sqrt((arrayOfDouble1[0] - arrayOfDouble2[0]) * (arrayOfDouble1[0] - arrayOfDouble2[0])
                + (arrayOfDouble1[1] - arrayOfDouble2[1]) * (arrayOfDouble1[1] - arrayOfDouble2[1])
                + (arrayOfDouble1[2] - arrayOfDouble2[2]) * (arrayOfDouble1[2] - arrayOfDouble2[2]));

        return (Math.asin(d14 / 2.0D) * 12742001.579854401D);

    }

    /**
     * 计算两个坐标点距离
     *
     * @param lngA
     * @param latA
     * @param lngB
     * @param latB
     * @return 单位：米
     */
    public static double lineDistance(double lngA, double latA, double lngB, double latB) {
        double earthR = 6371000;
        double x = Math.cos(latA * Math.PI / 180.) * Math.cos(latB * Math.PI / 180.)
                * Math.cos((lngA - lngB) * Math.PI / 180);
        double y = Math.sin(latA * Math.PI / 180.) * Math.sin(latB * Math.PI / 180.);
        double s = x + y;
        if (s > 1)
            s = 1;
        if (s < -1)
            s = -1;
        double alpha = Math.acos(s);
        return alpha * earthR;
    }
}
