package com.sczn.wearlauncher.chat.util;

import android.media.SoundPool;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.player.SoundPoolUtil;

/**
 * Description:
 * Created by Bingo on 2019/3/21.
 */
public class MsgAlertPlayer {

    // 音频当前被占用的类别。有电话、播放器、soundPool
    public static String audioBusyType = null;

    private static MsgAlertPlayer player;
    /**
     * 播放微聊消息提示音
     */
    private long currentTime;
    private long lastTime = 0;
    private int msgAlertVoiceId = -1;

    public static MsgAlertPlayer getPlayer() {
        if (null == player) {
            synchronized (MsgAlertPlayer.class) {
                if (null == player) {
                    player = new MsgAlertPlayer();
                }
            }
        }
        return player;
    }

    /**
     * 播放微聊提示音
     */
    public void playMsgAlert() {
        if (audioBusyType == null) {
            currentTime = System.currentTimeMillis();
            // 假设前一条 消息和后一条消息间隔只在1s以内，就可以认为是频繁收到消息。这时候后一条消息就不响。
            if ((currentTime - lastTime) > 1000) {
                if (msgAlertVoiceId == -1) {// 第一次播放提示音，加载一次
                    SoundPoolUtil.getInstance().loadRes(R.raw.msg_alert, LauncherApp.getAppContext(), new SoundPool.OnLoadCompleteListener() {
                        @Override
                        public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                            msgAlertVoiceId = sampleId;
                            SoundPoolUtil.getInstance().play(msgAlertVoiceId, false);
                        }
                    });
                } else {
                    SoundPoolUtil.getInstance().play(msgAlertVoiceId, false);
                }
            }
            lastTime = currentTime;
        }
    }
}
