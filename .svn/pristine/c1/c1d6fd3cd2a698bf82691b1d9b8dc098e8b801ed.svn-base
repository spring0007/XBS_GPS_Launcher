package com.sczn.wearlauncher.location.bean;

/**
 * Description:
 * Created by Bingo on 2019/1/25.
 */
public class BaseStationInfo {
    // # MCC，Mobile Country Code，移动国家代码（中国的为460）；
    // # MNC，Mobile Network Code，移动网络号码（中国移动为0，中国联通为1，中国电信为2）；
    // # LAC，Location Area Code，位置区域码;
    // # CID，Cell Identity，基站编号；
    // # BSSS，Base station signal strength，基站信号强度。

    /**
     * 下面三个为电信的基站的参数
     */
    // # cdma用sid,是系统识别码，每个地级市只有一个sid，是唯一的。
    // # NID是网络识别码，由各本地网管理，也就是由地级分公司分配。每个地级市可能有1到3个nid。
    // # cdma用bid,表示的是网络中的某一个小区，可以理解为基站。

    private int mcc;
    private int mnc;
    private int lac;
    private int cid;
    private int bsss;

    /**
     * 基站类型
     * 0:非cdma类型
     * 1:为cdma类型
     */
    private int baseType;

    private int sid;
    private int nid;
    private int bid;

    public int getMcc() {
        return mcc;
    }

    public void setMcc(int mcc) {
        this.mcc = mcc;
    }

    public int getMnc() {
        return mnc;
    }

    public void setMnc(int mnc) {
        this.mnc = mnc;
    }

    public int getLac() {
        return lac;
    }

    public void setLac(int lac) {
        this.lac = lac;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getBsss() {
        return bsss;
    }

    public void setBsss(int bsss) {
        this.bsss = bsss;
    }

    public int getBaseType() {
        return baseType;
    }

    public void setBaseType(int baseType) {
        this.baseType = baseType;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getNid() {
        return nid;
    }

    public void setNid(int nid) {
        this.nid = nid;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    @Override
    public String toString() {
        return "BaseStationInfo{" +
                "mcc=" + mcc +
                ", mnc=" + mnc +
                ", lac=" + lac +
                ", cid=" + cid +
                ", bsss=" + bsss +
                ", baseType=" + baseType +
                ", sid=" + sid +
                ", nid=" + nid +
                ", bid=" + bid +
                '}';
    }
}
