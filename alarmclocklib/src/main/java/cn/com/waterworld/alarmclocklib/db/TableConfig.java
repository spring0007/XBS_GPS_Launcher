package cn.com.waterworld.alarmclocklib.db;

/**
 * Created by wangfeng on 2018/6/11.
 */
public class TableConfig {

    public static final String DATABASE_NAME = "wtwd_wear_alarm.db";
    public static final int DATABASE_VERSION = 5;
    public static final String SAVE_TIME = "saveTime";  //表示存入表的时间


    public static class ALARM {
        public static final String ALARM_TABLE = "alarm_table";
        public static final String ALARM_ID = "alarm_id";
        public static final String ALARM_TIME = "alarm_time";
        public static final String ALARM_WEEK = "alarm_week";
        public static final String ALARM_WEEK_TIP = "alarm_week_tip";
        public static final String ALARM_ISOFF = "alarm_isoff";
        public static final String ALARM_FLAG = "alarm_flag";

        public static final String ALARM_SQL = "create table if not exists " + ALARM_TABLE + " (" +
                "_id integer ," +
                ALARM_ID + " varchar(100) primary key," +
                ALARM_TIME + " integer," +
                ALARM_WEEK + " varchar(50) ," +
                ALARM_WEEK_TIP + " varchar(100) ," +
                ALARM_ISOFF + " integer ," +
                ALARM_FLAG + " integer ," +
                SAVE_TIME + " integer)";
    }

    public static class EMOJI {
        public static final String EMOJI_TABLE = "emoji_table";
        public static final String EMOJI_PACKAGE_ID = "package_id";
        public static final String EMOJI_PACKAGE_NAME = "package_name";
        public static final String EMOJI_ID = "emoji_id";
        public static final String EMOJI_NAME = "emoji_name";
        public static final String EMOJI_PIC_URL = "emoji_pic_url";
        public static final String EMOJI_THUMB_URL = "emoji_thumb_rel";

        public static final String EMOJI_SQL = "create table if not exists " + EMOJI_TABLE + " (" +
                "_id integer ," +
                EMOJI_PACKAGE_ID + " varchar(100)," +
                EMOJI_PACKAGE_NAME + " varchar(100)," +
                EMOJI_ID + " varchar(100) primary key ," +
                EMOJI_NAME + " varchar(100)," +
                EMOJI_PIC_URL + " varchar(500)," +
                EMOJI_THUMB_URL + " varchar(500)," +
                SAVE_TIME + " integer)";
    }
}
