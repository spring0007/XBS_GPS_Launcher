package com.sczn.wearlauncher.menu.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.app.MxyLog;

import java.io.InputStream;
import java.lang.ref.WeakReference;

@SuppressLint("AppCompatCustomView")
public class MenuBgView extends ImageView {

    private static final String TAG = "MenuBgView";

    public static final String SETTING_KEY_BG = "sczn_bg_style";
    public static final int BG_MENU = 0;

    private int bgForWitch;
    private int mCurrStyle = -1;
    private Drawable mBgDrawable = null;
    private LoadTask mLoadTask;
    private boolean uiVisible = true;
    private boolean isDrawableChanged = true;

    public MenuBgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        @SuppressLint("CustomViewStyleable")
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BgStyle);
        // 根据forWhich读取选择的样式
        bgForWitch = a.getInt(R.styleable.BgStyle_forWhich, BG_MENU);
        a.recycle();

        setScaleType(ScaleType.FIT_XY);
        setId(R.id.bg);

        //freshView();
        freshViewNew();
    }

    public void startFresh() {
        uiVisible = true;
        freshViewNew();
    }

    public void stopFresh() {
        uiVisible = false;
    }

    /**
     * 修改背景图片
     */
    public void switchStyle() {
        final int style = Settings.System.getInt(getContext().getContentResolver(), SETTING_KEY_BG, 0);
        if (style == 0) {
            startLoad(1, bgForWitch);
            Settings.System.putInt(getContext().getContentResolver(), MenuBgView.SETTING_KEY_BG, 1);
        } else {
            startLoad(0, bgForWitch);
            Settings.System.putInt(getContext().getContentResolver(), MenuBgView.SETTING_KEY_BG, 0);
        }
    }

    /**
     * 加载app列表的图标资源
     */
    private void freshViewNew() {
        final int style = Settings.System.getInt(getContext().getContentResolver(), SETTING_KEY_BG, 0);
        if (style == mCurrStyle) {
            if (isDrawableChanged) {
                setImageDrawable(mBgDrawable);
                isDrawableChanged = false;
            }
            return;
        }
        startLoad(style, bgForWitch);
        // 默认背景图片为第二个主题的
        // startLoad(1, bgForWitch);
    }

    private void startLoad(int index, int location) {
        stopLoad();
        mLoadTask = new LoadTask(this);
        mLoadTask.execute(index, location);
    }

    private void stopLoad() {
        if (mLoadTask != null) {
            final LoadTask old = mLoadTask;
            mLoadTask = null;
            old.cancel(true);
        }
    }

    private void setLoadResult(Drawable drawable, int index) {
        mBgDrawable = drawable;
        mCurrStyle = index;
        isDrawableChanged = true;
        if (uiVisible) {
            setImageDrawable(drawable);
            isDrawableChanged = false;
        }
    }

    private static class LoadTask extends AsyncTask<Integer, Void, Drawable> {
        private WeakReference<MenuBgView> mMenuBgView;
        private int index;
        private static final String PATH_ROOT = "bgStyle";

        private LoadTask(MenuBgView reference) {
            mMenuBgView = new WeakReference<>(reference);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Drawable doInBackground(Integer... params) {
            this.index = params[0];
            try {
                InputStream in;
                Bitmap bitmap;
                String[] paths = LauncherApp.appContext.getAssets().list(PATH_ROOT);
                if (paths == null || paths.length <= index) {
                    Settings.System.putInt(LauncherApp.appContext.getContentResolver(), MenuBgView.SETTING_KEY_BG, -1);
                    return null;
                }
                String fileName = PATH_ROOT + "/" + paths[index] + "/" + params[1] + ".png";
                MxyLog.d(TAG, fileName);
                in = LauncherApp.appContext.getAssets().open(fileName);
                bitmap = BitmapFactory.decodeStream(in);
                in.close();

                if (bitmap != null) {
                    return new BitmapDrawable(LauncherApp.appContext.getResources(), bitmap);
                } else {
                    Settings.System.putInt(LauncherApp.appContext.getContentResolver(), MenuBgView.SETTING_KEY_BG, -1);
                }
            } catch (Exception e) {
                MxyLog.e(MenuBgView.class.getSimpleName(), "LoadTask--e=" + e);
                Settings.System.putInt(LauncherApp.appContext.getContentResolver(), MenuBgView.SETTING_KEY_BG, -1);
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Drawable result) {
            super.onPostExecute(result);
            if (isCancelled()) return;
            final MenuBgView reference = mMenuBgView.get();
            if (reference != null) {
                if (result == null) {
                    index = -1;
                }
                reference.setLoadResult(result, index);
            }
        }
    }
}
