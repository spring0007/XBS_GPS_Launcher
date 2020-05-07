package com.sczn.wearlauncher.card.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.sczn.wearlauncher.R;

public class CircleBarView extends View {

    public static final float START_ANGLE = 120.0f;
    public static final float MAX_OFFSET = 300f;

    private RectF mRectF;
    private Paint mCirclePaint;
    private int mBarColorActive;
    private int mBarColorBg;
    private int mBarWidth;
    private int mBarPaddingOut;
    private int mBarRadius;
    private int mSize;
    private float mBarValueActive;


    public CircleBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleBarView);
        mBarColorBg = a.getColor(R.styleable.CircleBarView_progress_colorBg, Color.GRAY);
        mBarColorActive = a.getColor(R.styleable.CircleBarView_progress_color, Color.DKGRAY);
        mBarWidth = a.getDimensionPixelSize(R.styleable.CircleBarView_stroke_width, R.dimen.circle_bar_stroke_wodth);
        mBarPaddingOut = a.getDimensionPixelSize(R.styleable.CircleBarView_padingOut, R.dimen.circle_bar_padding_out);
        a.recycle();

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setDither(true);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        mCirclePaint.setStrokeWidth(mBarWidth);

        mRectF = new RectF();

        //mArrowPaint = new Paint();
        //mArrowPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mSize = Math.min(getMeasuredHeight(), getMeasuredWidth());
        setMeasuredDimension(mSize, mSize);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mRectF.top = top + mBarPaddingOut;
        mRectF.left = left + mBarPaddingOut;
        mRectF.right = right - mBarPaddingOut;
        mRectF.bottom = bottom - mBarPaddingOut;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mCirclePaint.setColor(mBarColorBg);
        canvas.drawArc(mRectF, START_ANGLE, MAX_OFFSET, false, mCirclePaint);
        mCirclePaint.setColor(mBarColorActive);
        canvas.drawArc(mRectF, START_ANGLE, mBarValueActive, false, mCirclePaint);
    }

    /**
     * 设置值
     *
     * @param value
     */
    public void setValue(double value) {
        if (value > MAX_OFFSET) {
            value = MAX_OFFSET;
        }
        mBarValueActive = (float) value;
        invalidate();
    }
}
