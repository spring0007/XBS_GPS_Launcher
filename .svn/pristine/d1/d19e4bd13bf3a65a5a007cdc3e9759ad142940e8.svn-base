package com.sczn.wearlauncher;

import android.app.Fragment;
import android.os.Bundle;

import com.sczn.wearlauncher.base.AbsActivity;
import com.sczn.wearlauncher.base.absFragment;
import com.sczn.wearlauncher.base.adapter.LoopViewPageAdapter;
import com.sczn.wearlauncher.base.view.VerticalViewPager;
import com.sczn.wearlauncher.fragment.MainFragment;
import com.sczn.wearlauncher.menu.view.MenuBgView;

import java.util.ArrayList;


/**
 * 为了兼容固件,点击只有一个侧键修改,滑动表盘后进入切换进入的activity.
 */
public class ItemMenuActivity extends AbsActivity {

    private VerticalViewPager itemMenuViewPager;
    private LoopViewPageAdapter loopViewPageAdapter;
    private ArrayList<absFragment> mFragmentList = new ArrayList<>();

    private MainFragment mainFragment;

    private MenuBgView mainMenuBgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_menu);
        itemMenuViewPager = findViewById(R.id.item_menu_viewPager);
        mFragmentList.add(0, getMainFragment());
        loopViewPageAdapter = new LoopViewPageAdapter(getFragmentManager(), false, itemMenuViewPager);
        itemMenuViewPager.setAdapter(loopViewPageAdapter);
        loopViewPageAdapter.setList(mFragmentList);
        mainMenuBgView = findViewById(R.id.bg);
    }

    /**
     * @return
     */
    public MainFragment getMainFragment() {
        if (itemMenuViewPager != null) {
            final Fragment fragment = getFragmentManager().findFragmentByTag(
                    LoopViewPageAdapter.makeFragmentName(itemMenuViewPager.getId(), 0));
            if (fragment instanceof MainFragment) {
                mainFragment = (MainFragment) fragment;
            }
        }
        if (mainFragment == null) {
            mainFragment = new MainFragment();
        }
        return mainFragment;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mainMenuBgView.stopFresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainMenuBgView.startFresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
