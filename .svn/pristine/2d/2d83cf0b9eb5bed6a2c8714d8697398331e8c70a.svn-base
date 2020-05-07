package com.sczn.wearlauncher.chat.util;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.socket.SocketConstant;

/**
 * Description:
 * Created by Bingo on 2019/3/21.
 */
public class ImageLoaderHelper {

    /**
     * 聊天好友的头像显示
     *
     * @param url
     * @param view
     */
    public static void displayImage(String url, ImageView view) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.contact_default)            // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.contact_default)          // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.contact_default)               // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)                                          // 设置下载的图片是否缓存在SD卡中
                .build();
        if (url != null && url.contains("sys_")) {
            ImageLoader.getInstance().displayImage(getSysImage(url), view);
        } else {
            ImageLoader.getInstance().displayImage(SocketConstant.chatFileAddress(url), view, options);
        }
    }

    /**
     * 聊天图片
     *
     * @param url
     * @param view
     */
    public static void displayChatImage(String url, ImageView view) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.temp_face)            // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.temp_face)          // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.temp_face)               // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)                                          // 设置下载的图片是否缓存在SD卡中
                .build();
        ImageLoader.getInstance().displayImage(SocketConstant.chatFileAddress(url), view, options);
    }


    /**
     * 聊天群组的头像显示
     *
     * @param url
     * @param view
     */
    public static void displayImageChatGroup(String url, ImageView view) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.temp_discuss)            // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.temp_discuss)          // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.temp_discuss)               // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)                                          // 设置下载的图片是否缓存在SD卡中
                .build();
        ImageLoader.getInstance().displayImage(SocketConstant.chatFileAddress(url), view, options);
    }

    /**
     * 获取系统的资源
     *
     * @param url
     * @return
     */
    private static String getSysImage(String url) {
        String uri = "drawable://" + R.drawable.contact_default;
        if (url.contains("sys_child_01")) {
            uri = "drawable://" + R.drawable.sys_child_01;
        } else if (url.contains("sys_child_02")) {
            uri = "drawable://" + R.drawable.sys_child_02;
        } else if (url.contains("sys_child_03")) {
            uri = "drawable://" + R.drawable.sys_child_03;
        } else if (url.contains("sys_child_04")) {
            uri = "drawable://" + R.drawable.sys_child_04;
        } else if (url.contains("sys_child_05")) {
            uri = "drawable://" + R.drawable.sys_child_05;
        } else if (url.contains("sys_child_06")) {
            uri = "drawable://" + R.drawable.sys_child_06;
        } else if (url.contains("sys_child_07")) {
            uri = "drawable://" + R.drawable.sys_child_07;
        } else if (url.contains("sys_child_08")) {
            uri = "drawable://" + R.drawable.sys_child_08;
        } else if (url.contains("sys_child_09")) {
            uri = "drawable://" + R.drawable.sys_child_09;
        } else if (url.contains("sys_child_10")) {
            uri = "drawable://" + R.drawable.sys_child_10;
        } else if (url.contains("sys_child_11")) {
            uri = "drawable://" + R.drawable.sys_child_11;
        } else if (url.contains("sys_child_12")) {
            uri = "drawable://" + R.drawable.sys_child_12;
        } else if (url.contains("sys_child_13")) {
            uri = "drawable://" + R.drawable.sys_child_13;
        } else if (url.contains("sys_child_14")) {
            uri = "drawable://" + R.drawable.sys_child_14;
        } else if (url.contains("sys_child_15")) {
            uri = "drawable://" + R.drawable.sys_child_15;
        } else if (url.contains("sys_child_16")) {
            uri = "drawable://" + R.drawable.sys_child_16;
        }
        //
        else if (url.contains("sys_parent_01")) {
            uri = "drawable://" + R.drawable.sys_parent_01;
        } else if (url.contains("sys_parent_02")) {
            uri = "drawable://" + R.drawable.sys_parent_02;
        } else if (url.contains("sys_parent_03")) {
            uri = "drawable://" + R.drawable.sys_parent_03;
        } else if (url.contains("sys_parent_04")) {
            uri = "drawable://" + R.drawable.sys_parent_04;
        } else if (url.contains("sys_parent_05")) {
            uri = "drawable://" + R.drawable.sys_parent_05;
        } else if (url.contains("sys_parent_06")) {
            uri = "drawable://" + R.drawable.sys_parent_06;
        } else if (url.contains("sys_parent_07")) {
            uri = "drawable://" + R.drawable.sys_parent_07;
        } else if (url.contains("sys_parent_08")) {
            uri = "drawable://" + R.drawable.sys_parent_08;
        } else if (url.contains("sys_parent_09")) {
            uri = "drawable://" + R.drawable.sys_parent_09;
        } else if (url.contains("sys_parent_10")) {
            uri = "drawable://" + R.drawable.sys_parent_10;
        } else if (url.contains("sys_parent_11")) {
            uri = "drawable://" + R.drawable.sys_parent_11;
        } else if (url.contains("sys_parent_12")) {
            uri = "drawable://" + R.drawable.sys_parent_12;
        } else if (url.contains("sys_parent_13")) {
            uri = "drawable://" + R.drawable.sys_parent_13;
        } else if (url.contains("sys_parent_14")) {
            uri = "drawable://" + R.drawable.sys_parent_14;
        } else if (url.contains("sys_parent_15")) {
            uri = "drawable://" + R.drawable.sys_parent_15;
        } else if (url.contains("sys_parent_16")) {
            uri = "drawable://" + R.drawable.sys_parent_16;
        }
        return uri;
    }
}
