package com.sczn.wearlauncher.status;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.absFragment;
import android.os.Bundle;


public class StatusFragmentFirst extends absFragment{
	
	public static final String ARG_IS_TMP = "is_tmp";
	public static StatusFragmentFirst newInstance(boolean isTmp){
		StatusFragmentFirst fragment = new StatusFragmentFirst();
		Bundle bdl = new Bundle();
		bdl.putBoolean(ARG_IS_TMP, isTmp);
		fragment.setArguments(bdl);
		return fragment;
		
	}
	
	private boolean isTmp = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//MxyLog.d(this, "onCreate" + isTmp);
		super.onCreate(savedInstanceState);
		Bundle bdl = getArguments();
		if(bdl != null){
			isTmp = bdl.getBoolean(ARG_IS_TMP, false);
		}
	}
	
	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_status_first;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
	}
	
}
