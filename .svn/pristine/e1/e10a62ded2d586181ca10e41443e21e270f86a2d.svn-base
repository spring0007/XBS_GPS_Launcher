package com.sczn.wearlauncher.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mxy on 2017/1/7.
 */
public class Provider {
	
	public static final String AUTHORITY_COMMON = "com.sczn.wearlauncher.db";


    public static final class ColumnsStep implements BaseColumns{
        public static final String AUTHORITY = AUTHORITY_COMMON + "." + ProviderStep.class.getSimpleName();

        public static final String TABLE_NAME = "step";
        public static final String COLUMNS_GET_TIME = "get_time";
        public static final String COLUMNS_SETP_COUNT = "step_count";
        public static final String COLUMNS_DISTANCE = "distance";
        public static final String COLUMNS_KCAL = "kcal";
        public static final String COLUMNS_TIME_STRING = "timestring";
        public static final String DEFAULT_SORT_ORDER = COLUMNS_GET_TIME;

        public static final Uri CONTENT_URI = Uri.parse("content://"+ AUTHORITY +"/"+TABLE_NAME);
    }

    public static final class ColumnsSleep implements BaseColumns{
        public static final String AUTHORITY = AUTHORITY_COMMON + "." + ProviderSleep.class.getSimpleName();

        public static final String TABLE_NAME = "sleep";
        public static final String COLUMNS_START_TIME = "start_time";
        public static final String COLUMNS_END_TIME = "end_time";
        public static final String COLUMNS_SLEEP_STATE = "sleep_state";
        public static final String COLUMNS_SLEEP_STATE_STEP = "step";
        public static final String COLUMNS_TIME_STRING = "timestring";
        public static final String DEFAULT_SORT_ORDER = COLUMNS_START_TIME;

        public static final Uri CONTENT_URI = Uri.parse("content://"+ AUTHORITY +"/"+TABLE_NAME);
    }
    
    public static final class ColumnsHeartRate implements BaseColumns{
    	public static final String AUTHORITY = AUTHORITY_COMMON + "." + ProviderHealthAlarm.class.getSimpleName();
    	
    	public static final String TABLE_NAME = "heart_rate";
        public static final String COLUMNS_TIME = "start_time";
        public static final String COLUMNS_HEART_RATE = "heart_rate";
        public static final String DEFAULT_SORT_ORDER = COLUMNS_TIME;

        public static final Uri CONTENT_URI = Uri.parse("content://"+ AUTHORITY +"/"+TABLE_NAME); 
    }
    
    public static final class ColumnsAtmosphere implements BaseColumns{
    	public static final String AUTHORITY = AUTHORITY_COMMON + "." + ProviderAtmosphere.class.getSimpleName();
    	
    	public static final String TABLE_NAME = "atmosphere";
        public static final String COLUMNS_TIME = "time";
        public static final String COLUMNS_PRESSURE = "pressure";
        public static final String DEFAULT_SORT_ORDER = COLUMNS_TIME;

        public static final Uri CONTENT_URI = Uri.parse("content://"+ AUTHORITY +"/"+TABLE_NAME); 
    }
    
    public static final class ColumnsHealthAlarm implements BaseColumns{
    	public static final String AUTHORITY = AUTHORITY_COMMON + "." + ProviderHealthAlarm.class.getSimpleName();
    	
    	public static final String TABLE_NAME = "healthalarm";
        public static final String COLUMNS_TIME = "time";
        public static final String COLUMNS_TYPE = "type";
        public static final String COLUMNS_REPEAT = "repeat";
        public static final String COLUMNS_EBABLE = "enable";
        public static final String COLUMNS_TIME_STRING = "timestring";
        public static final String DEFAULT_SORT_ORDER = COLUMNS_TIME;

        public static final Uri CONTENT_URI = Uri.parse("content://"+ AUTHORITY +"/"+TABLE_NAME); 
    }
}
