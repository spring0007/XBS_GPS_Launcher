package com.sczn.wearlauncher.setting.util;

/**
 * 自定义一个假装文本标签能够一直获取焦点的标签
 * Created by Zhang Jiaofa on 2017/11/16.
 */

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class FocusedTextView extends AppCompatTextView {

    public FocusedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        if (focused)
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override
    public void onWindowFocusChanged(boolean focused) {
        if (focused)
            super.onWindowFocusChanged(focused);
    }

    @Override
    public boolean isFocused() {
        return true;//一直返回true，假装这个控件一直获取着焦点
    }
}
