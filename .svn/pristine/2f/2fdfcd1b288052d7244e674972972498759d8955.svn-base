package com.sczn.wearlauncher.userinfo;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.util.MxyToast;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ActivityUserInfo extends Activity implements OnClickListener{
	
	private static final String FRAGMENT_TAG_EDIT = "fragment_edit";
	
	private TextView mName;
	private TextView mSex;
	private TextView mHeight;
	private TextView mWeight;
	private TextView mTarget;
	
	private UserInfoObserver mUserInfoObserver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_userinfo);
		
		mUserInfoObserver = new UserInfoObserver(new Handler());
		
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mName = (TextView) findViewById(R.id.userinfo_name);
		mSex = (TextView) findViewById(R.id.userinfo_sex);
		mHeight = (TextView) findViewById(R.id.userinfo_height);
		mWeight = (TextView) findViewById(R.id.userinfo_weight);
		mTarget = (TextView) findViewById(R.id.userinfo_target);
		
		mName.setOnClickListener(this);
		mSex.setOnClickListener(this);
		mHeight.setOnClickListener(this);
		mWeight.setOnClickListener(this);
		mTarget.setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mUserInfoObserver.register(getApplicationContext());
		initData();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		mUserInfoObserver.unRegister(getApplicationContext());
		super.onPause();
	}

	private void initData() {
		// TODO Auto-generated method stub
		mName.setText(UserInfoUtil.getName(this));
		mSex.setText(UserInfoUtil.getSex(this));
		mHeight.setText(UserInfoUtil.getHeight(this));
		mWeight.setText(UserInfoUtil.getWeight(this));
		mTarget.setText(String.valueOf(UserInfoUtil.getStepTarget(this)));
	}
	
	private void showEditFragment(UserInfoEditFragment editFragment){
		final FragmentManager fm = getFragmentManager();
		
		final UserInfoEditFragment fragment = (UserInfoEditFragment) fm.findFragmentByTag(FRAGMENT_TAG_EDIT);
		if(fragment != null){
			fragment.dismissAllowingStateLoss();
		}
		
		editFragment.show(fm, FRAGMENT_TAG_EDIT);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.userinfo_name:
				showEditFragment(FragmentEditName.newInstance());
				break;
			case R.id.userinfo_sex:
				showEditFragment(FragmentEditSex.newInstance());
				break;
			case R.id.userinfo_height:
				showEditFragment(FragmentEditHeight.newInstance());
				break;
			case R.id.userinfo_weight:
				showEditFragment(FragmentEditWeight.newInstance());
				break;
			case R.id.userinfo_target:
				showEditFragment(FragmentEditTarget.newInstance());
				break;
			default:
				break;
		}
	}
	
	private void freshData(){
		initData();
	}
	
	private class UserInfoObserver extends ContentObserver{
		
		private Uri uriName;
		private Uri uriSex;
		private Uri uriHeight;
		private Uri uriWeight;
		private Uri uriTarget;

		public UserInfoObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
			
			initUri();
		}
		
		private void initUri() {
			// TODO Auto-generated method stub
			uriName = UserInfoUtil.getUriName(getApplication());
			uriSex = UserInfoUtil.getUriSex(getApplication());
			uriHeight = UserInfoUtil.getUriHeight(getApplication());
			uriWeight = UserInfoUtil.getUriWeight(getApplication());
			uriTarget = UserInfoUtil.getUriStepTarget(getApplication());
		}

		public void register(Context context){
			
			context.getContentResolver().registerContentObserver(uriName, true,this);
			context.getContentResolver().registerContentObserver(uriSex, true,this);
			context.getContentResolver().registerContentObserver(uriHeight, true,this);
			context.getContentResolver().registerContentObserver(uriWeight, true,this);
			context.getContentResolver().registerContentObserver(uriTarget, true,this);
		}
		
		public void unRegister(Context context){
			context.getContentResolver().unregisterContentObserver(this);
		}
		
		@Override
		public void onChange(boolean selfChange, Uri uri) {
			// TODO Auto-generated method stub
			
			MxyToast.showShort(ActivityUserInfo.this, getString(R.string.userinfo_data_changed));
			if(uriName.equals(uri)){
				freshData();
				return;
			}
			if(uriSex.equals(uri)){
				freshData();	
				return;
			}
			if(uriHeight.equals(uri)){
				freshData();
				return;
			}
			if(uriWeight.equals(uri)){
				freshData();
				return;
			}
			if(uriTarget.equals(uri)){
				freshData();
				return;
			}
		}
	}
}
