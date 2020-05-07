package com.sczn.wearlauncher.status.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.view.ClickIcon;
import com.sczn.wearlauncher.base.view.ScrollerTextView;

public class StatusIconWithText extends LinearLayout {

    public static final int INVALUADE_RESOURCE = -1;

    protected ClickIcon mIcon;
    protected TextView mText;
    private int StringId;
    private int ImageId;

    private float TextSize;
    private int TextPadding;

    public StatusIconWithText(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StatusIcon);

        TextSize = a.getDimensionPixelSize(R.styleable.StatusIcon_textsize, getResources().getDimensionPixelSize(R.dimen.text_size_20));
        TextPadding = a.getDimensionPixelSize(R.styleable.StatusIcon_textpadding, getResources().getDimensionPixelSize(R.dimen.status_icon_text_padding));
        StringId = a.getResourceId(R.styleable.StatusIcon_stringId, INVALUADE_RESOURCE);
        ImageId = a.getResourceId(R.styleable.StatusIcon_imageId, INVALUADE_RESOURCE);
        a.recycle();

        mIcon = getIcon();
        mText = getTextView();
    }

    protected void onLocalChanged() {
        if (INVALUADE_RESOURCE != StringId) {
            mText.setText(StringId);
        }
    }

    /**
     * 文字
     *
     * @return
     */
    protected TextView getTextView() {
        if (mText == null) {
            mText = new ScrollerTextView(getContext());
            mText.setTextColor(Color.WHITE);
            if (INVALUADE_RESOURCE != StringId) {
                mText.setText(StringId);
            }
            mText.setTextSize(TextSize);
            mText.setPadding(0, TextPadding, 0, 0);
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            addView(mText, params);
            mText.setPadding(4, 0, 4, 0);
        }
        return mText;
    }

    /**
     * 图片
     *
     * @return
     */
    protected ClickIcon getIcon() {
        if (mIcon == null) {
            mIcon = new ClickIcon(getContext());
            if (INVALUADE_RESOURCE != ImageId) {
                mIcon.setImageResource(ImageId);
            }
            mIcon.setScaleType(ScaleType.CENTER_INSIDE);
            //LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            //mIcon.setScaleType(ScaleType.FIT_XY);
            LayoutParams params = new LayoutParams(78, 58);
            mIcon.setLayoutParams(params);
            addView(mIcon, params);
        }
        return mIcon;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        // TODO Auto-generated method stub
        mIcon.setOnClickListener(l);
    }

    public void startFresh() {
        // TODO Auto-generated method stub
    }

    public void stopFresh() {
        // TODO Auto-generated method stub
    }
}
