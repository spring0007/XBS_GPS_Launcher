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

public class FragmentEditWeight extends UserInfoEditFragment {
	
	public static FragmentEditWeight newInstance(){
		return new FragmentEditWeight();
	}
	
	private ListView mListView;
	private ArrayList<Integer> mWeights;
	private WeightAdapter mWeightAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mWeights = new ArrayList<Integer>();
		
		for(int i = 10 ; i < 250; i++){
			mWeights.add(Integer.valueOf(i));
		}
	}

	@Override
	protected void saveValue() {
		// TODO Auto-generated method stub
		if(mWeightAdapter == null){
			return;
		}
		final int weight = mWeights.get(mWeightAdapter.getSelectedPosition());
		Settings.System.putInt(getActivity().getContentResolver(), UserInfoUtil.SETTING_KEY_WEIGHT,weight);
	}

	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_status_userinfo_wheight;
	}
	
	

	@Override
	protected void creatView() {
		// TODO Auto-generated method stub

		mListView = findViewById(R.id.userinfo_edit_list);
		mWeightAdapter = new WeightAdapter(getActivity());
		
		mListView.setAdapter(mWeightAdapter);
		
		mListView.setSelection(mWeightAdapter.getSelectedPosition());
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				mWeightAdapter.setSelectedPosition(position);
			}
		});
	}

	private class WeightAdapter extends BaseAdapter{
		
		private Context mContext;
		private int SelectedPosition = mWeights.indexOf(Integer.valueOf(
				Settings.System.getInt(getActivity().getContentResolver(), UserInfoUtil.SETTING_KEY_WEIGHT, UserInfoUtil.DEFAULT_WEIGHT)));;
		
		public WeightAdapter(Context mContext) {
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
			return mWeights.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mWeights.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return mWeights.get(position);
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
			
			final int weight = mWeights.get(position).intValue();
			
			holder.mValue.setText(String.format(mContext.getString(R.string.userinfo_weight_value), weight));
			
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
