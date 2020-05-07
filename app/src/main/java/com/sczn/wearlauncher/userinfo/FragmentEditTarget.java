package com.sczn.wearlauncher.userinfo;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.util.MxyToast;

import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentEditTarget extends UserInfoEditFragment implements OnClickListener{
	
	public static FragmentEditTarget newInstance(){
		return new FragmentEditTarget();
	}

	private TextView mName;
	private ImageView mClear;
	
	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_status_usrinfo_target;
	}

	@Override
	protected void creatView() {
		// TODO Auto-generated method stub
		mName = findViewById(R.id.userinfo_edit_target);
		mClear = findViewById(R.id.userinfo_edit_delete);
		
		mClear.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		mName.setText(String.valueOf(UserInfoUtil.getStepTarget(getActivity())));
	}
	
	private void clearName(){
		Settings.System.putInt(getActivity().getContentResolver(), UserInfoUtil.USER_STEP_TARGET,UserInfoUtil. DEFAULT_STEP_TARGET);
		initData();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.userinfo_edit_delete:
				clearName();
				break;
	
			default:
				break;
		}
	}

	@Override
	protected void saveValue() {
		// TODO Auto-generated method stub

		try {
			int value = Integer.parseInt(mName.getEditableText().toString());
			if(value <= 0 || value > Integer.MAX_VALUE){
				MxyToast.showShort(getActivity(), R.string.userinfo_target_step_illegal);
				return;
			}
			Settings.System.putInt(getActivity().getContentResolver(), UserInfoUtil.USER_STEP_TARGET, value);
		} catch (Exception e) {
			// TODO: handle exception
			MxyToast.showShort(getActivity(), R.string.userinfo_target_step_illegal);
		}
	}
}
