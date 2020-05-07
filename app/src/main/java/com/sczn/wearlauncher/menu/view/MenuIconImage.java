package com.sczn.wearlauncher.menu.view;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.view.ClickIcon;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
 
public class MenuIconImage extends ClickIcon {
 
  private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;
 
  private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
  private static final int COLORDRAWABLE_DIMENSION = 1;
 
  private static final int DEFAULT_BORDER_WIDTH = 0;
  private static final int DEFAULT_BORDER_COLOR = Color.BLACK;
 
  private final RectF mDrawableRect = new RectF();
  private final RectF mBorderRect = new RectF();
 
  private final Matrix mShaderMatrix = new Matrix();
  private final Paint mBitmapPaint = new Paint();
  private final Paint mBorderPaint = new Paint();
 
  private int mBorderColor = DEFAULT_BORDER_COLOR;
  private int mBorderWidth = DEFAULT_BORDER_WIDTH;
 
  private Bitmap mBitmap;
  private BitmapShader mBitmapShader;
  private int mBitmapWidth;
  private int mBitmapHeight;
 
  private float mDrawableRadius;
  private float mBorderRadius;
 
  private boolean mReady;
  private boolean mSetupPending;
  
  private boolean mCropToCircle;
  private int mSize;
 
  public MenuIconImage(Context context) {
    super(context);
  }
 
  public MenuIconImage(Context context, AttributeSet attrs) {
    super(context, attrs);
    
    super.setScaleType(SCALE_TYPE);
    
    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MenuMore);
 
    mCropToCircle = a.getBoolean(R.styleable.MenuMore_CropToCircle, false);
    mBorderWidth = a.getDimensionPixelSize(R.styleable.MenuMore_BorderWidth, DEFAULT_BORDER_WIDTH);
    mBorderColor = getResources().getColor(a.getResourceId(R.styleable.MenuMore_BorderColor, R.color.blue));
 
    a.recycle();
 
    mReady = true;
 
    if (mSetupPending) {
      setup();
      mSetupPending = false;
    }
  }

  
  @Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		mSize = Math.min(getMeasuredWidth(), getMeasuredHeight());
		setMeasuredDimension(mSize, mSize);
	}
 
  @Override
  public ScaleType getScaleType() {
    return SCALE_TYPE;
  }
 
  @Override
  public void setScaleType(ScaleType scaleType) {
    if (scaleType != SCALE_TYPE) {
      throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));
    }
  }
 
  @Override
  protected void onDraw(Canvas canvas) {
	  if(!mCropToCircle){
		  super.onDraw(canvas);
		  return;
	  }
	  
    if (getDrawable() == null) {
      return;
    }
 
    canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBorderRadius, mBorderPaint);
    
    canvas.drawCircle(getWidth() / 2, getHeight() / 2, mDrawableRadius, mBitmapPaint);
  }
 
  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    setup();
  }
 
  @Override
  public void setImageBitmap(Bitmap bm) {
    super.setImageBitmap(bm);
    mBitmap = bm;
    setup();
  }
 
  @Override
  public void setImageDrawable(Drawable drawable) {
    super.setImageDrawable(drawable);
    mBitmap = getBitmapFromDrawable(drawable);
    setup();
  }
 
  @Override
  public void setImageResource(int resId) {
    super.setImageResource(resId);
    mBitmap = getBitmapFromDrawable(getDrawable());
    setup();
  }
 
  private Bitmap getBitmapFromDrawable(Drawable drawable) {
    if (drawable == null) {
      return null;
    }
 
    if (drawable instanceof BitmapDrawable) {
      return ((BitmapDrawable) drawable).getBitmap();
    }
 
    try {
      Bitmap bitmap;
 
      if (drawable instanceof ColorDrawable) {
        bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
      } else {
        bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
      }
 
      Canvas canvas = new Canvas(bitmap);
      drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
      drawable.draw(canvas);
      return bitmap;
    } catch (OutOfMemoryError e) {
      return null;
    }
  }
 
  private void setup() {
	  if(!mCropToCircle){
		  return;
	  }
    if (!mReady) {
      mSetupPending = true;
      return;
    }
 
    if (mBitmap == null) {
      return;
    }
 
    mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
 
    mBitmapPaint.setAntiAlias(true);
    mBitmapPaint.setShader(mBitmapShader);
    
    //设置图标透明度
    mBitmapPaint.setAlpha(192);
 
    mBorderPaint.setStyle(Paint.Style.STROKE);
    mBorderPaint.setAntiAlias(true);
    mBorderPaint.setColor(mBorderColor);
    
    //设置边框为实心圆
    mBorderRadius = mSize/4;
    mBorderPaint.setStrokeWidth(mSize/2);
    mBorderRect.set(0, 0, mSize, mSize);
 
    mBitmapHeight = mBitmap.getHeight();
    mBitmapWidth = mBitmap.getWidth();
 
    mDrawableRect.set(mBorderWidth, mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);
    mDrawableRadius = Math.min(mDrawableRect.height() / 2, mDrawableRect.width() / 2);
 
    updateShaderMatrix();
    invalidate();
  }
 
  private void updateShaderMatrix() {
    float scale;
    float dx = 0;
    float dy = 0;
 
    mShaderMatrix.set(null);
 
    if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
      scale = mDrawableRect.height() / (float) mBitmapHeight;
      dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
    } else {
      scale = mDrawableRect.width() / (float) mBitmapWidth;
      dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
    }
 
    mShaderMatrix.setScale(scale, scale);
    mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth, (int) (dy + 0.5f) + mBorderWidth);
 
    mBitmapShader.setLocalMatrix(mShaderMatrix);
  }
 
}