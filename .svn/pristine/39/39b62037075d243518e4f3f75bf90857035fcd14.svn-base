package com.sczn.wearlauncher.clock;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

import com.sczn.wearlauncher.MainActivity;
import com.sczn.wearlauncher.MainActivity.MyOnTouchListener;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.base.absFragment;
import com.sczn.wearlauncher.shortcut.ShortCutActivity;
import com.sczn.wearlauncher.sp.SPUtils;


public class Clockfragment extends absFragment implements OnLongClickListener, OnClickListener, MyOnTouchListener {

    private final static String TAG = "Clockfragment";

    public static final String ARG_CLOCK_SCALE = "clock_scale";
    public static final String ARG_CLOCK_TYPE = "clock_type";
    public static final int CLOCK_SCALE_DEFAULT = 400;
    public static final int INVALUED_INDEX = -1;

    // mListView  滑动最小距离
    private static final int FLING_MIN_DISTANCE = 20;

    public static Clockfragment newInstance(int clockSize, boolean isMainClock) {
        Clockfragment fragment = new Clockfragment();
        Bundle bdl = new Bundle();
        bdl.putInt(ARG_CLOCK_SCALE, clockSize);
        bdl.putBoolean(ARG_CLOCK_TYPE, isMainClock);
        fragment.setArguments(bdl);
        return fragment;
    }

    private ClockSkinLoader mClockSkinLoader;
    private ClockView mClockView;
    private int mClockSize = CLOCK_SCALE_DEFAULT;

    private int mClockIndex = INVALUED_INDEX;
    private boolean isMainClock = true;

    protected MainActivity mMainActivity;
    protected GestureDetector listViewGesture;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MainActivity) {
            mMainActivity = (MainActivity) activity;
        }
    }

    @Override
    public void onDetach() {
        mMainActivity = null;
        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bdl = getArguments();
        if (bdl != null) {
            mClockSize = bdl.getInt(ARG_CLOCK_SCALE, CLOCK_SCALE_DEFAULT);
            isMainClock = bdl.getBoolean(ARG_CLOCK_TYPE, true);
        }

        mClockSkinLoader = new ClockSkinLoader();
        listViewGesture = new GestureDetector(listViewGestureListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getLayoutResouceId() {
        return R.layout.fragment_clockmenu_clock;
    }

    @Override
    protected void initView() {
        mClockView = findViewById(R.id.cloct_main);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startGetClockSkin(SPUtils.getIntParam(mActivity, "BT_CLOCK", 0));
    }

    @Override
    public void onDestroyView() {
        stopGetClockSkin();
        super.onDestroyView();
    }

    @Override
    protected void initData() {
        if (isMainClock) {
            mClockView.setOnLongClickListener(this);
        }
    }

    private void ensureClock() {
        final int index = SPUtils.getIntParam(mActivity, "BT_CLOCK", 0);
        if (index != mClockIndex) {
            startGetClockSkin(index);
            mClockIndex = index;
        }
    }

    @Override
    protected void startFreshData() {
        mClockView.startDraw();
        ensureClock();
        if (isMainClock && mMainActivity != null) {
            mMainActivity.registerMyOnTouchListener(this);
        }
    }

    @Override
    protected void endFreshData() {
        if (isMainClock && mMainActivity != null) {
            mMainActivity.unregisterMyOnTouchListener(this);
        }
        mClockView.stopDraw();
    }

    private void gotoNotification() {
        Intent i = new Intent(getActivity().getApplicationContext(), ShortCutActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

    }

    long[] mHits = new long[2];

    private void onMainClockClick() {
        // 实现数组的移位操作，点击一次，左移一位，末尾补上当前开机时间（cpu的时间）
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        //双击事件的时间间隔500ms
        if (mHits[0] >= (SystemClock.uptimeMillis() - 1000)) {
            //双击后具体的操作
            gotoNotification();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cloct_main:
                onMainClockClick();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.cloct_main:
                // gotoChangeClock();
                return true;
            default:
                break;
        }
        return false;
    }

    private void setClockSkin(ModelClockSkin clockSkin) {
        if (mClockView == null) {
            return;
        }
        if (clockSkin == null) {
            MxyLog.d(this, "clockSkin == null");
            startGetClockSkin(0);
        } else {
            mClockView.setClockSkin(clockSkin);
        }
    }

    private void startGetClockSkin(int position) {
        if (mClockSkinLoader != null) {
            final ClockSkinLoader oldTask = mClockSkinLoader;
            oldTask.cancel(true);
            mClockSkinLoader = null;
        }
        mClockSkinLoader = new ClockSkinLoader();
        mClockSkinLoader.execute(mClockSize, position);
    }

    private void stopGetClockSkin() {
        if (mClockSkinLoader != null) {
            final ClockSkinLoader oldTask = mClockSkinLoader;
            oldTask.cancel(true);
            mClockSkinLoader = null;
        }

    }

    private class ClockSkinLoader extends AsyncTask<Integer, Void, ModelClockSkin> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ModelClockSkin doInBackground(Integer... params) {
            ClockSkinParse parser = new ClockSkinParse(params[0], params[0]);
            ModelClockSkin clockSkin = null;
            try {
                clockSkin = parser.getChildSkinByPosition(getActivity(), params[1]);
                if (!isCancelled()) {
                    mClockIndex = params[1];
                }
            } catch (Exception e) {
                MxyLog.e(TAG, "ClockSkinLoader--e=" + e.toString());
            }
            return clockSkin;
        }

        @Override
        protected void onPostExecute(ModelClockSkin result) {
            super.onPostExecute(result);
            if (!isCancelled()) {
                setClockSkin(result);
            }
        }
    }

    /**
     * @author jczmdeveloper
     * @desp mListView 的手势监听
     */
    private OnGestureListener listViewGestureListener = new OnGestureListener() {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //向上滑动
            if (e1.getY() - e2.getY() > FLING_MIN_DISTANCE) {
                mMainActivity.setGoHomefragment();
                return true;
            }
            // 向下滑动
            if (e2.getY() - e1.getY() > FLING_MIN_DISTANCE) {
                mMainActivity.setGoHomefragment();
                return true;
            }

            if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE) {
                mMainActivity.setGoHomefragment();
                return true;
            }
            if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE) {
                mMainActivity.setGoHomefragment();
                return true;
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }
    };

    @Override
    public boolean onTouch(MotionEvent event) {
        return listViewGesture.onTouchEvent(event);
    }
}
