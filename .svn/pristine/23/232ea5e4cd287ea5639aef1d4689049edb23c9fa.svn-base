package com.sczn.wearlauncher.player;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

import com.sczn.wearlauncher.util.ThreadUtil;

import java.util.LinkedList;

/**
 *
 */
public class SoundPoolUtil {

    private static SoundPoolUtil mInstance;

    private SoundPool mSoundPool;

    private LinkedList<Integer> loadedSoundIds = new LinkedList<>();

    private OnLoadCompleteListener onLoadCompleteListener = null;

    public static SoundPoolUtil getInstance() {
        if (mInstance == null || mInstance.mSoundPool == null) {
            mInstance = new SoundPoolUtil();
            mInstance.mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
            mInstance.mSoundPool.setOnLoadCompleteListener(mInstance.onLoadCompleteListenerSelf);
        }
        return mInstance;
    }

    /**
     * 加载资源,加载之后播放
     *
     * @param resId
     * @param context
     * @return
     */
    public void loadResAndPlay(final int resId, final Context context) {
        ThreadUtil.getPool().execute(new Runnable() {
            @Override
            public void run() {
                int soundId = mSoundPool.load(context, resId, 10000);
                mSoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                        mSoundPool.play(sampleId, 1, 1, 0, 0, 1);
                    }
                });
                loadedSoundIds.add(soundId);
            }
        });
    }

    /**
     * 加载资源,加载之后播放
     *
     * @param context
     * @param resId
     * @param loop
     */
    public void loadResAndPlay(final Context context, final int resId, final int loop) {
        ThreadUtil.getPool().execute(new Runnable() {
            @Override
            public void run() {
                int soundId = mSoundPool.load(context, resId, 10000);
                mSoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                        mSoundPool.play(sampleId, 1, 1, 0, loop, 1);
                    }
                });
                loadedSoundIds.add(soundId);
            }
        });
    }

    public int loadRes(int resId, Context context) {
        int soundId = mSoundPool.load(context, resId, 1);
        loadedSoundIds.add(soundId);
        return soundId;
    }

    public int loadRes(int resId, Context context, OnLoadCompleteListener listener) {
        int soundId = mSoundPool.load(context, resId, 1);
        loadedSoundIds.add(soundId);
        this.onLoadCompleteListener = listener;
        return soundId;
    }

    public boolean unLoadRes(int soundId) {
        try {
            loadedSoundIds.remove(soundId);
            return mInstance.mSoundPool.unload(soundId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void play(int id, boolean loop) {
        int loopInt = loop ? -1 : 0;
        mSoundPool.play(id, 1, 1, 0, loopInt, 1);
    }

    public static void release() {
        if (mInstance != null) {
            if (mInstance.mSoundPool != null) {
                for (int soundId : mInstance.loadedSoundIds) {
                    mInstance.mSoundPool.unload(soundId);
                }
                mInstance.mSoundPool.release();
                mInstance.mSoundPool = null;
            }
            mInstance = null;
        }
    }

    private OnLoadCompleteListener onLoadCompleteListenerSelf = new OnLoadCompleteListener() {

        @Override
        public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
            if (onLoadCompleteListener != null) {
                onLoadCompleteListener.onLoadComplete(soundPool, sampleId, status);
                onLoadCompleteListener = null;
            }
        }
    };
}
