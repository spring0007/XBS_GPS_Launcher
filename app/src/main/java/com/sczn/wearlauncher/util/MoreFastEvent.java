package com.sczn.wearlauncher.util;

/**
 * Description:多次点击过滤
 * Created by Bingo on 2019/1/23.
 */
public class MoreFastEvent {

    private static MoreFastEvent moreOnClickStack;
    private static long eventTime;

    public static MoreFastEvent getInstance() {
        if (moreOnClickStack == null) {
            synchronized (MoreFastEvent.class) {
                if (moreOnClickStack == null) {
                    moreOnClickStack = new MoreFastEvent();
                }
            }
        }
        return moreOnClickStack;
    }

    /**
     * @param timeMillis
     * @param listener
     */
    public void event(long timeMillis, onEventCallBackListener listener) {
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - eventTime) >= timeMillis) {
            if (listener != null) {
                listener.onCallback();
            }
        }
        eventTime = currentClickTime;
    }

    /**
     * @param timeMillis
     * @param listener
     */
    public void event(long timeMillis, long eventTime, onEventCallBackWithTimeListener listener) {
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - eventTime) >= timeMillis) {
            if (listener != null) {
                listener.onCallback(currentClickTime);
            }
        }
    }

    /**
     *
     */
    public interface onEventCallBackWithTimeListener {
        void onCallback(long eventTime);
    }

    /**
     *
     */
    public interface onEventCallBackListener {
        void onCallback();
    }
}
