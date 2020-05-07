package com.sczn.wearlauncher.userinfo;

import java.util.ArrayList;

import com.sczn.wearlauncher.R;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentEditHeight extends UserInfoEditFragment {
	
	public static FragmentEditHeight newInstance(){
		return new FragmentEditHeight();
	}
	
	private ListView mListView;
	private ArrayList<Integer> mHeights;
	private HeightAdapter mHeightAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mHeights = new ArrayList<Integer>();
		
		for(int i = 100 ; i < 250; i++){
			mHeights.add(Integer.valueOf(i));
		}
	}

	@Override
	protected void saveValue() {
		// TODO Auto-generated method stub
		if(mHeightAdapter == null){
			return;
		}
		final int height = mHeights.get(mHeightAdapter.getSelectedPosition());
		Settings.System.putInt(getActivity().getContentResolver(), UserInfoUtil.SETTING_KEY_HEIGHT,height);
	}

	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_status_userinfo_height;
	}
	
	

	@Override
	protected void creatView() {
		// TODO Auto-generated method stub

		mListView = findViewById(R.id.userinfo_edit_list);
		mHeightAdapter = new HeightAdapter(getActivity());
		
		mListView.setAdapter(mHeightAdapter);
		
		mListView.setSelection(mHeightAdapter.getSelectedPosition());
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				mHeightAdapter.setSelectedPosition(position);
			}
		});
	}

	private class HeightAdapter extends BaseAdapter{
		
		private Context mContext;
		private int SelectedPosition = mHeights.indexOf(Integer.valueOf(
				Settings.System.getInt(getActivity().getContentResolver(), UserInfoUtil.SETTING_KEY_HEIGHT, UserInfoUtil.DEFAULT_HEIGHT)));;
		
		public HeightAdapter(Context mContext) {
			super();
			this.mContext = mContext;
		}
		
		public int getSelectedPosition() {
			return SelectedPosition;
		}

		public void setSelectedPosition(int selectedPosition) {
			SelectedPosition = selectedPosition;
			notifyDataSetInvalidated();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mHeights.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mHeights.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return mHeights.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			final ViewHolder holder;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_userinfo_edit,parent, false);
				holder.mValue = (TextView) convertView.findViewById(R.id.userinfo_item_value);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			final int height = mHeights.get(position).intValue();
			
			holder.mValue.setText(String.format(mContext.getString(R.string.userinfo_height_value), height));
			
			if(position == SelectedPosition){
				convertView.setBackgroundResource(R.color.userinfo_bg_list_selected);
			}else{
				convertView.setBackgroundResource(R.color.userinfo_bg_list_normal);
			}
			return convertView;
		}
		
		public class ViewHolder{
			public TextView mValue;
		}
	}
}
