package com.sczn.wearlauncher.status.view;

import java.lang.ref.WeakReference;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.status.util.RaiseScreenUtil;
import com.sczn.wearlauncher.status.util.RaiseScreenUtil.IRaiseScreenState;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

public class RaiseScreen extends StatusIconWithText implements IRaiseScreenState{

	private RaiseScreenUtil mRaiseScreenUtil;
	private ObserverHandler mObserverHandler;
	
	public RaiseScreen(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		mObserverHandler = new ObserverHandler(this);
		initView();
	}
	
	@Override
	public void startFresh() {
		// TODO Auto-generated method stub
		super.startFresh();
		mRaiseScreenUtil.addListen(this);
	}
	
	@Override
	public void stopFresh() {
		// TODO Auto-generated method stub
		super.stopFresh();
		mRaiseScreenUtil.deldteListen(this);
	}

	private void initView() {
		// TODO Auto-generated method stub
		mRaiseScreenUtil = RaiseScreenUtil.getInstance();
		freshUi(mRaiseScreenUtil.getState());
		setOnClickListener(onRaiseScreenIcon);
	}
	
	private void freshUi(boolean isOpen){
		if(isOpen){
			mIcon.setImageResource(R.drawable.statu_icon_raisescreen_on);
		}else{
			mIcon.setImageResource(R.drawable.statu_icon_raisescreen_off);
		}
	}
	
	private OnClickListener onRaiseScreenIcon = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mRaiseScreenUtil.switchState();
		}
	};

	@Override
	public void onRaiseScreenState(boolean isopen) {
		// TODO Auto-generated method stub
		//freshUi(isopen);

		mObserverHandler.sendEmptyMessage(isopen ? 
				ObserverHandler.MSG_OPEN : ObserverHandler.MSG_CLOSE);
	}
	
	private static class ObserverHandler extends Handler{
		

		private static final int MSG_OPEN = 0;
		private static final int MSG_CLOSE = 1;

		private static  WeakReference<RaiseScreen> mIcon;
		
		private ObserverHandler(RaiseScreen icon) {  
			mIcon = new WeakReference<RaiseScreen>(icon);  
		}
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			RaiseScreen icon = mIcon.get();
			if(icon == null || !icon.isAttachedToWindow()){
				return;
			}
			switch (msg.what) {
				case MSG_OPEN:
					icon.freshUi(true);
					break;
				case MSG_CLOSE:
					icon.freshUi(false);
					break;
				default:
					break;
			}
		}
		
	}

}
