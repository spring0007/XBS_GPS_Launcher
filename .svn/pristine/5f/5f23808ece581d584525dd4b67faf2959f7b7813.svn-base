package com.sczn.wearlauncher.setting.adapetr;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.setting.bean.RingerMode;

import java.util.List;

public class RingerModeAdapter extends RecyclerView.Adapter<RingerModeAdapter.MyViewHolder> {

    private final List<RingerMode> mDataList;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int modeChange);
    }

    public RingerModeAdapter(List<RingerMode> list) {
        this.mDataList = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = View.inflate(parent.getContext(), R.layout.item_ringer_mode, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final RingerMode entity = mDataList.get(position);

        final String modeName = entity.getModeName();
        final int mode = entity.getMode();
        final boolean selected = entity.isSelected();

        holder.nameView.setText(modeName);

        if (selected) {
            holder.selectedView.setImageResource(R.drawable.icon_radio_checked);
        } else {
            holder.selectedView.setImageResource(R.drawable.icon_radio_normal);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(mode);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameView;
        private final ImageView selectedView;

        public MyViewHolder(View view) {
            super(view);
            nameView = view.findViewById(R.id.name_view);
            selectedView = view.findViewById(R.id.selected_view);
        }
    }
}
