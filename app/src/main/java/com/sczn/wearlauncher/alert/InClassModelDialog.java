package com.sczn.wearlauncher.alert;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.base.util.DateFormatUtil;
import com.sczn.wearlauncher.base.util.StringUtils;

import java.util.Calendar;

import static com.sczn.wearlauncher.menu.view.MenuBgView.SETTING_KEY_BG;
import static org.litepal.LitePalApplication.getContext;

/**
 * Description:显示上课模式
 * Created by Bingo on 2019/3/4.
 */
public class InClassModelDialog {

    public static final int SET_TIME = 112;

    private TextView tvCurrentTime;
    private TextView tvCurrentDate;
    private TextView tvCurrentClassTime;
    private TextView tvCurrentClass;

    private int style;

    private View contentView;
    private String mClass;
    private String mStartTime;
    private String mEndTime;

    private Dialog msgDialog;

    @SuppressLint("StaticFieldLeak")
    private static InClassModelDialog inClassModelDialog;

    public static InClassModelDialog getInstance() {
        if (null == inClassModelDialog) {
            synchronized (InClassModelDialog.class) {
                if (null == inClassModelDialog) {
                    inClassModelDialog = new InClassModelDialog();
                }
            }
        }
        return inClassModelDialog;
    }

    /**
     * 创建dialog,自定义大小
     *
     * @param context
     * @param view
     * @return
     */
    private Dialog createCustomDialog(Context context, View view) {
        Dialog dialog;
        dialog = new Dialog(context, R.style.loading_dialog);
        int width = context.getResources().getDisplayMetrics().widthPixels;
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(view, params);
        return dialog;
    }

    private Dialog onCreateDialog(Context mContext, String mClass, String mStartTime, String mEndTime) {
        this.mClass = mClass;
        this.mStartTime = mStartTime;
        this.mEndTime = mEndTime;
        style = Settings.System.getInt(getContext().getContentResolver(), SETTING_KEY_BG, 0);
        if (style == 0) {
            contentView = View.inflate(mContext, R.layout.activity_in_class_model_two, null);
        } else {
            contentView = View.inflate(mContext, R.layout.activity_in_class_model, null);
        }
        initView();
        initData();
        return createCustomDialog(mContext, contentView);
    }

    private void initView() {
        tvCurrentTime = contentView.findViewById(R.id.tv_current_time);
        tvCurrentDate = contentView.findViewById(R.id.tv_current_date);
        tvCurrentClassTime = contentView.findViewById(R.id.tv_current_class_time);
        tvCurrentClass = contentView.findViewById(R.id.tv_current_class);
    }

    private void initData() {
        //时:分
        mHandler.sendEmptyMessage(SET_TIME);
        //X月X日 周X
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dataDisplay;
        if (style == 0) {
            dataDisplay = StringUtils.getJoinString(month, LauncherApp.getAppContext().getString(R.string.date_mon),
                    day, LauncherApp.getAppContext().getString(R.string.date_day),
                    " ", LauncherApp.getAppContext().getString(DateFormatUtil.getCurrWeekDayRes()));
        } else {
            dataDisplay = StringUtils.getJoinString(LauncherApp.getAppContext().getString(DateFormatUtil.getCurrWeekDayRes()));
        }
        tvCurrentDate.setText(dataDisplay);

        if (mClass != null) {
            tvCurrentClass.setText(mClass);
        }
        if (mStartTime != null && mEndTime != null) {
            tvCurrentClassTime.setText(StringUtils.getJoinString(mStartTime, "-", mEndTime));
        }
    }

    private Handler mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == SET_TIME) {
                String mCurrentTime = DateFormatUtil.getCurrTimeString(DateFormatUtil.HM);
                if (mCurrentTime != null && mCurrentTime.equals(mEndTime)) {
                    dismissMsgDialog();
                } else {
                    tvCurrentTime.setText(mCurrentTime);
                    mHandler.sendEmptyMessageDelayed(SET_TIME, 1000);
                }
            }
            return false;
        }
    });

    /**
     * cancel
     */
    private void cancel() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (msgDialog != null) {
            msgDialog.dismiss();
            msgDialog = null;
        }
    }

    /**
     * show
     *
     * @param mContext
     * @param mClass
     * @param mStartTime
     * @param mEndTime
     */
    public void showMsgDialog(final Context mContext, final String mClass, final String mStartTime, final String mEndTime) {
        if (mHandler != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    cancel();
                    msgDialog = onCreateDialog(mContext, mClass, mStartTime, mEndTime);
                    if (msgDialog != null && msgDialog.getWindow() != null) {
                        msgDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                        msgDialog.setCancelable(false);
                        msgDialog.setCanceledOnTouchOutside(false);
                        msgDialog.show();
                    }
                }
            });
        }
    }

    /**
     * dismiss
     */
    public void dismissMsgDialog() {
        if (mHandler != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    cancel();
                }
            });
        }
    }
}
