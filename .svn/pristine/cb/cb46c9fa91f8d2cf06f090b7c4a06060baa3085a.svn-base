package com.sczn.wearlauncher.socket.command.bean;

/**
 * Description:
 * Created by Bingo on 2019/3/7.
 */
public class FenceAlarm extends BasePostBean {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private ContentBean content;

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public static class ContentBean {

            private String fenceName;
            private long uploadTime;
            private double lng;
            private double lat;

            public String getFenceName() {
                return fenceName;
            }

            public void setFenceName(String fenceName) {
                this.fenceName = fenceName;
            }

            public long getUploadTime() {
                return uploadTime;
            }

            public void setUploadTime(long uploadTime) {
                this.uploadTime = uploadTime;
            }

            public double getLng() {
                return lng;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }
        }
    }
}
