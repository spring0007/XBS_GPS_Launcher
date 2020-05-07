package com.sczn.wearlauncher.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtil {

    private static ExecutorService pool;

    public static ExecutorService getPool() {
        if (pool == null) {
            // 获取合适的线程池大小
            int i = Runtime.getRuntime().availableProcessors();
            pool = Executors.newFixedThreadPool((2 * i) + 1);
        }
        return pool;
    }

    /**
     * 线程休眠的方法
     *
     * @param ms
     */
    public static void threadDelayTime(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
