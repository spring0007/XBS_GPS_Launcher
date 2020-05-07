package com.sczn.wearlauncher.menu.util;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.menu.bean.AppMenu;

import java.util.List;

/**
 * Description:
 * Created by Bingo on 2019/1/3.
 */
public class MenuUtil {

    /**
     * @param position
     * @return
     */
    public static Drawable getIcon(Context mActivity, List<AppMenu> menuList, int position, boolean isStyle) {
        if (position < menuList.size()) {
            if (isStyle) {
                return menuList.get(position).getIconWithoutCache(mActivity);
            } else {
                return menuList.get(position).getIcon(mActivity);
            }
        }
        return mActivity.getResources().getDrawable(R.drawable.ic_launcher);
    }

    /**
     * @param position
     * @return
     */
    public static String getName(Context mActivity, List<AppMenu> menuList, int position) {
        if (position < menuList.size()) {
            return menuList.get(position).getName(mActivity);
        }
        return mActivity.getString(R.string.Unkown);
    }
}
