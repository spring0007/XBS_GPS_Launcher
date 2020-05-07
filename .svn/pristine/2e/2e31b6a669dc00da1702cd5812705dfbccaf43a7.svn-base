package cn.com.waterworld.alarmclocklib.anzewei.parallaxbacklayout;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.com.waterworld.alarmclocklib.anzewei.parallaxbacklayout.widget.ParallaxBackLayout;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParallaxBack {


    public enum Edge {

        LEFT(ViewDragHelper.EDGE_LEFT), RIGHT(ViewDragHelper.EDGE_RIGHT), TOP(ViewDragHelper.EDGE_TOP), BOTTOM(ViewDragHelper.EDGE_BOTTOM);
        private final int value;

        private Edge(int value) {
            this.value = value;
        }

        public
        @ParallaxBackLayout.Edge
        int getValue() {
            return value;
        }
    }


    public enum Layout {
        PARALLAX(ParallaxBackLayout.LAYOUT_PARALLAX), COVER(ParallaxBackLayout.LAYOUT_COVER), SLIDE(ParallaxBackLayout.LAYOUT_SLIDE);
        private final int value;

        private Layout(int value) {
            this.value = value;
        }

        public
        @ParallaxBackLayout.LayoutType
        int getValue() {
            return value;
        }

    }


    public enum EdgeMode {
        FULLSCREEN(ParallaxBackLayout.EDGE_MODE_FULL),
        EDGE(ParallaxBackLayout.EDGE_MODE_DEFAULT);
        private final int value;

        private EdgeMode(int value) {
            this.value = value;
        }

        public
        @ParallaxBackLayout.EdgeMode
        int getValue() {
            return value;
        }

    }


    Edge edge() default Edge.LEFT;


    Layout layout() default Layout.PARALLAX;


    EdgeMode edgeMode() default EdgeMode.EDGE;

}
