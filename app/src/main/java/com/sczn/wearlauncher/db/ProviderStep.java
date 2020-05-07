package com.sczn.wearlauncher.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

import com.sczn.wearlauncher.base.util.StringUtils;
import com.sczn.wearlauncher.db.Provider.ColumnsHealthAlarm;

/**
 * Created by mxy on 2016/3/21.
 */
public class ProviderStep extends ContentProvider {

    //判断Uri是否匹配
    private static final UriMatcher sUriMatcher;
    private static final int COLLECTION_INDICATOR = 1;
    private static final int SINGLE_INDICATOR = 2;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(Provider.ColumnsStep.AUTHORITY, Provider.ColumnsStep.TABLE_NAME, COLLECTION_INDICATOR);
        sUriMatcher.addURI(Provider.ColumnsStep.AUTHORITY, Provider.ColumnsStep.TABLE_NAME + "/#", SINGLE_INDICATOR);//更精确查询的标识#
    }
    //查询映射，抽象的字段映射到数据库中真实存在的字段
    private static HashMap<String,String> mStepconutProjectionMap;
    static {
        mStepconutProjectionMap = new HashMap<String,String >();
        mStepconutProjectionMap.put(Provider.ColumnsStep._ID,Provider.ColumnsStep._ID);
        mStepconutProjectionMap.put(Provider.ColumnsStep.COLUMNS_GET_TIME,Provider.ColumnsStep.COLUMNS_GET_TIME);
        mStepconutProjectionMap.put(Provider.ColumnsStep.COLUMNS_SETP_COUNT,Provider.ColumnsStep.COLUMNS_SETP_COUNT);
        mStepconutProjectionMap.put(Provider.ColumnsStep.COLUMNS_DISTANCE,Provider.ColumnsStep.COLUMNS_DISTANCE);
        mStepconutProjectionMap.put(Provider.ColumnsStep.COLUMNS_KCAL,Provider.ColumnsStep.COLUMNS_KCAL);
        mStepconutProjectionMap.put(Provider.ColumnsStep.COLUMNS_TIME_STRING,Provider.ColumnsStep.COLUMNS_TIME_STRING);
        mStepconutProjectionMap.put(Provider.ColumnsStep.DEFAULT_SORT_ORDER,Provider.ColumnsStep.DEFAULT_SORT_ORDER);
    }


    @Override
    public boolean onCreate() {
    	
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)){
            case COLLECTION_INDICATOR:
                // 设置查询的表
                qb.setTables(Provider.ColumnsStep.TABLE_NAME);
                // 设置投影映射
                qb.setProjectionMap(mStepconutProjectionMap);
                break;
            case SINGLE_INDICATOR:
                qb.setTables(Provider.ColumnsStep.TABLE_NAME);
                qb.setProjectionMap(mStepconutProjectionMap);
                qb.appendWhere(Provider.ColumnsStep._ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknow URI: " + uri);
        }

        String orderBy;
        if(StringUtils.isEmpty(sortOrder))
        {
            orderBy = Provider.ColumnsStep.DEFAULT_SORT_ORDER;
        } else {
            orderBy = sortOrder;
        }

        SQLiteDatabase db = DbMgr.getInstance(getContext()).openDatabase(); 
        Cursor c = qb.query(db,projection,selection,null,null,null,orderBy);

        return c;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		if(sUriMatcher.match(uri) != COLLECTION_INDICATOR){
			throw new IllegalArgumentException(Provider.ColumnsStep.class.getSimpleName() +  "Cannot insert into URL: " + uri);
		}
		if(values == null){
			throw new IllegalArgumentException(Provider.ColumnsStep.class.getSimpleName() +  "values is null");
		}
   
        SQLiteDatabase db = DbMgr.getInstance(getContext()).openDatabase(); 

        long rowId = db.insert(Provider.ColumnsStep.TABLE_NAME, null, values);  
  
        if (rowId < 0) {  
            return null;				// failed 
        }  
        Uri newUrl = ContentUris.withAppendedId(Provider.ColumnsStep.CONTENT_URI, rowId);  
        getContext().getContentResolver().notifyChange(Provider.ColumnsStep.CONTENT_URI, null);  
        
        DbMgr.getInstance(getContext()).closeDatabase();
        return newUrl;
	}

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		int count;  
		SQLiteDatabase db = DbMgr.getInstance(getContext()).openDatabase();
		
        switch (sUriMatcher.match(uri)){
            case COLLECTION_INDICATOR:
            	count = db.update(Provider.ColumnsStep.TABLE_NAME, values, selection, selectionArgs);
                break;
            case SINGLE_INDICATOR:
            	String segment = uri.getPathSegments().get(1);  
                if (TextUtils.isEmpty(selection)) {  
                	selection = "_id=" + segment;  
                } else {  
                	selection = "_id=" + segment + " AND (" + selection + ")";  
                }  
                count = db.update(Provider.ColumnsStep.TABLE_NAME, values, selection, null);
                break;
            default:
                throw new IllegalArgumentException("Unknow URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(Provider.ColumnsStep.CONTENT_URI, null);  
        
        DbMgr.getInstance(getContext()).closeDatabase();
        return count; 
	}
}
