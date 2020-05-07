package com.sczn.wearlauncher.menu.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.base.absDialogFragment;
import com.sczn.wearlauncher.base.util.MxyToast;
import com.sczn.wearlauncher.base.util.SysServices;
import com.sczn.wearlauncher.base.view.PagerRecylerView;
import com.sczn.wearlauncher.btconnect.BtStyleChooseActivity;
import com.sczn.wearlauncher.card.CardFragmentHeartRate;
import com.sczn.wearlauncher.card.CardFragmentSleep;
import com.sczn.wearlauncher.card.CardFragmentStep;
import com.sczn.wearlauncher.card.CardFragmentWeather;
import com.sczn.wearlauncher.menu.adapter.MenuSquareAdapter;
import com.sczn.wearlauncher.menu.adapter.MenuSquareAdapter.OnSquareMenuClickListen;
import com.sczn.wearlauncher.menu.bean.AppMenu;
import com.sczn.wearlauncher.menu.util.AppListUtil;

import java.util.ArrayList;
import java.util.List;

public class ApplistFragment extends absDialogFragment implements OnSquareMenuClickListen {

    private final static String TAG = MenuSquareFragment.class.getSimpleName();

    public static ApplistFragment newInstance(AppMenu mmenu) {
        ApplistFragment fragment = new ApplistFragment();
        Bundle bdl = new Bundle();
        bdl.putParcelable("menu", (Parcelable) mmenu);
        fragment.setArguments(bdl);
        return fragment;
    }

    private PagerRecylerView mMenuRecyclerView;
    private MenuSquareAdapter menuSquareAdapter;
    private ArrayList<AppMenu> dataList;

    @Override
    protected int getLayoutResouceId() {
        return R.layout.fragment_clockmenu_menu_square;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bdl = getArguments();
        dataList = new ArrayList<AppMenu>();
        if (bdl != null) {
            AppMenu mParentMenu = bdl.getParcelable("menu");
            if (mParentMenu != null) {
                dataList.addAll(mParentMenu.getChildrenList());
            }
        }
    }


    @Override
    protected void creatView() {
        mMenuRecyclerView = findViewById(R.id.menu_square);
        mMenuRecyclerView.initLayoutManager(LinearLayoutManager.VERTICAL, false);
        mMenuRecyclerView.setFlingVelocity(PagerRecylerView.FLING_DIEABLE);
        menuSquareAdapter = new MenuSquareAdapter(getActivity(), this, false);
        mMenuRecyclerView.setAdapter(menuSquareAdapter);

        menuSquareAdapter.setMenuList(dataList);
    }


    protected void doMenuClick(View view) {
        if (!(view.getTag() instanceof AppMenu)) {
            return;
        }
        doMenuClick((AppMenu) view.getTag());
    }

    protected void doMenuClick(AppMenu menu) {
        MxyLog.e("rongqiang", "menu" + menu.getMenuType());
        try {
            if (AppMenu.MENU_TYPE_MORE == menu.getMenuType()) {
                final AppMenu moreMenu = AppListUtil.getInctance().getMoreMenu();
                if (moreMenu != null) {
                    showChildren(moreMenu);
                } else {
                    MxyLog.e(TAG, "AppListUtil.getInctance().getMoreMenu() = null");
                }
                return;
            } else if (AppMenu.MENU_TYPE_ANDROID_ASSIST == menu.getMenuType()) {
                SysServices.setSystemSettingInt(getActivity(), BtStyleChooseActivity.SETTING_KEY_BT_STYLE,
                        BtStyleChooseActivity.BT_STYLE_ANDROID);
                final AppMenu androidAssist = AppListUtil.getInctance().getAssistantMenu();
                if (androidAssist != null) {
                    androidAssist.setFiltChildren(AppListUtil.getInctance().getAndroidAssistFilter());
                    showChildren(androidAssist);
                } else {
                    MxyLog.e(TAG, "AppListUtil.getInctance().getAssistantMenu() = null");
                }
                return;
            } else if (AppMenu.MENU_TYPE_IOS_ASSIST == menu.getMenuType()) {
                SysServices.setSystemSettingInt(getActivity(), BtStyleChooseActivity.SETTING_KEY_BT_STYLE,
                        BtStyleChooseActivity.BT_STYLE_IOS);
                final AppMenu iosAssist = AppListUtil.getInctance().getAssistantMenu();
                if (iosAssist != null) {
                    iosAssist.setFiltChildren(AppListUtil.getInctance().getIosAssistFilter());
                    showChildren(iosAssist);
                } else {
                    MxyLog.e(TAG, "AppListUtil.getInctance().getAssistantMenu() = null");
                }
                return;
            } else if (AppMenu.MENU_TYPE_FRAGMENT == menu.getMenuType()) {

                showfragment(menu.getClassName());

                return;
            }
            if (menu.getChildrenList() != null && menu.getChildrenList().size() > 0) {
                showChildren(menu);
                return;
            }

            if (menu.getAction() != null) {
                Intent i = new Intent(menu.getAction());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            } else if (menu.getInfo() != null) {
                Intent i = new Intent();
                i.setClassName(menu.getInfo().activityInfo.packageName,
                        menu.getInfo().activityInfo.name);
                //MxyLog.d(TAG, "menu.getInfo().activityInfo.packageName=" + menu.getInfo().activityInfo.packageName);
                //MxyLog.d(TAG, "menu.getIntentData()= " + menu.getIntentData());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            } else {

                ResolveInfo info = SysServices.getPkMgr(getActivity()).
                        resolveActivity(new Intent(menu.getClassName()), PackageManager.GET_INTENT_FILTERS);
                //MxyLog.d(TAG, "infoAction=" + info);
                if (info == null) {
                    Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
                    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    final List<ResolveInfo> appList = SysServices.getPkMgr(getActivity()).queryIntentActivities(mainIntent, 0);
                    // MxyLog.d(TAG, "initActivityMap--start");
                    for (ResolveInfo infoActivity : appList) {
                        if (menu.getClassName().equals(infoActivity.activityInfo.name)) {
                            info = infoActivity;
                            break;
                        }
                    }
                }
                Intent i = new Intent();
                i.setClassName(info.activityInfo.packageName,
                        info.activityInfo.name);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (AppMenu.RES_INVALUED != menu.getIntentData()) {
                    i.putExtra("model", menu.getIntentData());
                }
                startActivity(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
            MxyToast.showShort(getActivity(), getString(R.string.error_title));
        }

    }

    public static final String FRAGMENT_TAG_CHILDREN = "fragment_menu_children";
    protected absDialogFragment mChildrenFragment;

    private void showChildren(AppMenu parentMenu) {
        final Fragment fragment = getChildFragmentManager().findFragmentByTag(FRAGMENT_TAG_CHILDREN);
        if (fragment != null) {
            //absDialogFragment childfragment = (absDialogFragment) fragment;
            //childfragmenr.dismissAllowingStateLoss();
        } else {
            if (AppMenu.MENU_TYPE_MORE == parentMenu.getMenuType() || parentMenu.getChildrenList().size() > 2) {
                mChildrenFragment = MenuChildrenMoreFragment.newInstance(parentMenu);
            } else {
                mChildrenFragment = MenuChildrenTwoFragment.newInstance(parentMenu);
            }
            mChildrenFragment.show(getChildFragmentManager(), FRAGMENT_TAG_CHILDREN);
        }
    }

    private void showfragment(String classname) {
        final Fragment fragment = getChildFragmentManager().findFragmentByTag(classname);
        if (fragment != null) {
            //absDialogFragment childfragment = (absDialogFragment) fragment;
            //childfragmenr.dismissAllowingStateLoss();
        } else {
            if (classname.equals("fragment_Step")) {
                mChildrenFragment = CardFragmentStep.newInstance();

            } else if (classname.equals("fragment_Sleep")) {
                mChildrenFragment = CardFragmentSleep.newInstance();

            } else if (classname.equals("fragment_HeartRate")) {
                mChildrenFragment = CardFragmentHeartRate.newInstance();

            } else if (classname.equals("fragment_Weather")) {
                mChildrenFragment = CardFragmentWeather.newInstance();

            }

            mChildrenFragment.show(getChildFragmentManager(), FRAGMENT_TAG_CHILDREN);
        }
    }

    @Override
    public void onSquareMenuClick(View view) {
        doMenuClick(view);
    }

    @Override
    protected void destorytView() {
    }
}