package com.sczn.wearlauncher.chat.views;

import android.support.v7.widget.RecyclerView;

/**
 * Description:
 * Created by Bingo on 2019/3/21.
 */
public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {

    public PaginationScrollListener() {

    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        // newState分 0,1,2三个状态,2是滚动状态,0是停止
        //-1代表顶部,返回true表示没到顶,还可以滑
        //1代表底部,返回true表示没到底部,还可以滑
        boolean b = recyclerView.canScrollVertically(-1);
        if (!isLoading() && newState == 0 && !b) {
            loadBeforeItems();
        }
    }

    protected abstract void loadBeforeItems();

    public abstract boolean isLoading();
}