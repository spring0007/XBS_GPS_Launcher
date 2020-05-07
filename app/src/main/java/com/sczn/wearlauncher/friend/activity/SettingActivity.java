package com.sczn.wearlauncher.friend.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.kyleduo.switchbutton.SwitchButton;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.AbsActivity;
import com.sczn.wearlauncher.friend.utils.Constant;
import com.sczn.wearlauncher.sp.SPUtils;

/**
 * Created by k.liang on 2018/4/24 09:26
 */
public class SettingActivity extends AbsActivity {

    private SwitchButton swVoice;
    private SwitchButton swVt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_setting);
        initView();
        defaultData();
    }

    private void initView() {
        swVoice = findViewById(R.id.drawableSwitch_voice);
        swVt = findViewById(R.id.drawableSwitch_vibration);

        swVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.setParam(SettingActivity.this, Constant.VOICE_SWITCH_TYPE, swVoice.isChecked());
            }
        });
        swVt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.setParam(SettingActivity.this, Constant.VIBRATION_SWITCH_TYPE, swVt.isChecked());
            }
        });
    }

    private void defaultData() {
        boolean voice = (boolean) SPUtils.getParam(this, Constant.VOICE_SWITCH_TYPE, false);
        boolean vt = (boolean) SPUtils.getParam(this, Constant.VIBRATION_SWITCH_TYPE, false);
        if (voice) {
            swVoice.setChecked(true);
        }
        if (vt) {
            swVt.setChecked(true);
        }
    }
}
