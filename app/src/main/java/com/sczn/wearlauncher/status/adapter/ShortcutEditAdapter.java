package com.sczn.wearlauncher.status.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.view.ScrollerTextView;
import com.sczn.wearlauncher.menu.bean.AppMenu;
import com.sczn.wearlauncher.menu.view.MenuIconImage;

public class ShortcutEditAdapter extends Adapter<ShortcutEditAdapter.ShortCutHold>{

	private Context mContext;
	private ArrayList<AppMenu> mShortCutList;
	private IShortCutChoose mShortCutChoose;
	
	public ShortcutEditAdapter(Context context){
		
		this.mContext = context;
		
		mShortCutList = new ArrayList<AppMenu>();
		
	}
	
	public void setOnClickListen(IShortCutChoose listen) {
		this.mShortCutChoose = listen;
	}



	public void setShortCutList(ArrayList<AppMenu> list){
		mShortCutList.clear();
		if(list != null){
			mShortCutList.addAll(list);
		}
		notifyDataSetChanged();
	}
	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		if(mShortCutList == null){
			return 0;
		}
		return mShortCutList.size();
	}

	@Override
	public void onBindViewHolder(ShortCutHold arg0, int arg1) {
		// TODO Auto-generated method stub
		final AppMenu menu = mShortCutList.get(arg1);
		
		arg0.mIcon.setImageDrawable(menu.getIcon(mContext));
		arg0.mName.setText(menu.getName(mContext));
		
		arg0.itemView.setTag(menu);
		arg0.itemView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mShortCutChoose != null){
					mShortCutChoose.onShortCutChoose((AppMenu) v.getTag());
				}
			}
		});
	}

	@Override
	public ShortCutHold onCreateViewHolder(ViewGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		final View view = LayoutInflater.from(arg0.getContext())
				.inflate(R.layout.list_item_shortcut, arg0, false);
		return new ShortCutHold(view);
	}
	
	public class ShortCutHold extends ViewHolder{

		private MenuIconImage mIcon;
		private TextView mName;;

		public ShortCutHold(View arg0) {
			super(arg0);
			mIcon = (MenuIconImage) arg0.findViewById(R.id.shortcut_icon);
			mName = (TextView) arg0.findViewById(R.id.shortcut_name);
		}	
	}
	
	public interface IShortCutChoose{
		public void onShortCutChoose(AppMenu menu);
	}
}
