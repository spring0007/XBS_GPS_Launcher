package com.sczn.wearlauncher.card.sport;

import java.util.Calendar;
import java.util.HashMap;

/**
 * 
 * @author mxy  保存今天的运动信息，用于界面展示
 *
 */
public class StepInfoToday {
	
	public static final String ACTION_CHANGED = "action.bdcast.sczn.todaystep.changed";
	
	public static final String SHARE_KEY_STEP_TODAY = "sczn_step_today";       // Inter
	public static final String SHARE_KEY_KCAL_TODAY = "sczn_kcal_today";      // String of double
	public static final String SHARE_KEY_DISTODAY = "sczn_dis_today";			// String of double

	private int mSteps;
	private double mKcal;
	private double mDis;
	private long mCurrDay;
	
	private HashMap<String, String> mStepDetails;  //存储每个小时的步数(0~23)
	
	public StepInfoToday() {
		// TODO Auto-generated constructor stub
		
		mSteps = 0;
		mKcal = 0.0;
		mDis = 0.0;	
		
		final Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MILLISECOND,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        mCurrDay = calendar.getTimeInMillis();
	}

	public long getmCurrDay() {
		return mCurrDay;
	}

	public int getmSteps() {
		return mSteps;
	}

	public void setmSteps(int mSteps) {
		this.mSteps = mSteps;
	}

	public double getmKcal() {
		return mKcal;
	}

	public void setmKcal(double mKcal) {
		this.mKcal = mKcal;
	}

	public double getmDis() {
		return mDis;
	}

	public void setmDis(double mDis) {
		this.mDis = mDis;
	}

	public HashMap<String, String> getmStepDetails() {
		if(mStepDetails == null){
			mStepDetails = new HashMap<String, String>();
		}
		return mStepDetails;
	}
	
	public void addStepInHour(int step, int hour){
		final String lastStepString = getmStepDetails().get(String.valueOf(hour));
		mStepDetails.put(String.valueOf(hour),
				String.valueOf(lastStepString == null ? step : (Integer.parseInt(lastStepString) + step)));
	}
	
	public int getStepInHour(int hour){
		final String stepString = getmStepDetails().get(String.valueOf(hour));
		return stepString == null ? 0 : Integer.parseInt(stepString);
	}
	
}
