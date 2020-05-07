package com.sczn.wearlauncher.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import java.util.HashMap;

import com.sczn.wearlauncher.base.util.StringUtils;
import com.sczn.wearlauncher.db.Provider.ColumnsSleep;

/**
 * Created by mxy on 2016/6/29.
 */
public class ProviderSleep extends ContentProvider {

    //判断Uri是否匹配
    private static final UriMatcher sUriMatcher;
    private static final int COLLECTION_INDICATOR = 1;
    private static final int SINGLE_INDICATOR = 2;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(Provider.ColumnsSleep.AUTHORITY, ColumnsSleep.TABLE_NAME, COLLECTION_INDICATOR);
        sUriMatcher.addURI(Provider.ColumnsSleep.AUTHORITY, ColumnsSleep.TABLE_NAME + "/#", SINGLE_INDICATOR);//更精确查询的标识#
    }
    //查询映射，抽象的字段映射到数据库中真实存在的字段
    private static HashMap<String,String> mSleepTimeProjectionMap;
    static {
        mSleepTimeProjectionMap = new HashMap<String,String >();
        mSleepTimeProjectionMap.put(Provider.ColumnsSleep._ID, Provider.ColumnsSleep._ID);
        mSleepTimeProjectionMap.put(Provider.ColumnsSleep.COLUMNS_START_TIME, Provider.ColumnsSleep.COLUMNS_START_TIME);
        mSleepTimeProjectionMap.put(Provider.ColumnsSleep.COLUMNS_END_TIME, Provider.ColumnsSleep.COLUMNS_END_TIME);
        mSleepTimeProjectionMap.put(Provider.ColumnsSleep.COLUMNS_SLEEP_STATE, Provider.ColumnsSleep.COLUMNS_SLEEP_STATE);
        mSleepTimeProjectionMap.put(Provider.ColumnsSleep.COLUMNS_SLEEP_STATE_STEP, Provider.ColumnsSleep.COLUMNS_SLEEP_STATE_STEP);
        mSleepTimeProjectionMap.put(Provider.ColumnsStep.COLUMNS_TIME_STRING,Provider.ColumnsStep.COLUMNS_TIME_STRING);
        mSleepTimeProjectionMap.put(Provider.ColumnsSleep.DEFAULT_SORT_ORDER, Provider.ColumnsSleep.DEFAULT_SORT_ORDER);
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
                qb.setTables(Provider.ColumnsSleep.TABLE_NAME);
                // 设置投影映射
                qb.setProjectionMap(mSleepTimeProjectionMap);
                break;
            case SINGLE_INDICATOR:
                qb.setTables(Provider.ColumnsSleep.TABLE_NAME);
                qb.setProjectionMap(mSleepTimeProjectionMap);
                qb.appendWhere(Provider.ColumnsSleep._ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknow URI: " + uri);
        }

        String orderBy;
        if(StringUtils.isEmpty(sortOrder))
        {
            orderBy = Provider.ColumnsSleep.DEFAULT_SORT_ORDER;
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
			throw new IllegalArgumentException( ProviderSleep.class.getSimpleName() +  "Cannot insert into URL: " + uri);
		}
		if(values == null){
			throw new IllegalArgumentException( ProviderSleep.class.getSimpleName() +  "values is null");
		}
   
        SQLiteDatabase db = DbMgr.getInstance(getContext()).openDatabase(); 

        long rowId = db.insert(ColumnsSleep.TABLE_NAME, null, values);  
  
        if (rowId < 0) {  
            return null;				// failed 
        }  
        Uri newUrl = ContentUris.withAppendedId(ColumnsSleep.CONTENT_URI, rowId);  
        getContext().getContentResolver().notifyChange(ColumnsSleep.CONTENT_URI, null);  
        
        DbMgr.getInstance(getContext()).closeDatabase();
        return newUrl;
	}

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
