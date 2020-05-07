package com.sczn.wearlauncher.ruisocket;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sczn.wearlauncher.app.MxyLog;

public class LocationDBHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "locatedata.db";  //数据库名称
    public static String TABLE_NAME = "locate"; //表名

    /**
     * super(参数1，参数2，参数3，参数4)，其中参数4是代表数据库的版本，
     * 是一个大于等于1的整数，如果要修改（添加字段）表中的字段，则设置
     * 一个比当前的 参数4大的整数 ，把更新的语句写在onUpgrade(),下一次
     * 调用
     */
    public LocationDBHelper(Context context) {
        super(context, DB_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create table
        String sql = "CREATE TABLE " + TABLE_NAME + "("
                + "_id INTEGER PRIMARY KEY,"
                + "location TEXT);";

        MxyLog.e("table oncreate", "create table");
        db.execSQL(sql);        //创建表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        MxyLog.e("update", "update");
//		db.execSQL("ALTER TABLE "+ MyHelper.TABLE_NAME+" ADD sex TEXT"); //修改字段
    }

}
