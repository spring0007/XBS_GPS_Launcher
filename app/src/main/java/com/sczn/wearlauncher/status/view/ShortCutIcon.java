package com.sczn.wearlauncher.status.view;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.menu.bean.AppMenu;
import com.sczn.wearlauncher.menu.util.AppListUtil;
import com.sczn.wearlauncher.sp.SPUtils;

import java.util.ArrayList;

public class ShortCutIcon extends FrameLayout {

    public static final int STATE_NORMAL = 0;
    public static final int STATE_EDIT = 1;

    public static final String SHARE_KEY_SHORTCUT_FIRST = "shortcut_first_init";
    public static final String SHARE_KEY_SHORTCUT_SECOND = "shortcut_second_init";
    public static final String SHARE_KEY_SHORTCUT_THIRD = "shortcut_third_init";

    public static final String getShareKey(int id) {
        return String.valueOf(id);
    }

    private ShortCutLoad mLoadTask;
    private IShortCutEvent mShortCutEvent;
    private StatusIconWithText mStatusIconWithText;
    private ImageView mDeleteImage;

    private int mState = STATE_NORMAL;
    private boolean isEmpty = true;

    public ShortCutIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onFinishInflate() {
        // TODO Auto-generated method stub
        super.onFinishInflate();
        mStatusIconWithText = (StatusIconWithText) findViewById(R.id.shortcut_icon);
        mDeleteImage = (ImageView) findViewById(R.id.shortcut_delete);

        mStatusIconWithText.mText.setTag(R.string.shortcut_no);
        mStatusIconWithText.mIcon.setImageResource(R.drawable.status_icon_shortcut);
        mDeleteImage.setOnClickListener(OnDeleteClick);

        setState(STATE_NORMAL);
    }

    public void setState(int state) {
        switch (state) {
            case STATE_EDIT:
                if (isEmpty) {
                    mDeleteImage.setVisibility(View.GONE);
                } else {
                    mDeleteImage.setVisibility(View.VISIBLE);
                }
                mState = STATE_EDIT;
                break;
            case STATE_NORMAL:
                mDeleteImage.setVisibility(View.GONE);
                mState = STATE_NORMAL;
            default:
                break;
        }
    }

    public void setmShortCutEvent(IShortCutEvent mShortCutEvent) {
        this.mShortCutEvent = mShortCutEvent;
    }

    public void startLoad() {
        setState(STATE_NORMAL);
        stopLoad();
        mLoadTask = new ShortCutLoad();
        mLoadTask.execute(ShortCutIcon.getShareKey(getId()));

        setEnabled(false);
        //mText.setText(R.string.shortcut_loading);
    }

    private void stopLoad() {
        if (mLoadTask != null) {
            mLoadTask.cancel(true);
            mLoadTask = null;
        }
    }

    private void onLoadFinish(final AppMenu menu) {

        setState(STATE_NORMAL);

        if (menu == null) {
            isEmpty = true;
            //mDeleteImage.setVisibility(View.GONE);
            mStatusIconWithText.mIcon.setLongClickable(false);
            mStatusIconWithText.mText.setText(R.string.shortcut_no);
            mStatusIconWithText.mIcon.setImageResource(R.drawable.status_icon_shortcut);
            mStatusIconWithText.mIcon.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    onEditShortCut();
                }
            });
        } else {
            isEmpty = false;
            mStatusIconWithText.mIcon.setLongClickable(true);
            //mDeleteImage.setVisibility(View.VISIBLE);
            mStatusIconWithText.mIcon.setImageDrawable(menu.getIcon(getContext()));
            mStatusIconWithText.mIcon.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (STATE_NORMAL == mState) {
                        onGotoShortCut(menu);
                    } else {
                        //setState(STATE_NORMAL);
                        onEditShortCut();
                    }
                }
            });
            mStatusIconWithText.mIcon.setOnLongClickListener(new OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    // TODO Auto-generated method stub
                    //onEditShortCut();
                    IconStatechange(STATE_EDIT);
                    return true;
                }
            });
            mStatusIconWithText.mText.setText(menu.getName(getContext()));
        }

        setEnabled(true);
    }


    private void onGotoShortCut(AppMenu menu) {
        if (mShortCutEvent != null) {
            mShortCutEvent.onShortCutAction(this, menu);
        }
    }

    private void onEditShortCut() {
        if (mShortCutEvent != null) {
            mShortCutEvent.onShortCutEdit(this);
        }
    }

    private void IconStatechange(int state) {
        if (mShortCutEvent != null) {
            mShortCutEvent.onShortCutStateChange(state);
        }
    }

    private OnClickListener OnDeleteClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
			/*
			if(R.id.shortcut_first == getId() &&
					"com.android.settings.Settings.png".equals(SPUtils.getStringParam(getContext(), ShortCutIcon.getShareKey(getId()), null))){
				MxyToast.showShort(getContext(), R.string.shortcut_setting_toast);
				return;
			}*/
            SPUtils.setParam(getContext(), ShortCutIcon.getShareKey(getId()), null);
            startLoad();
        }
    };

    private class ShortCutLoad extends AsyncTask<String, Void, AppMenu> {

        @Override
        protected AppMenu doInBackground(String... params) {
            // TODO Auto-generated method stub

            final int id = Integer.parseInt(params[0]);
            final String className = (String) SPUtils.getParam(getContext(), ShortCutIcon.getShareKey(id), null);
            final ArrayList<AppMenu> menus = AppListUtil.getInctance().getAppList();
            //MxyLog.d(this, "id=" + id + "--className=" + className);
            if (className == null) {
                switch (id) {
                    case R.id.shortcut_first:
                        if (!(boolean) SPUtils.getParam(getContext(), SHARE_KEY_SHORTCUT_FIRST, false)) {
                            for (AppMenu menu : menus) {
                                if ("com.android.settings.Settings.png".equals(menu.getCustomImageName())) {
                                    SPUtils.setParam(getContext(), ShortCutIcon.getShareKey(id), menu.getCustomImageName());
                                    SPUtils.setParam(getContext(), SHARE_KEY_SHORTCUT_FIRST, Boolean.valueOf(true));
                                    return menu;
                                }
                            }
                        }
                        break;
                    case R.id.shortcut_second:
                        if (!(boolean) SPUtils.getParam(getContext(), SHARE_KEY_SHORTCUT_SECOND, false)) {
                            for (AppMenu menu : menus) {
                                final ArrayList<AppMenu> mSeconfMenus = menu.getChildrenList();
                                if (mSeconfMenus != null && mSeconfMenus.size() > 0) {
                                    for (AppMenu secondMenu : mSeconfMenus) {
                                        MxyLog.i("ShortCutIcon", "secondMenu.getCustomImageName()=" + secondMenu.getCustomImageName());
                                        if ("com.android.dialer.DialtactsActivity.png".equals(secondMenu.getCustomImageName())) {
                                            MxyLog.d("ShortCutIcon", "com.android.dialer.DialtactsActivity.png=" + secondMenu.getCustomImageName());
                                            SPUtils.setParam(getContext(), ShortCutIcon.getShareKey(id), secondMenu.getCustomImageName());
                                            SPUtils.setParam(getContext(), SHARE_KEY_SHORTCUT_SECOND, Boolean.valueOf(true));
                                            return secondMenu;
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    case R.id.shortcut_third:
                        if (!(boolean) SPUtils.getParam(getContext(), SHARE_KEY_SHORTCUT_THIRD, false)) {
                            for (AppMenu menu : menus) {
                                final ArrayList<AppMenu> mSeconfMenus = menu.getChildrenList();
                                if (mSeconfMenus != null && mSeconfMenus.size() > 0) {
                                    for (AppMenu secondMenu : mSeconfMenus) {
                                        if ("com.android.mms.ui.BootActivity.png".equals(secondMenu.getCustomImageName())) {
                                            SPUtils.setParam(getContext(), ShortCutIcon.getShareKey(id), secondMenu.getCustomImageName());
                                            SPUtils.setParam(getContext(), SHARE_KEY_SHORTCUT_THIRD, Boolean.valueOf(true));
                                            return secondMenu;
                                        }
                                    }
                                }
                            }
                        }
                        break;

                    default:
                        return null;
                }
                return null;
            }

            for (AppMenu menu : menus) {
                final ArrayList<AppMenu> secondMenus = menu.getChildrenList();
                if (className.equals(menu.getCustomImageName())) {
                    return menu;
                }

                if (secondMenus != null && secondMenus.size() > 0) {
                    for (AppMenu secondMenu : secondMenus) {
                        if (className.equals(secondMenu.getCustomImageName())) {
                            return secondMenu;
                        }
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(AppMenu result) {
            // TODO Auto-generated method stub
            if (isCancelled()) {
                return;
            }
            onLoadFinish(result);
        }
    }

    public interface IShortCutEvent {
        public void onShortCutAction(ShortCutIcon v, AppMenu menu);

        public void onShortCutEdit(ShortCutIcon v);

        public void onShortCutStateChange(int state);
    }
}
