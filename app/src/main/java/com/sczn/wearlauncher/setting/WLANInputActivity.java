package com.sczn.wearlauncher.setting;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.base.AbsActivity;
import com.sczn.wearlauncher.location.bean.WiFiScanBean;
import com.sczn.wearlauncher.setting.util.WLANInputHelper;
import com.sczn.wearlauncher.setting.util.WifiAdminBiz;
import com.sczn.wearlauncher.util.AlertInfoDialog;
import com.sczn.wearlauncher.util.ThreadUtil;

/**
 * Description:WiFi密码输入页面
 */
public class WLANInputActivity extends AbsActivity {
    private WLANInputHelper mWlanInputHelper;

    public static final String SCAN_RESULT_TAG = "scan_result_tag";

    private EditText mInputView;

    private View mDelView;
    private ImageView mInputTypeView;
    private ImageView mCapsView;
    private View mSureView;

    private WiFiScanBean mScanResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        final View contentView = View.inflate(this, R.layout.activity_wlan_input, null);
        setContentView(contentView);
        mWlanInputHelper = new WLANInputHelper(this, contentView);
        initView();
        initListener();
        initData();
    }

    private void initView() {
        mInputView = findViewById(R.id.input_view);
        mInputView.setInputType(InputType.TYPE_NULL);

        mDelView = findViewById(R.id.del_view);
        mInputTypeView = findViewById(R.id.input_type_view);
        mCapsView = findViewById(R.id.caps_view);
        mSureView = findViewById(R.id.sure_view);
    }

    private void initListener() {
        mDelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteText(mInputView);
            }
        });

        mInputTypeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWlanInputHelper.changeInputType();
            }
        });

        mCapsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWlanInputHelper.changeCaps();
            }
        });

        mSureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mScanResult != null) {
                    // FIXME: 2017/9/21 去掉首尾空格符的过滤的逻辑  避免密码存在的首尾空串的连接不上
                    //final String password = mInputView.getText().toString().trim();
                    final String password = mInputView.getText().toString();

                    ThreadUtil.getPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            WifiAdminBiz.getInstance(LauncherApp.getAppContext()).connectWifi(mScanResult.getSsid(), password);
                        }
                    });

                    AlertInfoDialog.newInstance()
                            .newDialog(WLANInputActivity.this, false)
                            .setMsg(getString(R.string.setting_wlan_connect_begin))
                            .setDuration(2 * 1000)
                            .setListener(new AlertInfoDialog.DoListener() {
                                @Override
                                public void onDismiss() {
                                    finish();
                                }
                            })
                            .show();
                } else {
                    finish();
                }
            }
        });

        mWlanInputHelper.setOnStatusChangeListener(new WLANInputHelper.OnStatusChangeListener() {
            @Override
            public void onStatusChange(int inputType, int capsType) {
                setInputTypeIcon();
                setCapsTypeIcon();
            }
        });

        mWlanInputHelper.setOnItemClickListener(new WLANInputHelper.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String character) {
                insertText(mInputView, character);
            }
        });

        mInputView.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
        mInputView.setLongClickable(false);
    }

    private void initData() {
        if (getIntent() != null) {
            mScanResult = getIntent().getParcelableExtra(SCAN_RESULT_TAG);
        }
        setInputTypeIcon();
        setCapsTypeIcon();
    }

    private void setInputTypeIcon() {
        final int inputType = mWlanInputHelper.getInputType();
        if (inputType == WLANInputHelper.INPUT_TYPE_NUMBER) {
            mInputTypeView.setImageResource(R.drawable.setting_input_number);
        } else if (inputType == WLANInputHelper.INPUT_TYPE_LETTER) {
            mInputTypeView.setImageResource(R.drawable.setting_input_letter);
        } else if (inputType == WLANInputHelper.INPUT_TYPE_SYMBOL) {
            mInputTypeView.setImageResource(R.drawable.setting_input_symbol);
        }
    }

    private void setCapsTypeIcon() {
        final int capsType = mWlanInputHelper.getCapsType();

        if (capsType == WLANInputHelper.CAPS_TYPE_UPPER) {
            mCapsView.setImageResource(R.drawable.setting_input_letter_upper);
        } else {
            mCapsView.setImageResource(R.drawable.setting_input_letter_lower);
        }
    }

    private int getEditTextCursorIndex(EditText mEditText) {
        return mEditText.getSelectionStart();
    }

    private void insertText(EditText mEditText, CharSequence mText) {
        mEditText.getText().insert(getEditTextCursorIndex(mEditText), mText);
    }

    private void deleteText(EditText mEditText) {
        if (!TextUtils.isEmpty(mEditText.getText().toString())) {
            final int cursorIndex = getEditTextCursorIndex(mEditText);
            if (cursorIndex <= 0) {
                return;
            }
            mEditText.getText().delete(cursorIndex - 1, cursorIndex);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
