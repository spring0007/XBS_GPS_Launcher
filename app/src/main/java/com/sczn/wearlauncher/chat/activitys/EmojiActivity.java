package com.sczn.wearlauncher.chat.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.AbsActivity;
import com.sczn.wearlauncher.chat.adapters.EmojiAdapter;
import com.sczn.wearlauncher.chat.dao.EmojiDao;
import com.sczn.wearlauncher.chat.model.EmojiModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by k.liang on 2018/4/19 18:56
 */
public class EmojiActivity extends AbsActivity {

    private RecyclerView mRecyclerView;
    private EmojiDao emojiDao;
    private EmojiAdapter emojiAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_emoji);
        init();
        defaultData();
        getEmojiList();
    }

    public void defaultData() {
        if (emojiDao != null) {
            List<EmojiModel> list = emojiDao.queryAll();
            if (list.size() > 0 && emojiAdapter != null) {
                emojiAdapter.addData(list);
                emojiAdapter.notifyDataSetChanged();
            } else {
                showButtonTip(getString(R.string.syncing), 2000);
            }
        }
    }

    private void getEmojiList() {

    }

    private EmojiAdapter.OnItemListener onItemListener = new EmojiAdapter.OnItemListener() {
        @Override
        public void OnItem(EmojiModel emojiModel) {
            Intent intent = new Intent();
            Bundle mBundle = new Bundle();
            mBundle.putString("thumb", emojiModel.getEmojiPicUrl());
            intent.putExtras(mBundle);
            setResult(1, intent);
            finish();
        }
    };

    private void init() {
        emojiDao = new EmojiDao();
        mRecyclerView = findViewById(R.id.mRecyclerView);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(200);
        defaultItemAnimator.setRemoveDuration(200);
        mRecyclerView.setItemAnimator(defaultItemAnimator);
        emojiAdapter = new EmojiAdapter(this, new ArrayList<EmojiModel>());
        mRecyclerView.setAdapter(emojiAdapter);
        emojiAdapter.setOnItemListener(onItemListener);
    }
}
