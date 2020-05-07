package com.sczn.wearlauncher.base.util;


public class SensorMgrWrap {

    public static final int STATE_SLEEP = 1;
    public static final int STATE_STEP = 2;

    public static final int SLEEP_STATE_DEEP = 0;
    public static final int SLEEP_STATE_LIGHT = 1;
    public static final int SLEEP_STATE_NONE = 2;

    public static final String SETTING_KEY_LAST_STATE = "sczn_last_state";    //type int
    public static final String SETTING_KEY_STEP = "sczn_step";            //type:int
    public static final String SETTING_KEY_SLEEP = "sczn_sleep";        //type:long
    public static final String SETTING_KEY_SLEEP_STATE = "sczn_sleep_state";        //type:int
    public static final String SETTING_KEY_SLEEP_STATE_START = "sczn_sleep_start";    //type:long

    public static final long SLEEP_STATE_SEPARATOR = 30;

    public static final int INVALUED_VALUE = -1;
}
