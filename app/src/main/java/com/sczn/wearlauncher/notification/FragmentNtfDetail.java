package com.sczn.wearlauncher.notification;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.absDialogFragment;
import com.sczn.wearlauncher.base.view.MyRecyclerView;
import com.sczn.wearlauncher.base.view.ScrollerTextView;
import com.sczn.wearlauncher.base.view.WrapLinearLayoutManager;

public class FragmentNtfDetail extends absDialogFragment implements OnClickListener{
	private static final String TAG = FragmentNtfDetail.class.getSimpleName();
	
	private static final String ARG_NOTIFICATIONS = "notification_detail";
	public static FragmentNtfDetail newInstance(ModelPhonePkgNotification ntf){
		
		final FragmentNtfDetail fragment = new FragmentNtfDetail();
		Bundle arg = new Bundle();
		arg.putParcelable(ARG_NOTIFICATIONS, ntf);
		fragment.setArguments(arg);
		return fragment;
	}

	private ModelPhonePkgNotification notificationDetails;
	
	private ScrollerTextView mPkgName;
	private MyRecyclerView mNotifications;
	private TextView mNotificationsClear;
	
	private AdapterNftDetail mNotificationDetailAdapter;
	public ICloseDetail mCloseDetail;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if(getArguments() != null){
			notificationDetails = (ModelPhonePkgNotification) getArguments().get(ARG_NOTIFICATIONS);
		}
		
		if(notificationDetails == null){
			dismissAllowingStateLoss();
		}
	}
	
	public void setCloseListen(ICloseDetail listen){
		this.mCloseDetail = listen;
	}
	
	public String getDetailPkgName(){
		return notificationDetails.getPkgName();
	}
	
	public void freshDetail(){
		if(mNotificationDetailAdapter != null){
			mNotificationDetailAdapter.notifyItemInserted(0);
		}
	}

	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_notification_detail;
	}

	@Override
	protected void creatView() {
		// TODO Auto-generated method stub
		initView();
		initData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mPkgName = findViewById(R.id.notification_detail_title);
		mNotifications = findViewById(R.id.notification_detail_list);
		mNotificationsClear = findViewById(R.id.notification_detail_clear);
	}

	private void initData() {
		// TODO Auto-generated method stub
		if(notificationDetails == null){
			return;
		}
		mPkgName.setText(notificationDetails.getAppName());
		mNotificationsClear.setOnClickListener(this);
		mNotificationDetailAdapter = new AdapterNftDetail(notificationDetails);
		mNotifications.setLayoutManager(new WrapLinearLayoutManager(getActivity()));
		mNotifications.setAdapter(mNotificationDetailAdapter);
	}

	@Override
	protected void destorytView() {
		// TODO Auto-generated method stub

	}
	
	private void finishWithClean(){
		if(mCloseDetail != null){
			mCloseDetail.closeWithClear(notificationDetails);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.notification_detail_clear:
				finishWithClean();
				break;
	
			default:
				break;
		}
	}
	
	public interface ICloseDetail{
		public void closeWithClear(ModelPhonePkgNotification notificationDetails);
	}

}
