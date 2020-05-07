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

public class FragmentEditSex extends UserInfoEditFragment {
	
	public static FragmentEditSex newInstance(){
		return new FragmentEditSex();
	}
	
	private ListView mListView;
	private ArrayList<Integer> mSexs;
	private SexAdapter mSexAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mSexs = new ArrayList<Integer>();
		mSexs.add(Integer.valueOf(UserInfoUtil.SEX_MAN));
		mSexs.add(Integer.valueOf(UserInfoUtil.SEX_WOMAN));
	}

	@Override
	protected void saveValue() {
		// TODO Auto-generated method stub
		if(mSexAdapter == null){
			return;
		}
		final int sex = mSexs.get(mSexAdapter.getSelectedPosition());
		Settings.System.putInt(getActivity().getContentResolver(), UserInfoUtil.SETTING_KEY_SEX,sex);
	}

	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_status_usrinfo_sex;
	}
	
	

	@Override
	protected void creatView() {
		// TODO Auto-generated method stub

		mListView = findViewById(R.id.userinfo_edit_list);
		mSexAdapter = new SexAdapter(getActivity());
		
		mListView.setAdapter(mSexAdapter);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				mSexAdapter.setSelectedPosition(position);
			}
		});
	}

	private class SexAdapter extends BaseAdapter{
		
		private Context mContext;
		private int SelectedPosition = mSexs.indexOf(Integer.valueOf(
				Settings.System.getInt(getActivity().getContentResolver(), UserInfoUtil.SETTING_KEY_SEX, UserInfoUtil.DEFAULT_SEX)));;
		
		public SexAdapter(Context mContext) {
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
			return mSexs.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mSexs.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return mSexs.get(position);
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
			
			final int sex = mSexs.get(position).intValue();
			switch (sex) {
				case UserInfoUtil.SEX_WOMAN:
					holder.mValue.setText(mContext.getString(R.string.userinfo_sex_woman));
					break;
				case UserInfoUtil.SEX_MAN:
				default:
					holder.mValue.setText(mContext.getString(R.string.userinfo_sex_man));
					break;
			}
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
