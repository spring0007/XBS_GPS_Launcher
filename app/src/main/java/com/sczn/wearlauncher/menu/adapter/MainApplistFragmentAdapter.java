package com.sczn.wearlauncher.menu.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.menu.bean.AppMenu;
import com.sczn.wearlauncher.menu.util.MenuUtil;

import java.util.ArrayList;

/**
 * Description:
 * Created by Bingo on 2019/1/3.
 */
public class MainApplistFragmentAdapter extends RecyclerView.Adapter<MainApplistFragmentAdapter.HolderView> {

    private ArrayList<AppMenu> menuList;
    private OnItemListener onItemListener;

    public interface OnItemListener {
        void OnItem(AppMenu appMenu);
    }

    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }


    public MainApplistFragmentAdapter(ArrayList<AppMenu> menuList) {
        this.menuList = menuList;
    }

    public void notifyData(ArrayList<AppMenu> menuList) {
        if (menuList != null && menuList.size() > 0) {
            MxyLog.d(MxyLog.TAG, "menuList = " + menuList.size());
            this.menuList = menuList;
            notifyDataSetChanged();
        }
    }

    @Override
    public HolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewpager_item, parent, false);
        return new HolderView(view);
    }

    @Override
    public void onBindViewHolder(HolderView holder, @SuppressLint("RecyclerView") final int position) {
        holder.view_title.setText(MenuUtil.getName(LauncherApp.getAppContext(), menuList, position));
        holder.view_image.setImageDrawable(MenuUtil.getIcon(LauncherApp.getAppContext(), menuList, position, false));
        holder.vp_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemListener != null) {
                    onItemListener.OnItem(menuList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuList == null ? 0 : menuList.size();
    }

    class HolderView extends RecyclerView.ViewHolder {
        LinearLayout vp_item;
        ImageView view_image;
        TextView view_title;

        HolderView(View itemView) {
            super(itemView);
            vp_item = itemView.findViewById(R.id.vp_item);
            view_image = itemView.findViewById(R.id.view_image);
            view_title = itemView.findViewById(R.id.view_title);
        }
    }
}
