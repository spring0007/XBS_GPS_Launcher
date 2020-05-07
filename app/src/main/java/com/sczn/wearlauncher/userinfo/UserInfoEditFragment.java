package com.sczn.wearlauncher.userinfo;

import com.sczn.wearlauncher.base.absDialogFragment;

import android.provider.Settings;

public abstract class UserInfoEditFragment extends absDialogFragment {

	@Override
	protected void destorytView() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
		saveValue();
		Settings.System.putLong(getActivity().getContentResolver(), UserInfoUtil.SETTING_KEY_EDITTIME, System.currentTimeMillis());
		super.onDestroy();
	}

	protected abstract void saveValue();
}
