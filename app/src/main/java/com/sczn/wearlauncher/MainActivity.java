package com.sczn.wearlauncher;

import android.app.Fragment;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.base.AbsActivity;
import com.sczn.wearlauncher.base.absDialogFragment;
import com.sczn.wearlauncher.base.absFragment;
import com.sczn.wearlauncher.base.adapter.LoopViewPageAdapter;
import com.sczn.wearlauncher.base.view.VerticalViewPager;
import com.sczn.wearlauncher.base.view.VerticalViewPager.VerticalScrollerAble;
import com.sczn.wearlauncher.chat.activitys.ChatGroupListActivity;
import com.sczn.wearlauncher.clock.Clockfragment;
import com.sczn.wearlauncher.friend.ObserverListener;
import com.sczn.wearlauncher.friend.ObserverManager;
import com.sczn.wearlauncher.menu.util.AppListUtil;
import com.sczn.wearlauncher.receiver.BatterySimReceiver;
import com.sczn.wearlauncher.socket.WaterSocketManager;
import com.sczn.wearlauncher.util.DialogUtil;

import java.util.ArrayList;

public class MainActivity extends AbsActivity implements VerticalScrollerAble, ObserverListener {
    private final static String TAG = MainActivity.class.getSimpleName();

    public static final String ARG_IS_NEW_REMIND = "arg_is_new_remind";
    public static final String ARG_IS_VALUE = "arg_is_value";

    private VerticalViewPager mMainViewpager;
    private ArrayList<absFragment> mFragmentList = new ArrayList<>();
    private LoopViewPageAdapter mViewPageAdapter;

    private ImageView ivNotify;

    private Clockfragment mClockMenuContain;

    private AppListUtil mAppListUtil;
    private BatterySimReceiver batterySimReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAppMenu();
        setContentView(R.layout.activity_launcher);
        initView();
        initData();
        ObserverManager.getInstance().add(this);

        /**
         * 注册检测电量和SIM的广播
         */
        batterySimReceiver = new BatterySimReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(BatterySimReceiver.ACTION_SIM_STATE_CHANGED);
        registerReceiver(batterySimReceiver, filter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        MxyLog.w(TAG, "Bingo-->>onNewIntent");
        setIntent(intent);
        CheckIntent();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        MxyLog.w(TAG, "Bingo-->>back home");
        super.onSaveInstanceState(outState);
    }

    private void CheckIntent() {
        if (getIntent().getBooleanExtra(ARG_IS_NEW_REMIND, false)) {
            String value = getIntent().getStringExtra(ARG_IS_VALUE);
            if (value != null) {
                DialogUtil.showMsgDialog(this, value);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAppListUtil.stopFresh(this);
        WaterSocketManager.getInstance().unInitSocketService(LauncherApp.appContext.getApplicationContext());
        DialogUtil.dismissMsgDialog();
        ObserverManager.getInstance().remove(this);
        unregisterReceiver(batterySimReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initAppMenu() {
        mAppListUtil = AppListUtil.getInctance();
        mAppListUtil.startFresh(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        AppListUtil.getInctance().executeTask();
        finish();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void initView() {
        mMainViewpager = findViewById(R.id.vertical_viewpager);
        ivNotify = findViewById(R.id.iv_notify);

        ivNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ChatGroupListActivity.class));
            }
        });
    }

    private void initData() {
        mFragmentList.clear();
        // mFragmentList.add(ITEM_INDEX_STATUS, getmStatusContain());
        // mFragmentList.add(ITEM_INDEX_MAIN, getMainFragment());
        mFragmentList.add(0, getmClockMenuContain());
        mViewPageAdapter = new LoopViewPageAdapter(getFragmentManager(), false, mMainViewpager);
        mMainViewpager.setAdapter(mViewPageAdapter);
        mViewPageAdapter.setList(mFragmentList);
        mMainViewpager.setVerticalScrollerAble(this);
    }

    /**
     * 表盘页面
     *
     * @return
     */
    public Clockfragment getmClockMenuContain() {
        if (mMainViewpager != null) {
            final Fragment fragment = getFragmentManager().findFragmentByTag(
                    LoopViewPageAdapter.makeFragmentName(mMainViewpager.getId(), 0));
            if (fragment instanceof ContainFragmentClockMenu) {
                mClockMenuContain = (Clockfragment) fragment;
            }
        }
        if (mClockMenuContain == null) {
            mClockMenuContain = Clockfragment.newInstance(getResources().getDisplayMetrics().widthPixels, true);
        }
        return mClockMenuContain;
    }

    @Override
    public boolean isVerticalScrollerAble() {
        return false;
    }


    public void dialogManager(boolean isAdd, String tag, absDialogFragment dialog) {
        super.dialogManager(isAdd, tag, dialog);
    }

    /**
     * 跳转到子菜单页面的activity
     */
    public void setGoHomefragment() {
        startActivity(new Intent(this, ItemMenuActivity.class));
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
    }

    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>(10);

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyOnTouchListener listener : onTouchListeners) {
            listener.onTouch(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }

    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener);
    }

    public interface MyOnTouchListener {
        boolean onTouch(MotionEvent ev);
    }

    @Override
    public void moduleRefresh(int type, final Object s) {
        if (type == ObserverManager.CHAT_MSG_OBSERVER) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ivNotify.setVisibility(View.VISIBLE);
                }
            });
        } else if (type == ObserverManager.CHAT_READ_MSG_OBSERVER) {
            ivNotify.setVisibility(View.GONE);
        }
    }
}
