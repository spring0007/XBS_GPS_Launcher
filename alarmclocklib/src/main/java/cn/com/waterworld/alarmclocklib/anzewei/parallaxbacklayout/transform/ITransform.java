package cn.com.waterworld.alarmclocklib.anzewei.parallaxbacklayout.transform;

import android.graphics.Canvas;
import android.view.View;

import cn.com.waterworld.alarmclocklib.anzewei.parallaxbacklayout.widget.ParallaxBackLayout;


public interface ITransform {
    void transform(Canvas canvas, ParallaxBackLayout parallaxBackLayout, View child);
}
