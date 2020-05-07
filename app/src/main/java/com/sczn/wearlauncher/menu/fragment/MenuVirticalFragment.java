package com.sczn.wearlauncher.menu.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.menu.adapter.MenuVirticalAdapter;
import com.sczn.wearlauncher.menu.adapter.MenuVirticalAdapter.IMenuVerticalClickListen;
import com.sczn.wearlauncher.menu.view.MenuVirticalRecyleView;

public class MenuVirticalFragment extends absMenuFragment implements IMenuVerticalClickListen {
    private final static String TAG = MenuVirticalFragment.class.getSimpleName();

    public static MenuVirticalFragment newInstance(boolean isApp, boolean isStyle) {
        MenuVirticalFragment fragment = new MenuVirticalFragment();
        Bundle bdl = new Bundle();
        bdl.putBoolean(ARG_IS_APP, isApp);
        bdl.putBoolean(ARG_IS_STYLE, isStyle);
        fragment.setArguments(bdl);
        return fragment;
    }

    private MenuVirticalRecyleView menuVirticalRecyleView;
    private MenuVirticalAdapter mVirticalAdapter;

    @Override
    protected int getLayoutResouceId() {
        return R.layout.fragment_clockmenu_menu_virtical;
    }

    @Override
    protected void initView() {
        menuVirticalRecyleView = findViewById(R.id.menu_virtical_recyleview);

        mVirticalAdapter = new MenuVirticalAdapter(isStyle, isApp, false);
        if (!isStyle) {
            mVirticalAdapter.setMenuClickListem(this);
        }
        menuVirticalRecyleView.initLayoutManager(LinearLayoutManager.VERTICAL, false);
        menuVirticalRecyleView.setAdapter(mVirticalAdapter);
    }


    @Override
    protected void initData() {
        super.initData();
        if (isApp) {
            mVirticalAdapter.setMenus(mAppListUtil.getAppList());
        }
    }

    @Override
    protected void freshData() {
        MxyLog.d(TAG, "freshData" + "isApp=" + isApp + "--mAppListUtil.getAppList()=" + mAppListUtil.getAppList().size());
        if (isApp) {
            mVirticalAdapter.setMenus(mAppListUtil.getAppList());
        } else {
            //mVirticalAdapter.setMenus(SportListUtil.getSportList());
        }
    }

    @Override
    public void onVerticalMenuClick(View view) {
        doMenuClick(view);
    }

}
