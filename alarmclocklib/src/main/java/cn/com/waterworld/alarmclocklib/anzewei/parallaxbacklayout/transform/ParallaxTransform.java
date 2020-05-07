package cn.com.waterworld.alarmclocklib.anzewei.parallaxbacklayout.transform;

import android.graphics.Canvas;
import android.view.View;

import cn.com.waterworld.alarmclocklib.anzewei.parallaxbacklayout.widget.ParallaxBackLayout;

import static cn.com.waterworld.alarmclocklib.anzewei.parallaxbacklayout.ViewDragHelper.EDGE_BOTTOM;
import static cn.com.waterworld.alarmclocklib.anzewei.parallaxbacklayout.ViewDragHelper.EDGE_LEFT;
import static cn.com.waterworld.alarmclocklib.anzewei.parallaxbacklayout.ViewDragHelper.EDGE_RIGHT;
import static cn.com.waterworld.alarmclocklib.anzewei.parallaxbacklayout.ViewDragHelper.EDGE_TOP;


public class ParallaxTransform implements ITransform {
    @Override
    public void transform(Canvas canvas, ParallaxBackLayout parallaxBackLayout, View child) {
        int mEdgeFlag = parallaxBackLayout.getEdgeFlag();
        int width = parallaxBackLayout.getWidth();
        int height = parallaxBackLayout.getHeight();
        int leftBar = parallaxBackLayout.getSystemLeft();
        int topBar = parallaxBackLayout.getSystemTop();
        if (mEdgeFlag == EDGE_LEFT) {
            int left = (child.getLeft() - width) / 2;
            canvas.translate(left, 0);
            canvas.clipRect(0, 0, left + width, child.getBottom());
        } else if (mEdgeFlag == EDGE_TOP) {
            int top = (child.getTop() - child.getHeight()) / 2;
            canvas.translate(0, top);
            canvas.clipRect(0, 0, child.getRight(), child.getHeight() + top + topBar);
        } else if (mEdgeFlag == EDGE_RIGHT) {
            int left = (child.getLeft() + child.getWidth() - leftBar) / 2;
            canvas.translate(left, 0);
            canvas.clipRect(left + leftBar, 0, width, child.getBottom());
        } else if (mEdgeFlag == EDGE_BOTTOM) {
            int top = (child.getBottom() - topBar) / 2;
            canvas.translate(0, top);
            canvas.clipRect(0, top + topBar, child.getRight(), height);
        }
    }
}
