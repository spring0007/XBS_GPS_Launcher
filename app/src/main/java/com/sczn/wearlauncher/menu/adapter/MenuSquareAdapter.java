package com.sczn.wearlauncher.menu.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.menu.bean.AppMenu;
import com.sczn.wearlauncher.menu.view.MenuSquarePager;
import com.sczn.wearlauncher.menu.view.MenuSquarePager.MenuSquareUnit;

import java.util.ArrayList;

public class MenuSquareAdapter extends Adapter<MenuSquareAdapter.MenuSquareHolder> {
    private final static String TAG = MenuSquareAdapter.class.getSimpleName();

    public static final int ITEM_TYPE_HEADER = 0, ITEM_TYPE_ITEM = 1, ITEM_TYPE_FOOT = 2;

    private Context mContext;
    private ArrayList<AppMenu> mMenuList;
    private OnSquareMenuClickListen mOnSquareMenuClickListen;
    private boolean isStyle = false;

    public MenuSquareAdapter(Context mContext, OnSquareMenuClickListen mOnItemClickListen, boolean isStyle) {
        super();
        this.mContext = mContext;
        this.mOnSquareMenuClickListen = mOnItemClickListen;

        mMenuList = new ArrayList<AppMenu>();
        this.isStyle = isStyle;
    }

    public void setMenuList(ArrayList<AppMenu> menuList) {
        mMenuList.clear();
        mMenuList.addAll(menuList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mMenuList.size() % MenuSquarePager.MENU_COUNT == 0) {
            return mMenuList.size() / MenuSquarePager.MENU_COUNT;
        } else {
            return mMenuList.size() / MenuSquarePager.MENU_COUNT + 1;
        }
    }

    @Override
    public void onBindViewHolder(MenuSquareHolder holder, int position) {
        int startIndex = position * MenuSquarePager.MENU_COUNT;
        //MxyLog.i(TAG, "onBindViewHolder" + "--position=" + position + "--holder.getMenus().size()=" + holder.getMenus().size());
        for (int i = 0; i < holder.getMenus().size(); i++) {

            if (startIndex + i >= mMenuList.size()) {
                holder.getMenus().get(i).setVisibility(View.INVISIBLE);
                continue;
            }

            final MenuSquareUnit view = holder.getMenus().get(i);
            view.setVisibility(View.VISIBLE);
            view.getIconView().setImageDrawable(getIcon(startIndex + i));
            view.getNameView().setText(getName(startIndex + i));
            if (startIndex + i < mMenuList.size()) {
                view.getIconView().setTag(mMenuList.get(startIndex + i));
            }
            view.getIconView().setOnClickListener(new OnItemClick());
        }
    }

    private Drawable getIcon(int position) {
        if (position < mMenuList.size()) {
            if (isStyle) {
                return mMenuList.get(position).getIconWithoutCache(mContext);
            } else {
                return mMenuList.get(position).getIcon(mContext);
            }
        }
        return mContext.getResources().getDrawable(R.drawable.ic_launcher);
    }

    private String getName(int position) {
        if (position < mMenuList.size()) {
            return mMenuList.get(position).getName(mContext);
        }
        return mContext.getString(R.string.Unkown);
    }

    @Override
    public MenuSquareHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 自定义View
        final MenuSquarePager view = new MenuSquarePager(mContext, parent, isStyle);
        return new MenuSquareHolder(view);
    }

    class MenuSquareHolder extends ViewHolder {

        private ArrayList<MenuSquareUnit> mMenus;
        private int mMenuCount;

        MenuSquareHolder(MenuSquarePager parent) {
            super(parent);
            mMenus = new ArrayList<>();
            mMenuCount = parent.getChildCount();
            for (int i = 0; i < mMenuCount; i++) {
                mMenus.add((MenuSquareUnit) parent.getChildAt(i));
            }
        }

        ArrayList<MenuSquareUnit> getMenus() {
            return mMenus;
        }
    }

    private class OnItemClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (mOnSquareMenuClickListen != null) {
                mOnSquareMenuClickListen.onSquareMenuClick(v);
            }
        }
    }

    public interface OnSquareMenuClickListen {
        void onSquareMenuClick(View view);
    }
}
