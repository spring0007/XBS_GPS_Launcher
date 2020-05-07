package com.sczn.wearlauncher.util;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.sczn.wearlauncher.app.LauncherApp;

import java.util.Locale;

/**
 * Description:
 * Created by Bingo on 2019/5/10.
 */
public class LanguageUtil {
    /**
     * 选择语言
     * zh
     * en
     *
     * @param language
     */
    public static void changeAppLanguage(String language) {
        Locale myLocale;
        if (language != null && !"".equals(language)) {
            myLocale = new Locale(language);
        } else {
            // 本地语言设置
            myLocale = Locale.getDefault();
        }
        Resources res = LauncherApp.getAppContext().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        ActivityUtils.gotoHome(LauncherApp.getAppContext(), "", "");
    }
}
