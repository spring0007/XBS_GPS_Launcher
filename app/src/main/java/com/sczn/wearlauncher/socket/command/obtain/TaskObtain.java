package com.sczn.wearlauncher.socket.command.obtain;

import java.util.List;

/**
 * Description:
 * Created by Bingo on 2019/2/26.
 */
public class TaskObtain {
    private int isCloseInClass; //是否上课关闭
    private List<String> course;    //每天的课程以 逗号 分隔，若此节课没安排 以空占位
    private List<TimeBean> time;

    public int getCloseInClass() {
        return isCloseInClass;
    }

    public void setCloseInClass(int closeInClass) {
        this.isCloseInClass = closeInClass;
    }

    public List<String> getCourse() {
        return course;
    }

    public void setCourse(List<String> course) {
        this.course = course;
    }

    public List<TimeBean> getTime() {
        return time;
    }

    public void setTime(List<TimeBean> time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "TaskObtain{" +
                "isCloseInClass=" + isCloseInClass +
                ", course=" + course +
                ", time=" + time +
                '}';
    }

    /**
     * 每天上课时间表
     */
    public static class TimeBean {
        private String startTime;
        private String endTime;

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        @Override
        public String toString() {
            return "TimeBean{" +
                    "startTime='" + startTime + '\'' +
                    ", endTime='" + endTime + '\'' +
                    '}';
        }
    }
}
