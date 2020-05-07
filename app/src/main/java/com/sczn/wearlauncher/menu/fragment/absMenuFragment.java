package com.sczn.wearlauncher.menu.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sczn.wearlauncher.base.absDialogFragment;
import com.sczn.wearlauncher.base.absFragment;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.base.util.MxyToast;
import com.sczn.wearlauncher.base.util.SysServices;
import com.sczn.wearlauncher.btconnect.BtStyleChooseActivity;
import com.sczn.wearlauncher.menu.bean.AppMenu;
import com.sczn.wearlauncher.menu.util.AppListUtil;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public abstract class absMenuFragment extends absFragment implements Observer{
	private static final String TAG = absMenuFragment.class.getSimpleName();
	
	public static final String FRAGMENT_TAG_CHILDREN = "fragment_menu_children";
	public static final String FRAGMENT_TAG_CHILDREN_MORE = "fragment_menu_children_more";
	public static final String FRAGMENT_TAG_CHILDREN_TWO = "fragment_menu_children_two";
	public static final String ARG_IS_APP = "is_app";
	public static final String ARG_IS_STYLE = "is_style";
	public static final int NO_NEED_FRESH = -1;
	protected boolean isApp;
	protected boolean isStyle;
	
	protected boolean needFresh;
	protected int needFreshType = NO_NEED_FRESH;
	
	protected AppListUtil mAppListUtil;
	
	protected MenuChildrenMoreFragment mMenuChildrenMoreFragment;
	protected MenuChildrenTwoFragment mMenuChildrenTwoFragment;
	protected absDialogFragment mChildrenFragment;
	
	protected boolean isInit = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bdl = getArguments();
		if(bdl != null){
			isApp = bdl.getBoolean(ARG_IS_APP, true);
			isStyle = bdl.getBoolean(ARG_IS_STYLE, false);
		}
		
		mAppListUtil = AppListUtil.getInctance();
	}
	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		needFresh = true;
		//if(isApp){
			//mAppListUtil.addObserver(this);
		//}
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		isInit = true;
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		//if(isApp){
			//mAppListUtil.deleteObserver(this);
		//}
		super.onDestroyView();
	}
	
	@Override
	protected void startFreshData() {
		// TODO Auto-generated method stub
		super.startFreshData();
		mAppListUtil.addObserver(this);
		needFresh = true;
		if(needFresh){
			freshData();
			if(isApp){
				freshChildFragment();
			}
			needFresh = false;
		}
	}
	
	@Override
	protected void endFreshData() {
		// TODO Auto-generated method stub
		super.endFreshData();
		mAppListUtil.deleteObserver(this);
	}
	
	@Override
	protected boolean isUserVisible() {
		// TODO Auto-generated method stub
		if(isInit){
			isInit = false;
			return true;
		}
		return super.isUserVisible();
	}

	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		//MxyLog.d(TAG, "update--data=" + data);
		int type;
		try {
			Integer value = (Integer) data;
			type = value;
		} catch (Exception e) {
			// TODO: handle exception
			return;
		}
		//MxyLog.d(this,"isApp=" + isApp + "--update" + "--type=" + type + "--isInit=" + isInit + "--isUserVisible()=" + isUserVisible() + "--isResumed=" + isResumed);
		switch (type) {
			case AppListUtil.OBSERVER_DATA_MENU_SKIN:
				mAppListUtil.clearMenuIconCache();
			case AppListUtil.OBSERVER_DATA_STYLE:
			case AppListUtil.OBSERVER_DATA_MENU:
			case AppListUtil.OBSERVER_DATA_MENU_MORE:
				/*
				if(!isUserVisible() || !isResumed){
					needFresh = true;
					return;
				}*/
				if(!isResumed){
					needFresh = true;
					return;
				}
				menuChanged(type);
				break;
	
			default:
				break;
		}
	}
	
	protected void doMenuClick(View view){
		if(isStyle){
			return;
		}
		if(!(view.getTag() instanceof AppMenu)){
			return;
		}
		doMenuClick((AppMenu) view.getTag());
	}
	
	protected void doMenuClick(AppMenu menu){
		// TODO Auto-generated method stub

		try {
			if(AppMenu.MENU_TYPE_MORE == menu.getMenuType()){
				final AppMenu moreMenu = AppListUtil.getInctance().getMoreMenu();
				if(moreMenu != null){
					showChildren(moreMenu);
				}else{
					MxyLog.e(TAG, "AppListUtil.getInctance().getMoreMenu() = null");
				}
				return;
			}else if(AppMenu.MENU_TYPE_ANDROID_ASSIST == menu.getMenuType()){
				SysServices.setSystemSettingInt(getActivity(), BtStyleChooseActivity.SETTING_KEY_BT_STYLE,
						BtStyleChooseActivity.BT_STYLE_ANDROID);
				final AppMenu androidAssist = AppListUtil.getInctance().getAssistantMenu();
				if(androidAssist != null){
					androidAssist.setFiltChildren(AppListUtil.getInctance().getAndroidAssistFilter());
					showChildren(androidAssist);
				}else{
					MxyLog.e(TAG, "AppListUtil.getInctance().getAssistantMenu() = null");
				}
				return;
			}else if(AppMenu.MENU_TYPE_IOS_ASSIST == menu.getMenuType()){
				SysServices.setSystemSettingInt(getActivity(), BtStyleChooseActivity.SETTING_KEY_BT_STYLE,
						BtStyleChooseActivity.BT_STYLE_IOS);
				final AppMenu iosAssist = AppListUtil.getInctance().getAssistantMenu();
				if(iosAssist != null){
					iosAssist.setFiltChildren(AppListUtil.getInctance().getIosAssistFilter());
					showChildren(iosAssist);
				}else{
					MxyLog.e(TAG, "AppListUtil.getInctance().getAssistantMenu() = null");
				}
				return;
			}
			if(menu.getChildrenList() != null && menu.getChildrenList().size() > 0){
				showChildren(menu);
				return;
			}
			
			if(menu.getAction() != null){
				Intent i = new Intent(menu.getAction());
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
			}else if(menu.getInfo() != null){
				Intent i = new Intent();
				i.setClassName(menu.getInfo().activityInfo.packageName,
						menu.getInfo().activityInfo.name);
				//MxyLog.d(TAG, "menu.getInfo().activityInfo.packageName=" + menu.getInfo().activityInfo.packageName);
				//MxyLog.d(TAG, "menu.getIntentData()= " + menu.getIntentData());
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
			}else{
				
				ResolveInfo info = SysServices.getPkMgr(getActivity()).
						resolveActivity(new Intent(menu.getClassName()), PackageManager.GET_INTENT_FILTERS);
				//MxyLog.d(TAG, "infoAction=" + info);
				if(info == null){
					Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
			        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			        final List<ResolveInfo> appList = SysServices.getPkMgr(getActivity()).queryIntentActivities(mainIntent, 0);
			       // MxyLog.d(TAG, "initActivityMap--start");
			        for(ResolveInfo infoActivity:appList){
			        	if(menu.getClassName().equals(infoActivity.activityInfo.name)){
			        		info = infoActivity;
			        		break;
			        	}
			        }
				}
				Intent i = new Intent();
				i.setClassName(info.activityInfo.packageName,
						info.activityInfo.name);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				if(AppMenu.RES_INVALUED != menu.getIntentData()){
					i.putExtra("model", menu.getIntentData());
				}
				startActivity(i);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MxyToast.showShort(getActivity(), e.toString());
		}
	
	}

	private void showChildren(AppMenu parentMenu){
		final Fragment fragment = getChildFragmentManager().findFragmentByTag(FRAGMENT_TAG_CHILDREN);
		if(fragment != null){
			//absDialogFragment childfragment = (absDialogFragment) fragment;
			//childfragmenr.dismissAllowingStateLoss();
		}else{
			if(AppMenu.MENU_TYPE_MORE == parentMenu.getMenuType() || parentMenu.getChildrenList().size() > 2){
				mChildrenFragment = MenuChildrenMoreFragment.newInstance(parentMenu);
			}else{
				mChildrenFragment = MenuChildrenTwoFragment.newInstance(parentMenu);
			}
			mChildrenFragment.show(getChildFragmentManager(), FRAGMENT_TAG_CHILDREN);
		}
	}
	
	
	private void freshChildFragment(){
		final Fragment fragment = getChildFragmentManager().findFragmentByTag(FRAGMENT_TAG_CHILDREN);
		if(fragment != null && fragment instanceof MenuChildrenMoreFragment){
			MenuChildrenMoreFragment childfragment = (MenuChildrenMoreFragment) fragment;
			childfragment.freshMoreMenus();
			
			/*
			childfragment.dismissAllowingStateLoss();
			final AppMenu moreMenu = AppListUtil.getInctance().getMoreMenu();
			if(moreMenu != null){
				showChildren(moreMenu);
			}*/
		}
	}

	@Override
	protected void initData(){
	}
	
	protected void menuChanged(int type){
		//MxyLog.d(TAG, "menuChanged--type=" + type);
		switch (type) {
			case AppListUtil.OBSERVER_DATA_MENU_SKIN:
				freshData();
				break;
			case AppListUtil.OBSERVER_DATA_MENU_MORE:
				freshChildFragment();
				break;
			case AppListUtil.OBSERVER_DATA_MENU:
				freshData();
				freshChildFragment();
				break;
			default:
				break;
		}
	}
	
	
	@Override
	protected abstract int getLayoutResouceId();

	@Override
	protected abstract void initView();

	protected abstract void freshData();
	
}
