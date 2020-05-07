package cn.com.waterworld.alarmclocklib.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.com.waterworld.alarmclocklib.app.AlarmClockApp;


/**
 * Created by wangfeng on 2018/6/11.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper instance;

    /**
     * 双重加锁检查
     */
    public static synchronized DBHelper getInstance() {
        if (instance == null) {
            synchronized (DBHelper.class) {
                if (instance == null) {
                    instance = new DBHelper(AlarmClockApp.getApplication());
                }
            }
        }
        return instance;
    }

    public DBHelper(Context context) {
        super(context, TableConfig.DATABASE_NAME, null, TableConfig.DATABASE_VERSION);
    }

    /**
     * 数据库第一次被创建时会被调用
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TableConfig.ALARM.ALARM_SQL);
        db.execSQL(TableConfig.EMOJI.EMOJI_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

}
