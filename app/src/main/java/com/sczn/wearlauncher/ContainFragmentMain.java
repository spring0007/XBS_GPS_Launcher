package com.sczn.wearlauncher;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.sczn.wearlauncher.base.absFragment;
import com.sczn.wearlauncher.base.adapter.LoopViewPageAdapter;
import com.sczn.wearlauncher.base.view.VerticalViewPager;
import com.sczn.wearlauncher.base.view.VerticalViewPager.VerticalScrollerAble;
import com.sczn.wearlauncher.fragment.HomeFragment;

import java.util.ArrayList;

public class ContainFragmentMain extends absFragment implements VerticalScrollerAble {

    private VerticalViewPager mMainViewpager;
    private ArrayList<absFragment> mFragmentList = new ArrayList<absFragment>();
    private HomeFragment mHomeFragment;

    private LoopViewPageAdapter mViewPageAdapter;
    private OnItemChangeLitener mOnItemChangeLitener;

    public interface OnItemChangeLitener {
        void onItemchange(int position);
    }

    public void setOnItemChangeLitener(OnItemChangeLitener onItemChangeLitener) {
        this.mOnItemChangeLitener = onItemChangeLitener;
    }

    public static ContainFragmentMain newInstance() {
        return new ContainFragmentMain();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResouceId() {
        return R.layout.fragment_contain_main;
    }

    @Override
    protected void initView() {
        mMainViewpager = (VerticalViewPager) findViewById(R.id.vertical_viewpager);

    }

    @Override
    protected void initData() {
        mFragmentList.clear();

        // mFragmentList.add(0, getmSecondStatusFragment());
        mFragmentList.add(0, getmHomeFragment());

        mViewPageAdapter = new LoopViewPageAdapter(getFragmentManager(), false, mMainViewpager);
        mMainViewpager.setAdapter(mViewPageAdapter);
        mViewPageAdapter.setList(mFragmentList);
        // mMainViewpager.setCurrentItem(1);
        mMainViewpager.setVerticalScrollerAble(this);
        mMainViewpager.setOnPageChangeListener(PagerPageChangeListener);
    }

    private OnPageChangeListener PagerPageChangeListener = new OnPageChangeListener() {

        @Override
        public void onPageSelected(int arg0) {
            if (arg0 == 1) {
                Settings.System.putInt(getActivity().getContentResolver(), "isHome", 1);
            } else {
                Settings.System.putInt(getActivity().getContentResolver(), "isHome", 0);
            }
            if (mOnItemChangeLitener != null) {
                mOnItemChangeLitener.onItemchange(arg0);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    @Override
    protected void startFreshData() {
        if (mMainViewpager != null && mMainViewpager.getCurrentItem() == 1) {
            Settings.System.putInt(getActivity().getContentResolver(), "isHome", 1);
        }

        super.startFreshData();
        mViewPageAdapter.notifyDataSetChanged();
    }

    @Override
    protected void endFreshData() {
        Settings.System.putInt(getActivity().getContentResolver(), "isHome", 0);
        super.endFreshData();

    }

    public HomeFragment getmHomeFragment() {
        if (mHomeFragment == null) {
            mHomeFragment = new HomeFragment();
        }
        return mHomeFragment;
    }

    /**
     * 电池和信号和蓝牙和WiFi展示的页面
     *
     * @return
     */
    // public StatusFragmentSecond getmSecondStatusFragment() {
    //     if (mMainViewpager != null) {
    //         final Fragment fragment = getFragmentManager().findFragmentByTag(
    //                 LoopViewPageAdapter.makeFragmentName(mMainViewpager.getId(), 1));
    //         if (fragment instanceof MainFragment) {
    //             mSecondStatusFragment = (StatusFragmentSecond) fragment;
    //         }
    //     }
    //     if (mSecondStatusFragment == null) {
    //         mSecondStatusFragment = new StatusFragmentSecond();
    //     }
    //     return mSecondStatusFragment;
    // }
    @Override
    public boolean isVerticalScrollerAble() {
        return true;
    }

    public boolean canScrollerHorizal() {
        if (mMainViewpager == null) {
            return false;
        }
        return 1 == mMainViewpager.getCurrentItem();
    }

    public void setDefaultPager() {
        if (mFragmentList != null && mMainViewpager != null && mMainViewpager.getCurrentItem() != 1) {
            mMainViewpager.setCurrentItem(1);
        }
    }
}
