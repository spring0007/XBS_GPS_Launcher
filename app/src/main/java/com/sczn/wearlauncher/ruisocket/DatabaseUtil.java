package com.sczn.wearlauncher.ruisocket;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.sczn.wearlauncher.app.MxyLog;

public class DatabaseUtil {
    private LocationDBHelper helper;

    public DatabaseUtil(Context context) {
        super();
        helper = new LocationDBHelper(context);
    }

    /**
     * 插入数据
     *
     * @param data
     * @return
     */
    public boolean Insert(String data) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "insert into " + LocationDBHelper.TABLE_NAME
                + "(location) values (" + "'" + data + "')";
        try {
            db.execSQL(sql);
            return true;
        } catch (SQLException e) {
            MxyLog.e("err", "insert failed");
            return false;
        } finally {
            db.close();
        }

    }

    /**
     * 删除数据
     *
     * @param id
     */
    public void Delete(int id) {

        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            int raw = db.delete(LocationDBHelper.TABLE_NAME, "_id=?",
                    new String[]{id + ""});
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            db.close();
        }


    }

    /**
     * 查询数据
     *
     * @return
     */
    public String querydata() {
        SQLiteDatabase db = helper.getReadableDatabase();
        String location = null;
        try {
            Cursor cursor = db.query(LocationDBHelper.TABLE_NAME, null, null, null,
                    null, null, null);
            if (cursor.getColumnCount() == 0) {
                return null;
            }

            while (cursor.moveToNext()) {
                location = cursor.getInt(cursor.getColumnIndex("_id")) + " --- "
                        + cursor.getString(cursor.getColumnIndex("location"));
                break;
            }

            cursor.close();
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            db.close();
        }

        return location;
    }


    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    public String queryByid(int id) {

        SQLiteDatabase db = helper.getReadableDatabase();
        String location = null;

        try {
            Cursor cursor = db.query(LocationDBHelper.TABLE_NAME,
                    new String[]{"location"}, "_id=?", new String[]{id + ""},
                    null, null, null);
            // db.delete(table, whereClause, whereArgs)
            while (cursor.moveToNext()) {
                location = cursor.getString(cursor.getColumnIndex("location"));

            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            db.close();
        }

        return location;
    }

}
