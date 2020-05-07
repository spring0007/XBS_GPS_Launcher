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
import com.sczn.wearlauncher.db.Provider.ColumnsAtmosphere;

/**
 * Created by mxy on 2016/6/29.
 */
public class ProviderAtmosphere extends ContentProvider {

    //判断Uri是否匹配
    private static final UriMatcher sUriMatcher;
    private static final int COLLECTION_INDICATOR = 1;
    private static final int SINGLE_INDICATOR = 2;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(Provider.ColumnsAtmosphere.AUTHORITY, ColumnsAtmosphere.TABLE_NAME, COLLECTION_INDICATOR);
        sUriMatcher.addURI(Provider.ColumnsAtmosphere.AUTHORITY, ColumnsAtmosphere.TABLE_NAME + "/#", SINGLE_INDICATOR);//更精确查询的标识#
    }
    //查询映射，抽象的字段映射到数据库中真实存在的字段
    private static HashMap<String,String> mPressureProjectionMap;
    static {
        mPressureProjectionMap = new HashMap<String,String >();
        mPressureProjectionMap.put(Provider.ColumnsAtmosphere._ID, Provider.ColumnsAtmosphere._ID);
        mPressureProjectionMap.put(Provider.ColumnsAtmosphere.COLUMNS_TIME, Provider.ColumnsAtmosphere.COLUMNS_TIME);
        mPressureProjectionMap.put(Provider.ColumnsAtmosphere.COLUMNS_PRESSURE, Provider.ColumnsAtmosphere.COLUMNS_PRESSURE);
        mPressureProjectionMap.put(Provider.ColumnsAtmosphere.DEFAULT_SORT_ORDER, Provider.ColumnsAtmosphere.DEFAULT_SORT_ORDER);
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
                qb.setTables(Provider.ColumnsAtmosphere.TABLE_NAME);
                // 设置投影映射
                qb.setProjectionMap(mPressureProjectionMap);
                break;
            case SINGLE_INDICATOR:
                qb.setTables(Provider.ColumnsAtmosphere.TABLE_NAME);
                qb.setProjectionMap(mPressureProjectionMap);
                qb.appendWhere(Provider.ColumnsAtmosphere._ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknow URI: " + uri);
        }

        String orderBy;
        if(StringUtils.isEmpty(sortOrder))
        {
            orderBy = Provider.ColumnsAtmosphere.DEFAULT_SORT_ORDER;
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
    	if(sUriMatcher.match(uri) != COLLECTION_INDICATOR){
			throw new IllegalArgumentException( ProviderAtmosphere.class.getSimpleName() +  "Cannot insert into URL: " + uri);
		}
		if(values == null){
			throw new IllegalArgumentException( ProviderAtmosphere.class.getSimpleName() +  "values is null");
		}
   
        SQLiteDatabase db = DbMgr.getInstance(getContext()).openDatabase();  

        long rowId = db.insert(ColumnsAtmosphere.TABLE_NAME, null, values);  
  
        if (rowId < 0) {  
            return null;				// failed 
        }  
        Uri newUrl = ContentUris.withAppendedId(ColumnsAtmosphere.CONTENT_URI, rowId);  
        getContext().getContentResolver().notifyChange(ColumnsAtmosphere.CONTENT_URI, null);  
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
