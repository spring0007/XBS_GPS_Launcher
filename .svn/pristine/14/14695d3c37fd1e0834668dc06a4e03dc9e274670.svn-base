package com.sczn.wearlauncher.alert;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.base.util.DateFormatUtil;
import com.sczn.wearlauncher.base.util.StringUtils;

import java.util.Calendar;

import static com.sczn.wearlauncher.menu.view.MenuBgView.SETTING_KEY_BG;
import static org.litepal.LitePalApplication.getContext;

/**
 * Description:显示上课模式锁屏
 * Created by Bingo on 2019/4/13.
 */
public class InClassModelLockScreen {

    private static InClassModelLockScreen screen;

    private ViewGroup viewGroup;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLp;

    private TextView tvCurrentTime;
    private TextView tvCurrentDate;
    private TextView tvCurrentClassTime;
    private TextView tvCurrentClass;
    private TextView tvNextClass;

    private static final int SET_TIME = 112;
    private boolean isShow = false;
    private int style;

    public static InClassModelLockScreen getInstance() {
        if (null == screen) {
            synchronized (InClassModelLockScreen.class) {
                if (null == screen) {
                    screen = new InClassModelLockScreen();
                }
            }
        }
        return screen;
    }

    /**
     * @param mContext
     * @param mClass
     * @param mClassTime
     * @param nextClass
     */
    public void showInClassModelLockScreen(Context mContext, String mClass, String mClassTime, String nextClass) {
        style = Settings.System.getInt(getContext().getContentResolver(), SETTING_KEY_BG, 0);
        if (style == 0) {
            viewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.activity_in_class_model_two, null);
        } else {
            viewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.activity_in_class_model, null);
        }
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mLp = new WindowManager.LayoutParams();
        initView();
        initLp();
        initPara(mClass, mClassTime, nextClass);
        show(true);
        mHandler.sendEmptyMessage(SET_TIME);
    }

    private void initView() {
        tvCurrentTime = viewGroup.findViewById(R.id.tv_current_time);
        tvCurrentDate = viewGroup.findViewById(R.id.tv_current_date);
        tvCurrentClassTime = viewGroup.findViewById(R.id.tv_current_class_time);
        tvCurrentClass = viewGroup.findViewById(R.id.tv_current_class);
        tvNextClass = viewGroup.findViewById(R.id.tv_next_class);
    }

    private void initPara(String mClass, String mClassTime, String nextClass) {
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
        if (mClass != null) {
            tvCurrentClassTime.setText(mClassTime);
        }
        //显示下一节课
        // if (nextClass != null && !nextClass.equals("")) {
        //     tvNextClass.setText(getString(R.string.next_class) + nextClass);
        // } else {
        //     tvNextClass.setText("");
        // }
    }

    private void initLp() {
        mLp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;// TYPE_SYSTEM_ALERT;
        mLp.format = PixelFormat.RGBA_8888;
        mLp.flags |= WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        mLp.systemUiVisibility = View.STATUS_BAR_HIDDEN;
        mLp.width = WindowManager.LayoutParams.MATCH_PARENT;
        mLp.height = WindowManager.LayoutParams.MATCH_PARENT;
    }

    public boolean isShow() {
        return isShow;
    }

    /**
     * 是否显示
     *
     * @param flag
     */
    public void show(boolean flag) {
        if (flag) {
            if (mWindowManager != null) {
                isShow = true;
                mWindowManager.addView(viewGroup, mLp);
            }
        } else {
            if (mWindowManager != null && viewGroup != null) {
                isShow = false;
                mWindowManager.removeView(viewGroup);
                mWindowManager = null;
                mLp = null;
                viewGroup = null;
                if (mHandler != null) {
                    mHandler.removeCallbacksAndMessages(null);
                }
            }
        }
    }

    private Handler mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SET_TIME:
                    tvCurrentTime.setText(DateFormatUtil.getCurrTimeString(DateFormatUtil.HMS));
                    mHandler.sendEmptyMessageDelayed(SET_TIME, 1000);
                    break;
                default:
                    break;
            }
            return false;
        }
    });
}
