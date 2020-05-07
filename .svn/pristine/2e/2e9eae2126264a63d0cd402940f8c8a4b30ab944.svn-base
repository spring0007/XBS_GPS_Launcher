package com.sczn.wearlauncher.setting.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sczn.wearlauncher.R;


public class SeekBarWithMark extends LinearLayout {
    private static final String TAG = SeekBarWithMark.class.getSimpleName();

    private static final boolean DEBUG = false;

    private SeekBar mSeekBar;

    private LinearLayout mBottomLL;

    private int MAX = 80;

    private int mMarkItemNum = 9;

    private int mSpacing = MAX / (mMarkItemNum - 1);

    private int nowMarkItem;

    private String[] mMarkDescArray = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8"};

    private OnSelectItemListener mSelectItemListener;

    private boolean isFirstInflate = true;

    private boolean isShowPoint = true;

    private Drawable mProgressDrawable;

    private Drawable mThumbDrawable;

    private float mMarkTextSize = 13;

    private SeekBarWithMark(Context context) {
        super(context);
    }

    public SeekBarWithMark(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SeekBarWithMark(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SeekBarWithMark);

        final int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = a.getIndex(i);

            if (attr == R.styleable.SeekBarWithMark_sbw_markItemNum) {
                this.mMarkItemNum = a.getInteger(attr, 9);
                this.MAX = (mMarkItemNum - 1) * 10;
                this.mSpacing = MAX / (mMarkItemNum - 1);
            } else if (attr == R.styleable.SeekBarWithMark_sbw_markDescArray) {
                String content = a.getString(attr);
                if (!TextUtils.isEmpty(content)) {

                    String[] strings = content.split("\\|");
                    this.mMarkDescArray = strings;
                }
            } else if (attr == R.styleable.SeekBarWithMark_sbw_isShowPoint) {
                this.isShowPoint = a.getBoolean(attr, false);
            } else if (attr == R.styleable.SeekBarWithMark_sbw_progressDrawabler) {
                Drawable drawable = a.getDrawable(attr);
                if (drawable != null) {
                    this.mProgressDrawable = a.getDrawable(attr);
                }
            } else if (attr == R.styleable.SeekBarWithMark_sbw_thumbDrawable) {
                Drawable drawable = a.getDrawable(attr);
                if (drawable != null) {
                    this.mThumbDrawable = drawable;
                }
            } else if (attr == R.styleable.SeekBarWithMark_sbw_markTextSize) {
                this.mMarkTextSize = ScreenUtils.pxToDp(getContext(), a.getDimensionPixelSize(attr, 13));
            }
        }

        a.recycle();

        init();
    }

    public SeekBarWithMark(Context mContext, Builder builder) {
        this(mContext);

        this.mMarkItemNum = builder.mMarkItemNum;
        this.MAX = (mMarkItemNum - 1) * 10;
        this.mSpacing = MAX / (mMarkItemNum - 1);
        this.mMarkDescArray = builder.mMarkDescArray;
        this.isShowPoint = builder.isShowPoint;
        this.mProgressDrawable = builder.mProgressDrawable;
        this.mThumbDrawable = builder.mThumbDrawable;
        this.mMarkTextSize = builder.mMarkTextSize;

        if (builder.mLayoutParams != null) {
            this.setLayoutParams(builder.mLayoutParams);
        }

        init();
    }

    private void init() {

        if (this.mMarkDescArray.length < this.mMarkItemNum) {
            throw new RuntimeException("刻度的下标的名称数组length不能小于刻度的数目");
        }

        this.setOrientation(LinearLayout.VERTICAL);
        this.setGravity(Gravity.CENTER);

        this.mSeekBar = (SeekBar) View.inflate(getContext(), R.layout.seekbarwithmark, null);
        this.mSeekBar.setMax(MAX);

        if (mProgressDrawable != null) {
            this.mSeekBar.setProgressDrawable(mProgressDrawable);
        }

        if (mThumbDrawable != null) {
            this.mSeekBar.setThumb(mThumbDrawable);
        }

        LayoutParams mSeekBarLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        this.mSeekBar.setLayoutParams(mSeekBarLp);

        mSeekBar.setPadding(10, 0, 10, 0);

        this.mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            private int shouldInProgress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                nowMarkItem = Math.round(progress / mSpacing);

                shouldInProgress = mSpacing * nowMarkItem;

                SeekBarWithMark.this.mSeekBar.setProgress(shouldInProgress);

                if (DEBUG) {
                    Log.e(TAG, "progress---" + progress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (DEBUG) {
                    Log.e(TAG, "shouldInProgress---" + shouldInProgress);
                }

                if (mSelectItemListener != null) {
                    mSelectItemListener.selectItem(nowMarkItem, mMarkDescArray[nowMarkItem]);
                }
            }
        });

        mBottomLL = new LinearLayout(getContext());
        mBottomLL.setOrientation(LinearLayout.HORIZONTAL);

        LayoutParams mBottomLLLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        mBottomLLLp.setMargins(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()), 0, 0);

        mBottomLL.setLayoutParams(mBottomLLLp);

        mBottomLL.setPadding(this.mSeekBar.getPaddingLeft(), 0, this.mSeekBar.getPaddingRight(), 0);

        this.addView(this.mSeekBar);
        this.addView(mBottomLL);

        addAllMarkItem();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (isFirstInflate) {
            isFirstInflate = false;

            ViewGroup bottomLL = (ViewGroup) this.getChildAt(1);

            View view = bottomLL.getChildAt(0);

            int bottomLLWidth = bottomLL.getWidth();
            int width = view.getWidth();

            int i = bottomLLWidth - width;

            ViewGroup.LayoutParams layoutParams = this.mSeekBar.getLayoutParams();

            layoutParams.width = i;
            this.mSeekBar.setLayoutParams(layoutParams);
        }
    }


    private void addAllMarkItem() {
        if (mBottomLL == null) {
            throw new RuntimeException("装载刻度框的Layout不能为null");
        }

        mBottomLL.removeAllViews();

        Drawable drawablePoint = null;

        TextView textView = null;
        LayoutParams tvLp = null;
        for (int i = 0; i < mMarkItemNum; i++) {
            textView = new TextView(getContext());

            tvLp = new LayoutParams(0, LayoutParams.WRAP_CONTENT);

            tvLp.weight = 1;
            tvLp.gravity = Gravity.CENTER;

            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(tvLp);
            textView.setTextColor(Color.WHITE);

            if (DEBUG) {
                if (i == 0 || i == 2 || i == 4) {
                    textView.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                }
            }

            textView.setTextSize(mMarkTextSize);
            textView.setText(mMarkDescArray[i]);

            if (isShowPoint) {
                if (drawablePoint == null) {
                    drawablePoint = getContext().getResources().getDrawable(R.drawable.point_min_gray);
                }
                textView.setCompoundDrawablesWithIntrinsicBounds(null, drawablePoint, null, null);
                textView.setCompoundDrawablePadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, getResources().getDisplayMetrics()));
            }

            mBottomLL.addView(textView);
        }

    }

    public SeekBar getSeekBar() {
        return mSeekBar;
    }


    public void setOnSelectItemListener(OnSelectItemListener l) {
        this.mSelectItemListener = l;
    }

    public interface OnSelectItemListener {
        void selectItem(int nowSelectItemNum, String val);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        mSeekBar.setEnabled(enabled);
    }


    public void selectMarkItem(int selectItemNum) {

        nowMarkItem = selectItemNum;

        int shouldInProgress = mSpacing * selectItemNum;

        mSeekBar.setProgress(shouldInProgress);
    }


    public int getNowMarkItem() {
        return nowMarkItem;
    }


    public static class Builder {
        private final Context mContext;


        private int mMarkItemNum = 9;

        private String[] mMarkDescArray;

        private boolean isShowPoint = true;

        private ViewGroup.LayoutParams mLayoutParams;

        private Drawable mProgressDrawable;

        private Drawable mThumbDrawable;

        public float mMarkTextSize = 13;

        public Builder(Context context) {
            this.mContext = context;
        }

        public int getMarkItemNum() {
            return mMarkItemNum;
        }

        public Builder setMarkItemNum(int mMarkItemNum) {
            this.mMarkItemNum = mMarkItemNum;
            return this;
        }

        public String[] getmMarkDescArray() {
            return mMarkDescArray;
        }

        public Builder setmMarkDescArray(String[] mMarkDescArray) {
            this.mMarkDescArray = mMarkDescArray;
            return this;
        }

        public boolean isShowPoint() {
            return isShowPoint;
        }

        public Builder setIsShowPoint(boolean isShowPoint) {
            this.isShowPoint = isShowPoint;
            return this;
        }

        public ViewGroup.LayoutParams getLayoutParams() {
            return mLayoutParams;
        }

        public Builder setLayoutParams(ViewGroup.LayoutParams mLayoutParams) {
            this.mLayoutParams = mLayoutParams;
            return this;
        }

        public Drawable getThumbDrawable() {
            return mThumbDrawable;
        }

        public Builder setThumbDrawable(Drawable mThumbDrawable) {
            this.mThumbDrawable = mThumbDrawable;
            return this;
        }

        public Drawable getProgressDrawable() {
            return mProgressDrawable;
        }

        public Builder setProgressDrawable(Drawable mProgressDrawable) {
            this.mProgressDrawable = mProgressDrawable;
            return this;
        }

        public float getMarkTextSize() {
            return mMarkTextSize;
        }

        public Builder setMarkTextSize(float mMarkTextSize) {
            this.mMarkTextSize = mMarkTextSize;
            return this;
        }

        public SeekBarWithMark create() {
            SeekBarWithMark seekBarWithMark = new SeekBarWithMark(mContext, this);

            return seekBarWithMark;
        }

    }

}
