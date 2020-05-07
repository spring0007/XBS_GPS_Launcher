package com.sczn.wearlauncher.shortcut;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.menu.bean.AppMenu;
import com.sczn.wearlauncher.menu.fragment.absMenuFragment;
import com.sczn.wearlauncher.status.fragment.ShortCutEditFragment;
import com.sczn.wearlauncher.status.fragment.ShortCutEditFragment.IEditResultListen;
import com.sczn.wearlauncher.status.view.ShortCutIcon;
import com.sczn.wearlauncher.status.view.ShortCutIcon.IShortCutEvent;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class ShortCutFragment extends absMenuFragment implements IShortCutEvent, IEditResultListen,OnClickListener{
	
	public static final String FRAGMENT_TAG_SHORTCUT_EDIT = "shortcut_edit";
	
	private ShortCutIcon mFirstShortCut;
	private ShortCutIcon mSencondShortCut;
	private ShortCutIcon mThirdShortCut;
	private ShortCutIcon mForthShortCut;
	private ShortCutEditFragment mShortCutEditFragment;
	
	private int IconState = ShortCutIcon.STATE_NORMAL;

	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_shortcut;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		MxyLog.d(this, "initData()");
		mFirstShortCut = findViewById(R.id.shortcut_first);
		mSencondShortCut = findViewById(R.id.shortcut_second);
		mThirdShortCut = findViewById(R.id.shortcut_third);
		mForthShortCut = findViewById(R.id.shortcut_forth);
		
		//mFirstShortCut.startLoad();
		//mSencondShortCut.startLoad();
		//mThirdShortCut.startLoad();
		//mForthShortCut.startLoad();
	}
	
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		super.initData();
		mFirstShortCut.setmShortCutEvent(this);
		mSencondShortCut.setmShortCutEvent(this);
		mThirdShortCut.setmShortCutEvent(this);
		mForthShortCut.setmShortCutEvent(this);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		view.setOnClickListener(this);
	}
	
	private void setIconState(int state){
		this.IconState = state;
		mFirstShortCut.setState(state);
		mSencondShortCut.setState(state);
		mThirdShortCut.setState(state);
		mForthShortCut.setState(state);
	}
	
	private void showEditFragment(int shortCutId){
		MxyLog.d(this, "showEditFragment--shortcutId=" + shortCutId );
		mShortCutEditFragment = (ShortCutEditFragment) getChildFragmentManager().findFragmentByTag(FRAGMENT_TAG_SHORTCUT_EDIT);
		if(mShortCutEditFragment != null){
			mShortCutEditFragment.dismissAllowingStateLoss();
		}
		mShortCutEditFragment = ShortCutEditFragment.newInstances(shortCutId);
		mShortCutEditFragment.setEditResultListen(this);
		mShortCutEditFragment.show(getChildFragmentManager(), FRAGMENT_TAG_SHORTCUT_EDIT);
		
	}
	
	@Override
	protected boolean isUserVisible() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	protected void startFreshData() {
		// TODO Auto-generated method stub
		//MxyLog.d(this, "startFreshData");
		super.startFreshData();
	}
	
	@Override
	protected void endFreshData() {
		// TODO Auto-generated method stub
		//MxyLog.d(this, "endFreshData");
		super.endFreshData();
	}

	@Override
	protected void freshData() {
		// TODO Auto-generated method stub
		//MxyLog.d(this, "freshData");
		if(mShortCutEditFragment != null && !mShortCutEditFragment.isDetached()){
			mShortCutEditFragment.freshData();
		}
		mFirstShortCut.startLoad();
		mSencondShortCut.startLoad();
		mThirdShortCut.startLoad();
		mForthShortCut.startLoad();
	}

	@Override
	public void onShortCutAction(ShortCutIcon v, AppMenu menu) {
		// TODO Auto-generated method stub
		doMenuClick(menu);
	}

	@Override
	public void onShortCutEdit(ShortCutIcon v) {
		// TODO Auto-generated method stub
		//showEditFragment(v.getShortCutID());
		showEditFragment(v.getId());
	}

	@Override
	public void onEditResult(int shortcutId, AppMenu menu) {
		// TODO Auto-generated method stub
		MxyLog.d(this, "onEditResult--shortcutId=" + shortcutId + "menu=" + (menu == null ? "null" : menu.getClassName()));
		switch (shortcutId) {
			case R.id.shortcut_first:
				mFirstShortCut.startLoad();
				break;
			
			case R.id.shortcut_second:
				mSencondShortCut.startLoad();
				break;
				
			case R.id.shortcut_third:
				mThirdShortCut.startLoad();
				break;
			
			case R.id.shortcut_forth:
				mForthShortCut.startLoad();
				break;
			default:
				break;
		}
	}

	@Override
	public void onShortCutStateChange(int state) {
		// TODO Auto-generated method stub
		setIconState(state);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(ShortCutIcon.STATE_EDIT == IconState){
			setIconState(ShortCutIcon.STATE_NORMAL);
		}else{
			mActivity.finish();
		}
	}

}
