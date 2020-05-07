package com.sczn.wearlauncher.chat.bean;

/**
 * Description:
 * Created by Bingo on 2019/3/19.
 */
public class UploadGroupVoiceObtain extends GroupBaseBeanObtain {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * voiceUrl : group_voice/14/155288175191600008.wma
         * groupId : 14
         */

        private String voiceUrl;
        private int groupId;

        public String getVoiceUrl() {
            return voiceUrl;
        }

        public void setVoiceUrl(String voiceUrl) {
            this.voiceUrl = voiceUrl;
        }

        public int getGroupId() {
            return groupId;
        }

        public void setGroupId(int groupId) {
            this.groupId = groupId;
        }
    }
}
