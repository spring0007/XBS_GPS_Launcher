package com.sczn.wearlauncher.chat.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.chat.model.WechatGroupInfo;
import com.sczn.wearlauncher.chat.util.ImageLoaderHelper;
import com.sczn.wearlauncher.contact.view.CircleImageView;

import java.util.List;

/**
 * Created by k.liang on 2018/4/13 13:48
 */
public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.MyViewHolder> {

    private Context mContext;
    private List<WechatGroupInfo> mDatas;
    private OnItemListener onItemListener;
    private String groupID = "";
    private boolean isAllDataLoading = false; //是否加载全部数据
    private boolean isHasNews = false; //是否开启新消息提示

    public GroupListAdapter(Context context, List<WechatGroupInfo> mDatas) {
        this.mContext = context;
        this.mDatas = mDatas;
    }

    public interface OnItemListener {
        void OnItem(WechatGroupInfo wechatGroupInfo);
    }

    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    /**
     * 增加组
     *
     * @param data
     */
    public void addData(List<WechatGroupInfo> data) {
        if (data.size() > 0) {
            this.isAllDataLoading = true;
            mDatas.clear();
            mDatas.addAll(data);
            notifyDataSetChanged();
        }
    }

    /**
     * 更新红点
     *
     * @param groupID
     * @param isHasNews
     */
    public void setNewsMark(String groupID, boolean isHasNews) {
        this.isAllDataLoading = false;
        this.groupID = groupID;
        this.isHasNews = isHasNews;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_chat_group_list, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final int pos = position;
        ImageLoaderHelper.displayImageChatGroup(mDatas.get(pos).getHead(), holder.image_head);

        if (isAllDataLoading) {//是否加载全部数据
            if (mDatas.get(position).getMsgReadStatus() == 1) {
                holder.view_news_msg.setVisibility(View.VISIBLE);
            } else {
                holder.view_news_msg.setVisibility(View.GONE);
            }
        } else {
            if (groupID.equals(mDatas.get(pos).getGroupId()) && isHasNews) {
                holder.view_news_msg.setVisibility(View.VISIBLE);
            } else {
                holder.view_news_msg.setVisibility(View.GONE);
            }
        }

        holder.group_name.setText(mDatas.get(pos).getGroupName());

        holder.relativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemListener.OnItem(mDatas.get(pos));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image_head;
        TextView group_name;
        RelativeLayout relativelayout;
        View view_news_msg;

        public MyViewHolder(View view) {
            super(view);
            image_head = itemView.findViewById(R.id.image_head);
            group_name = itemView.findViewById(R.id.group_name);
            relativelayout = itemView.findViewById(R.id.relativelayout);
            view_news_msg = itemView.findViewById(R.id.view_news_msg);
        }
    }
}
