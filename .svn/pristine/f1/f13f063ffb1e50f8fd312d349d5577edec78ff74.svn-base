package com.sczn.wearlauncher.chat.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.chat.model.WechatMessageInfo;
import com.sczn.wearlauncher.chat.util.ImageLoaderHelper;
import com.sczn.wearlauncher.contact.view.CircleImageView;

import java.util.List;

/**
 * Created by k.liang on 2018/4/16 16:18
 */
public class GroupMsgAdapter extends RecyclerView.Adapter<GroupMsgAdapter.MyViewHolder> {

    private Context mContext;
    private List<WechatMessageInfo> mDatas;
    private OnVoiceListener onVoiceListener;

    public static final int TXT_MSG_OBTAIN_TYPE = 1;
    public static final int VOICE_MSG_OBTAIN_TYPE = 2;
    public static final int IMG_MSG_OBTAIN_TYPE = 3;
    public static final int VIDEO_MSG_OBTAIN_TYPE = 4;

    //当前用户的watchId
    private int watchId;

    public GroupMsgAdapter(Context context, int watchId, List<WechatMessageInfo> mDatas) {
        super();
        this.mContext = context;
        this.mDatas = mDatas;
        this.watchId = watchId;
    }

    public interface OnVoiceListener {
        void OnVoice(ImageView imageView, WechatMessageInfo wechatMessageInfo);
    }

    public void setOnVoiceListener(OnVoiceListener onVoiceListener) {
        this.onVoiceListener = onVoiceListener;
    }

    /**
     * 往头部添加数据
     *
     * @param wechatMessageInfo
     */
    public void addTop(WechatMessageInfo wechatMessageInfo) {
        if (wechatMessageInfo != null && mDatas != null) {
            mDatas.add(0, wechatMessageInfo);
            notifyItemInserted(0);
        }
    }

    public void addAllTop(List<WechatMessageInfo> list) {
        if (list != null && list.size() > 0 && mDatas != null) {
            for (WechatMessageInfo msg : list) {
                mDatas.add(0, msg);
                notifyItemInserted(0);
            }
        }
    }

    /**
     * 往底部插入一条数据
     *
     * @param msg
     */
    public void add(WechatMessageInfo msg) {
        if (msg != null && mDatas != null) {
            mDatas.add(msg);
            notifyItemInserted(mDatas.size() - 1);
        }
    }

    public void addAll(List<WechatMessageInfo> list) {
        if (list != null && list.size() > 0) {
            mDatas.clear();
            mDatas.addAll(list);
            notifyDataSetChanged();
        }
    }

    /**
     * 添加数据,不删除数据
     *
     * @param list
     */
    public void addAllNotClear(List<WechatMessageInfo> list) {
        if (list != null && list.size() > 0) {
            for (WechatMessageInfo info : list) {
                if (!mDatas.contains(info)) {
                    mDatas.add(info);
                }
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public GroupMsgAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GroupMsgAdapter.MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_chat_msg, parent, false));
    }

    @Override
    public void onBindViewHolder(GroupMsgAdapter.MyViewHolder holder, int position) {
        WechatMessageInfo wechatMessageInfo = mDatas.get(position);
        switch (wechatMessageInfo.getType()) {
            case TXT_MSG_OBTAIN_TYPE:
                txt(holder, wechatMessageInfo);
                break;
            case VOICE_MSG_OBTAIN_TYPE:
                voice(holder, wechatMessageInfo);
                break;
            case IMG_MSG_OBTAIN_TYPE:
                image(holder, wechatMessageInfo);
                break;
            case VIDEO_MSG_OBTAIN_TYPE:
                break;
            default:
                break;
        }
        if (wechatMessageInfo.getSenderType() == 2 &&
                wechatMessageInfo.getSenderId().equals(String.valueOf(watchId))) {//当前为自己的布局
            ImageLoaderHelper.displayImage(wechatMessageInfo.getHead(), holder.image_head_right);
        } else {
            ImageLoaderHelper.displayImage(wechatMessageInfo.getHead(), holder.image_head_left);
        }
    }

    /**
     * 语音布局
     *
     * @param holder
     * @param wechatMessageInfo
     */
    private void voice(final GroupMsgAdapter.MyViewHolder holder, final WechatMessageInfo wechatMessageInfo) {
        if (wechatMessageInfo.getSenderType() == 2 &&
                wechatMessageInfo.getSenderId().equals(String.valueOf(watchId))) {//当前为自己的布局
            holder.msg_obtain_relativelayout.setVisibility(View.GONE);
            holder.msg_send_relativelayout.setVisibility(View.VISIBLE);
            holder.text_right_relativelayout.setVisibility(View.GONE);
            holder.voice_right_relativelayout.setVisibility(View.VISIBLE);
            holder.img_right_relativelayout.setVisibility(View.GONE);
            holder.voice_send_duration_txt.setText(wechatMessageInfo.getDuration() + "'");
            holder.time_head_right.setText(wechatMessageInfo.getCreateTime());
            holder.voice_send_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击播放语音，并有动画
                    onVoiceListener.OnVoice(holder.voice_send_image, wechatMessageInfo);
                }
            });
        } else {
            holder.msg_obtain_relativelayout.setVisibility(View.VISIBLE);
            holder.msg_send_relativelayout.setVisibility(View.GONE);
            holder.msg_left_relativelayout.setVisibility(View.GONE);
            holder.img_left_relativelayout.setVisibility(View.GONE);
            holder.voice_left_relativelayout.setVisibility(View.VISIBLE);
            holder.voice_obtain_duration_txt.setText(wechatMessageInfo.getDuration() + "'");
            holder.time_head_left.setText(wechatMessageInfo.getSenderName() + ":" + wechatMessageInfo.getCreateTime());
            holder.voice_obtain_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击播放语音，并有动画
                    onVoiceListener.OnVoice(holder.voice_obtain_image, wechatMessageInfo);
                }
            });
        }
    }

    /**
     * 文字布局
     *
     * @param holder
     * @param wechatMessageInfo
     */
    private void txt(GroupMsgAdapter.MyViewHolder holder, WechatMessageInfo wechatMessageInfo) {
        if (wechatMessageInfo.getSenderType() == 2 &&
                wechatMessageInfo.getSenderId().equals(String.valueOf(watchId))) {
            holder.msg_obtain_relativelayout.setVisibility(View.GONE);
            holder.msg_send_relativelayout.setVisibility(View.VISIBLE);
            holder.text_right_relativelayout.setVisibility(View.VISIBLE);
            holder.voice_right_relativelayout.setVisibility(View.GONE);
            holder.img_right_relativelayout.setVisibility(View.GONE);
            holder.msg_send_txt.setText(wechatMessageInfo.getContent());
            holder.time_head_right.setText(wechatMessageInfo.getCreateTime());
        } else {
            holder.msg_obtain_relativelayout.setVisibility(View.VISIBLE);
            holder.msg_send_relativelayout.setVisibility(View.GONE);
            holder.msg_left_relativelayout.setVisibility(View.VISIBLE);
            holder.voice_left_relativelayout.setVisibility(View.GONE);
            holder.img_left_relativelayout.setVisibility(View.GONE);
            holder.msg_obtain_txt.setText(wechatMessageInfo.getContent());
            holder.time_head_left.setText(wechatMessageInfo.getSenderName() + ":" + wechatMessageInfo.getCreateTime());
        }
    }

    /**
     * 图片布局
     *
     * @param holder
     * @param wechatMessageInfo
     */
    private void image(GroupMsgAdapter.MyViewHolder holder, WechatMessageInfo wechatMessageInfo) {
        if (wechatMessageInfo.getSenderType() == 2 &&
                wechatMessageInfo.getSenderId().equals(String.valueOf(watchId))) {
            holder.msg_obtain_relativelayout.setVisibility(View.GONE);
            holder.msg_send_relativelayout.setVisibility(View.VISIBLE);
            holder.text_right_relativelayout.setVisibility(View.GONE);
            holder.voice_right_relativelayout.setVisibility(View.GONE);
            holder.img_right_relativelayout.setVisibility(View.VISIBLE);
            holder.time_head_right.setText(wechatMessageInfo.getCreateTime());
            ImageLoaderHelper.displayChatImage(wechatMessageInfo.getContent(), holder.msg_send_img);

        } else {
            holder.msg_obtain_relativelayout.setVisibility(View.VISIBLE);
            holder.msg_send_relativelayout.setVisibility(View.GONE);
            holder.msg_left_relativelayout.setVisibility(View.GONE);
            holder.voice_left_relativelayout.setVisibility(View.GONE);
            holder.img_left_relativelayout.setVisibility(View.VISIBLE);
            holder.time_head_left.setText(wechatMessageInfo.getSenderName() + ":" + wechatMessageInfo.getCreateTime());
            ImageLoaderHelper.displayChatImage(wechatMessageInfo.getContent(), holder.msg_obtain_img);
        }
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        //收取的消息
        RelativeLayout msg_obtain_relativelayout;
        CircleImageView image_head_left;
        //收取的消息，文本、表情模式
        RelativeLayout msg_left_relativelayout;
        TextView msg_obtain_txt;
        //收取的消息，语音模式
        RelativeLayout voice_left_relativelayout;
        ImageView voice_obtain_image;
        TextView voice_obtain_duration_txt;
        //发送的消息
        RelativeLayout msg_send_relativelayout;
        CircleImageView image_head_right;
        //发送，文本、表情模式
        RelativeLayout text_right_relativelayout;
        TextView msg_send_txt;
        //发送，语音模式
        RelativeLayout voice_right_relativelayout;
        ImageView voice_send_image;
        TextView voice_send_duration_txt;

        //收取的消息，图片模式
        RelativeLayout img_left_relativelayout;
        ImageView msg_obtain_img;

        //发送，图片模式
        RelativeLayout img_right_relativelayout;
        ImageView msg_send_img;

        //显示的时间
        TextView time_head_left;
        TextView time_head_right;

        public MyViewHolder(View view) {
            super(view);
            msg_obtain_relativelayout = itemView.findViewById(R.id.msg_obtain_relativelayout);
            image_head_left = itemView.findViewById(R.id.image_head_left);
            msg_left_relativelayout = itemView.findViewById(R.id.msg_left_relativelayout);
            msg_obtain_txt = itemView.findViewById(R.id.msg_obtain_txt);
            voice_left_relativelayout = itemView.findViewById(R.id.voice_left_relativelayout);
            voice_obtain_image = itemView.findViewById(R.id.voice_obtain_image);
            voice_obtain_duration_txt = itemView.findViewById(R.id.voice_obtain_duration_txt);
            msg_send_relativelayout = itemView.findViewById(R.id.msg_send_relativelayout);
            image_head_right = itemView.findViewById(R.id.image_head_right);
            text_right_relativelayout = itemView.findViewById(R.id.text_right_relativelayout);
            msg_send_txt = itemView.findViewById(R.id.msg_send_txt);
            voice_right_relativelayout = itemView.findViewById(R.id.voice_right_relativelayout);
            voice_send_image = itemView.findViewById(R.id.voice_send_image);
            voice_send_duration_txt = itemView.findViewById(R.id.voice_send_duration_txt);

            time_head_left = itemView.findViewById(R.id.time_head_left);
            time_head_right = itemView.findViewById(R.id.time_head_right);

            img_left_relativelayout = itemView.findViewById(R.id.img_left_relativelayout);
            msg_obtain_img = itemView.findViewById(R.id.msg_obtain_img);

            img_right_relativelayout = itemView.findViewById(R.id.img_right_relativelayout);
            msg_send_img = itemView.findViewById(R.id.msg_send_img);
        }
    }
}
