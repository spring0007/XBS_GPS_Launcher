package com.sczn.wearlauncher.status.view;

import com.sczn.wearlauncher.userinfo.ActivityUserInfo;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;

public class UserInfoIcon extends StatusIconWithText {

	public UserInfoIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getContext(), ActivityUserInfo.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getContext().startActivity(i);
				
			}
		});
	}

	
}
