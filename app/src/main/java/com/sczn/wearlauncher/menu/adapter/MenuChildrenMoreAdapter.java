package com.sczn.wearlauncher.menu.adapter;

import java.util.ArrayList;

import com.sczn.wearlauncher.menu.view.MenuMore;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class MenuChildrenMoreAdapter extends PagerAdapter {
	private static final String TAG = MenuChildrenMoreAdapter.class.getSimpleName();
	
	private Context mContext;
	
	private ArrayList<MenuMore> mItems;
	
	public MenuChildrenMoreAdapter(Context mContext,
			ArrayList<MenuMore> items) {
		super();
		this.mContext = mContext;
		this.mItems = new ArrayList<MenuMore>();
		if(items != null){
			this.mItems.addAll(items);
		}
	}
	
	public MenuChildrenMoreAdapter(Context mContext) {
		super();
		this.mContext = mContext;
		this.mItems = new ArrayList<MenuMore>();
	}
	
	public void setItems(ArrayList<MenuMore> items){
		mItems.clear();
		if(items != null){
			mItems.addAll(items);
		}
		notifyDataSetChanged();
	}
	
	
	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}
	
	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return POSITION_NONE;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mItems.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		final MenuMore children = mItems.get(position);
		//MxyLog.d(TAG, "instantiateItem--position=" + position);
		container.addView(children);
		children.setScaleX(1.1f);
		return children;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		if(object instanceof View){
			container.removeView((View) object);
		}
	}

}
