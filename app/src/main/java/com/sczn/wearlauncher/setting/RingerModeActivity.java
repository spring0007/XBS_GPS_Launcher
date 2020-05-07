package com.sczn.wearlauncher.setting;

import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.base.AbsActivity;
import com.sczn.wearlauncher.player.SoundPoolUtil;
import com.sczn.wearlauncher.setting.adapetr.RingerModeAdapter;
import com.sczn.wearlauncher.setting.bean.RingerMode;
import com.sczn.wearlauncher.setting.decoration.WlanDecoration;
import com.sczn.wearlauncher.setting.util.AudioManagerHelper;
import com.sczn.wearlauncher.sp.SPKey;
import com.sczn.wearlauncher.sp.SPUtils;
import com.sczn.wearlauncher.util.ThreadUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 情景模式
 */
public class RingerModeActivity extends AbsActivity {

    private AudioManagerHelper mAudioManagerHelper;

    private RecyclerView recyclerView;

    private List<RingerMode> mDataList = new ArrayList<>();
    private RingerModeAdapter mDataAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ringer_mode);
        mAudioManagerHelper = new AudioManagerHelper(getApplicationContext());
        initRecyclerView();
        initData();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.ringer_recyclerView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        final WlanDecoration decoration = new WlanDecoration();
        recyclerView.addItemDecoration(decoration);
    }

    private void initData() {
        ThreadUtil.getPool().execute(new Runnable() {
            @Override
            public void run() {
                List<RingerMode> list = getRingerMode();
                mDataList.clear();
                mDataList.addAll(list);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDataAdapter = new RingerModeAdapter(mDataList);
                        recyclerView.setAdapter(mDataAdapter);
                        mDataAdapter.setOnItemClickListener(new RingerModeAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int modeChange) {
                                changeMode(modeChange);
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * 修改模式
     *
     * @param modeChange
     */
    private void changeMode(int modeChange) {
        if (mDataList == null) {
            return;
        }
        for (RingerMode ringerMode : mDataList) {
            final int mode = ringerMode.getMode();
            if (mode == modeChange) {
                ringerMode.setSelected(true);
            } else {
                ringerMode.setSelected(false);
            }
        }
        MxyLog.d("modeChange", ">>>>>" + modeChange);
        if (modeChange == 0) {
            mAudioManagerHelper.silent();
            mAudioManagerHelper.setVolume(0);
        } else if (modeChange == 1) {
            mAudioManagerHelper.vibrate();
        } else if (modeChange == 2) {
            mAudioManagerHelper.ring();
            //默认把音量开到上一次的音量的保存的数值
            //如果上次是静音则开启响铃模式时默认设置为2
            int volumeLevel = (int) SPUtils.getParam(SPKey.WATCH_VOLUME, 0);
            if (volumeLevel == 0) {
                volumeLevel = 2;
            }
            mAudioManagerHelper.setVolume(volumeLevel);
            SoundPoolUtil.getInstance().loadResAndPlay(R.raw.ring_adjust_volume, this);
        } else if (modeChange == 3) {
            mAudioManagerHelper.ringAndVibrate();
            SoundPoolUtil.getInstance().loadResAndPlay(R.raw.ring_adjust_volume, this);
        }
        mDataAdapter.notifyDataSetChanged();
    }

    /**
     * 获取情景模式
     *
     * @return
     */
    private List<RingerMode> getRingerMode() {
        List<RingerMode> list = new ArrayList<>();
        final RingerMode silentMode = new RingerMode(getString(R.string.setting_profile_silent), 0, false);
        final RingerMode vibrateMode = new RingerMode(getString(R.string.setting_profile_vibrate), 1, false);
        final RingerMode ringMode = new RingerMode(getString(R.string.setting_profile_ring), 2, false);
        final RingerMode ringAndVibrateMode = new RingerMode(getString(R.string.setting_profile_ring_vibrate), 3, false);

        final int initRingMode = mAudioManagerHelper.getRingMode();

        if (initRingMode == AudioManager.RINGER_MODE_SILENT) {
            silentMode.setSelected(true);
        } else if (initRingMode == AudioManager.RINGER_MODE_NORMAL) {
            final boolean vibrateWhenRinging = mAudioManagerHelper.isVibrateWhenRinging();
            if (vibrateWhenRinging) {
                ringAndVibrateMode.setSelected(true);
            } else {
                ringMode.setSelected(true);
            }
        } else if (initRingMode == AudioManager.RINGER_MODE_VIBRATE) {
            vibrateMode.setSelected(true);
        }
        //当音量减小至最低时切换为静音模式
        if ((!silentMode.isSelected()) && (!ringMode.isSelected())) {
            if (mAudioManagerHelper.getVolumeLevel() == 0) {
                silentMode.setSelected(true);
            }
        }
        list.add(silentMode);
        list.add(vibrateMode);
        list.add(ringMode);
        list.add(ringAndVibrateMode);
        return list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SoundPoolUtil.release();
    }
}
