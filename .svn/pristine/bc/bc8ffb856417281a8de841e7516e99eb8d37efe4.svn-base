package com.sczn.wearlauncher.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.util.AlertInfoDialog;

import java.util.ArrayList;

public abstract class AbsActivity extends AppCompatActivity {
    private static final String TAG = AbsActivity.class.getSimpleName();

    public static final String ARG_DIALOG_COUNT = "arg_dialog_count";
    protected int mDialogCount;
    protected ArrayList<String> mDialogs;
    protected ArrayList<absDialogFragment> mDialogFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mDialogs = savedInstanceState.getStringArrayList(ARG_DIALOG_COUNT);
            MxyLog.i(TAG, "onCreate--mDialogCount=" + mDialogCount);
        }
        if (mDialogs == null) {
            mDialogs = new ArrayList<>();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void dialogManager(boolean isAdd, String tag, absDialogFragment dialog) {
        if (mDialogs == null) {
            mDialogs = new ArrayList<>();
        }
        if (isAdd && !mDialogs.contains(tag)) {
            mDialogs.add(tag);
        } else if (!isAdd && mDialogs.contains(tag)) {
            mDialogs.remove(tag);
        }

        if (mDialogFragments == null) {
            mDialogFragments = new ArrayList<>();
        }
        if (isAdd) {
            mDialogFragments.add(dialog);
        } else {
            mDialogFragments.remove(dialog);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList(ARG_DIALOG_COUNT, mDialogs);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }


    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * 弹出底部tip
     *
     * @param tip
     */
    public void showButtonTip(final String tip) {
        if (this.isDestroyed() || this.isFinishing()) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertInfoDialog.newInstance()
                        .newDialog(AbsActivity.this, false)
                        .setMsg(tip)
                        .setDuration(1000)
                        .show();
            }
        });
    }

    /**
     * 弹出底部tip
     *
     * @param tip
     * @param millis
     */
    public void showButtonTip(final String tip, final int millis) {
        if (this.isDestroyed() || this.isFinishing()) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertInfoDialog.newInstance()
                        .newDialog(AbsActivity.this, false)
                        .setMsg(tip)
                        .setDuration(millis)
                        .show();
            }
        });
    }
}
