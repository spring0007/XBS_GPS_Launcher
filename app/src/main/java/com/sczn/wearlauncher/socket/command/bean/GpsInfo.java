package com.sczn.wearlauncher.socket.command.bean;

import java.util.List;

/**
 * Description:
 * Created by Bingo on 2019/2/28.
 */
public class GpsInfo extends BasePostBean {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int locationType;
        private String optUserId;
        private List<GpsBean> gps;

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

        public List<GpsBean> getGps() {
            return gps;
        }

        public void setGps(List<GpsBean> gps) {
            this.gps = gps;
        }

        public static class GpsBean {
            /**
             * lat : 12.2424
             * lng : 110.2424
             * accuracy : 10
             * timestamp : 1501123709
             */

            private double lat;
            private double lng;
            private float accuracy;
            private long timestamp;

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }

            public double getLng() {
                return lng;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }

            public float getAccuracy() {
                return accuracy;
            }

            public void setAccuracy(float accuracy) {
                this.accuracy = accuracy;
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
