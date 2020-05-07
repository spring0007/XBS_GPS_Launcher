package com.sczn.wearlauncher.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.AbsActivity;
import com.sczn.wearlauncher.setting.util.AudioManagerHelper;
import com.sczn.wearlauncher.setting.util.SeekBarWithMark;
import com.sczn.wearlauncher.player.SoundPoolUtil;
import com.sczn.wearlauncher.sp.SPKey;
import com.sczn.wearlauncher.sp.SPUtils;

/**
 * Description:音量调节
 */
public class VolumeActivity extends AbsActivity {
    public final String TAG = VolumeActivity.class.getSimpleName();

    private AudioManagerHelper mAudioManagerHelper;

    private SeekBarWithMark mSeekBar;
    private ImageView mMinusView;
    private ImageView mPlusView;

    private final int MAX_VOLUME_LEVEL = 3;

    // FIXME: 2017/9/19 修改SeekBar控件  引入两个新的控件来进行模拟
    private ImageView mImageThumb;
    private RelativeLayout rvGreenSeekBar;

    private int initialThumbMargin = 0;
    private int initialGreenWidth = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volume);

        mAudioManagerHelper = new AudioManagerHelper(getApplicationContext());
        initView();
        initListener();
        initData(mAudioManagerHelper.getVolumeLevel());
    }

    private void initView() {
        mSeekBar = findViewById(R.id.seek_bar);
        mMinusView = findViewById(R.id.minus_view);
        mPlusView = findViewById(R.id.plus_view);

        mImageThumb = findViewById(R.id.img_thumb);
        rvGreenSeekBar = findViewById(R.id.rv_seek_green);

        RelativeLayout.LayoutParams imgThumbParam = (RelativeLayout.LayoutParams) mImageThumb.getLayoutParams();
        RelativeLayout.LayoutParams rvGreenParams = (RelativeLayout.LayoutParams) rvGreenSeekBar.getLayoutParams();
        initialThumbMargin = imgThumbParam.leftMargin;
        initialGreenWidth = rvGreenParams.width;
    }

    private void initListener() {
        mSeekBar.setOnSelectItemListener(new SeekBarWithMark.OnSelectItemListener() {
            @Override
            public void selectItem(int nowSelectItemNum, String val) {
                setVolume(nowSelectItemNum);
            }
        });

        mMinusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nowMarkItem = mSeekBar.getNowMarkItem();

                if (nowMarkItem <= 0) {
                    return;
                }
                mSeekBar.selectMarkItem(--nowMarkItem);
                setVolume(nowMarkItem);
                initData(nowMarkItem);
                SoundPoolUtil.getInstance().loadResAndPlay(R.raw.ring_adjust_volume, VolumeActivity.this);
            }
        });

        mPlusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int max = MAX_VOLUME_LEVEL;
                int nowMarkItem = mSeekBar.getNowMarkItem();
                if (nowMarkItem >= max) {
                    return;
                }
                mSeekBar.selectMarkItem(++nowMarkItem);
                setVolume(nowMarkItem);
                initData(nowMarkItem);
                SoundPoolUtil.getInstance().loadResAndPlay(R.raw.ring_adjust_volume, VolumeActivity.this);
            }
        });
    }

    private void initData(int volume) {
        final int volumeLevel = volume;

        mSeekBar.selectMarkItem(volumeLevel);
        RelativeLayout.LayoutParams imgThumbParam = (RelativeLayout.LayoutParams) mImageThumb.getLayoutParams();
        RelativeLayout.LayoutParams rvGreenParams = (RelativeLayout.LayoutParams) rvGreenSeekBar.getLayoutParams();
        int curLevel = volumeLevel;
        switch (curLevel) {
            case 0:
                imgThumbParam.setMargins(initialThumbMargin + 5, 0, 0, 0);
                rvGreenParams.width = initialGreenWidth + 5;
                mImageThumb.setImageResource(R.drawable.icon_silent_volume);
                rvGreenSeekBar.setVisibility(View.INVISIBLE);
                break;
            case 1:
                imgThumbParam.setMargins(initialThumbMargin + 57, 0, 0, 0);
                rvGreenParams.width = initialGreenWidth + 57;
                mImageThumb.setImageResource(R.drawable.icon_normal_volume);
                rvGreenSeekBar.setVisibility(View.VISIBLE);
                break;
            case 2:
                imgThumbParam.setMargins(initialThumbMargin + 57 * 2, 0, 0, 0);
                rvGreenParams.width = initialGreenWidth + 57 * 2;
                mImageThumb.setImageResource(R.drawable.icon_normal_volume);
                rvGreenSeekBar.setVisibility(View.VISIBLE);
                break;
            case 3:
                imgThumbParam.setMargins(initialThumbMargin + 57 * 3, 0, 0, 0);
                rvGreenParams.width = initialGreenWidth + 57 * 3;
                mImageThumb.setImageResource(R.drawable.icon_normal_volume);
                rvGreenSeekBar.setVisibility(View.VISIBLE);
                break;
        }
        rvGreenSeekBar.setLayoutParams(rvGreenParams);
    }

    /**
     * 设置音量等级,并且储存音量等级
     *
     * @param level
     */
    private void setVolume(int level) {
        mAudioManagerHelper.setVolume(level);
        SPUtils.setParam(SPKey.WATCH_VOLUME, level);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SoundPoolUtil.release();
    }
}
