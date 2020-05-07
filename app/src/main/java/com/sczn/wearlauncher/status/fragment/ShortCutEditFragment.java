package com.sczn.wearlauncher.status.fragment;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.absDialogFragment;
import com.sczn.wearlauncher.sp.SPUtils;
import com.sczn.wearlauncher.base.view.MyRecyclerView;
import com.sczn.wearlauncher.base.view.WrapLinearLayoutManager;
import com.sczn.wearlauncher.menu.bean.AppMenu;
import com.sczn.wearlauncher.menu.util.AppListUtil;
import com.sczn.wearlauncher.status.adapter.ShortcutEditAdapter;
import com.sczn.wearlauncher.status.adapter.ShortcutEditAdapter.IShortCutChoose;
import com.sczn.wearlauncher.status.view.ShortCutIcon;

import android.os.Bundle;


public class ShortCutEditFragment extends absDialogFragment implements IShortCutChoose{
	
	public static final String ARG_SHORT_CUT_ID = "shortcut_id";
	
	public static ShortCutEditFragment newInstances(int shortCutId){
		final ShortCutEditFragment fragment = new ShortCutEditFragment();
		Bundle bdl = new Bundle();
		bdl.putInt(ARG_SHORT_CUT_ID, shortCutId);
		fragment.setArguments(bdl);
		return fragment;
	}

	private MyRecyclerView mShortCutList;
	private ShortcutEditAdapter mShortCutAdapter;
	private int shortcutId;
	private IEditResultListen mEditResultListen;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		shortcutId = getArguments().getInt(ARG_SHORT_CUT_ID);
	}
	
	public void setEditResultListen(IEditResultListen listen){
		this.mEditResultListen = listen;
	}
	
	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_status_shortcut_edit;
	}
	
	@Override
	protected void creatView() {
		// TODO Auto-generated method stub
		mShortCutList = findViewById(R.id.shortcut_list);
		mShortCutList.setEmpty(findViewById(R.id.shortcut_empty));
		
		mShortCutAdapter = new ShortcutEditAdapter(getActivity());

		mShortCutList.setLayoutManager(new WrapLinearLayoutManager(getActivity()));
		mShortCutList.setAdapter(mShortCutAdapter);
		
		mShortCutAdapter.setShortCutList(AppListUtil.getInctance().getAppList());
		
		mShortCutAdapter.setOnClickListen(this);
	}

	@Override
	protected void destorytView() {
		// TODO Auto-generated method stub
		
	}
	
	public void freshData(){
		if(mShortCutAdapter != null){
			mShortCutAdapter.setShortCutList(AppListUtil.getInctance().getAppList());
		}
	}

	@Override
	public void onShortCutChoose(AppMenu menu) {
		// TODO Auto-generated method stub
		SPUtils.setParam(getActivity(), ShortCutIcon.getShareKey(shortcutId), menu.getCustomImageName());
		
		dismissAllowingStateLoss();
		if(mEditResultListen != null){
			mEditResultListen.onEditResult(shortcutId, menu);
		}
	}
	
	public interface IEditResultListen{
		public void onEditResult(int shortcutId, AppMenu menu);
	}

}
