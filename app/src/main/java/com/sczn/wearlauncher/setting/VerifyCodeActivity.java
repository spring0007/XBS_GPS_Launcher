package com.sczn.wearlauncher.setting;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.base.AbsActivity;
import com.sczn.wearlauncher.setting.util.WLANInputHelper;
import com.sczn.wearlauncher.socket.WaterSocketManager;
import com.sczn.wearlauncher.socket.command.CommandResultCallback;
import com.sczn.wearlauncher.socket.command.post.ResetCmd;
import com.sczn.wearlauncher.util.DialogUtil;
import com.sczn.wearlauncher.util.SystemPermissionUtil;

/**
 * Description:暗码恢复出厂设置页面
 * Created by Bingo on 2019/3/12.
 */
public class VerifyCodeActivity extends AbsActivity {

    private final String PASSWORD = "955723";

    private EditText editText;
    private ImageView delView;
    private ImageView sureView;

    private WLANInputHelper mWlanInputHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        final View view = View.inflate(this, R.layout.activity_verify_code, null);
        setContentView(view);
        mWlanInputHelper = new WLANInputHelper(this, view);
        initView();
    }

    private void initView() {
        editText = findViewById(R.id.input_view);
        editText.setInputType(InputType.TYPE_NULL);
        delView = findViewById(R.id.del_view);
        sureView = findViewById(R.id.sure_view);

        delView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteText(editText);
            }
        });
        sureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String password = editText.getText().toString();
                if (password.equals(PASSWORD)) {
                    doOnUnBindAction();
                }
            }
        });
        mWlanInputHelper.setOnItemClickListener(new WLANInputHelper.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String character) {
                insertText(editText, character);
            }
        });
    }

    /**
     * 需要进行解绑操作
     */
    private void doOnUnBindAction() {
        DialogUtil.showUnBindDialog(VerifyCodeActivity.this, new DialogUtil.DialogConfirm() {
            @Override
            public void sure() {
                MxyLog.d("resetCmd", "sure");
                ResetCmd resetCmd = new ResetCmd(new CommandResultCallback() {
                    @Override
                    public void onSuccess(String baseObtain) {
                        MxyLog.d("resetCmd", "解绑成功.开始恢复出厂设置.");
                        SystemPermissionUtil.reset();
                    }

                    @Override
                    public void onFail() {
                        MxyLog.d("resetCmd", "解绑失败.");
                    }
                });
                WaterSocketManager.getInstance().send(resetCmd);
            }

            @Override
            public void cancel() {
                MxyLog.d("resetCmd", "cancel");
            }
        });
    }

    /**
     * 删除editView的文字
     *
     * @param mEditText
     */
    private void deleteText(EditText mEditText) {
        if (!TextUtils.isEmpty(mEditText.getText().toString())) {
            final int cursorIndex = getEditTextCursorIndex(mEditText);
            if (cursorIndex <= 0) {
                return;
            }
            mEditText.getText().delete(cursorIndex - 1, cursorIndex);
        }
    }

    private int getEditTextCursorIndex(EditText mEditText) {
        return mEditText.getSelectionStart();
    }

    private void insertText(EditText mEditText, CharSequence mText) {
        mEditText.getText().insert(getEditTextCursorIndex(mEditText), mText);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
