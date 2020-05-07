package com.sczn.wearlauncher.chat.model;

import android.support.annotation.NonNull;

import org.litepal.crud.LitePalSupport;

/**
 * Description:
 * Created by Bingo on 2019/1/26.
 */
public class WechatMessageInfo extends LitePalSupport implements Comparable<WechatMessageInfo> {
    private String groupId = null;
    private int msgId;
    private String voiceKey = null;
    private String duration = null;
    private String senderId = null;
    private int senderType;
    private String senderName = null;
    private String time = null;
    private int type = 1;
    private String content = null;
    private String voicePath;

    private String head;
    private int msgLength;
    private String createTime;
    private int msgReadStatus;//消息已读0,消息未读1

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public String getVoiceKey() {
        return voiceKey;
    }

    public void setVoiceKey(String voiceKey) {
        this.voiceKey = voiceKey;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public int getSenderType() {
        return senderType;
    }

    public void setSenderType(int senderType) {
        this.senderType = senderType;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVoicePath() {
        return voicePath;
    }

    public void setVoicePath(String voicePath) {
        this.voicePath = voicePath;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public int getMsgLength() {
        return msgLength;
    }

    public void setMsgLength(int msgLength) {
        this.msgLength = msgLength;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getMsgReadStatus() {
        return msgReadStatus;
    }

    public void setMsgReadStatus(int msgReadStatus) {
        this.msgReadStatus = msgReadStatus;
    }

    @Override
    public int compareTo(@NonNull WechatMessageInfo o) {
        return msgId - o.msgId;
    }

    @Override
    public String toString() {
        return "WechatMessageInfo{" +
                "groupId='" + groupId + '\'' +
                ", msgId='" + msgId + '\'' +
                ", voiceKey='" + voiceKey + '\'' +
                ", duration='" + duration + '\'' +
                ", senderId='" + senderId + '\'' +
                ", senderName='" + senderName + '\'' +
                ", time='" + time + '\'' +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", voicePath='" + voicePath + '\'' +
                ", head='" + head + '\'' +
                ", msgLength=" + msgLength +
                ", createTime=" + createTime +
                ", msgReadStatus=" + msgReadStatus +
                '}';
    }
}
