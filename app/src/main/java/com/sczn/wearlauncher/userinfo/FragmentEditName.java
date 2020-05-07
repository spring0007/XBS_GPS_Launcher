package com.sczn.wearlauncher.userinfo;

import com.sczn.wearlauncher.R;

import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentEditName extends UserInfoEditFragment implements OnClickListener{
	
	public static FragmentEditName newInstance(){
		return new FragmentEditName();
	}

	private TextView mName;
	private ImageView mClear;
	
	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_status_usrinfo_name;
	}

	@Override
	protected void creatView() {
		// TODO Auto-generated method stub
		mName = findViewById(R.id.userinfo_edit_name);
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
		mName.setText(UserInfoUtil.getName(getActivity()));
	}
	
	private void clearName(){
		Settings.System.putString(getActivity().getContentResolver(), UserInfoUtil.SETTING_KEY_NAME, null);
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
		final String name = mName.getEditableText().toString();
		Settings.System.putString(getActivity().getContentResolver(), UserInfoUtil.SETTING_KEY_NAME
				, name.isEmpty() ? null : name);
	}
}
