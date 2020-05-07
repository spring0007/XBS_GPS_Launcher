package cn.com.waterworld.alarmclocklib.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import cn.com.waterworld.alarmclocklib.util.Unit;

/**
 * Created by wangfeng on 2018/6/11.
 */
public class AlarmBean implements Parcelable, Serializable {
    /**
     * 用以区分数据库中的各个时间，相当于一个标志位
     */
    private String ID;
    /**
     * 时间,以分钟来表示,一天24小时为1440分钟(分钟)
     */
    private int alarmTime;
    /**
     * 以16进制表示，但实际上是以二进制区分
     */
    private String weekValue;
    /**
     * 显示，比如星期一，星期二...
     */
    private String weekTip;
    /**
     * 由于sqlite3没有boolean类型，所以以数字表示，0是开，1是关
     */
    private int isOff;
    /**
     * flag = 0 表示一次性的闹钟
     * flag = 1 表示每天提醒的闹钟(1天的时间间隔)
     * flag = 2表示按周每周提醒的闹钟（一周的周期性时间间隔）
     */
    private int flag;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(int alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getWeekValue() {
        return weekValue;
    }

    public void setWeekValue(String weekValue) {
        this.weekValue = weekValue;
    }

    public String getWeekTip() {
        return weekTip;
    }

    public void setWeekTip(String weekTip) {
        this.weekTip = weekTip;
    }

    public int getIsOff() {
        return isOff;
    }

    public void setIsOff(int isOff) {
        this.isOff = isOff;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "AlarmBean{" +
                "ID='" + ID + '\'' +
                ", alarmTime=" + alarmTime +
                ", weekValue='" + weekValue + '\'' +
                ", weekValue='" + Unit.toBinaryString2(weekValue) + '\'' +
                ", weekTip='" + weekTip + '\'' +
                ", isOff=" + isOff +
                ", flag=" + flag +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ID);
        dest.writeInt(this.alarmTime);
        dest.writeString(this.weekValue);
        dest.writeString(this.weekTip);
        dest.writeInt(this.isOff);
        dest.writeInt(this.flag);
    }

    public AlarmBean() {
    }

    protected AlarmBean(Parcel in) {
        this.ID = in.readString();
        this.alarmTime = in.readInt();
        this.weekValue = in.readString();
        this.weekTip = in.readString();
        this.isOff = in.readInt();
        this.flag = in.readInt();
    }

    public static final Parcelable.Creator<AlarmBean> CREATOR = new Parcelable.Creator<AlarmBean>() {
        @Override
        public AlarmBean createFromParcel(Parcel source) {
            return new AlarmBean(source);
        }

        @Override
        public AlarmBean[] newArray(int size) {
            return new AlarmBean[size];
        }
    };
}
