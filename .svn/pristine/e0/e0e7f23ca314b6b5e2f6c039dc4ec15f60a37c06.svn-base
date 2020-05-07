package com.sczn.wearlauncher.card.healthalarm;

import java.util.ArrayList;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.absFragment;
import com.sczn.wearlauncher.base.adapter.LoopViewPageAdapter;
import com.sczn.wearlauncher.base.view.HorizalViewPager;
import com.sczn.wearlauncher.base.view.ViewPagerIndicator;
import com.sczn.wearlauncher.base.view.HorizalViewPager.IHorizalViewPagerSelected;
import com.sczn.wearlauncher.btconnect.BtConnectAndroidFragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;

public class ActivityAlarmSetting extends HealthAlarmActivity implements IHorizalViewPagerSelected{
	
	public static final String ARG_ALATM_TYPE = "alarm_type";
	
	private HorizalViewPager mViewPager;
	private ViewPagerIndicator mPagerIndicator;
	private LoopViewPageAdapter mPageAdapter;
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		setIntent(intent);
	}
	
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_card_healthalarm_setting;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mViewPager = (HorizalViewPager) findViewById(R.id.bt_connectviewpager);
		mPagerIndicator = (ViewPagerIndicator) findViewById(R.id.bt_connect_ind);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		mPageAdapter = new LoopViewPageAdapter(getFragmentManager(), false, mViewPager);
		mViewPager.setHorizalViewPagerSelected(this);
		
		mViewPager.setAdapter(mPageAdapter);
		
		final ArrayList<absFragment> list = new ArrayList<absFragment>();
		switch (getIntent().getIntExtra(ARG_ALATM_TYPE, ModelAlarm.ALARM_TYPE_SIT)) {
			case ModelAlarm.ALARM_TYPE_DRINK:
				list.add(FragmentAlarmList.newInstance(ModelAlarm.ALARM_TYPE_DRINK));
				list.add(FragmentDrinkSetting.newInstance());
				break;
			case ModelAlarm.ALARM_TYPE_SIT:
				list.add(FragmentAlarmList.newInstance(ModelAlarm.ALARM_TYPE_SIT));
				break;
			default:
				return;
		}
		
		mPageAdapter.setList(list);
		mViewPager.setCurrentItem(0);
		
		mPagerIndicator.init(list.size(),ViewPagerIndicator.TYPE_CARD_CONTAIN);
		mPagerIndicator.setSelect(0);
	}

	@Override
	public void horizalViewPageSelected(int index) {
		// TODO Auto-generated method stub
		mPagerIndicator.setSelect(index);
	}

}
