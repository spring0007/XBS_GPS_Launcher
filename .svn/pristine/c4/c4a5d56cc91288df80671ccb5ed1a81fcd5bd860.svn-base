package com.sczn.wearlauncher.userinfo;

import java.math.BigDecimal;

import com.sczn.wearlauncher.R;

import android.content.Context;
import android.net.Uri;
import android.provider.Settings;

public class UserInfoUtil {
	public static final String SETTING_KEY_NAME = "user_name";
	public static final String SETTING_KEY_SEX = "user_sex";
	public static final String SETTING_KEY_HEIGHT = "user_height";
	public static final String SETTING_KEY_WEIGHT = "user_weight";
	public static final String SETTING_KEY_EDITTIME = "user_modify";
	
	public static final String  USER_STEP_TARGET = "suggest_steps";
    public static final String  USER_KCAL_TARGET = "suggest_kcal";
	
	public static final int SEX_MAN = 0;
	public static final int SEX_WOMAN = 1;
	
	public static final int DEFAULT_SEX = 0;
	public static final int DEFAULT_HEIGHT = 170;
	public static final int DEFAULT_WEIGHT = 60;
	public static final long DEFAULT_EDITTIME= 0;
	
	public static final int DEFAULT_STEP_TARGET = 5000;
	
	public static final double USER_ONE_STEP_DISTANCE_SMALL = 0.6; //单位m
    public static final double USER_ONE_STEP_DISTANCE_NORMAL = 0.7;
    public static final double USER_ONE_STEP_DISTANCE_BIG = 0.8;
	
	public static final int getStepTarget(Context context){
		return Settings.System.getInt(context.getContentResolver(), USER_STEP_TARGET, DEFAULT_STEP_TARGET);
	}
	
	public static final double getKcalTarget(Context context){
		return Settings.System.getFloat(context.getContentResolver(), USER_STEP_TARGET, 
				(float) (new BigDecimal(
			    		DEFAULT_STEP_TARGET * 0.7 * 0.001)
			    		.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue()* getWeightValue(context) * 1.036));
	}
	
	public static final int getHeightValue(Context context){
		return Settings.System.getInt(context.getContentResolver(), SETTING_KEY_HEIGHT, DEFAULT_HEIGHT);
	}
	
	public static final int getWeightValue(Context context){
		return Settings.System.getInt(context.getContentResolver(), SETTING_KEY_WEIGHT, DEFAULT_WEIGHT);
	}
	
	public static final String getName(Context context){
		final String name = Settings.System.getString(context.getContentResolver(), SETTING_KEY_NAME);
		if(name != null){
			return  name;
		}
		return  context.getString(R.string.userinfo_name_unsettting);
	}
	
	public static String getSex(Context context){
		final int sex = Settings.System.getInt(context.getContentResolver(), SETTING_KEY_SEX, DEFAULT_SEX);
		switch (sex) {
			case SEX_WOMAN:
				return context.getString(R.string.userinfo_sex_woman);
			default:
				return context.getString(R.string.userinfo_sex_man);
		}
	}
	
	public static String getHeight(Context context){
		return String.format(context.getString(R.string.userinfo_height_value), 
				Settings.System.getInt(context.getContentResolver(), SETTING_KEY_HEIGHT, DEFAULT_HEIGHT));
	}
	
	public static String getWeight(Context context){
		return String.format(context.getString(R.string.userinfo_weight_value), 
				Settings.System.getInt(context.getContentResolver(), SETTING_KEY_WEIGHT, DEFAULT_WEIGHT));
	}
	
	public static Uri getUriName(Context context){
		final Uri uri = Settings.System.getUriFor(UserInfoUtil.SETTING_KEY_NAME);
		if(uri == null){
			Settings.System.putString(context.getContentResolver(), SETTING_KEY_NAME, context.getString(R.string.userinfo_name_unsettting));
			return Settings.System.getUriFor(UserInfoUtil.SETTING_KEY_NAME);
		}
		return uri;
	}
	public static Uri getUriSex(Context context){
		final Uri uri = Settings.System.getUriFor(UserInfoUtil.SETTING_KEY_SEX);
		if(uri == null){
			Settings.System.putInt(context.getContentResolver(), SETTING_KEY_SEX, DEFAULT_SEX);
			return Settings.System.getUriFor(UserInfoUtil.SETTING_KEY_SEX);
		}
		return uri;
	}
	public static Uri getUriHeight(Context context){
		final Uri uri = Settings.System.getUriFor(UserInfoUtil.SETTING_KEY_HEIGHT);
		if(uri == null){
			Settings.System.putInt(context.getContentResolver(), SETTING_KEY_HEIGHT, DEFAULT_HEIGHT);
			return Settings.System.getUriFor(UserInfoUtil.SETTING_KEY_HEIGHT);
		}
		return uri;
	}
	public static Uri getUriWeight(Context context){
		final Uri uri = Settings.System.getUriFor(UserInfoUtil.SETTING_KEY_WEIGHT);
		if(uri == null){
			Settings.System.putInt(context.getContentResolver(), SETTING_KEY_WEIGHT, DEFAULT_WEIGHT);
			return Settings.System.getUriFor(UserInfoUtil.SETTING_KEY_WEIGHT);
		}
		return uri;
	}
	public static Uri getUriStepTarget(Context context){
		final Uri uri = Settings.System.getUriFor(UserInfoUtil.USER_STEP_TARGET);
		if(uri == null){
			Settings.System.putInt(context.getContentResolver(), USER_STEP_TARGET, DEFAULT_STEP_TARGET);
			return Settings.System.getUriFor(UserInfoUtil.USER_STEP_TARGET);
		}
		return uri;
	}
}
