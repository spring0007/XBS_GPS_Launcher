package cn.com.waterworld.alarmclocklib.anzewei.parallaxbacklayout;

import android.app.Activity;
import android.app.Application;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import cn.com.waterworld.alarmclocklib.R;
import cn.com.waterworld.alarmclocklib.anzewei.parallaxbacklayout.widget.ParallaxBackLayout;


public class ParallaxHelper implements Application.ActivityLifecycleCallbacks {

    private static ParallaxHelper sParallaxHelper;
    private LinkedStack<Activity, TraceInfo> mLinkedStack = new LinkedStack<>();


    public static ParallaxHelper getInstance() {
        if (sParallaxHelper == null)
            sParallaxHelper = new ParallaxHelper();
        return sParallaxHelper;
    }

    private ParallaxHelper() {

    }

    @Override
    public void onActivityCreated(final Activity activity, Bundle savedInstanceState) {
        final TraceInfo traceInfo = new TraceInfo();
        mLinkedStack.put(activity, traceInfo);
        traceInfo.mCurrent = activity;

        ParallaxBack parallaxBack = checkAnnotation(activity.getClass());
        if (mLinkedStack.size() > 0 && parallaxBack != null) {
            ParallaxBackLayout layout = enableParallaxBack(activity);
            layout.setEdgeFlag(parallaxBack.edge().getValue());
            layout.setEdgeMode(parallaxBack.edgeMode().getValue());
            layout.setLayoutType(parallaxBack.layout().getValue(), null);
        }
    }

    private ParallaxBack checkAnnotation(Class<? extends Activity> c) {
        Class mc = c;
        ParallaxBack parallaxBack;
        while (Activity.class.isAssignableFrom(mc)) {
            parallaxBack = (ParallaxBack) mc.getAnnotation(ParallaxBack.class);
            if (parallaxBack != null)
                return parallaxBack;
            mc = mc.getSuperclass();
        }
        return null;
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mLinkedStack.remove(activity);
    }


    public static void disableParallaxBack(Activity activity) {
        ParallaxBackLayout layout = getParallaxBackLayout(activity);
        if (layout != null)
            layout.setEnableGesture(false);
    }


    public static ParallaxBackLayout enableParallaxBack(Activity activity) {
        ParallaxBackLayout layout = getParallaxBackLayout(activity, true);
        layout.setEnableGesture(true);
        return layout;
    }


    public static ParallaxBackLayout getParallaxBackLayout(Activity activity) {
        return getParallaxBackLayout(activity, false);
    }


    public static ParallaxBackLayout getParallaxBackLayout(Activity activity, boolean create) {
        View view = ((ViewGroup) activity.getWindow().getDecorView()).getChildAt(0);
        if (view instanceof ParallaxBackLayout)
            return (ParallaxBackLayout) view;
        view = activity.findViewById(R.id.pllayout);
        if (view instanceof ParallaxBackLayout)
            return (ParallaxBackLayout) view;
        if (create) {
            ParallaxBackLayout backLayout = new ParallaxBackLayout(activity);
            backLayout.setId(R.id.pllayout);
            backLayout.attachToActivity(activity);
            backLayout.setBackgroundView(new GoBackView(activity));
            return backLayout;
        }
        return null;
    }


    public static class TraceInfo {
        private Activity mCurrent;
    }

    public static class GoBackView implements ParallaxBackLayout.IBackgroundView {

        private Activity mActivity;
        private Activity mActivityBack;

        private boolean canDrawBackgroundView = false;

        private GoBackView(Activity activity) {
            mActivity = activity;

            if (activity instanceof ParallaxBackLayout.ICanDrawBackgroundView) {
                canDrawBackgroundView = ((ParallaxBackLayout.ICanDrawBackgroundView) activity).canDrawBackgroundView();
            }
        }

        @Override
        public void draw(Canvas canvas) {
            if (canDrawBackgroundView && mActivityBack != null) {
                mActivityBack.getWindow().getDecorView().requestLayout();
                mActivityBack.getWindow().getDecorView().draw(canvas);
            }
        }

        @Override
        public boolean canGoBack() {


            if (canDrawBackgroundView) {
                mActivityBack = sParallaxHelper.mLinkedStack.before(mActivity);
            }

            return true;
        }
    }
}
