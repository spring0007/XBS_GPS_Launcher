package com.sczn.wearlauncher.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;

import com.sczn.wearlauncher.R;

/**
 * Description:
 * Created by Bingo on 2019/3/12.
 */
public class DialogUtil {

    private static Dialog msgDialog;

    public interface DialogConfirm {
        void sure();

        void cancel();
    }

    /**
     * @param mContext
     */
    public static void showUnBindDialog(Context mContext, final DialogConfirm dialogConfirm) {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(mContext);
        normalDialog.setTitle(R.string.exit_dialog_title);
        normalDialog.setMessage(mContext.getResources().getString(R.string.un_bind_content));
        normalDialog.setPositiveButton(R.string.bind_sure,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogConfirm.sure();
                    }
                });
        normalDialog.setNegativeButton(R.string.bind_cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogConfirm.cancel();
                    }
                });
        final AlertDialog dialog = normalDialog.create();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
            // 显示
            dialog.show();
        }
    }

    /**
     * @param mContext
     * @param content
     * @param pClickListener
     */
    public static void showMsgDialog(final Context mContext, final String content, final View.OnClickListener pClickListener) {
        dismissMsgDialog();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                msgDialog = DialogWhiteUtil.createDialogPositive(mContext, content, pClickListener);
                if (msgDialog != null && msgDialog.getWindow() != null) {
                    msgDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    msgDialog.setCancelable(false);
                    msgDialog.setCanceledOnTouchOutside(false);
                    msgDialog.show();
                }
            }
        });
    }

    /**
     * @param mContext
     * @param content
     */
    public static void showMsgDialog(final Context mContext, final String content) {
        dismissMsgDialog();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                msgDialog = DialogWhiteUtil.createDialogPositive(mContext, content);
                if (msgDialog != null && msgDialog.getWindow() != null) {
                    msgDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    msgDialog.setCancelable(false);
                    msgDialog.setCanceledOnTouchOutside(false);
                    msgDialog.show();
                }
            }
        });
    }

    /**
     *
     */
    public static void dismissMsgDialog() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (msgDialog != null) {
                    msgDialog.dismiss();
                    msgDialog = null;
                }
            }
        });
    }

    private static Handler mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    });
}
