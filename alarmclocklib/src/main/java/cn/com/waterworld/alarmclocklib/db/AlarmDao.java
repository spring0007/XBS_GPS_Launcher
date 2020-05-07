package cn.com.waterworld.alarmclocklib.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.com.waterworld.alarmclocklib.model.AlarmBean;

/**
 * Created by wangfeng on 2018/6/11.
 */
public class AlarmDao extends BaseDao {

    private static AlarmDao alarmDao;
    private SQLiteDatabase db;

    public static AlarmDao getInstance() {
        if (null == alarmDao) {
            synchronized (AlarmDao.class) {
                if (null == alarmDao) {
                    alarmDao = new AlarmDao();
                }
            }
        }
        return alarmDao;
    }

    public AlarmDao() {
        super();
        db = DBHelper.getInstance().getWritableDatabase();
    }

    @Override
    public void insert(Object o) {
        if (o instanceof AlarmBean) {
            AlarmBean alarmBean = (AlarmBean) o;
            if (db != null) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(TableConfig.ALARM.ALARM_ID, alarmBean.getID());
                contentValues.put(TableConfig.ALARM.ALARM_TIME, alarmBean.getAlarmTime());
                contentValues.put(TableConfig.ALARM.ALARM_WEEK, alarmBean.getWeekValue());
                contentValues.put(TableConfig.ALARM.ALARM_WEEK_TIP, alarmBean.getWeekTip());
                contentValues.put(TableConfig.ALARM.ALARM_ISOFF, alarmBean.getIsOff());
                contentValues.put(TableConfig.ALARM.ALARM_FLAG, alarmBean.getFlag());
                contentValues.put(TableConfig.SAVE_TIME, System.currentTimeMillis() / 1000);
                db.replace(TableConfig.ALARM.ALARM_TABLE, null, contentValues);
            }
        }
    }

    @Override
    public List queryAll() {
        List<AlarmBean> list = new ArrayList<>();
        if (db != null) {
            Cursor cursor = null;
            try {
                cursor = db.query(TableConfig.ALARM.ALARM_TABLE, null, null, null, null, null, null);
                list = doOnFindResult(cursor);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return list;
    }

    @Override
    public AlarmBean query(Object o) {
        AlarmBean alarmBean = null;
        if (db != null) {
            Cursor cursor = null;
            try {
                cursor = db.query(TableConfig.ALARM.ALARM_TABLE, null, "alarm_id = ?",
                        new String[]{(String) o}, null, null, null);
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        alarmBean = setData(cursor);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return alarmBean;
    }

    @Override
    public List queryList(Object o) {
        List<AlarmBean> list = new ArrayList<>();
        if (db != null) {
            Cursor cursor = null;
            try {
                cursor = db.query(TableConfig.ALARM.ALARM_TABLE, null, "alarm_time = ?", new String[]{(String) o}, null, null, null);
                list = doOnFindResult(cursor);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return list;
    }

    @Override
    public boolean updata(Object o) {
        if (o instanceof AlarmBean) {
            AlarmBean alarmBean = (AlarmBean) o;
            if (db != null) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(TableConfig.ALARM.ALARM_FLAG, alarmBean.getFlag());
                contentValues.put(TableConfig.ALARM.ALARM_ISOFF, alarmBean.getIsOff());
                contentValues.put(TableConfig.ALARM.ALARM_WEEK, alarmBean.getWeekValue());
                contentValues.put(TableConfig.ALARM.ALARM_WEEK_TIP, alarmBean.getWeekTip());
                contentValues.put(TableConfig.ALARM.ALARM_TIME, alarmBean.getAlarmTime());
                contentValues.put(TableConfig.ALARM.ALARM_ID, alarmBean.getID());
                db.update(TableConfig.ALARM.ALARM_TABLE, contentValues, TableConfig.ALARM.ALARM_ID + " = ?", new String[]{alarmBean.getID()});
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean delete(Object o) {
        if (o instanceof String) {
            String id = (String) o;
            if (db != null) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(TableConfig.ALARM.ALARM_ID, id);
                db.delete(TableConfig.ALARM.ALARM_TABLE, TableConfig.ALARM.ALARM_ID + " = ?", new String[]{id});
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean deleteAll() {
        return false;
    }

    /**
     * 判断如果这两个字段相等,就表示这个闹钟存在
     * alarm_time
     * alarm_flag
     *
     * @param time
     * @param flag
     * @return
     */
    public boolean hasByTimeAndFlag(int time, int flag) {
        if (db != null) {
            Cursor cursor = null;
            try {
                cursor = db.query(TableConfig.ALARM.ALARM_TABLE, null,
                        "alarm_time = ? and alarm_flag = ?",
                        new String[]{String.valueOf(time), String.valueOf(flag)},
                        null, null, null);
                if (cursor != null && cursor.getCount() > 0) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return false;
    }


    /**
     * 将数据库查询到的结果遍历返回
     *
     * @param cursor
     * @return
     */
    private List<AlarmBean> doOnFindResult(Cursor cursor) {
        List<AlarmBean> list = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                list.add(setData(cursor));
            }
        }
        return list;
    }

    /**
     * @param cursor
     * @return
     */
    private AlarmBean setData(Cursor cursor) {
        int idindex = cursor.getColumnIndex(TableConfig.ALARM.ALARM_ID);
        String id = cursor.getString(idindex);

        int timeIndex = cursor.getColumnIndex(TableConfig.ALARM.ALARM_TIME);
        int time = cursor.getInt(timeIndex);

        int weekIndex = cursor.getColumnIndex(TableConfig.ALARM.ALARM_WEEK);
        String week = cursor.getString(weekIndex);

        int weekTipIndex = cursor.getColumnIndex(TableConfig.ALARM.ALARM_WEEK_TIP);
        String weekTip = cursor.getString(weekTipIndex);

        int typeIndex = cursor.getColumnIndex(TableConfig.ALARM.ALARM_ISOFF);
        int type = cursor.getInt(typeIndex);

        int flagIndex = cursor.getColumnIndex(TableConfig.ALARM.ALARM_FLAG);
        int flag = cursor.getInt(flagIndex);

        AlarmBean alarmBean = new AlarmBean();
        alarmBean.setID(id);
        alarmBean.setAlarmTime(time);
        alarmBean.setWeekValue(week);
        alarmBean.setWeekTip(weekTip);
        alarmBean.setIsOff(type);
        alarmBean.setFlag(flag);
        return alarmBean;
    }
}
