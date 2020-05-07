package com.sczn.wearlauncher.base.view;

import com.sczn.wearlauncher.base.adapter.LoopViewPageAdapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class HorizalViewPager extends ViewPager{
	
	private final static String TAG = "HorizalViewPager";
	
	private IHorizalViewPagerSelected mHorizalViewPagerSelected;
	private int mWidth;
	public boolean isScroll=true;

    

	public HorizalViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setOnPageChangeListener(new LoopChangeListener());
	}
	
	@Override
	protected void onMeasure(int arg0, int arg1) {
		// TODO Auto-generated method stub
		super.onMeasure(arg0, arg1);
		mWidth = getMeasuredWidth();
	}
    
    @Override
	public void setCurrentItem(int item, boolean smoothScroll) {
		// TODO Auto-generated method stub
    	super.setCurrentItem(item, smoothScroll);
    	
	}

    private boolean setToLoop(int position){
		if(!isLoop()){
			return false;
		}

		//MxyLog.d(TAG, "setToLoop position=" + position + "--getAdapter().getCount()=" + getAdapter().getCount());
		if(position == getAdapter().getCount() - 1){
			setCurrentItem(1,false);
			return true;
		}else if(position == 0){
			setCurrentItem(getAdapter().getCount() - 2,false);
			return true;
		}
		return false;
	}

    private boolean isLoop(){
    	if(!(getAdapter() instanceof LoopViewPageAdapter)){
			return false;
		}
    	final LoopViewPageAdapter adapter = (LoopViewPageAdapter) getAdapter();
    	return adapter.isLoop();
    }
    public boolean isScroll() {
        return isScroll;
    }
    public void setScroll(boolean scroll) {
        isScroll = scroll;
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isScroll){
            return super.onTouchEvent(ev);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isScroll){
            return super.onInterceptTouchEvent(ev);
        }
        return false;

    }
	private class LoopChangeListener implements OnPageChangeListener{

    	private int scrollState = SCROLL_STATE_IDLE;
    	private int targetPosition = 0;
    	
    	private int lastPage;
    	
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			scrollState = arg0;
			//MxyLog.d(this, "onPageScrollStateChanged--arg0=" + arg0 + "--targetPosition=" + targetPosition);
			if(SCROLL_STATE_IDLE == arg0){
				setToLoop(targetPosition);
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			//MxyLog.i(this, "onPageScrolled--arg0=" + arg0 + "arg1=" + arg1 + "arg2=" + arg2);
			if(!isLoop()){
				return;
			}	
			if(arg2 == 0){
				lastPage = arg0;
				if(arg0 == 0){
					//setCurrentItem(getAdapter().getCount() - 2,false);
				}else if(arg0 == getAdapter().getCount() - 1){
					//setCurrentItem(1,false);
				}
			}else if(lastPage != arg0){
				if(arg0 == 0){
					//setCurrentItem(getAdapter().getCount() - 1,false);
				}
			}else{
				if(arg0 == getAdapter().getCount() - 2){
					//setCurrentItem(0,false);
				}
			}
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			targetPosition = arg0;
			//MxyLog.d(this, "onPageSelected" + arg0 + "--scrollState=" + scrollState);
			if(mHorizalViewPagerSelected != null){
				mHorizalViewPagerSelected.horizalViewPageSelected(arg0);
			}
		}
    }
	
	public void setHorizalViewPagerSelected(IHorizalViewPagerSelected listen){
		this.mHorizalViewPagerSelected = listen;
	}
	public interface IHorizalViewPagerSelected{
		public void horizalViewPageSelected(int index);
	}

}
