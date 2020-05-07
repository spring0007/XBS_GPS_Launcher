package com.sczn.wearlauncher.socket.command.bean;

import org.litepal.crud.LitePalSupport;

/**
 * Description:联系人的信息
 * Created by Bingo on 2019/1/28.
 */
public class Linkman extends LitePalSupport {
    private String head;//头像
    private int id;
    private String name;//昵称
    private String phone;//手机号
    private String role; //权限 1-普通通话，2-紧急通话权限，3-单向聆听权限
    private int watchId;

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getWatchId() {
        return watchId;
    }

    public void setWatchId(int watchId) {
        this.watchId = watchId;
    }
}
