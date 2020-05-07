package com.sczn.wearlauncher.chat.util;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.util.MxyToast;

/**
 * Created by k.liang on 2018/4/19 15:48
 */
public class DialogManager {

    private Dialog mDialog;

    private ImageView mIcon;
    private ImageView mVoice;

    private Context mContext;

    public DialogManager(Context context) {
        mContext = context;
    }

    public void showRecordingDialog() {

        mDialog = new Dialog(mContext, R.style.Theme_AudioDialog);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_chat_audio, null);
        mDialog.setContentView(view);

        mIcon = (ImageView) view.findViewById(R.id.id_recorder_dialog_icon);
        mVoice = (ImageView) view.findViewById(R.id.id_recorder_dialog_voice);

        mDialog.show();
    }

    //正在播放时的状态
    public void recording() {
        if (mDialog != null && mDialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.drawable.temp_recorder);
        }
    }

    //想要取消
    public void wantToCancel() {
        if (mDialog != null && mDialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);

            mIcon.setImageResource(R.drawable.temp_cancel);
        }
    }

    //录音时间太短
    public void tooShort() {
        if (mDialog != null && mDialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mIcon.setImageResource(R.drawable.temp_voice_to_short);
            MxyToast.showShort(mContext, "录音时间过短");
        }
    }

    //关闭dialog
    public void dimissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    /**
     * 通过level更新voice上的图片
     *
     * @param level
     */
    public void updateVoiceLevel(int level) {
        if (mDialog != null && mDialog.isShowing()) {
            int resId = mContext.getResources().getIdentifier("temp_v" + level, "drawable", mContext.getPackageName());
            mVoice.setImageResource(resId);
        }
    }

}
