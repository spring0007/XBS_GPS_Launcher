package com.sczn.wearlauncher.card.healthalarm;

import com.sczn.wearlauncher.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListItemAlarm extends HorizontalScrollView {

	private TextView mDelete;
	
    private int mScrollWidth;	//记录滚动条滚动的距离
    private boolean canTouch;
    private boolean isOpen = false;
    
    private View mContent;
	
	public ListItemAlarm(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.canTouch = true;
		this.isOpen = false;
		
		setScrollBarSize(0);
	}
	
	@Override
	public void onFinishTemporaryDetach() {
		// TODO Auto-generated method stub
		super.onFinishTemporaryDetach();
		
		mContent = findViewById(R.id.scroller_content);
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		mDelete = (TextView) findViewById(R.id.alarm_delete);
		mDelete.setEnabled(false);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		
		mScrollWidth = mDelete.getMeasuredWidth();
		scrollerToOpen(false);
	}
	
	public boolean isCanTouch() {
		return canTouch;
	}

	public void setCanTouch(boolean canTouch) {
		this.canTouch = canTouch;
	}
	
	 @Override
	 public boolean onTouchEvent(MotionEvent ev) {
		 if(!canTouch){
			 return true;
		 }
		 int action = ev.getAction();
		 switch (action) {
			case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
				break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            	changeScrollx();
                return true;
			default:
				break;
			}
		 return super.onTouchEvent(ev);
	 }
	 
	 public void changeScrollx() {
		 if(getScrollX() >= (mScrollWidth / 2)){
			 this.smoothScrollTo(mScrollWidth, 0);
			 changeOpenState(true);
		 }else{
			 this.smoothScrollTo(0, 0);
			 changeOpenState(false);
		 }
	 }
	 
	 private void changeOpenState(boolean isOpen){
		 this.isOpen = isOpen;
		 mDelete.setEnabled(isOpen);
	 }
	 
	 public void scrollerToOpen(boolean isOpen){
		 this.scrollTo(isOpen ? mScrollWidth : 0, 0);
	 }
}
