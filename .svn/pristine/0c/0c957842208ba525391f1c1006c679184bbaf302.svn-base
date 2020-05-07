package com.sczn.wearlauncher.chat.util;

import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;

/**
 * Description:
 * Created by Bingo on 2019/3/22.
 */
public class VoiceImagePlayer {

    private static VoiceImagePlayer player;

    private ImageView lastImageView;
    private AnimationDrawable animationDrawable;

    public static VoiceImagePlayer getPlayer() {
        if (null == player) {
            synchronized (VoiceImagePlayer.class) {
                if (null == player) {
                    player = new VoiceImagePlayer();
                }
            }
        }
        return player;
    }

    public void start(int type, ImageView imageView) {
        stop();
        this.lastImageView = imageView;
        animationDrawable = (AnimationDrawable) lastImageView.getBackground();
        animationDrawable.start();
    }

    public void stop() {
        if (animationDrawable != null) {
            animationDrawable.stop();
        }
    }
}
