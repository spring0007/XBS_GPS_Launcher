package com.sczn.wearlauncher.menu.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.sczn.wearlauncher.menu.bean.AppMenu;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MenuVerticalBg extends FrameLayout{
	private static final String TAG = MenuVerticalBg.class.getSimpleName();
	
	//private int menuSportBgType = SportListUtil.MENU_SPORT_BG;
	private int menuSportBgType = 0;
	private HashMap<AppMenu, BgImage> mSportBgs = new HashMap<AppMenu, BgImage>();
	
	public MenuVerticalBg(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		//menuSportBgType = SysServices.getSystemSettingInt(getContext(),
		//		SportListUtil.SETTING_KEY_MENU_SPORT_BG, menuSportBgType);
	}
	
	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
	}

	public void setSportMenus(ArrayList<AppMenu> menus){
		final LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mSportBgs.clear();
		//MxyLog.d(TAG, "setSportMenus" + menus.size());
		for(AppMenu menu : menus){
			final BgImage image = new BgImage(getContext(), menu);
			mSportBgs.put(menu, image);
			addView(image, params);
		}
	}
	
	public void freshBg(AppMenu menu, float alpha){
		if(menu == null){
			return;
		}
		if(alpha <= 0){
			alpha = 0;
		}
		final BgImage image = mSportBgs.get(menu);
		if(image != null){
			image.setAlpha(alpha);
		}
		
	}
	
	private class BgImage extends ImageView{
		private AppMenu menu;
		
		public BgImage(Context context, AppMenu menu) {
			super(context);
			this.menu = menu;
			setVisibility(GONE);
		}


		@Override
		public void setAlpha(float alpha) {
			// TODO Auto-generated method stub
			if(alpha < 0.1){
				setVisibility(GONE);
				return;
			}else{
				setVisibility(VISIBLE);
				//setImageResource(SportListUtil.MENU_SPORT_BG == menuSportBgType?
				//		menu.getBgImageRes():menu.getBgImageBwRes());
			}
			super.setAlpha(alpha);
		}
		
	}

}
