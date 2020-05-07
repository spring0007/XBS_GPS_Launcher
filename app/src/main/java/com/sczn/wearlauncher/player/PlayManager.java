package com.sczn.wearlauncher.player;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import com.sczn.wearlauncher.app.LauncherApp;

import java.io.IOException;

/**
 * Created by k.liang on 2018/4/18 19:57
 */
public class PlayManager {

    private static PlayManager mp3Manager;
    private MediaPlayer mp;
    private boolean isPlaying = false;
    private String cachePath = "";  //用途：点击的音频是否同一个，是否在播放

    public static PlayManager getInstance() {
        if (null == mp3Manager) {
            synchronized (PlayManager.class) {
                if (null == mp3Manager) {
                    mp3Manager = new PlayManager();
                }
            }
        }
        return mp3Manager;
    }

    public PlayManager() {
        super();
        mp = new MediaPlayer();//新建一个的实例
    }

    public boolean isSomeItem(String path) {
        return cachePath.equals(path);
    }

    public void play(String path, MediaPlayer.OnCompletionListener completionListener) {
        if (!cachePath.equals(path)) {
            stop();
            mp = new MediaPlayer();
            try {
                isPlaying = true;
                cachePath = path;
                mp.setOnCompletionListener(completionListener);
                mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mp.setDataSource(path);
                mp.prepareAsync();
                mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            stop();
        }
    }

    public void play(Uri alert) {
        try {
            mp.setDataSource(LauncherApp.getAppContext(), alert);
            mp.setAudioStreamType(AudioManager.STREAM_ALARM);
            mp.setLooping(true);
            mp.prepare();
            mp.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void stop() {
        if (mp != null && isPlaying) {
            isPlaying = false;
            mp.stop();
            mp.release();
            cachePath = "";
        }
    }
}
