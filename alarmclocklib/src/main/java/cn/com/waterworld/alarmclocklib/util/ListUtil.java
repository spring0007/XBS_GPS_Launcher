package cn.com.waterworld.alarmclocklib.util;

import java.util.List;

/**
 * Created by Administrator on 2018/3/28.
 */

public class ListUtil {

    public static boolean isEmpty(List list) {
        if ((null != list) && (list.size() > 0)) {
            return false;
        } else {
            return true;
        }
    }
}
