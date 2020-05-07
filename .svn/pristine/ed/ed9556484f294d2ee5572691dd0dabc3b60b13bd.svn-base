package com.sczn.wearlauncher.util;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sczn.wearlauncher.R;

/**
 * Description:
 * Created by Bingo on 2019/3/13.
 */
public class DialogWhiteUtil {
    private static Dialog createCustomDialog(Context context, View view) {
        Dialog dialog;
        dialog = new Dialog(context, R.style.loading_dialog);
        int width = context.getResources().getDisplayMetrics().widthPixels;
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(width - (int) dip2px(context, 40), ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(view, params);
        return dialog;
    }

    @SuppressLint("SetTextI18n")
    private static Dialog createDialogWhiteNote(Context context, String title, String note, String nText, String pText, View.OnClickListener nClickListener, View.OnClickListener pClickListener) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_white_layout, null);
        final Dialog dialog = createCustomDialog(context, view);
        LinearLayout llLayout = view.findViewById(R.id.ll_dialog);
        ViewGroup.LayoutParams layoutParams = llLayout.getLayoutParams();
        int width = context.getResources().getDisplayMetrics().widthPixels;
        layoutParams.width = (width / 7) * 5;
        llLayout.setLayoutParams(layoutParams);
        TextView tvTitle = view.findViewById(R.id.tv_dialog_title);
        TextView tvNote = view.findViewById(R.id.tv_dialog_note);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnOk = view.findViewById(R.id.btn_ok);
        View viewSplit = view.findViewById(R.id.view_split);
        if (title != null) {
            tvTitle.setText(title + "");
        }
        if (note != null) {
            tvNote.setText(note + "");
        }
        if (nText != null && !nText.equals("")) {
            btnCancel.setText(nText + "");
        } else {
            btnCancel.setVisibility(View.GONE);
            viewSplit.setVisibility(View.GONE);
        }
        if (pText != null && !pText.equals("")) {
            btnOk.setText(pText + "");
        } else {
            btnOk.setVisibility(View.GONE);
            viewSplit.setVisibility(View.GONE);
        }
        if (nClickListener != null) {
            btnCancel.setOnClickListener(nClickListener);
        } else {
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
        }
        if (pClickListener != null) {
            btnOk.setOnClickListener(pClickListener);
        } else {
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
        }
        return dialog;
    }


    public static Dialog createDialogCancleAndPositive(Context context, String note, String nText, String pText, View.OnClickListener nClickListener, View.OnClickListener pClickListener) {
        return createDialogWhiteNote(context, context.getString(R.string.tips), note, nText, pText, nClickListener, pClickListener);
    }


    public static Dialog createDialogCancleAndPositive(Context context, String title, String note, String pText, View.OnClickListener pClickListener) {
        return createDialogWhiteNote(context, title, note, context.getString(R.string.cancel), pText, null, pClickListener);
    }


    public static Dialog createDialogCancleAndPositive(Context context, String title, String note, View.OnClickListener pClickListener) {
        return createDialogWhiteNote(context, title, note, context.getString(R.string.cancel), context.getString(R.string.sure), null, pClickListener);
    }

    public static Dialog createDialogCancleAndPositive(Context context, String note, View.OnClickListener pClickListener, View.OnClickListener nClickListener) {
        return createDialogWhiteNote(context, context.getString(R.string.tips), note, context.getString(R.string.cancel), context.getString(R.string.sure), nClickListener, pClickListener);
    }

    public static Dialog createDialogCancleAndPositive(Context context, String note, View.OnClickListener pClickListener) {
        return createDialogWhiteNote(context, context.getString(R.string.tips), note, context.getString(R.string.cancel), context.getString(R.string.sure), null, pClickListener);
    }

    public static Dialog createDialogPositive(Context context, String note, String pText, View.OnClickListener pClickListener, View.OnClickListener nClickListener) {
        return createDialogWhiteNote(context, context.getString(R.string.tips), note, context.getString(R.string.cancel), pText, pClickListener, nClickListener);
    }

    public static Dialog createDialogPositive(Context context, String title, String note, View.OnClickListener pClickListener) {
        return createDialogWhiteNote(context, title, note, null, context.getString(R.string.sure), null, pClickListener);
    }

    public static Dialog createDialogPositive(Context context, String note, View.OnClickListener pClickListener) {
        return createDialogWhiteNote(context, context.getString(R.string.tips), note, null, context.getString(R.string.sure), null, pClickListener);
    }

    public static Dialog createDialogPositive(Context context, String title, String note) {
        return createDialogWhiteNote(context, title, note, null, context.getString(R.string.sure), null, null);
    }

    public static Dialog createDialogPositive(Context context, String note) {
        return createDialogWhiteNote(context, context.getString(R.string.tips), note, null, context.getString(R.string.sure), null, null);
    }

    private static float dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dpValue * scale;
    }
}
