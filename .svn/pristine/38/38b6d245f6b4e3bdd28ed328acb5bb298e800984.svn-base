package com.sczn.wearlauncher.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.location.bean.BaseStationInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:获取基站信息
 * <p>
 * 获取基站信息
 * 返回值MCC + MNC
 * # MCC，Mobile Country Code，移动国家代码（中国的为460）；
 * # MNC，Mobile Network Code，移动网络号码（中国移动为0，中国联通为1，中国电信为2）；
 * # LAC，Location Area Code，位置区域码;
 * # CID，Cell Identity，基站编号；
 * # BSSS，Base station signal strength，基站信号强度。
 * <p>
 * Created by Bingo on 2019/1/15.
 */
public class BaseStationHelper {
    private static final String TAG = "BaseStationHelper";

    /**
     * 获取当前连接基站信息
     *
     * @param mContext
     */
    public static BaseStationInfo getConnectedCellInfo(Context mContext) {
        TelephonyManager mTelephonyManager = (TelephonyManager) mContext.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephonyManager == null) {
            return null;
        }
        String operator = mTelephonyManager.getNetworkOperator();
        MxyLog.i(TAG, "获取的基站信息是" + operator);
        if (operator == null || operator.length() < 5) {
            MxyLog.i(TAG, "获取基站信息有问题,可能是手机没插sim卡");
            return null;
        }
        int mcc = Integer.parseInt(operator.substring(0, 3));
        int mnc = Integer.parseInt(operator.substring(3));
        int lac;
        int cellId;
        CellLocation cellLocation = mTelephonyManager.getCellLocation();
        if (cellLocation == null) {
            MxyLog.i(TAG, "手机没插sim卡吧");
            return null;
        }
        MxyLog.i(TAG, "通信运营商: " + getCarrier(operator));
        if (mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {// 中国移动和中国联通获取LAC,CID的方式
            MxyLog.i(TAG, "当前连接的是gsm基站");
            GsmCellLocation location = (GsmCellLocation) cellLocation;
            lac = location.getLac();
            cellId = location.getCid();
            BaseStationInfo baseStationInfo = new BaseStationInfo();
            baseStationInfo.setMcc(mcc);
            baseStationInfo.setMnc(mnc);
            baseStationInfo.setLac(lac);
            baseStationInfo.setCid(cellId);
            baseStationInfo.setBaseType(0);
            MxyLog.e(TAG, "基站信息mcc=" + mcc + ",mnc=" + mnc + ",lac=" + lac + ",cellId=" + cellId);
            MxyLog.i(TAG, "--------------------------------------------------------");
            return baseStationInfo;
        } else if (mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {// 中国电信获取LAC,CID的方式
            MxyLog.i(TAG, "当前连接是cdma基站");
            CdmaCellLocation cdma = (CdmaCellLocation) mTelephonyManager.getCellLocation();
            BaseStationInfo baseStationInfo = new BaseStationInfo();
            baseStationInfo.setBaseType(1);
            baseStationInfo.setSid(cdma.getSystemId());
            baseStationInfo.setNid(cdma.getNetworkId());
            baseStationInfo.setBid(cdma.getBaseStationId());
            MxyLog.e(TAG, "基站信息sid=" + cdma.getSystemId() + ",nid=" + cdma.getNetworkId() + ",bid=" + cdma.getBaseStationId());
            MxyLog.i(TAG, "--------------------------------------------------------");
            return baseStationInfo;
        } else {
            MxyLog.i(TAG, "---------------------现在不知道是什么基站-------------------------");
            return null;
        }
    }

    /**
     * 获取附近的基站信息
     *
     * @param mContext
     */
    @SuppressLint("MissingPermission")
    public static List<BaseStationInfo> getCellInfo(Context mContext) {
        List<BaseStationInfo> list = new ArrayList<>();
        TelephonyManager mTelephonyManager = (TelephonyManager) mContext.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephonyManager == null) {
            return list;
        }
        // 获取邻区基站信息
        List<CellInfo> infos = mTelephonyManager.getAllCellInfo();
        if (infos != null) {
            if (infos.size() == 0) {
                MxyLog.i(TAG, "附近没有其他基站");
                list.size();
                return list;
            }
            MxyLog.i(TAG, "附近发现" + infos.size() + "个基站");
            for (CellInfo i : infos) { // 根据邻区总数进行循环
                MxyLog.d(TAG, "附近基站信息是" + i.toString());//这里如果出现很多cid
                if (i instanceof CellInfoGsm) {//这里的塔分为好几种类型
                    CellIdentityGsm cellIdentityGsm = ((CellInfoGsm) i).getCellIdentity();//从这个类里面可以取出好多有用的东西
                    if (cellIdentityGsm == null) continue;
                    BaseStationInfo baseStationInfo = new BaseStationInfo();
                    baseStationInfo.setBaseType(0);
                    baseStationInfo.setMcc(cellIdentityGsm.getMcc());
                    baseStationInfo.setMnc(cellIdentityGsm.getMnc());
                    baseStationInfo.setLac(cellIdentityGsm.getLac());
                    baseStationInfo.setCid(cellIdentityGsm.getCid());
                    list.add(baseStationInfo);
                    // MxyLog.i(TAG, "附近发现gsm基站Mcc = " + cellIdentityGsm.getMcc());
                    // MxyLog.i(TAG, "附近发现gsm基站Mnc = " + cellIdentityGsm.getMnc());
                    // MxyLog.i(TAG, "附近发现gsm基站Lac = " + cellIdentityGsm.getLac());
                    // MxyLog.i(TAG, "附近发现gsm基站Cid = " + cellIdentityGsm.getCid());
                } else if (i instanceof CellInfoCdma) {
                    CellIdentityCdma cellIdentityCdma = ((CellInfoCdma) i).getCellIdentity();
                    if (cellIdentityCdma == null) continue;
                    BaseStationInfo baseStationInfo = new BaseStationInfo();
                    baseStationInfo.setBaseType(1);
                    baseStationInfo.setSid(cellIdentityCdma.getSystemId());
                    baseStationInfo.setNid(cellIdentityCdma.getNetworkId());
                    baseStationInfo.setBid(cellIdentityCdma.getBasestationId());
                    list.add(baseStationInfo);
                    // MxyLog.i(TAG, "附近发现cdma基站sid = " + cellIdentityCdma.getSystemId());
                    // MxyLog.i(TAG, "附近发现cdma基站nid = " + cellIdentityCdma.getNetworkId());
                    // MxyLog.i(TAG, "附近发现cdma基站sid = " + cellIdentityCdma.getBasestationId());
                } else if (i instanceof CellInfoLte) {
                    CellIdentityLte cellIdentityLte = ((CellInfoLte) i).getCellIdentity();
                    if (cellIdentityLte == null) continue;
                    BaseStationInfo baseStationInfo = new BaseStationInfo();
                    baseStationInfo.setBaseType(0);
                    baseStationInfo.setMcc(cellIdentityLte.getMcc());
                    baseStationInfo.setMnc(cellIdentityLte.getMnc());
                    baseStationInfo.setLac(cellIdentityLte.getTac());
                    baseStationInfo.setCid(cellIdentityLte.getCi());
                    list.add(baseStationInfo);
                    // MxyLog.i(TAG, "附近发现lte基站Mcc = " + cellIdentityLte.getMcc());
                    // MxyLog.i(TAG, "附近发现lte基站Mnc = " + cellIdentityLte.getMnc());
                    // MxyLog.i(TAG, "附近发现lte基站Lac = " + cellIdentityLte.getTac());
                    // MxyLog.i(TAG, "附近发现lte基站Cid = " + cellIdentityLte.getCi());
                } else if (i instanceof CellInfoWcdma) {
                    CellIdentityWcdma cellIdentityWcdma = ((CellInfoWcdma) i).getCellIdentity();
                    if (cellIdentityWcdma == null) continue;
                    BaseStationInfo baseStationInfo = new BaseStationInfo();
                    baseStationInfo.setBaseType(0);
                    baseStationInfo.setMcc(cellIdentityWcdma.getMcc());
                    baseStationInfo.setMnc(cellIdentityWcdma.getMnc());
                    baseStationInfo.setLac(cellIdentityWcdma.getLac());
                    baseStationInfo.setCid(cellIdentityWcdma.getCid());
                    list.add(baseStationInfo);
                    // MxyLog.i(TAG, "附近发现wcdma基站Mcc = " + cellIdentityWcdma.getMcc());
                    // MxyLog.i(TAG, "附近发现wcdma基站Mnc = " + cellIdentityWcdma.getMnc());
                    // MxyLog.i(TAG, "附近发现wcdma基站Lac = " + cellIdentityWcdma.getLac());
                    // MxyLog.i(TAG, "附近发现wcdma基站Cid = " + cellIdentityWcdma.getCid());
                } else {
                    MxyLog.i(TAG, "未知附近的基站是什么.");
                }
            }
        } else {//有些手机拿不到的话，就用废弃的方法，有时候即使手机支持，getNeighboringCellInfo的返回结果也常常是null
            MxyLog.i(TAG, "通过高版本SDK无法拿到基站信息，准备用低版本的方法");
            List<NeighboringCellInfo> infos2 = mTelephonyManager.getNeighboringCellInfo();
            if (infos2 == null || infos2.size() == 0) {
                MxyLog.i(TAG, "该手机确实不支持基站定位，已经无能为力了");
                list.size();
                return list;
            }
            for (NeighboringCellInfo i : infos2) {//根据邻区总数进行循环
                BaseStationInfo baseStationInfo = new BaseStationInfo();
                baseStationInfo.setMcc(i.getPsc());
                baseStationInfo.setMnc(i.getNetworkType());
                baseStationInfo.setLac(i.getLac());
                baseStationInfo.setCid(i.getCid());
                baseStationInfo.setBsss(-113 + 2 * i.getRssi());
                list.add(baseStationInfo);
            }
        }
        MxyLog.i(TAG, "基站数量：" + list.size());
        return list;
    }

    /**
     * 根据国家代码获取通信运营商名字
     *
     * @param operatorString
     * @return
     */
    private static String getCarrier(String operatorString) {
        if (operatorString == null) {
            return "0";
        }
        switch (operatorString) {
            case "46000":
            case "46002":
                return "中国移动";
            case "46001":
                return "中国联通";
            case "46003":
                return "中国电信";
        }
        return "未知";
    }

    /**
     * 格式化基站信息,为服务器需要的数据格式
     *
     * @return
     */
    public static String formatBaseInfo(BaseStationInfo info) {
        if (info != null) {
            if (info.getBaseType() == 0) {
                return info.getMcc() + "," + info.getMnc() + "," + info.getLac() + "," + info.getCid() + "," + info.getBsss();
            } else {
                return info.getSid() + "," + info.getNid() + "," + info.getBid() + "," + "" + "," + info.getBsss();
            }
        }
        return "";
    }

    /**
     * 格式化基站信息,为服务器需要的数据格式
     *
     * @param list
     * @return
     */
    public static String formatNearbyBaseInfo(List<BaseStationInfo> list) {
        StringBuilder builder = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < (list.size() >= 4 ? 4 : list.size()); i++) {
                builder.append(formatBaseInfo(list.get(i))).append("|");
            }
            return builder.deleteCharAt(builder.length() - 1).toString();
        }
        return "";
    }

    /**
     * gprs协议格式化基站信息,为服务器需要的数据格式
     *
     * @param info
     * @param list
     * @return
     */
    public static String formatBaseInfo(BaseStationInfo info, List<BaseStationInfo> list) {
        StringBuilder builder = new StringBuilder();
        if (info != null && list != null && list.size() > 0) {
            builder.append(info.getMcc()).append(",")
                    .append(info.getMnc()).append(",")
                    .append(info.getLac()).append(",")
                    .append(info.getCid()).append(",")
                    .append(info.getBsss()).append(",");
            for (int i = 0; i < (list.size() >= 4 ? 4 : list.size()); i++) {
                builder.append(list.get(i).getLac()).append(",")
                        .append(list.get(i).getCid()).append(",")
                        .append(list.get(i).getBsss()).append(",");
            }
            return builder.deleteCharAt(builder.length() - 1).toString();
        }
        return "";
    }
}
