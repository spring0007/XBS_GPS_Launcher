package com.sczn.wearlauncher.status.view;

import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.btconnect.QrcodeActivity;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;

public class QrcodeIcon extends StatusIconWithText {
	

	public QrcodeIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		getIcon().setOnClickListener(showQrCode);
	}
	
	private OnClickListener showQrCode = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i = new Intent(LauncherApp.appContext, QrcodeActivity.class);
			getContext().startActivity(i);
		}
	};
}
