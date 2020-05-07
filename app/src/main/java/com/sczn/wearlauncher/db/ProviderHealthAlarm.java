package com.sczn.wearlauncher.db;

import java.util.HashMap;

import com.sczn.wearlauncher.base.util.StringUtils;
import com.sczn.wearlauncher.db.Provider.ColumnsHealthAlarm;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class ProviderHealthAlarm extends ContentProvider {

    //判断Uri是否匹配
    private static final UriMatcher sUriMatcher;
    private static final int COLLECTION_INDICATOR = 1;
    private static final int SINGLE_INDICATOR = 2;
    
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(Provider.ColumnsHealthAlarm.AUTHORITY, ColumnsHealthAlarm.TABLE_NAME, COLLECTION_INDICATOR);
        sUriMatcher.addURI(Provider.ColumnsHealthAlarm.AUTHORITY, ColumnsHealthAlarm.TABLE_NAME + "/#", SINGLE_INDICATOR);//更精确查询的标识#
    }
    //查询映射，抽象的字段映射到数据库中真实存在的字段
    private static HashMap<String,String> mHealthAlarmProjectionMap;
    static {
    	mHealthAlarmProjectionMap = new HashMap<String,String >();
    	mHealthAlarmProjectionMap.put(Provider.ColumnsHealthAlarm._ID,Provider.ColumnsHealthAlarm._ID);
    	mHealthAlarmProjectionMap.put(Provider.ColumnsHealthAlarm.COLUMNS_TIME,Provider.ColumnsHealthAlarm.COLUMNS_TIME);
    	mHealthAlarmProjectionMap.put(Provider.ColumnsHealthAlarm.COLUMNS_TYPE,Provider.ColumnsHealthAlarm.COLUMNS_TYPE);
    	mHealthAlarmProjectionMap.put(Provider.ColumnsHealthAlarm.COLUMNS_REPEAT,Provider.ColumnsHealthAlarm.COLUMNS_REPEAT);
    	mHealthAlarmProjectionMap.put(Provider.ColumnsHealthAlarm.COLUMNS_EBABLE,Provider.ColumnsHealthAlarm.COLUMNS_EBABLE);
    	mHealthAlarmProjectionMap.put(Provider.ColumnsHealthAlarm.COLUMNS_TIME_STRING,Provider.ColumnsHealthAlarm.COLUMNS_TIME_STRING);
    	mHealthAlarmProjectionMap.put(Provider.ColumnsHealthAlarm.DEFAULT_SORT_ORDER,Provider.ColumnsHealthAlarm.DEFAULT_SORT_ORDER);
    }

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = DbMgr.getInstance(getContext()).openDatabase();
		
		int count = 0;
		switch (sUriMatcher.match(uri)) {
			case COLLECTION_INDICATOR:
				count = db.delete(Provider.ColumnsHealthAlarm.TABLE_NAME, selection, selectionArgs);
				break;
			case SINGLE_INDICATOR:
				String segment = uri.getPathSegments().get(1);  
                if (TextUtils.isEmpty(selection)) {  
                	selection = "_id=" + segment;  
                } else {  
                	selection = "_id=" + segment + " AND (" + selection + ")";  
                }  
                count = db.delete(Provider.ColumnsHealthAlarm.TABLE_NAME, selection, selectionArgs);
				break;
			default:
				throw new IllegalArgumentException("HealthAlarmProvider Cannot delete from URL: " + uri);
		}
		getContext().getContentResolver().notifyChange(ColumnsHealthAlarm.CONTENT_URI, null); 
		
		DbMgr.getInstance(getContext()).closeDatabase();
		return count;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		int match = sUriMatcher.match(uri);  
        switch (match) {  
            case COLLECTION_INDICATOR:  
                return "vnd.android.cursor.dir/alarms";  
            case SINGLE_INDICATOR:  
                return "vnd.android.cursor.item/alarms";  
            default:  
                throw new IllegalArgumentException("Unknown URL");  
        }
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		if(sUriMatcher.match(uri) != COLLECTION_INDICATOR){
			throw new IllegalArgumentException( ProviderHealthAlarm.class.getSimpleName() +  "Cannot insert into URL: " + uri);
		}
		if(values == null){
			throw new IllegalArgumentException( ProviderHealthAlarm.class.getSimpleName() +  "values is null");
		}
   
        SQLiteDatabase db = DbMgr.getInstance(getContext()).openDatabase(); 

        long rowId = db.insert(ColumnsHealthAlarm.TABLE_NAME, null, values);  
  
        if (rowId < 0) {  
            return null;				// failed 
        }  
        Uri newUrl = ContentUris.withAppendedId(ColumnsHealthAlarm.CONTENT_URI, rowId);  
        getContext().getContentResolver().notifyChange(ColumnsHealthAlarm.CONTENT_URI, null);  
        
        DbMgr.getInstance(getContext()).closeDatabase();
        return newUrl;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
        return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)){
            case COLLECTION_INDICATOR:
                // 设置查询的表
                qb.setTables(Provider.ColumnsHealthAlarm.TABLE_NAME);
                // 设置投影映射
                qb.setProjectionMap(mHealthAlarmProjectionMap);
                break;
            case SINGLE_INDICATOR:
                qb.setTables(Provider.ColumnsHealthAlarm.TABLE_NAME);
                qb.setProjectionMap(mHealthAlarmProjectionMap);
                qb.appendWhere(Provider.ColumnsHealthAlarm._ID + "=" + uri.getPathSegments().get(1));
                
                break;
            default:
                throw new IllegalArgumentException("Unknow URI: " + uri);
        }

        String orderBy;
        if(StringUtils.isEmpty(sortOrder))
        {
            orderBy = Provider.ColumnsHealthAlarm.DEFAULT_SORT_ORDER;
        } else {
            orderBy = sortOrder;
        }

        SQLiteDatabase db = DbMgr.getInstance(getContext()).openDatabase();
        Cursor c = qb.query(db,projection,selection,null,null,null,orderBy);
        return c;
    }

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		int count;  
		SQLiteDatabase db = DbMgr.getInstance(getContext()).openDatabase();
		
        switch (sUriMatcher.match(uri)){
            case COLLECTION_INDICATOR:
            	count = db.update(ColumnsHealthAlarm.TABLE_NAME, values, selection, selectionArgs);
                break;
            case SINGLE_INDICATOR:
            	String segment = uri.getPathSegments().get(1);  
                if (TextUtils.isEmpty(selection)) {  
                	selection = "_id=" + segment;  
                } else {  
                	selection = "_id=" + segment + " AND (" + selection + ")";  
                }  
                count = db.update(ColumnsHealthAlarm.TABLE_NAME, values, selection, null);
                break;
            default:
                throw new IllegalArgumentException("Unknow URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(ColumnsHealthAlarm.CONTENT_URI, null);  
        
        DbMgr.getInstance(getContext()).closeDatabase();
        return count; 
	}

}
