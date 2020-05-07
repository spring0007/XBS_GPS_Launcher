package com.sczn.wearlauncher.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.AbsActivity;
import com.sczn.wearlauncher.setting.util.BrightnessHelper;
import com.sczn.wearlauncher.setting.util.SeekBarWithMark;

/**
 * Description:亮度调节
 */
public class BrightnessActivity extends AbsActivity {

    private final int MAX_LIGHTNESS_LEVEL = 3;

    private SeekBarWithMark mSeekBar;
    private ImageView mMinusView;
    private ImageView mPlusView;

    private BrightnessHelper mBrightnessHelper;

    private ImageView mImageThumb;
    private RelativeLayout rvGreenSeekBar;
    private RelativeLayout rvBlackSeekBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lightness);
        mBrightnessHelper = new BrightnessHelper(getApplicationContext());
        initView();
        initListener();
        initData();
    }

    private void initView() {
        mSeekBar = findViewById(R.id.seek_bar);
        mMinusView = findViewById(R.id.minus_view);
        mPlusView = findViewById(R.id.plus_view);

        mImageThumb = findViewById(R.id.img_thumb);
        rvGreenSeekBar = findViewById(R.id.rv_seek_green);
        rvBlackSeekBar = findViewById(R.id.rv_seek_black);
    }

    private void initData() {
        RelativeLayout.LayoutParams imgThumbParam = (RelativeLayout.LayoutParams) mImageThumb.getLayoutParams();
        mSeekBar.selectMarkItem(mBrightnessHelper.getScreenBrightnessLevel() - 1);
        int curLevel = mBrightnessHelper.getScreenBrightnessLevel() - 1;
        switch (curLevel) {
            case 0:
                imgThumbParam.setMargins(imgThumbParam.leftMargin + 47, 0, 0, 0);
                rvGreenSeekBar.getLayoutParams().width = rvGreenSeekBar.getLayoutParams().width + 47;
                break;
            case 1:
                imgThumbParam.setMargins(imgThumbParam.leftMargin + 94, 0, 0, 0);
                rvGreenSeekBar.getLayoutParams().width = rvGreenSeekBar.getLayoutParams().width + 94;
                break;
            case 2:
                imgThumbParam.setMargins(imgThumbParam.leftMargin + 141, 0, 0, 0);
                rvGreenSeekBar.getLayoutParams().width = rvGreenSeekBar.getLayoutParams().width + 141;
                break;
        }
    }

    private void initListener() {
        mSeekBar.setOnSelectItemListener(new SeekBarWithMark.OnSelectItemListener() {
            @Override
            public void selectItem(int nowSelectItemNum, String val) {
                BrightnessHelper.saveBrightness(BrightnessActivity.this, nowSelectItemNum + 1);
            }
        });

        mMinusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nowMarkItem = mSeekBar.getNowMarkItem();

                if ((nowMarkItem + 1) <= 1) {
                    return;
                }
                RelativeLayout.LayoutParams imgThumbParam = (RelativeLayout.LayoutParams) mImageThumb.getLayoutParams();
                imgThumbParam.setMargins(imgThumbParam.leftMargin - 47, 0, 0, 0);
                mImageThumb.invalidate();
                RelativeLayout.LayoutParams rvGreenParms = (RelativeLayout.LayoutParams) rvGreenSeekBar.getLayoutParams();
                rvGreenParms.width = rvGreenSeekBar.getLayoutParams().width - 47;
                rvGreenSeekBar.setLayoutParams(rvGreenParms);
                rvBlackSeekBar.invalidate();
                mSeekBar.selectMarkItem(--nowMarkItem);
                BrightnessHelper.saveBrightness(BrightnessActivity.this, nowMarkItem + 1);
            }
        });

        mPlusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nowMarkItem = mSeekBar.getNowMarkItem();
                if ((nowMarkItem + 1) >= MAX_LIGHTNESS_LEVEL) {
                    return;
                }
                RelativeLayout.LayoutParams imgThumbParam = (RelativeLayout.LayoutParams) mImageThumb.getLayoutParams();
                imgThumbParam.setMargins(imgThumbParam.leftMargin + 47, 0, 0, 0);
                mImageThumb.invalidate();
                RelativeLayout.LayoutParams rvGreenParms = (RelativeLayout.LayoutParams) rvGreenSeekBar.getLayoutParams();
                rvGreenParms.width = rvGreenSeekBar.getLayoutParams().width + 47;
                rvGreenSeekBar.setLayoutParams(rvGreenParms);
                rvBlackSeekBar.invalidate();
                mSeekBar.selectMarkItem(++nowMarkItem);

                BrightnessHelper.saveBrightness(BrightnessActivity.this, nowMarkItem + 1);
            }
        });
    }
}
