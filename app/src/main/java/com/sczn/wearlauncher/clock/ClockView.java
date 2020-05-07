package com.sczn.wearlauncher.clock;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

public class ClockView extends View implements Runnable{
	private static final String TAG = ClockView.class.getSimpleName();
	
	private static final int FRESH_FREQUENCY = 50;
	private Paint mPaint;
    private ModelClockSkin mClockSkin;
    private Thread mThread;
    private int sleepTime = FRESH_FREQUENCY;
    private boolean isRunning = false;
    
    private boolean isDrawing;
    private ReDrawHandler mReDrawHandler;
    
    public ClockView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setDither(true);
        this.mPaint.setColor(Color.BLACK);
        
        isDrawing = false;
        mReDrawHandler = new ReDrawHandler(this);
	}
    
    public void setFreshFreQuency(int duration){
    	this.sleepTime = duration;
    }
    
    public void setClockSkin(ModelClockSkin paramSkin){
        this.mClockSkin = paramSkin;
        //startDraw();
    }
    
    public void startDraw(){
    	isRunning = true;
    	if(mThread != null && mThread.isAlive()){
    		return;
    	}
    	mThread = new Thread(this);
    	mThread.start();
    }
    public void stopDraw(){
    	isRunning = false;
    }

    private void DrawByType(Canvas paramCanvas){
        if (this.mClockSkin == null) return;
        this.mClockSkin.drawClock(paramCanvas, getContext());
    }

    protected void onDraw(Canvas paramCanvas){
        //super.onDraw(paramCanvas);
    	isDrawing = true;
        paramCanvas.drawPaint(this.mPaint);
        try{
            DrawByType(paramCanvas);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        isDrawing = false;
    }
    
    public void reDraw(){
    	//MxyLog.d(this, "reDraw" + "--isDrawing=" + isDrawing);
    	if(!isDrawing){
    		invalidate();
    	}
    }

    public void run(){
        while (isRunning) {
            try {
            	if(isAttachedToWindow()){
            		if(mReDrawHandler != null){
            			mReDrawHandler.removeMessages(ReDrawHandler.MSG_FRESH);
            			mReDrawHandler.sendEmptyMessage(ReDrawHandler.MSG_FRESH);
            		}
            	}
                Thread.sleep(this.sleepTime);
            } catch (InterruptedException localInterruptedException) {

            }
        }
    }
    
    static class ReDrawHandler extends Handler{
    	static final int MSG_FRESH = 1;
    	private WeakReference<ClockView> mContent;
    	
    	public ReDrawHandler(ClockView content) {
			// TODO Auto-generated constructor stub
    		this.mContent = new WeakReference<ClockView>(content);
		}
    	
    	@Override
    	public void handleMessage(Message msg) {
    		// TODO Auto-generated method stub
    		
    		final ClockView content = mContent.get();
    		if(content == null){
    			return;
    		}
    		if(MSG_FRESH == msg.what){
    			content.reDraw();
    		}
    	}
    }
    
}
