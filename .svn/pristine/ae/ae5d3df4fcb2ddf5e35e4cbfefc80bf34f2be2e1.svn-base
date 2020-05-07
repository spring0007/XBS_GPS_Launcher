package com.sczn.wearlauncher.setting;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.base.AbsActivity;
import com.sczn.wearlauncher.base.view.HorizalViewPager;
import com.sczn.wearlauncher.clock.ClockSkinUtil;
import com.sczn.wearlauncher.setting.adapetr.LargePicturesPagerAdapter;
import com.sczn.wearlauncher.sp.SPUtils;
import com.sczn.wearlauncher.util.ThreadUtil;

/**
 * Description:表盘皮肤更换
 * Created by Bingo on 2019/4/20.
 */
public class SelectIndexSkinActivity extends AbsActivity {

    public static final String RESULT_EXTRA_CLOCK_INDEX = "clock_index";
    public static final int RESULT_CODE_CLOCK_CHOOSE = 2;

    private HorizalViewPager horizalViewPager;
    private LargePicturesPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_index_skin);
        initView();
        initData();
    }

    private void initView() {
        horizalViewPager = findViewById(R.id.sk_viewpager);
    }

    private void initData() {
        ThreadUtil.getPool().execute(new Runnable() {
            @Override
            public void run() {
                /**
                 * 加载表盘数据
                 */
                String[] list = ClockSkinUtil.getAllClockSkins(LauncherApp.getAppContext());
                if (list != null && list.length > 0) {
                    setData(list);
                }
            }
        });
    }

    private void setData(final String[] list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new LargePicturesPagerAdapter(getApplicationContext(), list);
                horizalViewPager.setAdapter(adapter);
                int pos = SPUtils.getIntParam(getApplicationContext(), "BT_CLOCK", 0);
                horizalViewPager.setCurrentItem(pos);

                /**
                 * item的点击事件
                 */
                adapter.setListener(new LargePicturesPagerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int pos) {
                        SPUtils.setParam(getApplicationContext(), "BT_CLOCK", pos);
                        Intent intent = new Intent();
                        intent.putExtra(RESULT_EXTRA_CLOCK_INDEX, pos);
                        SelectIndexSkinActivity.this.setResult(RESULT_CODE_CLOCK_CHOOSE, intent);
                        Toast.makeText(getApplicationContext(), R.string.bg_change_success, Toast.LENGTH_SHORT).show();
                        SelectIndexSkinActivity.this.finish();
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
