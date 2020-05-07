package com.sczn.wearlauncher.base;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.menu.view.MenuBgView;

import java.lang.reflect.Field;
import java.util.Locale;

public abstract class absFragment extends Fragment implements Cloneable {

    private static final String TAG = absFragment.class.getSimpleName();
    private static final Boolean DEBUG = false;

    protected Activity mActivity;
    protected View mRootView;

    private boolean isUserVisible = false;
    protected Boolean isResumed = false;
    protected Locale mCurrLocale;

    protected absFragment mParentFragment;

    private MenuBgView mMenuBgView;

    private void absFragmentLog(String info) {
        if (DEBUG) {
            MxyLog.d(this, "--" + info);
        }
    }

    public void setParentFragment(absFragment viewPagerFragment) {
        this.mParentFragment = viewPagerFragment;
        onParentVisibleChange();
    }

    @Override
    public void onAttach(Activity activity) {
        absFragmentLog("onAttach");
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        absFragmentLog("onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        absFragmentLog("onCreateView");
        mRootView = inflater.inflate(getLayoutResouceId(), container, false);
        initView();
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        absFragmentLog("onViewCreated");
        initData();
        super.onViewCreated(view, savedInstanceState);
        mMenuBgView = findViewById(R.id.bg);
    }


    @Override
    public void onStart() {
        super.onStart();
        absFragmentLog("onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        isResumed = true;
        absFragmentLog("onResume");
        if (isUserVisible()) {
            startFreshData();
        }
    }

    @Override
    public void onPause() {
        absFragmentLog("onPause");
        isResumed = false;
        if (isUserVisible()) {
            endFreshData();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        absFragmentLog("onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        absFragmentLog("onDetach");
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    private void changeFreshState() {
        if (!isResumed) {
            return;
        }
        if (isUserVisible()) {
            startFreshData();
        } else {
            endFreshData();
        }
    }

    public void onParentVisibleChange() {
        changeFreshState();
    }

    protected boolean isUserVisible() {
        if (mParentFragment != null) {
            if (!mParentFragment.isUserVisible()) {
                return false;
            }
        }
        return isUserVisible;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        absFragmentLog("setUserVisibleHint =" + isVisibleToUser);
        //MxyLog.d(TAG , "--" + toString() + "--" + "setUserVisibleHint =" + isVisibleToUser);
        if (getActivity() != null && getFragmentManager() != null) {
            super.setUserVisibleHint(isVisibleToUser);
        }

        this.isUserVisible = isVisibleToUser;

        changeFreshState();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }


    public <T extends absFragment> T cloneInstance() {
        T ac = null;
        try {
            ac = (T) clone();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return ac;
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }

    protected void startFreshData() {
        if (mMenuBgView != null) {
            mMenuBgView.startFresh();
        }

        if (mCurrLocale == null) {
            mCurrLocale = (Locale) getResources().getConfiguration().locale.clone();
        } else {
            if (!mCurrLocale.equals(getResources().getConfiguration().locale)) {
                mCurrLocale = (Locale) getResources().getConfiguration().locale.clone();
                onLocaleChanged();
            }
        }
    }

    protected void onLocaleChanged() {
        // TODO Auto-generated method stub

    }

    protected void viewReload(View view) {
        if (view != null) {
            view.postInvalidate();
        }
    }

    protected void endFreshData() {
        if (mMenuBgView != null) {
            mMenuBgView.stopFresh();
        }
    }

    protected abstract int getLayoutResouceId();

    protected abstract void initView();

    protected abstract void initData();

    protected <T extends View> T findViewById(int resId) {
        if (mRootView == null) {
            return null;
        }

        final View view = mRootView.findViewById(resId);
        if (view == null) {
            return null;
        }
        return (T) view;
    }
}
