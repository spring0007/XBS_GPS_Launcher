package com.sczn.wearlauncher.base;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sczn.wearlauncher.MainActivity;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.util.SysServices;

public abstract class absDialogFragment extends DialogFragment {

    protected View mRootView;
    protected MainActivity mMainActivity;
    protected String mTag;

    public absDialogFragment() {
        setShowsDialog(false);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MainActivity) {
            mMainActivity = (MainActivity) activity;
            mTag = getTag();
            if (mTag == null) {
                mTag = getClass().getSimpleName();
            }
            mMainActivity.dialogManager(true, mTag, this);
        }
    }

    @Override
    public void onDetach() {
        if (mMainActivity != null) {
            mMainActivity.dialogManager(false, mTag, this);
        }
        mMainActivity = null;
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutResouceId(), container, false);
        creatView();
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        destorytView();
        super.onDestroyView();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialogAnim;
        return dialog;
    }

    @Override
    public void onStop() {
        super.onStop();
        //dismiss();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (!isAdded()) {
            super.show(manager, tag);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public int getSystemSettingInt(String name) {
        return SysServices.getSystemSettingInt(getActivity(), name);
    }

    public void setSystemSettingInt(String name, int value) {
        SysServices.setSystemSettingInt(getActivity(), name, value);
    }


    protected abstract int getLayoutResouceId();

    protected abstract void creatView();

    protected abstract void destorytView();

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
