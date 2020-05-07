package com.sczn.wearlauncher.chat.util;

import android.os.Handler;
import android.widget.ImageView;

import com.sczn.wearlauncher.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by k.liang on 2018/4/19 10:19
 * 语音播放动画
 */
public class VoicePlayingBgUtil {

    private Handler handler;

    private ImageView imageView;

    private ImageView lastImageView;

    private Timer timer = new Timer();
    private TimerTask timerTask;

    private int i;

    private int modelType = 1;//类型

    private int[] leftVoiceBg = new int[]{R.drawable.temp_voice_left1, R.drawable.temp_voice_left2, R.drawable.temp_voice_left3};

    private int[] rightVoiceBg = new int[]{R.drawable.temp_voice_right1, R.drawable.temp_voice_right2, R.drawable.temp_voice_right3};

    public VoicePlayingBgUtil(Handler handler) {
        super();
        this.handler = handler;
    }

    public void voicePlay() {
        if (imageView == null) {
            return;
        }
        i = 0;
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (imageView != null) {
                    if (modelType == 1) {
                        changeBg(leftVoiceBg[i % 3], false);
                    } else if (modelType == 2) {
                        changeBg(rightVoiceBg[i % 3], false);
                    }
                } else {
                    return;
                }
                i++;
            }
        };
        timer.schedule(timerTask, 0, 500);
    }

    public void stopPlay() {
        lastImageView = imageView;
        if (lastImageView != null) {
            switch (modelType) {
                case 1:
                    changeBg(R.drawable.temp_voice_left3, true);
                    break;
                case 2:
                    changeBg(R.drawable.temp_voice_right3, true);
                    break;
                default:
                    break;
            }
            if (timerTask != null) {
                timerTask.cancel();
            }
        }
    }

    private void changeBg(final int id, final boolean isStop) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (isStop) {
                    lastImageView.setImageResource(id);
                } else {
                    imageView.setImageResource(id);
                }
            }
        });
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setModelType(int modelType) {
        this.modelType = modelType;
    }

}
