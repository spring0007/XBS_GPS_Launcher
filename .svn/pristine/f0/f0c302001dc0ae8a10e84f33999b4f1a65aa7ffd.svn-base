package com.sczn.wearlauncher.setting.adapetr;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.setting.util.WLANInputHelper;

import java.util.List;

public class InputHelperAdapter extends RecyclerView.Adapter<InputHelperAdapter.MyViewHolder> {

    private final Context mContext;
    private final List<String> mDataList;

    private final WLANInputHelper mInputHelper;

    public InputHelperAdapter(Context context, List<String> list, WLANInputHelper inputHelper) {
        this.mContext = context.getApplicationContext();
        this.mDataList = list;
        this.mInputHelper = inputHelper;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = View.inflate(mContext, R.layout.item_input_helper, null);

        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        String character = mDataList.get(position);

        final String realCharacter;

        if (mInputHelper.getInputType() == WLANInputHelper.INPUT_TYPE_LETTER) {
            holder.blankView.setVisibility(View.GONE);
            if (mInputHelper.getCapsType() == WLANInputHelper.CAPS_TYPE_UPPER) {

                realCharacter = character.toUpperCase();

                holder.nameView.setText(realCharacter);
            } else {

                realCharacter = character;

                holder.nameView.setText(character);
            }
        } else {
            realCharacter = character;
            holder.blankView.setVisibility(View.GONE);
            // FIXME: 2017/9/20 由于需求中需要增加空格符 用图片替换
            if (TextUtils.isEmpty(realCharacter.trim())) {
                holder.blankView.setVisibility(View.VISIBLE);
            }
            holder.nameView.setText(character);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInputHelper.getOnItemClickListener() != null) {
                    if (TextUtils.isEmpty(realCharacter)) {
                        mInputHelper.getOnItemClickListener().onItemClick(position, " ");
                    } else {
                        mInputHelper.getOnItemClickListener().onItemClick(position, realCharacter);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameView;
        ImageView blankView;

        public MyViewHolder(View view) {
            super(view);
            nameView = (TextView) view.findViewById(R.id.name_view);
            blankView = (ImageView) view.findViewById(R.id.img_blank);
        }
    }

}
