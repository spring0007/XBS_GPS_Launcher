package com.sczn.wearlauncher.setting.util;

import android.app.Service;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Vibrator;
import android.provider.Settings;

import com.sczn.wearlauncher.app.MxyLog;

import java.util.HashMap;
import java.util.Map;


public class AudioManagerHelper {

    private final String TAG = AudioManagerHelper.class.getSimpleName();

    private final Context mContext;

    private final AudioManager mAudioManager;


    private final int DEFAULT_STREAM_FOR_MAX_LEVEL_6 = AudioManager.STREAM_MUSIC;
    private final int DEFAULT_STREAM_FOR_MAX_LEVEL_4 = AudioManager.STREAM_MUSIC;

    public AudioManagerHelper(Context context) {
        this.mContext = context.getApplicationContext();
        mAudioManager = (AudioManager) mContext.getSystemService(Service.AUDIO_SERVICE);
    }

    public int getRingMode() {
        return mAudioManager.getRingerMode();
    }

    public boolean canRing() {

        int mode = mAudioManager.getRingerMode();

        if (mode == AudioManager.RINGER_MODE_NORMAL) {
            return true;
        }

        return false;
    }

    public boolean canVibrate() {

        int mode = mAudioManager.getRingerMode();

        if (mode == AudioManager.RINGER_MODE_SILENT) {
            return false;
        } else if (mode == AudioManager.RINGER_MODE_VIBRATE) {
            return true;
        }

        final boolean vibrateWhenRinging = isVibrateWhenRinging();

        return vibrateWhenRinging;
    }

    public boolean isVibrateWhenRinging() {
        boolean vibrateWhenRinging = false;

        final int mode = Settings.System.getInt(mContext.getContentResolver(), Settings.System.VIBRATE_WHEN_RINGING, 0);


        if (mode == 0) {

            vibrateWhenRinging = false;
        } else if (mode == 1) {

            vibrateWhenRinging = true;
        }

        return vibrateWhenRinging;
    }

    public void vibrate() {
        mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_ON);
        mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_ON);

        //新增加一个开启震动的效果
        Vibrator vibrator = (Vibrator) mContext.getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(500);
    }

    public void silent() {
        mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);
        mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_OFF);
    }

    public void ring() {
        mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        final boolean success = Settings.System.putInt(mContext.getContentResolver(), Settings.System.VIBRATE_WHEN_RINGING, 0);
        mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);
        mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_OFF);
        sprdSetting(false);
    }

    /**
     * 震动模式
     */
    public void ringAndVibrate() {
        vibrate();
        mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        final boolean success = Settings.System.putInt(mContext.getContentResolver(), Settings.System.VIBRATE_WHEN_RINGING, 1);

        mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_ON);
        mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_ON);

        //新增加一个开启震动的效果
        Vibrator vibrator = (Vibrator) mContext.getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(500);

        sprdSetting(true);
        // 默认把音量开到上一次的音量的保存的数值
    }


    private void sprdSetting(boolean vibrate) {
        int vibrateInt = vibrate ? 1 : 0;

        try {

            String PROFILE_TABLE = "profiles";
            String AUTHORITY = "com.sprd.audioprofile.provider";
            Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PROFILE_TABLE);

            String IS_VIBRATE_KEY = "isVibrate";

            ContentValues values = new ContentValues();

            values.put(IS_VIBRATE_KEY, vibrateInt);

            int id = 1;

            mContext.getContentResolver()
                    .update(ContentUris.withAppendedId(CONTENT_URI, id), values, null, null);

            Settings.System.putInt(mContext.getContentResolver(), "generalAudioProfileVibrate", vibrateInt);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, VolumeEntity> getVolume() {
        Map<Integer, VolumeEntity> volumeMap = new HashMap<>();


        volumeMap.put(AudioManager.STREAM_VOICE_CALL,
                new VolumeEntity(AudioManager.STREAM_VOICE_CALL,
                        mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
                        mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL))
        );

        volumeMap.put(AudioManager.STREAM_SYSTEM,
                new VolumeEntity(AudioManager.STREAM_SYSTEM,
                        mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM),
                        mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM))
        );

        volumeMap.put(AudioManager.STREAM_RING,
                new VolumeEntity(AudioManager.STREAM_RING,
                        mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING),
                        mAudioManager.getStreamVolume(AudioManager.STREAM_RING))
        );

        volumeMap.put(AudioManager.STREAM_MUSIC,
                new VolumeEntity(AudioManager.STREAM_MUSIC,
                        mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                        mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC))
        );

        volumeMap.put(AudioManager.STREAM_ALARM,
                new VolumeEntity(AudioManager.STREAM_ALARM,
                        mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM),
                        mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM))
        );

        return volumeMap;
    }


    private Map<Integer, VolumeEntity> getNeedChangeVolume() {
        Map<Integer, VolumeEntity> volumeMap = new HashMap<>();


        volumeMap.put(AudioManager.STREAM_MUSIC,
                new VolumeEntity(AudioManager.STREAM_MUSIC,
                        mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                        mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC))
        );

        volumeMap.put(AudioManager.STREAM_ALARM,
                new VolumeEntity(AudioManager.STREAM_ALARM,
                        mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM),
                        mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM))
        );

        return volumeMap;
    }

    /**
     * 设置音量
     *
     * @param level
     */
    public void setVolume(int level) {
        MxyLog.d(TAG, ">>>>>level:" + level);
        setVolumeForMaxLevel4(level);
    }

    /**
     * 获取音量
     *
     * @return
     */
    public int getVolumeLevel() {
        return getVolumeLevelForMaxLevel4();
    }


    public void setVolumeForMaxLevel6(final int level) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                setStreamVolumeMaxVolume7ForMaxLevel6(AudioManager.STREAM_RING, level);

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Map<Integer, VolumeEntity> entityMap = getNeedChangeVolume();

                for (VolumeEntity entity : entityMap.values()) {
                    int type = entity.getType();
                    int max = entity.getMax();
                    if (entity.getType() == DEFAULT_STREAM_FOR_MAX_LEVEL_6) {
                        int i = 0;

                        if (level == 0) {
                            i = 0;
                        } else if (level == 1) {
                            i = 2;
                        } else if (level == 2) {
                            i = 5;
                        } else if (level == 3) {
                            i = 8;
                        } else if (level == 4) {
                            i = 10;
                        } else if (level == 5) {
                            i = 12;
                        } else if (level == 6) {
                            i = 15;
                        }

                        mAudioManager.setStreamVolume(type, i, 0);
                    } else if (entity.getType() == AudioManager.STREAM_ALARM) {
                        int i = 0;

                        if (level == 0) {
                            i = 0;
                        } else if (level == 1) {
                            i = 1;
                        } else if (level == 2) {
                            i = 2;
                        } else if (level == 3) {
                            i = 3;
                        } else if (level == 4) {
                            i = 4;
                        } else if (level == 5) {
                            i = 5;
                        } else if (level == 6) {
                            i = 7;
                        }

                        mAudioManager.setStreamVolume(type, i, 0);
                    } else {
                        int i = max * level / 6;

                        mAudioManager.setStreamVolume(type, i, 0);
                    }
                }
            }
        }).start();
    }


    public int getVolumeLevelForMaxLevel6() {
        final int maxVolume = mAudioManager.getStreamMaxVolume(DEFAULT_STREAM_FOR_MAX_LEVEL_6);

        final int currentVolume = mAudioManager.getStreamVolume(DEFAULT_STREAM_FOR_MAX_LEVEL_6);

        int v = 0;
        if (currentVolume == 0) {
            v = 0;
        } else if (currentVolume > 0 && currentVolume <= 2) {
            v = 1;
        } else if (currentVolume > 2 && currentVolume <= 5) {
            v = 2;
        } else if (currentVolume > 5 && currentVolume <= 8) {
            v = 3;
        } else if (currentVolume > 8 && currentVolume <= 10) {
            v = 4;
        } else if (currentVolume > 10 && currentVolume <= 12) {
            v = 5;
        } else if (currentVolume > 12 && currentVolume <= 15) {
            v = 6;
        }

        return v;
    }


    public void setVolumeForMaxLevel4(final int level) {
        setStreamVolumeMaxVolume7ForMaxLevel4(AudioManager.STREAM_RING, level);
        Map<Integer, VolumeEntity> entityMap = getNeedChangeVolume();

        for (VolumeEntity entity : entityMap.values()) {
            int type = entity.getType();
            int max = entity.getMax();
            if (entity.getType() == DEFAULT_STREAM_FOR_MAX_LEVEL_4) {
                int i = 0;

                if (level == 0) {
                    i = 0;
                } else if (level == 1) {
                    i = 7;
                } else if (level == 2) {
                    i = 10;
                } else if (level == 3) {
                    i = 15;
                }

                mAudioManager.setStreamVolume(type, i, 0);
            } else if (entity.getType() == AudioManager.STREAM_ALARM) {
                int i = 0;

                if (level == 0) {
                    i = 0;
                } else if (level == 1) {
                    i = 2;
                } else if (level == 2) {
                    i = 5;
                } else if (level == 3) {
                    i = 7;
                }

                mAudioManager.setStreamVolume(type, i, 0);
            } else {
                int i = max * level / 4;

                mAudioManager.setStreamVolume(type, i, 0);
            }

        }
    }


    public int getVolumeLevelForMaxLevel4() {
        final int maxVolume = mAudioManager.getStreamMaxVolume(DEFAULT_STREAM_FOR_MAX_LEVEL_4);

        final int currentVolume = mAudioManager.getStreamVolume(DEFAULT_STREAM_FOR_MAX_LEVEL_4);

        int v = 0;
        if (currentVolume == 0) {
            v = 0;
        } else if (currentVolume > 0 && currentVolume <= 7) {
            v = 1;
        } else if (currentVolume > 7 && currentVolume <= 12) {
            v = 2;
        } else if (currentVolume > 12 && currentVolume <= 15) {
            v = 3;
        }

        return v;
    }

    private void setStreamVolumeMaxVolume7ForMaxLevel4(int type, int level) {
        int i = 0;

        if (level == 0) {
            i = 0;
        } else if (level == 1) {
            i = 2;
        } else if (level == 2) {
            i = 5;
        } else if (level == 3) {
            i = 7;
        }

        mAudioManager.setStreamVolume(type, i, 0);
    }

    private void setStreamVolumeMaxVolume7ForMaxLevel6(int type, int level) {
        int i = 0;

        if (level == 0) {
            i = 0;
        } else if (level == 1) {
            i = 1;
        } else if (level == 2) {
            i = 2;
        } else if (level == 3) {
            i = 3;
        } else if (level == 4) {
            i = 4;
        } else if (level == 5) {
            i = 5;
        } else if (level == 6) {
            i = 7;
        }

        mAudioManager.setStreamVolume(type, i, 0);
    }

    public class VolumeEntity {
        private int type;
        private int max;
        private int current;

        public VolumeEntity(int type, int max, int current) {
            this.type = type;
            this.max = max;
            this.current = current;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
            this.current = current;
        }

        @Override
        public String toString() {
            return new StringBuilder().append(" type:")
                    .append(type)
                    .append(" max:")
                    .append(max)
                    .append(" current:")
                    .append(current).toString();
        }
    }


}
