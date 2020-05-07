package com.sczn.wearlauncher.db;

import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbMgr extends SQLiteOpenHelper{
	
	private static final String DATABASE_NAME = "sczn_wearlauncher.db";
    private static final int DATABASE_VERSION = 3;	//2--添加时间字符串
    
    private static DbMgr instance;
    
    public static void init(Context context){
    	instance = new DbMgr(context);
    }
    
    public static synchronized DbMgr getInstance(Context context){
    	/*
    	if(context == null){
    		throw new NullPointerException("DbMgr.getInstance(Context context)----context = null");
    	}
    	
    	if (instance == null) {
    		instance = new DbMgr(context.getApplicationContext());
        }else if(context.getApplicationContext() != instance.getContext()){
        	instance.destroyDatabase();
        	instance = new DbMgr(context.getApplicationContext());
        }*/
    	return instance;
    }

    private Context mContext;
    private AtomicInteger mOpenCounter;
    private SQLiteDatabase mDatabase;
    
	private DbMgr(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		mOpenCounter = new AtomicInteger();
	}
	
	public Context getContext(){
		return mContext;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
        db.execSQL("CREATE TABLE " + Provider.ColumnsStep.TABLE_NAME + " ("
                + Provider.ColumnsStep._ID + " INTEGER PRIMARY KEY,"
                + Provider.ColumnsStep.COLUMNS_GET_TIME + " LONG NOT NULL,"
                + Provider.ColumnsStep.COLUMNS_SETP_COUNT + " INTEGER,"
                + Provider.ColumnsStep.COLUMNS_DISTANCE + " DOUBLE,"
                + Provider.ColumnsStep.COLUMNS_KCAL + " DOUBLE,"
                + Provider.ColumnsStep.COLUMNS_TIME_STRING + " STRING"
                + ");");

        db.execSQL("CREATE TABLE " + Provider.ColumnsSleep.TABLE_NAME + " ("
                + Provider.ColumnsSleep._ID + " INTEGER PRIMARY KEY,"
                + Provider.ColumnsSleep.COLUMNS_START_TIME + " LONG NOT NULL,"
                + Provider.ColumnsSleep.COLUMNS_END_TIME + " LONG NOT NULL,"
                + Provider.ColumnsSleep.COLUMNS_SLEEP_STATE + " INTEGER,"
                + Provider.ColumnsSleep.COLUMNS_SLEEP_STATE_STEP + " INTEGER,"
                + Provider.ColumnsSleep.COLUMNS_TIME_STRING + " STRING"
                + ");");
        db.execSQL("CREATE TABLE " + Provider.ColumnsHeartRate.TABLE_NAME + " ("
                + Provider.ColumnsHeartRate._ID + " INTEGER PRIMARY KEY,"
                + Provider.ColumnsHeartRate.COLUMNS_TIME + " LONG NOT NULL,"
                + Provider.ColumnsHeartRate.COLUMNS_HEART_RATE + " LONG"
                + ");");
        db.execSQL("CREATE TABLE " + Provider.ColumnsAtmosphere.TABLE_NAME + " ("
                + Provider.ColumnsAtmosphere._ID + " INTEGER PRIMARY KEY,"
                + Provider.ColumnsAtmosphere.COLUMNS_TIME + " LONG NOT NULL,"
                + Provider.ColumnsAtmosphere.COLUMNS_PRESSURE + " DOUBLE"
                + ");");
        
        //健康提醒闹钟数据表
        db.execSQL("CREATE TABLE " + Provider.ColumnsHealthAlarm.TABLE_NAME + " ("
                + Provider.ColumnsHealthAlarm._ID + " INTEGER PRIMARY KEY,"
                + Provider.ColumnsHealthAlarm.COLUMNS_TIME + " LONG NOT NULL,"
                + Provider.ColumnsHealthAlarm.COLUMNS_TYPE + " INTEGER,"
                + Provider.ColumnsHealthAlarm.COLUMNS_REPEAT + " INTEGER,"
                + Provider.ColumnsHealthAlarm.COLUMNS_EBABLE + " INTEGER,"
                + Provider.ColumnsHealthAlarm.COLUMNS_TIME_STRING + " STRING"
                + ");");
    
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + Provider.ColumnsStep.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Provider.ColumnsSleep.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Provider.ColumnsHeartRate.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Provider.ColumnsAtmosphere.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Provider.ColumnsHealthAlarm.TABLE_NAME);
        onCreate(db);
    
	}

	public synchronized SQLiteDatabase openDatabase() {
        if(mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            mDatabase = getWritableDatabase();
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        if(mOpenCounter.decrementAndGet() == 0) {
            // Closing database
            mDatabase.close();
        }
    }

    public synchronized void destroyDatabase() {
        if(mDatabase.isOpen()){
        	mDatabase.close();
        }
        mOpenCounter.set(0);
        mDatabase = null;
    }
    
}
