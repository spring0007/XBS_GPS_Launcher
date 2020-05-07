package com.sczn.wearlauncher.setting.adapetr;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sczn.wearlauncher.clock.ClockSkinUtil;

/**
 * Description:viewpager+glide 大量加载本地图片卡顿和oom
 * Created by Bingo on 2019/4/20.
 */
public class LargePicturesPagerAdapter extends PagerAdapter {

    private String[] files;
    private Context mContext;
    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    public LargePicturesPagerAdapter(Context context, String[] files) {
        mContext = context;
        if (files != null) {
            this.files = files;
        }
    }

    public void setData(String[] files) {
        if (files != null) {
            this.files = files;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return files == null ? 0 : files.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 必须要实现的方法
     * 每次滑动的时实例化一个页面,ViewPager同时加载3个页面,假如此时你正在第二个页面，向左滑动，
     * 将实例化第4个页面
     **/
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView imageView = new ImageView(mContext);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(layoutParams);
        imageView.setPadding(30, 30, 30, 30);
        container.addView(imageView);

        imageView.setImageDrawable(ClockSkinUtil.getClockSkinModelByName(mContext, files[position]));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });
        return imageView;
    }

    /**
     * 必须要实现的方法
     * 滑动切换的时销毁一个页面，ViewPager同时加载3个页面,假如此时你正在第二个页面，向左滑动，
     * 将销毁第1个页面
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ImageView imageView = (ImageView) object;
        if (imageView == null)
            return;
        // Glide.clear(imageView);//核心，解决OOM
        container.removeView(imageView);
    }
}
