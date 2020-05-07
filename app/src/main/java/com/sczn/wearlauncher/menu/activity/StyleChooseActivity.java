package com.sczn.wearlauncher.menu.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.base.AbsActivity;
import com.sczn.wearlauncher.base.absFragment;
import com.sczn.wearlauncher.base.adapter.LoopViewPageAdapter;
import com.sczn.wearlauncher.base.view.ViewPagerIndicator;
import com.sczn.wearlauncher.menu.fragment.MenuSquareFragment;
import com.sczn.wearlauncher.menu.fragment.MenuVirticalFragment;
import com.sczn.wearlauncher.menu.util.AppListUtil;
import com.sczn.wearlauncher.menu.view.MenuBgView;
import com.sczn.wearlauncher.menu.view.StyleChooseViewPager;
import com.sczn.wearlauncher.sp.SPKey;
import com.sczn.wearlauncher.sp.SPUtils;

import java.util.ArrayList;

/**
 * 更换样式
 */
public class StyleChooseActivity extends AbsActivity implements OnClickListener, StyleChooseViewPager.IStyleViewPagerSelected {
    private static final String TAG = StyleChooseActivity.class.getSimpleName();

    private final int ITEM_INDEX_VIRTICAL = 0;
    private final int ITEM_INDEX_GRIDVIEW = 1;
    private MenuSquareFragment mSquareFragment;
    private MenuVirticalFragment mVirticalFragment;

    private ArrayList<absFragment> mFragmentList = new ArrayList<>();
    private StyleChooseViewPager mStyleViewPager;
    private ViewPagerIndicator mStyleViewPageIndicator;
    private LoopViewPageAdapter mViewpageAdapter;
    private TextView mSkinChange;
    private TextView mBgChange;

    private MenuBgView mMenuBgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style_choose);
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMenuBgView.startFresh();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMenuBgView.stopFresh();
    }

    private void initView() {
        mStyleViewPager = findViewById(R.id.style_viewpager);
        mStyleViewPageIndicator = findViewById(R.id.style_choose_style_indicator);
        mSkinChange = findViewById(R.id.style_choose_skin_text);
        mBgChange = findViewById(R.id.style_choose_bg);
        mMenuBgView = findViewById(R.id.bg);

        mSkinChange.setOnClickListener(this);
        mBgChange.setOnClickListener(this);
        mStyleViewPager.setHorizalViewPagerSelected(this);
    }

    private void initViewPage() {
        mViewpageAdapter = new LoopViewPageAdapter(this.getFragmentManager(), false, mStyleViewPager);
        mStyleViewPager.setAdapter(mViewpageAdapter);
        mFragmentList.clear();
        mFragmentList.add(ITEM_INDEX_VIRTICAL, getmVirticalFragment());
        mFragmentList.add(ITEM_INDEX_GRIDVIEW, getmSquareFragment());
        mViewpageAdapter.setList(mFragmentList);
    }

    /**
     * 初始化页面
     */
    private void initData() {
        initViewPage();
        final int defaultIndex = (int) SPUtils.getParam(SPKey.APP_MENU_STYLE, 0);
        mStyleViewPageIndicator.init(mFragmentList.size(), ViewPagerIndicator.TYPE_STYLE_CHOOSE);
        mStyleViewPageIndicator.setSelect(defaultIndex);
        mStyleViewPager.setCurrentItem(defaultIndex);
    }

    /**
     * @return
     */
    public MenuSquareFragment getmSquareFragment() {
        mSquareFragment = findFragmentByIndex(ITEM_INDEX_GRIDVIEW);
        if (mSquareFragment == null) {
            mSquareFragment = MenuSquareFragment.newInstance(true, true);
        }
        return mSquareFragment;
    }

    /**
     * @return
     */
    public MenuVirticalFragment getmVirticalFragment() {
        mVirticalFragment = findFragmentByIndex(ITEM_INDEX_VIRTICAL);
        if (mVirticalFragment == null) {
            mVirticalFragment = MenuVirticalFragment.newInstance(true, true);
        }
        return mVirticalFragment;
    }

    @Override
    public void onStyleSelected(int index) {
        MxyLog.d(TAG, "style index = " + index);
        mStyleViewPageIndicator.setSelect(index);
        SPUtils.setParam(SPKey.APP_MENU_STYLE, index);
    }

    /**
     * @param index
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    private <T extends Fragment> T findFragmentByIndex(int index) {
        if (mStyleViewPager != null) {
            final Fragment fragment = getFragmentManager().findFragmentByTag(
                    LoopViewPageAdapter.makeFragmentName(mStyleViewPager.getId(), index));
            try {
                return (T) fragment;
            } catch (Exception e) {
                MxyLog.e(TAG, "findFragmentByIndex--e=" + e.toString());
                return null;
            }
        }
        return null;
    }

    /**
     * 切换不同的图标
     */
    private void switchMenuIconSkin() {
        String menuSkin = AppListUtil.getMenuIconSkin(StyleChooseActivity.this);
        if (AppListUtil.SKIN_PATH0.equals(menuSkin)) {
            AppListUtil.setMenuIconSkin(StyleChooseActivity.this, AppListUtil.SKIN_PATH1);
        } else {
            AppListUtil.setMenuIconSkin(StyleChooseActivity.this, AppListUtil.SKIN_PATH0);
        }
    }

    /**
     * 修改背景图片背景
     */
    private void switchBgStyle() {
        mMenuBgView.switchStyle();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.style_choose_skin_text:
                switchMenuIconSkin();
                break;
            case R.id.style_choose_bg:
                switchBgStyle();
                break;
            default:
                break;
        }
    }
}
