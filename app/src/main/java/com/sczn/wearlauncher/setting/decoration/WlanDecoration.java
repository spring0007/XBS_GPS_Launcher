package com.sczn.wearlauncher.setting.decoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 网络热点列表的分割线的自定义
 * Created by Zhang Jiaofa on 2017/9/18.
 */

public class WlanDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint;

    public WlanDecoration() {
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#000000"));
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin +
                    Math.round(ViewCompat.getTranslationY(child));
            final int bottom = top + 1;
            c.drawRect(left + 5, top, right - 5, bottom, mPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(5, 0, 5, 0);
    }
}
