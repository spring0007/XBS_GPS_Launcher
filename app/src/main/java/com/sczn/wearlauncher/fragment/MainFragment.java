package com.sczn.wearlauncher.fragment;

import android.os.Bundle;
import android.view.View;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.absViewPagerFragment;
import com.sczn.wearlauncher.menu.MainApplistFragment;
import com.sczn.wearlauncher.notification.FragmentNtfMain;

public class MainFragment extends absViewPagerFragment {
    public static MainFragment newInstance(boolean isLoop) {
        MainFragment fragment = new MainFragment();
        Bundle bdl = new Bundle();
        bdl.putBoolean(ARG_VIEWPAGER_IS_LOOP, isLoop);
        fragment.setArguments(bdl);
        return fragment;
    }

    /**
     * item的position
     */
    public static final int ITEM_INDEX_NOTIFY = 0;
    public static final int ITEM_INDEX_HOME = 0;
    public static final int ITEM_INDEX_APP = 0;

    private FragmentNtfMain mNotificationFragment;
    private HomeFragment homeFragment;
    private MainApplistFragment mAppListFragment;

    public MainFragment() {
        super();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected int getLayoutResouceId() {
        return R.layout.fragment_clockmenu_contain;
    }

    @Override
    protected int getViewPagerId() {
        return R.id.viewpager;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void initFragmentList() {
        mFragmentList.clear();
        // mFragmentList.add(ITEM_INDEX_NOTIFY, getmNotificationFragment());
        // mFragmentList.add(ITEM_INDEX_HOME, getHomeFragment());
        mFragmentList.add(ITEM_INDEX_APP, getmAppListFragment());
    }

    @Override
    protected void startFreshData() {
        super.startFreshData();
    }

    @Override
    protected void endFreshData() {
        super.endFreshData();
    }

    /**
     * app列表页面
     *
     * @return
     */
    public MainApplistFragment getmAppListFragment() {
        mAppListFragment = findFragmentByIndex(ITEM_INDEX_APP);
        if (mAppListFragment == null) {
            mAppListFragment = MainApplistFragment.newInstance();
        }
        return mAppListFragment;
    }

    /**
     * 主页包含天气预报的页面
     *
     * @return
     */
    public HomeFragment getHomeFragment() {
        homeFragment = findFragmentByIndex(ITEM_INDEX_HOME);
        if (homeFragment == null) {
            homeFragment = HomeFragment.newInstance();
        }
        return homeFragment;
    }

    /**
     * 通知页面
     *
     * @return
     */
    public FragmentNtfMain getmNotificationFragment() {
        mNotificationFragment = findFragmentByIndex(ITEM_INDEX_NOTIFY);
        if (mNotificationFragment == null) {
            mNotificationFragment = FragmentNtfMain.newInstance();
        }
        return mNotificationFragment;
    }

    public void setDefaultPager() {
        if (mFragmentList != null && mHorizalViewPager != null && mHorizalViewPager.getCurrentItem() != getDefaultPagerCurrIndex()) {
            mHorizalViewPager.setCurrentItem(getDefaultPagerCurrIndex());
        }
    }


    @Override
    protected int getPagerCurrIndex() {
        if (mHorizalViewPager != null) {
            return mHorizalViewPager.getCurrentItem();
        }
        return getDefaultPagerCurrIndex();
    }

    @Override
    protected int getDefaultPagerCurrIndex() {
        return ITEM_INDEX_HOME;
    }
}
