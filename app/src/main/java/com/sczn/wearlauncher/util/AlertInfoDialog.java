package com.sczn.wearlauncher.util;

import android.app.Activity;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.sczn.wearlauncher.R;


public class AlertInfoDialog {
    private DialogPlus dialog;

    private TextView mMsgView;

    private String mMsg = null;
    private long mDurationMillis = -1;
    private DoListener mListener;

    private Handler mHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            dismiss();
        }
    };

    private AlertInfoDialog() {
    }

    public static AlertInfoDialog newInstance() {
        return new AlertInfoDialog();
    }

    public AlertInfoDialog newDialog(Activity activity, String msg) {
        setMsg(msg);
        return newDialog(activity, false);
    }

    public AlertInfoDialog newDialog(Activity activity, boolean cancelable) {
        View view = View.inflate(activity, R.layout.dialog_layout_alert_info, null);

        mMsgView = view.findViewById(R.id.msg_view);

        final ViewHolder viewHolder = new ViewHolder(view);

        dialog = DialogPlus.newDialog(activity)
                .setContentHolder(viewHolder)

                .setHeader(null)
                .setCancelable(cancelable)
                .setGravity(Gravity.BOTTOM)
                .setOnDismissListener(null)
                .setExpanded(false)
                // .setContentWidth(240)
                // .setContentHeight(55)
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel(DialogPlus dialog) {
                        onDismiss();
                    }
                })
                .setContentBackgroundResource(android.R.color.transparent)
                // .setContentBackgroundResource(android.R.color.white)
                .setOnClickListener(null)
                .create();

        return this;
    }

    public AlertInfoDialog setMsg(String msg) {
        this.mMsg = msg;

        return this;
    }

    public AlertInfoDialog setDuration(int millis) {
        this.mDurationMillis = millis;

        return this;
    }

    public AlertInfoDialog setListener(DoListener listener) {
        this.mListener = listener;

        return this;
    }

    public void show() {
        if (dialog != null) {
            if (mMsgView != null) {
                mMsgView.setText(mMsg);
            }
            mHandler.removeCallbacks(runnable);
            if (mDurationMillis > 0) {
                mHandler.postDelayed(runnable, mDurationMillis);
            }

            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            onDismiss();
            try {
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void onDismiss() {
        mHandler.removeCallbacks(runnable);
        if (mListener != null) {
            mListener.onDismiss();
        }
    }

    public interface DoListener {
        void onDismiss();
    }
}
