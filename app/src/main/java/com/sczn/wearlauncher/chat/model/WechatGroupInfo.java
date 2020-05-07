package com.sczn.wearlauncher.chat.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.LitePalSupport;

/**
 * Description:
 * Created by Bingo on 2019/1/26.
 */
public class WechatGroupInfo extends LitePalSupport implements Parcelable {
    private String groupId;
    private String groupName;
    private int type;
    private String watchname;
    private String guid;
    private String head;
    private int maxMsgId;
    private int msgReadStatus;//消息已读0,消息未读1

    //服务器参数
    private String watchId;
    private String createTime;
    private int createUserId;
    private String createUserName;
    private String name;
    private int owner;
    private String updateTime;

    public WechatGroupInfo() {
    }

    protected WechatGroupInfo(Parcel in) {
        groupId = in.readString();
        groupName = in.readString();
        type = in.readInt();
        watchId = in.readString();
        watchname = in.readString();
        guid = in.readString();
        head = in.readString();
        maxMsgId = in.readInt();
        msgReadStatus = in.readInt();

        createTime = in.readString();
        createUserId = in.readInt();
        createUserName = in.readString();
        name = in.readString();
        owner = in.readInt();
        updateTime = in.readString();
    }

    public static final Creator<WechatGroupInfo> CREATOR = new Creator<WechatGroupInfo>() {
        @Override
        public WechatGroupInfo createFromParcel(Parcel in) {
            return new WechatGroupInfo(in);
        }

        @Override
        public WechatGroupInfo[] newArray(int size) {
            return new WechatGroupInfo[size];
        }
    };

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String var1) {
        this.groupId = var1;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String var1) {
        this.groupName = var1;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int var1) {
        this.type = var1;
    }

    public String getWatchid() {
        return this.watchId;
    }

    public void setWatchid(String var1) {
        this.watchId = var1;
    }

    public String getWatchname() {
        return this.watchname;
    }

    public void setWatchname(String var1) {
        this.watchname = var1;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public int getMaxMsgId() {
        return maxMsgId;
    }

    public void setMaxMsgId(int maxMsgId) {
        this.maxMsgId = maxMsgId;
    }

    public int getMsgReadStatus() {
        return msgReadStatus;
    }

    public void setMsgReadStatus(int msgReadStatus) {
        this.msgReadStatus = msgReadStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(int createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "WechatGroupInfo{" +
                "groupId='" + groupId + '\'' +
                ", groupName='" + groupName + '\'' +
                ", type=" + type +
                ", watchId='" + watchId + '\'' +
                ", watchname='" + watchname + '\'' +
                ", guid='" + guid + '\'' +
                ", head='" + head + '\'' +
                ", maxMsgId='" + maxMsgId + '\'' +
                ", msgReadStatus='" + msgReadStatus + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(groupId);
        dest.writeString(groupName);
        dest.writeInt(type);
        dest.writeString(watchId);
        dest.writeString(watchname);
        dest.writeString(guid);
        dest.writeString(head);
        dest.writeInt(maxMsgId);
        dest.writeInt(msgReadStatus);

        dest.writeString(createTime);
        dest.writeInt(createUserId);
        dest.writeString(createUserName);
        dest.writeString(name);
        dest.writeInt(owner);
        dest.writeString(updateTime);
    }
}
