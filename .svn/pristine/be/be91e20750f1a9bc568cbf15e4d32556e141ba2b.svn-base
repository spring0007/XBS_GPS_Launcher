package com.sczn.wearlauncher.menu.view;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.util.MxyToast;
import com.sczn.wearlauncher.base.util.SysServices;
import com.sczn.wearlauncher.menu.bean.AppMenu;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MenuMore extends RelativeLayout {

	private TextView mName;
	private ImageView mIcon;
	private AppMenu mMenu;
	private boolean finishInflate;
	private int mParentWidth;
	
	public MenuMore(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		finishInflate = false;
		setWillNotDraw(false);
	}
	
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		mName = (TextView) findViewById(R.id.menu_child_text);
		mIcon = (ImageView) findViewById(R.id.menu_child_icon);
		finishInflate = true;
		initView();
	}

	public void setMenu(AppMenu menu){
		this.mMenu = menu;
		mIcon.setImageDrawable(menu.getIcon(getContext()));
		mName.setText(menu.getName(getContext()));
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		if(!finishInflate || mMenu == null){
			return;
		}
		mIcon.setImageDrawable(mMenu.getIcon(getContext()));
		mName.setText(mMenu.getName(getContext()));
		mIcon.setTag(mMenu);
		mIcon.setOnClickListener(onMenuClick);
	}
	
	private OnClickListener onMenuClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {

			// TODO Auto-generated method stub
			if(!(v.getTag() instanceof AppMenu)){
				return;
			}
			AppMenu menu = (AppMenu) v.getTag();

			try {
				if(menu.getAction() != null){
					Intent i = new Intent(menu.getAction());
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					getContext().startActivity(i);
				}else if(menu.getInfo() != null){
					Intent i = new Intent();
					i.setClassName(menu.getInfo().activityInfo.packageName,
							menu.getInfo().activityInfo.name);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					getContext().startActivity(i);
				}else{
					final ResolveInfo info = SysServices.getPkMgr(getContext()).
							resolveActivity(new Intent(menu.getClassName()), PackageManager.GET_INTENT_FILTERS);
					Intent i = new Intent();
					i.setClassName(info.activityInfo.packageName,
							info.activityInfo.name);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					getContext().startActivity(i);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				MxyToast.showShort(getContext(), e.toString());
			}
		
		}
	};
	
}
