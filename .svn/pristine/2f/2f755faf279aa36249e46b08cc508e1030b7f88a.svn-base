package com.sczn.wearlauncher.chat.activitys;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sczn.wearlauncher.Config;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.base.AbsActivity;
import com.sczn.wearlauncher.chat.adapters.GroupMsgAdapter;
import com.sczn.wearlauncher.chat.dao.EmojiDao;
import com.sczn.wearlauncher.chat.model.WechatGroupInfo;
import com.sczn.wearlauncher.chat.model.WechatMessageInfo;
import com.sczn.wearlauncher.chat.util.VoicePlayingBgUtil;
import com.sczn.wearlauncher.chat.views.AudioRecorderButton;
import com.sczn.wearlauncher.chat.views.PaginationScrollListener;
import com.sczn.wearlauncher.friend.ObserverListener;
import com.sczn.wearlauncher.friend.ObserverManager;
import com.sczn.wearlauncher.player.PlayManager;
import com.sczn.wearlauncher.socket.WaterSocketManager;
import com.sczn.wearlauncher.socket.command.CmdCode;
import com.sczn.wearlauncher.socket.command.CommandHelper;
import com.sczn.wearlauncher.socket.command.CommandResultCallback;
import com.sczn.wearlauncher.socket.command.gprs.post.StringTo;
import com.sczn.wearlauncher.socket.command.gprs.post.TK;
import com.sczn.wearlauncher.sp.SPKey;
import com.sczn.wearlauncher.sp.SPUtils;
import com.sczn.wearlauncher.util.FileHelper;
import com.sczn.wearlauncher.util.ThreadUtil;
import com.sczn.wearlauncher.util.TimeUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by k.liang on 2018/4/14 11:08
 */
public class ChatMsgListActivity extends AbsActivity implements ObserverListener {

    private final String TAG = "ChatMsgListActivity";

    private TextView group_name;
    private RecyclerView mRecyclerView;
    private Button btn_face;
    private AudioRecorderButton mAudioRecorderButton;

    private GroupMsgAdapter groupMsgAdapter;
    private WechatGroupInfo wechatGroupInfo;
    private EmojiDao emojiDao;
    private VoicePlayingBgUtil voicePlayingBgUtil;

    //聊天记录
    private List<WechatMessageInfo> msgList;

    private int initMinMsgId;//当前加载的最小msgId
    private int initMaxMsgId;//数据库记录的最大的msgId
    //当前用户的watchId
    private int watchId;

    //下拉是否正处于加载中.
    private boolean isLoading = false;
    //下拉的页数
    private int pageNum = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_msg);
        emojiDao = new EmojiDao();
        initView();
        initPrimaryData();
        getChatMsgList();
        ObserverManager.getInstance().add(this);
    }

    private void initView() {
        voicePlayingBgUtil = new VoicePlayingBgUtil(new Handler());
        group_name = findViewById(R.id.group_name);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        btn_face = findViewById(R.id.btn_face);
        mAudioRecorderButton = findViewById(R.id.mAudioRecorderButton);
        mAudioRecorderButton.setAudioFinishRecorderListener(audioFinishRecorderListener);

        //recycleView
        LinearLayoutManager m = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(m);
        List<WechatMessageInfo> mDatas = new ArrayList<>();
        watchId = (int) SPUtils.getParam(SPKey.WATCHID, 0);
        groupMsgAdapter = new GroupMsgAdapter(this, watchId, mDatas); //可以先加载数据库的数据，然后等服务器拉下来之后再更新
        mRecyclerView.setAdapter(groupMsgAdapter);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(200);
        defaultItemAnimator.setRemoveDuration(200);
        mRecyclerView.setItemAnimator(defaultItemAnimator);
        //mRecyclerView.scrollToPosition(groupMsgAdapter.getItemCount() - 1);
        groupMsgAdapter.setOnVoiceListener(voiceListener);

        btn_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emojiDao != null && emojiDao.queryAll().size() > 0) {
                    Intent intent = new Intent(ChatMsgListActivity.this, EmojiActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    showButtonTip(getResources().getString(R.string.syncing), 2000);
                }
            }
        });
    }

    private void initPrimaryData() {
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            wechatGroupInfo = mBundle.getParcelable("wechatGroupInfo");
            if (wechatGroupInfo != null) {
                String id = wechatGroupInfo.getGroupId();
                String name = wechatGroupInfo.getWatchname();
                wechatGroupInfo.setGroupId(id);
                wechatGroupInfo.setWatchname(name);
                group_name.setText(wechatGroupInfo.getGroupName());
                read(id);
            }
            defaultData();
            mRecyclerView.addOnScrollListener(new PaginationScrollListener() {
                @Override
                protected void loadBeforeItems() {
                    isLoading = true;
                    loadOldRecorded();
                }

                @Override
                public boolean isLoading() {
                    return isLoading;
                }
            });
        }
    }

    /**
     * 更新标记当前组,已读
     *
     * @param groupId
     */
    private void read(final String groupId) {
        //更新标记当前组,已读
        ThreadUtil.getPool().execute(new Runnable() {
            @Override
            public void run() {
                WechatGroupInfo group = new WechatGroupInfo();
                group.setMsgReadStatus(0);
                group.saveOrUpdate("groupId = ?", groupId);
            }
        });
    }

    /**
     * 查看历史记录
     * 1.查询本地的记录
     * 2.没有本地记录了,查询服务器的记录
     */
    private void loadOldRecorded() {
        MxyLog.d(TAG, "查看历史记录");
        ThreadUtil.getPool().execute(new Runnable() {
            @Override
            public void run() {
                //获取本地最小的id,去请求旧的数据
                final List<WechatMessageInfo> oldMsgList = LitePal.where("groupId = ? and msgId < ?", wechatGroupInfo.getGroupId(), String.valueOf(initMinMsgId))
                        .order("msgId DESC")
                        .limit(5)
                        .find(WechatMessageInfo.class);
                if (oldMsgList != null && oldMsgList.size() > 0 && groupMsgAdapter != null) {
                    initMinMsgId = oldMsgList.get(oldMsgList.size() - 1).getMsgId();
                    MxyLog.i(TAG, "initMinMsgId:" + initMinMsgId);
                    ThreadUtil.threadDelayTime(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            isLoading = false;
                            groupMsgAdapter.addAllTop(oldMsgList);
                            mRecyclerView.scrollToPosition(0);
                        }
                    });
                } else {
                    //2.没有本地记录了,查询服务器的记录
                    notRecord();
                }
            }
        });
    }

    /**
     * 提示没有记录了.
     */
    private void notRecord() {
        isLoading = false;
        showButtonTip(getString(R.string.not_recorded), 2000);
    }

    /**
     * 显示本地缓存
     */
    private void defaultData() {
        msgList = LitePal.where("groupId = ?", wechatGroupInfo.getGroupId())
                .order("createTime ASC")
                .limit(20)
                .find(WechatMessageInfo.class);
        if (msgList != null && msgList.size() > 0 && groupMsgAdapter != null) {
            Collections.sort(msgList);
            initMinMsgId = msgList.get(0).getMsgId();
            initMaxMsgId = msgList.get(msgList.size() - 1).getMsgId();
            groupMsgAdapter.addAll(msgList);
            mRecyclerView.scrollToPosition(groupMsgAdapter.getItemCount() - 1);
        } else {
            initMinMsgId = 0;
            initMaxMsgId = 0;
        }
    }

    public void getChatMsgList() {
        WaterSocketManager.getInstance().send(new StringTo(true, CmdCode.TKQ, new CommandResultCallback() {
            @Override
            public void onSuccess(String baseObtain) {

            }

            @Override
            public void onFail() {

            }
        }));
    }

    /**
     * 推送新消息过来了
     */
    @Override
    public void moduleRefresh(int type, Object s) {
        if (type == ObserverManager.CHAT_MSG_OBSERVER) {
            MxyLog.d(TAG, "页面接收到推送新消息过来了");
            if (wechatGroupInfo != null && s.equals(wechatGroupInfo.getGroupId())) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        defaultData();
                    }
                });
            }
        }
    }

    private GroupMsgAdapter.OnVoiceListener voiceListener = new GroupMsgAdapter.OnVoiceListener() {
        @Override
        public void OnVoice(ImageView imageView, WechatMessageInfo wechatMessageInfo) {
            voicePlayingBgUtil.stopPlay();
            voicePlayingBgUtil.setImageView(imageView);
            if (wechatMessageInfo.getSenderId().equals(String.valueOf(watchId))) {
                voicePlayingBgUtil.setModelType(2);
            } else {
                voicePlayingBgUtil.setModelType(1);
            }
            if (PlayManager.getInstance().isSomeItem(wechatMessageInfo.getVoicePath()) && PlayManager.getInstance().isPlaying()) {
                voicePlayingBgUtil.stopPlay();//点击同一个，并且在播放声音
            } else if (PlayManager.getInstance().isSomeItem(wechatMessageInfo.getVoicePath()) && !PlayManager.getInstance().isPlaying()) {
                voicePlayingBgUtil.voicePlay();//点击同一个，但不在播放声音
            } else {
                voicePlayingBgUtil.voicePlay();//不点击同一个
            }
            MxyLog.d(TAG, "播放地址 = " + wechatMessageInfo.getVoicePath());
            PlayManager.getInstance().play(wechatMessageInfo.getVoicePath(), new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    PlayManager.getInstance().stop();
                    voicePlayingBgUtil.stopPlay();
                }
            });
        }
    };

    /**
     * 录音结束，发送
     */
    private AudioRecorderButton.AudioFinishRecorderListener audioFinishRecorderListener = new AudioRecorderButton.AudioFinishRecorderListener() {
        @Override
        public void onFinish(final float seconds, final String filePath) {
            final WechatMessageInfo wechatMessageInfo = new WechatMessageInfo();
            ThreadUtil.getPool().execute(new Runnable() {
                @Override
                public void run() {
                    byte[] bytes = FileHelper.file2byte(filePath);
                    String originally = CommandHelper.bytesToHexString(bytes);
                    if (originally != null) {
                        originally = originally
                                .replace("7d", "7d01")
                                .replace("5b", "7d02")
                                .replace("5d", "7d03")
                                .replace("2c", "7d04")
                                .replace("2a", "7d05");
                        TK tk = new TK(CommandHelper.hexStringToBytes(originally), new CommandResultCallback() {
                            @Override
                            public void onSuccess(String baseObtain) {
                                sendMsg(1, wechatMessageInfo, GroupMsgAdapter.VOICE_MSG_OBTAIN_TYPE, seconds, filePath);
                            }

                            @Override
                            public void onFail() {
                                showButtonTip(getString(R.string.send_msg_error));
                            }
                        });
                        WaterSocketManager.getInstance().send(tk);
                    }
                }
            });
        }
    };

    /**
     * 发送消息,并且把内容保存到本地
     *
     * @param wechatMessageInfo
     */
    private void sendMsg(int msgId, final WechatMessageInfo wechatMessageInfo, int type, float seconds, String filePath) {
        MxyLog.d(TAG, "type:" + type + ",filePath" + filePath);
        wechatMessageInfo.setMsgId(msgId);
        wechatMessageInfo.setSenderId(watchId + "");
        wechatMessageInfo.setSenderType(2);
        wechatMessageInfo.setSenderName(Config.IMEI);
        wechatMessageInfo.setVoicePath(filePath);
        wechatMessageInfo.setType(type);
        wechatMessageInfo.setGroupId(wechatGroupInfo.getGroupId());
        wechatMessageInfo.setDuration((int) seconds + "");
        wechatMessageInfo.setContent(filePath);
        wechatMessageInfo.setCreateTime(TimeUtil.getTime());
        //保存并更新
        wechatMessageInfo.save();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                groupMsgAdapter.add(wechatMessageInfo);
                mRecyclerView.scrollToPosition(groupMsgAdapter.getItemCount() - 1);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PlayManager.getInstance().stop();
        ObserverManager.getInstance().remove(this);
    }
}
