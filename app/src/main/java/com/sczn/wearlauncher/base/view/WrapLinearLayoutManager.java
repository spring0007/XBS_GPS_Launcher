package com.sczn.wearlauncher.base.view;

import com.sczn.wearlauncher.app.MxyLog;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView.Recycler;
import android.support.v7.widget.RecyclerView.State;

public class WrapLinearLayoutManager extends LinearLayoutManager {

	public WrapLinearLayoutManager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public WrapLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
		super(context, orientation, reverseLayout);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onLayoutChildren(Recycler arg0, State arg1) {
		// TODO Auto-generated method stub
		try {
			super.onLayoutChildren(arg0, arg1);
		} catch (Exception e) {
			// TODO: handle exception
			MxyLog.e(this, "onLayoutChildren--e=" + e.toString());
		}
	}
}
