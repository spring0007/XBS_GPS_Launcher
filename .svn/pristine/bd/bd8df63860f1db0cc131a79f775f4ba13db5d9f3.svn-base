package com.sczn.wearlauncher.notification;

import java.util.ArrayList;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.util.DateFormatUtil;
import com.sczn.wearlauncher.base.util.SysServices;
import com.sczn.wearlauncher.base.view.ScrollerTextView;

import android.content.Context;
import android.service.notification.StatusBarNotification;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterNtfMain extends Adapter<AdapterNtfMain.NotificaionMainHolder> {
	private static final String TAG = AdapterNtfMain.class.getSimpleName();
	
	public static final int ITEM_TYPE_WATCH = 0;
	public static final int ITEM_TYPE_PHONE = 1;
	private ArrayList<ModelNotification> mNotificationList;
	private INotificationItemClick mINotificationItemClick;
	
	public AdapterNtfMain(Context mContext,
			ArrayList<ModelNotification> mNotificationList,
			INotificationItemClick mINotificationItemClick) {
		super();
		this.mNotificationList = mNotificationList;
		this.mINotificationItemClick = mINotificationItemClick;
	}


	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return mNotificationList.size();
	}
	
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if(ModelNotification.NTF_TYPE_WATCH == mNotificationList.get(position).getNtfType()){
			return ITEM_TYPE_WATCH;
		}else if(ModelNotification.NTF_TYPE_PHONE== mNotificationList.get(position).getNtfType()){
			return ITEM_TYPE_PHONE;
		}
		else{
			return ITEM_TYPE_WATCH;
		}
	}
	

	@Override
	public void onBindViewHolder(NotificaionMainHolder arg0, int arg1) {
		// TODO Auto-generated method stub
		if(arg1 > mNotificationList.size()){
			return;
		}
		ModelNotification item = mNotificationList.get(arg1);
		if(ModelNotification.NTF_TYPE_WATCH == item.getNtfType()){
			final StatusBarNotification notification = item.getWatchNtf();
			arg0.mIcon.setImageResource(notification.getNotification().icon);
			arg0.mTime.setText(DateFormatUtil.getTimeString(DateFormatUtil.HM,
					notification.getPostTime()));
			arg0.mMessage.setText(notification.getNotification().tickerText);
		}else if(ModelNotification.NTF_TYPE_PHONE== item.getNtfType()) {
	
			final ModlePhoneMessage message = item.getPhoneNtf().getmMessageList().get(0);
			//arg0.mIcon.setImageDrawable(SysServices.getAppIcon(mContext, message.getPackageName()));
			arg0.mAppName.setText(message.getAppName());
			arg0.mTime.setText(message.getTime());
			arg0.mMessage.setText(message.getTickerText());
		}else{
			return;
		}
		arg0.mMessage.setTag(item);
		arg0.mMessage.setOnClickListener(new OnItemClick(arg1));
	}

	@Override
	public NotificaionMainHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		final View view = LayoutInflater.from(arg0.getContext())
				.inflate(R.layout.item_notification_main, arg0, false);
		switch (arg1) {
			case ITEM_TYPE_PHONE:
				view.findViewById(R.id.notification_main_icon).setVisibility(View.GONE);
				break;
	
			default:
				break;
		}
		return new NotificaionMainHolder(view);
	}
	
	public class NotificaionMainHolder extends ViewHolder{

		private ImageView mIcon;
		private TextView mAppName;
		private TextView mTime;
		private ScrollerTextView  mMessage;

		public NotificaionMainHolder(View arg0) {
			super(arg0);
			mIcon = (ImageView) arg0.findViewById(R.id.notification_main_icon);
			mTime = (TextView) arg0.findViewById(R.id.notification_main_time);
			mAppName = (TextView) arg0.findViewById(R.id.notification_main_app);
			mMessage = (ScrollerTextView ) arg0.findViewById(R.id.notification_main_message);
		}	
	}

	private class OnItemClick implements OnClickListener{
		
		private final int position;

		public OnItemClick(int position) {
			super();
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(mINotificationItemClick != null){
				mINotificationItemClick.onNotificationClick(v,position);
			}
		}
		
	}
	public interface INotificationItemClick{
		public void onNotificationClick(View view,int position);
	}
}
