package com.sczn.wearlauncher.socket.command.bean;

/**
 * Description:消息通知
 * Created by Bingo on 2019/2/20.
 */
public class NotifyMsg {

    /**
     * type : 103
     * timestamp : 1501123709
     * data : {"content":""}
     */

    private int type;
    private long timestamp;
    private DataBean data;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * content :
         */

        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
