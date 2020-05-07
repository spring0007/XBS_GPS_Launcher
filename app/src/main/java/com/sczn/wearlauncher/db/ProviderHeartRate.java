package com.sczn.wearlauncher.db;

import java.util.HashMap;

import com.sczn.wearlauncher.base.util.StringUtils;
import com.sczn.wearlauncher.db.Provider.ColumnsHeartRate;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class ProviderHeartRate extends ContentProvider{

    //判断Uri是否匹配
    private static final UriMatcher sUriMatcher;
    private static final int COLLECTION_INDICATOR = 1;
    private static final int SINGLE_INDICATOR = 2;
    
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(Provider.ColumnsHeartRate.AUTHORITY, ColumnsHeartRate.TABLE_NAME, COLLECTION_INDICATOR);
        sUriMatcher.addURI(Provider.ColumnsHeartRate.AUTHORITY, ColumnsHeartRate.TABLE_NAME + "/#", SINGLE_INDICATOR);//更精确查询的标识#
    }
    //查询映射，抽象的字段映射到数据库中真实存在的字段
    private static HashMap<String,String> mHeartRateProjectionMap;
    static {
        mHeartRateProjectionMap = new HashMap<String,String >();
        mHeartRateProjectionMap.put(Provider.ColumnsHeartRate._ID,Provider.ColumnsHeartRate._ID);
        mHeartRateProjectionMap.put(Provider.ColumnsHeartRate.COLUMNS_TIME,Provider.ColumnsHeartRate.COLUMNS_TIME);
        mHeartRateProjectionMap.put(Provider.ColumnsHeartRate.COLUMNS_HEART_RATE,Provider.ColumnsHeartRate.COLUMNS_HEART_RATE);
        mHeartRateProjectionMap.put(Provider.ColumnsHeartRate.DEFAULT_SORT_ORDER,Provider.ColumnsHeartRate.DEFAULT_SORT_ORDER);
    }

    @Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
        return false;
	}
    
    @Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)){
            case COLLECTION_INDICATOR:
                // 设置查询的表
                qb.setTables(Provider.ColumnsHeartRate.TABLE_NAME);
                // 设置投影映射
                qb.setProjectionMap(mHeartRateProjectionMap);
                break;
            case SINGLE_INDICATOR:
                qb.setTables(Provider.ColumnsHeartRate.TABLE_NAME);
                qb.setProjectionMap(mHeartRateProjectionMap);
                qb.appendWhere(Provider.ColumnsHeartRate._ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknow URI: " + uri);
        }

        String orderBy;
        if(StringUtils.isEmpty(sortOrder))
        {
            orderBy = Provider.ColumnsHeartRate.DEFAULT_SORT_ORDER;
        } else {
            orderBy = sortOrder;
        }

        SQLiteDatabase db = DbMgr.getInstance(getContext()).openDatabase();
        Cursor c = qb.query(db,projection,selection,null,null,null,orderBy);

        return c;
	}
    
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		if(sUriMatcher.match(uri) != COLLECTION_INDICATOR){
			throw new IllegalArgumentException( ProviderHeartRate.class.getSimpleName() +  "Cannot insert into URL: " + uri);
		}
		if(values == null){
			throw new IllegalArgumentException( ProviderHeartRate.class.getSimpleName() +  "values is null");
		}
   
        SQLiteDatabase db = DbMgr.getInstance(getContext()).openDatabase();  

        long rowId = db.insert(ColumnsHeartRate.TABLE_NAME, null, values);  
  
        if (rowId < 0) {  
            return null;				// failed 
        }  
        Uri newUrl = ContentUris.withAppendedId(ColumnsHeartRate.CONTENT_URI, rowId);  
        getContext().getContentResolver().notifyChange(ColumnsHeartRate.CONTENT_URI, null); 
        
        DbMgr.getInstance(getContext()).closeDatabase();
        return newUrl;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}
}
