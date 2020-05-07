package com.sczn.wearlauncher.base.view;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.app.MxyLog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class ClickIcon extends ImageView{
	private static final String TAG = ClickIcon.class.getSimpleName();
	
	private AnimClickListem mAnimClickListem;

	public ClickIcon(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mAnimClickListem = new AnimClickListem();
	}

	public ClickIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		mAnimClickListem = new AnimClickListem();
	}
	
	public void startFresh(){
		
	}
	public void stopFresh(){
		
	}

	@Override
	public void setPressed(boolean pressed) {
		// TODO Auto-generated method stub
		super.setPressed(pressed);
		/*
		if(pressed){
			setScaleX(0.8f);
			setScaleY(0.8f);
		}else{
			setScaleX(1.0f);
			setScaleY(1.0f);
		}*/
	}



	@Override
	public final void setOnClickListener(OnClickListener l) {
		// TODO Auto-generated method stub
		//MxyLog.d(this, "setOnClickListener" + "--l=" + l);
		mAnimClickListem.setListen(l);
		super.setOnClickListener(mAnimClickListem);
	}
	
	
	private class AnimClickListem implements OnClickListener{
		private OnClickListener listen = null;
		
		private Animation animation;
		
		public AnimClickListem() {
			animation = AnimationUtils.loadAnimation(getContext(), R.anim.icon_click);
			animation.setAnimationListener(new ClickAnimListen());
		}
		
		public void setListen(OnClickListener listen){
			this.listen = listen;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			startAnimation(animation);
		}
		
		private class ClickAnimListen implements AnimationListener{
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				MxyLog.i(TAG, "onAnimationEnd" + "listen=" + listen);
				if(listen != null){
					listen.onClick(ClickIcon.this);
				}
			}

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
		}
	}
	
}
