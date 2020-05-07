package com.sczn.wearlauncher.base.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MxyToast {

    private static final int DUARING_SHORT = 1000;

    private static Toast mToast;
    private static Toast mViewToast;
    private static WeakReference<Context> mcontext;
    private static String ts;

    /**
     * @param context
     * @param string
     */
    public static void showShort(Context context, String string) {
        mcontext = new WeakReference<>(context);
        ts = string;
        mHandler.sendEmptyMessage(0);
    }

    public static void show(Context context, String string, int duaring) {
        if (context == null || string == null) {
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), string, duaring);
        } else {
            mToast.setText(string);
            mToast.setDuration(duaring);
        }
        mToast.show();
    }

    public static void showShort(Context context, int stringId) {
        show(context, stringId, DUARING_SHORT);
    }

    public static void show(Context context, int stringId, int duaring) {
        if (context == null) {
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), stringId, duaring);
        } else {
            mToast.setText(stringId);
            mToast.setDuration(duaring);
        }
        mToast.show();
    }

    @SuppressLint("WrongConstant")
    public static void showView(Context context, View view) {
        if (context == null) {
            return;
        }
        if (mViewToast == null) {
            mViewToast = new Toast(context);
        }
        mViewToast.setView(view);
        mViewToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        mViewToast.setDuration(DUARING_SHORT);
        mViewToast.show();
    }

    private static Handler mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (mcontext != null && mcontext.get() != null) {
                        show(mcontext.get(), ts, DUARING_SHORT);
                    }
                    break;
            }
            return false;
        }
    });
}
