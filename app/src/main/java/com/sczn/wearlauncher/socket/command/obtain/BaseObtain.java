package com.sczn.wearlauncher.socket.command.obtain;

/**
 * Description:服务器返回的数据bean
 * Created by Bingo on 2019/1/17.
 */
public class BaseObtain<T> {
    private int a;
    private String message;
    private int status;
    private long timestamp;
    private int type;
    private String no;
    private T data;

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseObtain{" +
                "a=" + a +
                ", message='" + message + '\'' +
                ", status=" + status +
                ", timestamp=" + timestamp +
                ", type=" + type +
                ", no='" + no + '\'' +
                ", data=" + data.toString() +
                '}';
    }
}
