package com.sczn.wearlauncher.status.view;

import com.sczn.wearlauncher.R;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;

public class RecentIcon extends StatusIconWithText {

	public RecentIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mIcon.setImageResource(R.drawable.statu_icon_recent);
		setOnClickListener(onRecentIcon);
	}

	private OnClickListener onRecentIcon = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i = new Intent("com.android.systemui.recent.action.TOGGLE_RECENTS");
			i.setClassName("com.android.systemui",
                    "com.android.systemui.recent.RecentsActivity");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            getContext().startActivity(i);
		}
	};
}
