package com.sczn.wearlauncher;


import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.sczn.wearlauncher.base.absViewPagerFragment;
import com.sczn.wearlauncher.base.util.DateFormatUtil;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.base.view.ViewPagerIndicator;
import com.sczn.wearlauncher.base.view.HorizalViewPager.IHorizalViewPagerSelected;
import com.sczn.wearlauncher.status.StatusFragmentFirst;
import com.sczn.wearlauncher.status.StatusFragmentSecond;
import com.sczn.wearlauncher.status.StatusFragmentThird;

public class ContainFragmentStatus extends absViewPagerFragment implements IHorizalViewPagerSelected{
	private final static String TAG = "StatusContainFragment";
	
	public static ContainFragmentStatus newInstance(boolean isLoop){
		ContainFragmentStatus fragment = new ContainFragmentStatus();
		Bundle bdl = new Bundle();
		bdl.putBoolean(ARG_VIEWPAGER_IS_LOOP, isLoop);
		fragment.setArguments(bdl);
		return fragment;
	}
	
	public static final long STATU_TIME_FRESH_GAP = 1000;	
	
	public static final int ITEM_INDEX_START = 0;
	private static int AUTO_ADD = 0;
	public static final int ITEM_INDEX_THIRD_TMP = ITEM_INDEX_START + AUTO_ADD++;
	public static final int ITEM_INDEX_FIRST = ITEM_INDEX_START + AUTO_ADD++;
	public static final int ITEM_INDEX_SECOND = ITEM_INDEX_START + AUTO_ADD++;
	public static final int ITEM_INDEX_THIRD = ITEM_INDEX_START + AUTO_ADD++;
	public static final int ITEM_INDEX_FIRST_TMP = ITEM_INDEX_START + AUTO_ADD++;
	public static final int ITEM_COUNT = ITEM_INDEX_START + AUTO_ADD;
	
	private StatusFragmentThird mThirdStatusFragmentTmp; 
	private StatusFragmentFirst mFirstStatusFragment;
	private StatusFragmentSecond mSecondStatusFragment;
	private StatusFragmentThird mThirdStatusFragment;
	private StatusFragmentFirst mFirstStatusFragmentTmp;
	
	
	private TextView mDateText;
	private TextView mWeekdayText;
	private TextView mTimetext;
	private ViewPagerIndicator mStatusViewPageIndicator;
	private Handler mHandler = new Handler();

	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_status_contain;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		
		mStatusViewPageIndicator = findViewById(R.id.status_page_ind);
		mDateText = findViewById(R.id.status_time_date);
		mWeekdayText = findViewById(R.id.status_time_weekday);
		mTimetext = findViewById(R.id.status_time_time);
		mHorizalViewPager.setHorizalViewPagerSelected(this);
		
		//MxyLog.d(TAG, "ITEM_INDEX_THIRD_TMP=" + ITEM_INDEX_THIRD_TMP + "--ITEM_INDEX_FIRST_TMP=" + ITEM_INDEX_FIRST_TMP + "--ITEM_COUNT=" + ITEM_COUNT);
	}
	
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		super.initData();
		mDateText.setText(DateFormatUtil.getCurrTimeString(DateFormatUtil.YYYY_MM_DD));
		mWeekdayText.setText(getString(DateFormatUtil.getCurrWeekDayRes()));
		mTimetext.setText(DateFormatUtil.getCurrTimeString(DateFormatUtil.HM));
		mStatusViewPageIndicator.init(mFragmentList.size() - 2,ViewPagerIndicator.TYPE_STATUS_CONTAIN);
		mStatusViewPageIndicator.setSelect(getLoopIndicator(getPagerCurrIndex()));
	}
	
	@Override
	protected int getViewPagerId() {
		// TODO Auto-generated method stub
		return R.id.viewpager;
	}

	@Override
	protected void initFragmentList() {
		// TODO Auto-generated method stub
		mFragmentList.clear();
		mFragmentList.add(ITEM_INDEX_THIRD_TMP, getmThirdStatusFragmentTmp());
		mFragmentList.add(ITEM_INDEX_FIRST, getmFirstStatusFragment());
		mFragmentList.add(ITEM_INDEX_SECOND, getmSecondStatusFragment());
		mFragmentList.add(ITEM_INDEX_THIRD, getmThirdStatusFragment());
		mFragmentList.add(ITEM_INDEX_FIRST_TMP, getmFirstStatusFragmentTmp());
	}

	public StatusFragmentFirst getmFirstStatusFragment() {
		mFirstStatusFragment = (StatusFragmentFirst) findFragmentByIndex(ITEM_INDEX_FIRST);
		if(mFirstStatusFragment == null){
			mFirstStatusFragment = new StatusFragmentFirst();
		}
		return mFirstStatusFragment;
	}

	public StatusFragmentSecond getmSecondStatusFragment() {
		mSecondStatusFragment = (StatusFragmentSecond) findFragmentByIndex(ITEM_INDEX_SECOND);
		if(mSecondStatusFragment == null){
			mSecondStatusFragment = new StatusFragmentSecond();
		}
		return mSecondStatusFragment;
	}

	public StatusFragmentThird getmThirdStatusFragment() {
		mThirdStatusFragment = (StatusFragmentThird) findFragmentByIndex(ITEM_INDEX_THIRD);
		if(mThirdStatusFragment == null){
			mThirdStatusFragment = new StatusFragmentThird();
		}
		return mThirdStatusFragment;
	}
	
	public StatusFragmentThird getmThirdStatusFragmentTmp() {
		mThirdStatusFragmentTmp = (StatusFragmentThird) findFragmentByIndex(ITEM_INDEX_THIRD_TMP);
		if(mThirdStatusFragmentTmp == null){
			mThirdStatusFragmentTmp = StatusFragmentThird.newInstance(true);
		}
		return mThirdStatusFragmentTmp;
	}

	public StatusFragmentFirst getmFirstStatusFragmentTmp() {
		mFirstStatusFragmentTmp = (StatusFragmentFirst) findFragmentByIndex(ITEM_INDEX_FIRST_TMP);
		if(mFirstStatusFragmentTmp == null){
			mFirstStatusFragmentTmp = StatusFragmentFirst.newInstance(true);
		}
		return mFirstStatusFragmentTmp;
	}

	@Override
	protected void startFreshData() {
		// TODO Auto-generated method stub
		super.startFreshData();
		
		mHandler.postDelayed(new Runnable(){   

		    public void run() {   
		    	mHandler.removeCallbacksAndMessages(null);
		    	//mStatusTime.setText(DateFormatUtil.getCurrTimeString(DateFormatUtil.HM));
		    	mDateText.setText(DateFormatUtil.getCurrTimeString(DateFormatUtil.YYYY_MM_DD));
				mWeekdayText.setText(getString(DateFormatUtil.getCurrWeekDayRes()));
				mTimetext.setText(DateFormatUtil.getCurrTimeString(DateFormatUtil.HM));
		    	mHandler.postDelayed(this,STATU_TIME_FRESH_GAP);
		    }   

		 }, 1000);
	}

	@Override
	protected void endFreshData() {
		// TODO Auto-generated method stub
		super.endFreshData();
		mHandler.removeCallbacksAndMessages(null);
	}

	@Override
	protected int getPagerCurrIndex() {
		// TODO Auto-generated method stub
		if(mHorizalViewPager != null){
			return mHorizalViewPager.getCurrentItem();
		}
		return getDefaultPagerCurrIndex();
	}

	@Override
	protected int getDefaultPagerCurrIndex() {
		// TODO Auto-generated method stub
		return ITEM_INDEX_SECOND;
	}
	
	private int getLoopIndicator(int position){

    	if(position == ITEM_COUNT - 1){
			return ITEM_INDEX_START;
		}else if(position == ITEM_INDEX_START){
			return ITEM_COUNT - 3;
		}else{
			return position - 1;
		}
    }

	@Override
	public void horizalViewPageSelected(int index) {
		// TODO Auto-generated method stub
		if(mFragmentList == null){
			return;
		}
		if(mStatusViewPageIndicator != null){
			MxyLog.i(TAG, "horizalViewPageSelected-" + index);
			mStatusViewPageIndicator.setSelect(getLoopIndicator(index));
		}
	}
	
	public void resetPagerCurrIndex(){
		if(mHorizalViewPager != null){
			mHorizalViewPager.setCurrentItem(getDefaultPagerCurrIndex(), false);
		}
	}
}
