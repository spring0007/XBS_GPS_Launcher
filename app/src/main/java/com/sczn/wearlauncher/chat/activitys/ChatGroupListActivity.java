package com.sczn.wearlauncher.chat.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.base.AbsActivity;
import com.sczn.wearlauncher.base.view.MyRecyclerView;
import com.sczn.wearlauncher.chat.adapters.GroupListAdapter;
import com.sczn.wearlauncher.chat.model.WechatGroupInfo;
import com.sczn.wearlauncher.friend.ObserverListener;
import com.sczn.wearlauncher.friend.ObserverManager;
import com.sczn.wearlauncher.util.NetworkUtils;
import com.sczn.wearlauncher.util.ThreadUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by k.liang on 2018/4/13 10:47
 */
public class ChatGroupListActivity extends AbsActivity implements ObserverListener {

    private final String TAG = "ChatGroupListActivity";

    private TextView chatEmpty;
    private MyRecyclerView mRecyclerView;
    private List<WechatGroupInfo> wechatGroupInfoList;
    private GroupListAdapter groupListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_group_list);
        initView();

        ThreadUtil.getPool().execute(new Runnable() {
            @Override
            public void run() {
                defaultData();
            }
        });
    }

    protected void initView() {
        wechatGroupInfoList = new ArrayList<>();
        ObserverManager.getInstance().add(this);

        chatEmpty = findViewById(R.id.chat_empty);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(200);
        defaultItemAnimator.setRemoveDuration(200);
        mRecyclerView.setItemAnimator(defaultItemAnimator);

        groupListAdapter = new GroupListAdapter(this, wechatGroupInfoList);
        groupListAdapter.setOnItemListener(onItemListener);

        mRecyclerView.setEmpty(chatEmpty);
        mRecyclerView.setAdapter(groupListAdapter);

        if (!NetworkUtils.isNetworkAvailable(LauncherApp.getAppContext())) {
            showButtonTip(getString(R.string.bad_network));
        } else {
            showButtonTip(getString(R.string.syncing), 1000);
        }

        //取消消息红点
        ObserverManager.getInstance().notification(ObserverManager.CHAT_READ_MSG_OBSERVER, 0);
    }

    /**
     * 显示本地缓存,然后再获取服务器的数据
     */
    private void defaultData() {
        /**
         * 默认有的分组
         */
        WechatGroupInfo wechatGroupInfo = new WechatGroupInfo();
        wechatGroupInfo.setGroupId("0");
        wechatGroupInfo.setGroupName("我的家庭组");
        wechatGroupInfo.saveOrUpdate("groupId = ?", "0");

        final List<WechatGroupInfo> list = LitePal.findAll(WechatGroupInfo.class);
        if (list.size() > 0 && groupListAdapter != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    groupListAdapter.addData(list);
                }
            });
        }
    }

    /**
     * adapter点击事件
     */
    private GroupListAdapter.OnItemListener onItemListener = new GroupListAdapter.OnItemListener() {
        @Override
        public void OnItem(WechatGroupInfo wechatGroupInfo) {
            groupListAdapter.setNewsMark(wechatGroupInfo.getGroupId(), false);
            Intent intent = new Intent(ChatGroupListActivity.this, ChatMsgListActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putParcelable("wechatGroupInfo", wechatGroupInfo);
            intent.putExtras(mBundle);
            startActivity(intent);
        }
    };


    @Override
    public void moduleRefresh(int type, final Object s) {
        if (groupListAdapter != null && type == ObserverManager.CHAT_MSG_OBSERVER) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    groupListAdapter.setNewsMark((String) s, true);
                }
            });
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消消息红点
        ObserverManager.getInstance().notification(ObserverManager.CHAT_READ_MSG_OBSERVER, 0);
        ObserverManager.getInstance().remove(this);
    }
}
