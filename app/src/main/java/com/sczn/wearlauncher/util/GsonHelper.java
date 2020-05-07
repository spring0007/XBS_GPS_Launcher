package com.sczn.wearlauncher.util;

import com.google.gson.Gson;

/**
 * Description:
 * Created by Bingo on 2019/2/26.
 */
public class GsonHelper {

    private static GsonHelper helper;
    private Gson gson;

    /**
     * @return
     */
    public static GsonHelper getInstance() {
        if (null == helper) {
            synchronized (GsonHelper.class) {
                if (null == helper) {
                    helper = new GsonHelper();
                }
            }
        }
        return helper;
    }

    public GsonHelper() {
        gson = new Gson();
    }

    public Gson getGson() {
        return gson;
    }
}
