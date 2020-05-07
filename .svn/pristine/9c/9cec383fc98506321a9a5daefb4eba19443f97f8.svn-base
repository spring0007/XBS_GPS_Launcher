package com.sczn.wearlauncher.menu;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.base.absDialogFragment;
import com.sczn.wearlauncher.base.absFragment;
import com.sczn.wearlauncher.base.util.MxyToast;
import com.sczn.wearlauncher.base.util.SysServices;
import com.sczn.wearlauncher.base.view.HorizalViewPager;
import com.sczn.wearlauncher.base.view.MyRecyclerView;
import com.sczn.wearlauncher.base.view.VerticalViewPager.VerticalScrollerAble;
import com.sczn.wearlauncher.base.view.ViewPagerIndicator;
import com.sczn.wearlauncher.btconnect.BtStyleChooseActivity;
import com.sczn.wearlauncher.card.CardFragmentHeartRate;
import com.sczn.wearlauncher.card.CardFragmentSleep;
import com.sczn.wearlauncher.card.CardFragmentStep;
import com.sczn.wearlauncher.card.CardFragmentWeather;
import com.sczn.wearlauncher.menu.adapter.MainApplistFragmentAdapter;
import com.sczn.wearlauncher.menu.adapter.ViewPagerAdapter;
import com.sczn.wearlauncher.menu.bean.AppMenu;
import com.sczn.wearlauncher.menu.fragment.ApplistFragment;
import com.sczn.wearlauncher.menu.fragment.absMenuFragment;
import com.sczn.wearlauncher.menu.util.AppListUtil;
import com.sczn.wearlauncher.menu.util.MenuUtil;
import com.sczn.wearlauncher.sp.SPKey;
import com.sczn.wearlauncher.sp.SPUtils;
import com.sczn.wearlauncher.util.ThreadUtil;

import java.util.ArrayList;
import java.util.List;


public class MainApplistFragment extends absFragment implements VerticalScrollerAble, OnClickListener, ViewPager.OnPageChangeListener {
    private static final String TAG = absMenuFragment.class.getSimpleName();

    private HorizalViewPager mMainViewpager;

    //横向布局
    private ViewPagerIndicator horizalViewpagerIndicator;
    private ViewPagerAdapter adapter;

    //网格布局
    private MainApplistFragmentAdapter fragmentAdapter;

    private ArrayList<AppMenu> menuList;
    private ArrayList<View> vlist;

    private int layoutStyle;//图标布局
    private String iconStyle;//图标样式

    // fragment相关
    protected absDialogFragment mChildrenFragment;
    public static final String FRAGMENT_TAG_CHILDREN = "fragment_menu_children";

    public static MainApplistFragment newInstance() {
        return new MainApplistFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResouceId() {
        return R.layout.fragment_app_list;
    }

    @Override
    protected void initView() {
        mMainViewpager = findViewById(R.id.horizal_viewpager);
        horizalViewpagerIndicator = findViewById(R.id.horizal_viewpager_indicator);
        mMainViewpager.addOnPageChangeListener(this);
        menuList = AppListUtil.getInctance().getAppList();
    }

    @Override
    protected void initData() {
        vlist = new ArrayList<>();
        adapter = new ViewPagerAdapter(vlist);
        mMainViewpager.setAdapter(adapter);
        initMenu();
    }

    @Override
    protected void startFreshData() {
        super.startFreshData();
        //比较两次加载的参数不一样,才需要重新布局,减少重复布局的次数
        if (layoutStyle != (int) SPUtils.getParam(SPKey.APP_MENU_STYLE, 0)) {
            initMenu();
            return;
        }
        //比较两次加载的参数不一样,才需要重新布局,减少重复布局的次数
        if (iconStyle != null && !iconStyle.equals(AppListUtil.getMenuIconSkin(getActivity()))) {
            initMenu();
            return;
        }
        /**
         * 一般第一次加载的时候,才会调用
         * onResume的时候会去重新加载图标的皮肤,修改更换图标之后,没有及时更新的问题
         * 需要判空才重新布局,减少重复布局的次数
         */
        if (menuList == null || menuList.size() == 0) {
            initMenu();
        }
    }

    /**
     * 初始化图标样式
     */
    private void initMenu() {
        layoutStyle = (int) SPUtils.getParam(SPKey.APP_MENU_STYLE, 0);
        iconStyle = AppListUtil.getMenuIconSkin(getActivity());
        if (layoutStyle == 0) {
            initMenuLayoutStyleOne();
        } else {
            initMenuLayoutStyleTwo();
        }
    }

    /**
     * 横向viewpager的布局方式
     */
    private void initMenuLayoutStyleOne() {
        // ViewPager的小圆点
        // horizalViewpagerIndicator.init(menuList.size(), ViewPagerIndicator.TYPE_STYLE_CHOOSE);
        // horizalViewpagerIndicator.setSelect(0);
        ThreadUtil.getPool().execute(new Runnable() {
            @Override
            public void run() {
                menuList = AppListUtil.getInctance().getAppList();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        vlist.clear();
                        for (int x = 0; x < menuList.size(); x++) {
                            AppMenu mAppMenu = menuList.get(x);
                            View view = LayoutInflater.from(mActivity).inflate(R.layout.viewpager_item, null);

                            TextView mTextView = view.findViewById(R.id.view_title);
                            ImageView mimage = view.findViewById(R.id.view_image);
                            mTextView.setText(MenuUtil.getName(mActivity, menuList, x));
                            mimage.setImageDrawable(MenuUtil.getIcon(mActivity, menuList, x, false));
                            mimage.setTag(mAppMenu);
                            mimage.setOnClickListener(MainApplistFragment.this);
                            vlist.add(view);
                        }
                        adapter.notifyData(vlist);

                        // ViewPager的小圆点
                        horizalViewpagerIndicator.setVisibility(View.VISIBLE);
                        horizalViewpagerIndicator.init(menuList.size(), ViewPagerIndicator.TYPE_STYLE_CHOOSE);
                        horizalViewpagerIndicator.setSelect(horizalViewpagerIndicator.getSelect());
                    }
                });
            }
        });
    }

    /**
     * GridView的布局方式
     */
    private void initMenuLayoutStyleTwo() {
        horizalViewpagerIndicator.setVisibility(View.GONE);
        vlist.clear();
        View view = LayoutInflater.from(mActivity).inflate(R.layout.layout_recycleview, null);
        vlist.add(view);
        adapter.notifyData(vlist);

        MyRecyclerView recyclerView = view.findViewById(R.id.rView);
        recyclerView.setLayoutManager(new GridLayoutManager(mActivity, 2));
        fragmentAdapter = new MainApplistFragmentAdapter(menuList);
        recyclerView.setAdapter(fragmentAdapter);

        ThreadUtil.getPool().execute(new Runnable() {
            @Override
            public void run() {
                menuList = AppListUtil.getInctance().getAppList();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fragmentAdapter.notifyData(menuList);
                    }
                });
            }
        });
        fragmentAdapter.setOnItemListener(new MainApplistFragmentAdapter.OnItemListener() {
            @Override
            public void OnItem(AppMenu appMenu) {
                doMenuClick(appMenu);
            }
        });
    }

    @Override
    protected void endFreshData() {
        super.endFreshData();
    }

    @Override
    public boolean isVerticalScrollerAble() {
        return true;
    }

    /**
     * item的点击事件
     *
     * @param menu
     */
    private void doMenuClick(AppMenu menu) {
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
                // 修改fragment类型的不需要子菜单页面了,直接显示
                showFragment(menu.getClassName());
                return;
            }
            if (menu.getChildrenList() != null && menu.getChildrenList().size() > 0) {
                showChildren(menu);
                return;
            }

            MxyLog.d(TAG, "menu:" + menu.toString());
            if (menu.getAction() != null) {
                Intent i = new Intent(menu.getAction());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            } else if (menu.getInfo() != null) {
                Intent i = new Intent();
                i.setClassName(menu.getInfo().activityInfo.packageName, menu.getInfo().activityInfo.name);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            } else {
                @SuppressLint("WrongConstant") ResolveInfo info = SysServices.getPkMgr(getActivity()).
                        resolveActivity(new Intent(menu.getClassName()), PackageManager.GET_INTENT_FILTERS);
                if (info == null) {
                    Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
                    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    final List<ResolveInfo> appList = SysServices.getPkMgr(getActivity()).queryIntentActivities(mainIntent, 0);
                    for (ResolveInfo infoActivity : appList) {
                        if (menu.getClassName().equals(infoActivity.activityInfo.name)) {
                            info = infoActivity;
                            break;
                        }
                    }
                }
                if (info != null) {
                    /**
                     * 下面这判断临时增加视频通话,用于当前版本测试用.
                     */
                    if (menu.getClassName().contains("fly")) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        ComponentName componentName = new ComponentName("com.kct.flyrtc", "com.kct.flyrtc.ui.MainActivity");
                        intent.setComponent(componentName);
                        startActivity(intent);
                        return;
                    }
                    Intent i = new Intent();
                    i.setClassName(info.activityInfo.packageName,
                            info.activityInfo.name);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (AppMenu.RES_INVALUED != menu.getIntentData()) {
                        i.putExtra("model", menu.getIntentData());
                    }
                    startActivity(i);
                } else {
                    MxyToast.showShort(getActivity(), R.string.unknown);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            MxyToast.showShort(getActivity(), e.toString());
        }
    }

    /**
     * 显示子菜单,由另外一个fragment装载item
     *
     * @param parentMenu
     */
    private void showChildren(AppMenu parentMenu) {
        MxyLog.d(TAG, "showChildren");
        final Fragment fragment = getChildFragmentManager().findFragmentByTag(FRAGMENT_TAG_CHILDREN);
        if (fragment != null) {
            //absDialogFragment childfragment = (absDialogFragment) fragment;
            //childfragmenr.dismissAllowingStateLoss();
        } else {
            mChildrenFragment = ApplistFragment.newInstance(parentMenu);
            mChildrenFragment.show(getChildFragmentManager(), FRAGMENT_TAG_CHILDREN);
        }
    }

    /**
     * 显示fragment页面
     *
     * @param classname
     */
    private void showFragment(String classname) {
        switch (classname) {
            case "fragment_Step":
                mChildrenFragment = CardFragmentStep.newInstance();
                break;
            case "fragment_Sleep":
                mChildrenFragment = CardFragmentSleep.newInstance();
                break;
            case "fragment_HeartRate":
                mChildrenFragment = CardFragmentHeartRate.newInstance();
                break;
            case "fragment_Weather":
                mChildrenFragment = CardFragmentWeather.newInstance();
                break;
        }
        mChildrenFragment.show(getChildFragmentManager(), FRAGMENT_TAG_CHILDREN);
    }

    @Override
    public void onClick(View v) {
        doMenuClick((AppMenu) v.getTag());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        horizalViewpagerIndicator.setSelect(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

