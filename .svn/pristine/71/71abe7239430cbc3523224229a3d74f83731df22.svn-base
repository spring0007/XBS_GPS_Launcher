package com.sczn.wearlauncher.contact.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.sczn.wearlauncher.R;

/**
 * Created by weisun3 on 2017/11/27 0027.
 */

public class MYScrollView extends ScrollView {
    public static final int SCROLL_UP = 1;
    public static final int SCROLL_DOWN = 2;
    private OnScrollChangedListener mOnScrollChangedListener;


    public MYScrollView(Context context) {
        this(context, null);
    }

    public MYScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.scrollViewStyle);
    }

    public MYScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(new ContextWrapperEdgeEffect(context), attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        int color = context.getResources().getColor(R.color.white);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EdgeEffectView, defStyle, 0);
            color = a.getColor(R.styleable.EdgeEffectView_edgeeffect_color, color);
            a.recycle();
        }
        setEdgeEffectColor(color);
    }

    public void setEdgeEffectColor(int edgeEffectColor) {
        ((ContextWrapperEdgeEffect) getContext()).setEdgeEffectColor(edgeEffectColor);
    }

    @Override
    protected void onScrollChanged(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        super.onScrollChanged(scrollX, scrollY, oldScrollX, oldScrollY);
        if (mOnScrollChangedListener != null) {
            int scrollDirection;
            if (scrollY > oldScrollY) {
                scrollDirection = SCROLL_UP;
            } else {
                scrollDirection = SCROLL_DOWN;
            }
            mOnScrollChangedListener.onScrollChanged(this, scrollDirection, scrollX, scrollY, oldScrollX, oldScrollY);
        }
    }

    public void setOnScrollChangedListener(OnScrollChangedListener listener) {
        mOnScrollChangedListener = listener;
    }

    public interface OnScrollChangedListener {
        void onScrollChanged(ScrollView scrollView, int scrollDirection, int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }
}
