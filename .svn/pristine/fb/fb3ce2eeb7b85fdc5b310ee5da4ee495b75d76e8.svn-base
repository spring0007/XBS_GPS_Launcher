package com.sczn.wearlauncher.chat.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.chat.model.EmojiModel;

import java.util.List;

/**
 * Created by k.liang on 2018/4/19 19:54
 */
public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.MyViewHolder> {

    private Context mContext;
    private List<EmojiModel> mDatas;
    private OnItemListener onItemListener;

    public EmojiAdapter(Context context, List<EmojiModel> mDatas) {
        this.mContext = context;
        this.mDatas = mDatas;
    }

    public interface OnItemListener {
        void OnItem(EmojiModel emojiModel);
    }

    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    /**
     * 增加组
     */
    public void addData(List<EmojiModel> data) {
        if (data.size() > 0) {
            mDatas.addAll(data);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_chat_emoji, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ImageLoader.getInstance().displayImage((mDatas.get(position).getEmojiPicUrl()), holder.image_emoji);
        holder.image_emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemListener.OnItem(mDatas.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView image_emoji;

        public MyViewHolder(View view) {
            super(view);
            image_emoji = (ImageView) itemView.findViewById(R.id.image_emoji);
        }
    }

}
