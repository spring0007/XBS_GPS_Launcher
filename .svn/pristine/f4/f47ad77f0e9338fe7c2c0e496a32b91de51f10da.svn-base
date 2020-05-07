package com.sczn.wearlauncher.friend;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by k.liang on 2018/4/18 17:05
 */
public class ObserverManager implements SubjectListener {

    private static ObserverManager observerManager;

    private List<ObserverListener> list = new ArrayList<>();

    public static int CHAT_MSG_OBSERVER = 1;
    public static int CHAT_READ_MSG_OBSERVER = 2;
    public static int BATTERY_CHANGE = 3;

    public static ObserverManager getInstance() {
        if (null == observerManager) {
            synchronized (ObserverManager.class) {
                if (null == observerManager) {
                    observerManager = new ObserverManager();
                }
            }
        }
        return observerManager;
    }

    @Override
    public void add(ObserverListener observerListener) {
        list.add(observerListener);
    }

    @Override
    public void notification(int type, Object o) {
        for (ObserverListener observerListener : list) {
            observerListener.moduleRefresh(type, o);
        }
    }

    @Override
    public void remove(ObserverListener observerListener) {
        if (list != null && list.contains(observerListener)) {
            list.remove(observerListener);
        }
    }
}
