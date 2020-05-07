package com.sczn.wearlauncher.contact.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.chat.util.ImageLoaderHelper;
import com.sczn.wearlauncher.contact.impl.OnItemClickListener;
import com.sczn.wearlauncher.contact.view.CircleImageView;
import com.sczn.wearlauncher.socket.command.bean.Linkman;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 联系人适配器
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {

    private WeakReference<Context> contextWeakReference;
    private List<Linkman> mDataList;
    private OnItemClickListener mOnItemClickListener;

    public ContactsAdapter(Context context) {
        contextWeakReference = new WeakReference<>(context);
    }

    public void setData(List<Linkman> list) {
        this.mDataList = list;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public ContactsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = View.inflate(contextWeakReference.get(), R.layout.item_contacts, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactsAdapter.MyViewHolder holder, int position) {
        final Linkman bean = mDataList.get(position);
        holder.nameView.setText(bean.getName() == null ? bean.getPhone() : bean.getName());
        ImageLoaderHelper.displayImage(bean.getHead(), holder.iconView);

        holder.iconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mContext.startActivity(new Intent(mContext, HeadImageDetailsActivity.class)
                //         .putExtra(HeadImageDetailsActivity.IMAGE_INTENT, bean.getHeadimageurl())
                //         .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(bean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameView;
        CircleImageView iconView;

        MyViewHolder(View view) {
            super(view);
            nameView = view.findViewById(R.id.name_view);
            iconView = view.findViewById(R.id.icon_view);
        }
    }
}
