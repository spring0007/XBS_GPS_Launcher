package com.sczn.wearlauncher.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.AbsActivity;
import com.sczn.wearlauncher.sp.SPKey;
import com.sczn.wearlauncher.sp.SPUtils;

/**
 * Description:AppMenu布局风格切换
 * Created by Bingo on 2019/3/6.
 */
public class AppMenuStyleActivity extends AbsActivity implements View.OnClickListener {

    private CheckBox cbHorizontalStyle;
    private CheckBox cbGridStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_menu_style);
        initView();
        initData();
    }

    /**
     *
     */
    private void initView() {
        cbHorizontalStyle = findViewById(R.id.cb_horizontalStyle);
        cbGridStyle = findViewById(R.id.cb_gridStyle);
        cbHorizontalStyle.setOnClickListener(this);
        cbGridStyle.setOnClickListener(this);
    }

    /**
     *
     */
    private void initData() {
        int style = (int) SPUtils.getParam(SPKey.APP_MENU_STYLE, 0);
        selectView(style);
    }

    /**
     * 选择
     *
     * @param style
     */
    private void selectView(int style) {
        if (style == 0) {
            cbHorizontalStyle.setChecked(true);
            cbGridStyle.setChecked(false);
        } else {
            cbHorizontalStyle.setChecked(false);
            cbGridStyle.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cb_horizontalStyle:
                selectView(0);
                SPUtils.setParam(SPKey.APP_MENU_STYLE, 0);
                break;
            case R.id.cb_gridStyle:
                selectView(1);
                SPUtils.setParam(SPKey.APP_MENU_STYLE, 1);
                break;
            default:
                break;
        }
    }
}
