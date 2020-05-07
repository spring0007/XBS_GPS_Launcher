package com.sczn.wearlauncher.socket.command.bean;

/**
 * Description:
 * Created by Bingo on 2019/3/1.
 */
public class WiFiAndBaseStationInfo extends BasePostBean {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * 0基站,1wifi,2gps
         */
        private int locationType;
        private String optUserId;
        private LbsBean lbs;

        public int getLocationType() {
            return locationType;
        }

        public void setLocationType(int locationType) {
            this.locationType = locationType;
        }

        public String getOptUserId() {
            return optUserId;
        }

        public void setOptUserId(String optUserId) {
            this.optUserId = optUserId;
        }

        public LbsBean getLbs() {
            return lbs;
        }

        public void setLbs(LbsBean lbs) {
            this.lbs = lbs;
        }

        public static class LbsBean {
            /**
             * 手机mac码1A(非必填，建议填写，提高精确度)
             * 例:smac:E0:DB:55:E4:C7:49
             */
            private String smac;
            /**
             * 设备接入基站时对应的网关IP,1A
             */
            private String serverip;
            /**
             * 电信0-非cdma,1-cdma,locationType=0必填
             */
            private int cdma;
            /**
             * 移动用户识别码1A
             */
            private String imsi;
            /**
             * 无线网络类型,locationType=0必填
             */
            private String network;
            /**
             * 接入基站信息,locationType=0必填非cdma(mcc,mnc,lac,cellid,signal)，cdma(sid,nid,bid,,,signal)
             * 例:460,01,40977,2205409,-65
             */
            private String bts;
            /**
             * 周边基站信息
             * 例:460,01,40977,2205409,-65|460,01,40977,2205409,-65
             */
            private String nearbts;
            /**
             * 已连接wifi信息,locationType=1必填（mac,signal,ssid）
             * 例:f0:7d:68:9e:18,f0:7d:68:9e:18,-41,TPLink
             */
            private String mmac;
            /**
             * wifi列表中信号最好的,2个以上
             * 例:f0:7d:68:9e:18,f0:7d:68:9e:18,-41,TPLink|f0:7d:68:9e:18,f0:7d:68:9e:18,-41,TPLink
             */
            private String macs;

            private long timestamp;

            public String getSmac() {
                return smac;
            }

            public void setSmac(String smac) {
                this.smac = smac;
            }

            public String getServerip() {
                return serverip;
            }

            public void setServerip(String serverip) {
                this.serverip = serverip;
            }

            public int getCdma() {
                return cdma;
            }

            public void setCdma(int cdma) {
                this.cdma = cdma;
            }

            public String getImsi() {
                return imsi;
            }

            public void setImsi(String imsi) {
                this.imsi = imsi;
            }

            public String getNetwork() {
                return network;
            }

            public void setNetwork(String network) {
                this.network = network;
            }

            public String getBts() {
                return bts;
            }

            public void setBts(String bts) {
                this.bts = bts;
            }

            public String getNearbts() {
                return nearbts;
            }

            public void setNearbts(String nearbts) {
                this.nearbts = nearbts;
            }

            public String getMmac() {
                return mmac;
            }

            public void setMmac(String mmac) {
                this.mmac = mmac;
            }

            public String getMacs() {
                return macs;
            }

            public void setMacs(String macs) {
                this.macs = macs;
            }

            public long getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(long timestamp) {
                this.timestamp = timestamp;
            }
        }
    }
}
