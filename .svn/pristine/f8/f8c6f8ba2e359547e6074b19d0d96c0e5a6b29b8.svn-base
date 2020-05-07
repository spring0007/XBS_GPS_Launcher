package com.sczn.wearlauncher;


import android.os.Bundle;

import com.sczn.wearlauncher.base.absFragment;
import com.sczn.wearlauncher.base.absViewPagerFragment;
import com.sczn.wearlauncher.base.view.HorizalViewPager.IHorizalViewPagerSelected;
import com.sczn.wearlauncher.base.view.ViewPagerIndicator;
import com.sczn.wearlauncher.card.CardFragmentCompass;
import com.sczn.wearlauncher.card.CardFragmentGeographic;
import com.sczn.wearlauncher.card.CardFragmentHealthAlam;
import com.sczn.wearlauncher.card.CardFragmentHeartRate;
import com.sczn.wearlauncher.card.CardFragmentSleep;
import com.sczn.wearlauncher.card.CardFragmentStep;
import com.sczn.wearlauncher.card.CardFragmentWeather;

public class ContainFragmentCard extends absViewPagerFragment implements IHorizalViewPagerSelected {
    private final static String TAG = "CardContainFragment";

    public static ContainFragmentCard newInstance(boolean isLoop) {
        ContainFragmentCard fragment = new ContainFragmentCard();
        Bundle bdl = new Bundle();
        bdl.putBoolean(ARG_VIEWPAGER_IS_LOOP, isLoop);
        fragment.setArguments(bdl);
        return fragment;
    }

    public static final int ITEM_INDEX_START = 0;
    private static int AUTO_ADD = 0;
    public static final int ITEM_INDEX_LAST_TMP = ITEM_INDEX_START + AUTO_ADD++;


    public static final int ITEM_INDEX_SLEEP = ITEM_INDEX_START + AUTO_ADD++;
    public static final int ITEM_INDEX_HEALTHALARM = ITEM_INDEX_START + AUTO_ADD++;
    public static final int ITEM_INDEX_HEALTH = ITEM_INDEX_START + AUTO_ADD++;
    public static final int ITEM_INDEX_STEP = ITEM_INDEX_START + AUTO_ADD++;
    public static final int ITEM_INDEX_WEATHER = ITEM_INDEX_START + AUTO_ADD++;
    public static final int ITEM_INDEX_GEOGRAPHIC = ITEM_INDEX_START + AUTO_ADD++;
    public static final int ITEM_INDEX_COMPASS = ITEM_INDEX_START + AUTO_ADD;

    public static final int ITEM_INDEX_FIRST_TMP = ITEM_INDEX_START + AUTO_ADD++;
    public static final int ITEM_COUNT = ITEM_INDEX_START + AUTO_ADD;

    private absFragment mLastFragmentTmp;
    private CardFragmentHeartRate mHealthFragment;
    private CardFragmentSleep mSleepFragment;
    private CardFragmentHealthAlam mHealthAlamFragment;
    private CardFragmentStep mStepFragment;
    private CardFragmentWeather mWeatherFragment;
    private CardFragmentGeographic mGeographicFragment;
    private CardFragmentCompass mCompassFragment;
    private absFragment mFirstFramentTmp;

    private ViewPagerIndicator mCardViewPageIndicator;

    @Override
    protected int getLayoutResouceId() {
        // TODO Auto-generated method stub
        return R.layout.fragment_card_contain;
    }

    @Override
    protected void initView() {
        // TODO Auto-generated method stub
        super.initView();
        mCardViewPageIndicator = findViewById(R.id.card_page_ind);

        mHorizalViewPager.setHorizalViewPagerSelected(this);
    }

    @Override
    protected void initData() {
        // TODO Auto-generated method stub
        super.initData();
        mCardViewPageIndicator.init(mFragmentList.size() - 2, ViewPagerIndicator.TYPE_CARD_CONTAIN);
        mCardViewPageIndicator.setSelect(getLoopIndicator(getPagerCurrIndex()));
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

		/*mFragmentList.add(ITEM_INDEX_LAST_TMP, getmLastFragmentTmp());
		
		
		mFragmentList.add(ITEM_INDEX_SLEEP, getmSleepFragment());
		mFragmentList.add(ITEM_INDEX_HEALTHALARM, getmHealthAlamFragment());
		mFragmentList.add(ITEM_INDEX_HEALTH, getmHealthFragment());
	//	mFragmentList.add(ITEM_INDEX_STEP, getmStepFragment());
		mFragmentList.add(ITEM_INDEX_WEATHER, getmWeatherFragment());
		mFragmentList.add(ITEM_INDEX_GEOGRAPHIC, getmGeographicFragment());
		//mFragmentList.add(ITEM_INDEX_COMPASS, getmCompassFragment());
		
		mFragmentList.add(ITEM_INDEX_FIRST_TMP, getmFirstFramentTmp());*/
    }


    public CardFragmentGeographic getmGeographicFragment() {
        mGeographicFragment = (CardFragmentGeographic) findFragmentByIndex(ITEM_INDEX_GEOGRAPHIC);
        if (mGeographicFragment == null) {
            mGeographicFragment = CardFragmentGeographic.newInstance(false);
        }
        return mGeographicFragment;
    }

    public CardFragmentCompass getmCompassFragment() {
        mCompassFragment = (CardFragmentCompass) findFragmentByIndex(ITEM_INDEX_COMPASS);
        if (mCompassFragment == null) {
            mCompassFragment = CardFragmentCompass.newInstance(false);
        }
        return mCompassFragment;
    }

    public absFragment getmLastFragmentTmp() {
        mLastFragmentTmp = (absFragment) findFragmentByIndex(ITEM_INDEX_LAST_TMP);
        if (mLastFragmentTmp == null) {
            mLastFragmentTmp = CardFragmentGeographic.newInstance(true);
        }
        return mLastFragmentTmp;
    }


    @Override
    protected int getPagerCurrIndex() {
        // TODO Auto-generated method stub
        if (mHorizalViewPager != null) {
            return mHorizalViewPager.getCurrentItem();
        }
        return getDefaultPagerCurrIndex();
    }

    @Override
    protected int getDefaultPagerCurrIndex() {
        // TODO Auto-generated method stub
        return ITEM_INDEX_STEP;
    }

    private int getLoopIndicator(int position) {

        if (position == ITEM_COUNT - 1) {
            return ITEM_INDEX_START;
        } else if (position == ITEM_INDEX_START) {
            return ITEM_COUNT - 3;
        } else {
            return position - 1;
        }
    }

    @Override
    public void horizalViewPageSelected(int index) {
        // TODO Auto-generated method stub
        if (mFragmentList == null) {
            return;
        }
        if (mCardViewPageIndicator != null) {
            mCardViewPageIndicator.setSelect(getLoopIndicator(index));
        }
    }

    public void resetPagerCurrIndex() {
        if (mHorizalViewPager != null) {
            mHorizalViewPager.setCurrentItem(getDefaultPagerCurrIndex(), false);
        }
    }
}
