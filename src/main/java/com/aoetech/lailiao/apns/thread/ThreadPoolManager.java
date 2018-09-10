package com.aoetech.lailiao.apns.thread;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by HASEE on 2018/5/5.
 */
public class ThreadPoolManager {
    private ExecutorService serverWorkPool;
    private static ThreadPoolManager ourInstance = new ThreadPoolManager();

    public static ThreadPoolManager getInstance() {
        return ourInstance;
    }

    private ThreadPoolManager() {
        int threadCount = Runtime.getRuntime().availableProcessors() * 2 + 1;
        serverWorkPool = Executors.newFixedThreadPool(threadCount);
    }

    public void dealWork(Runnable work){
        serverWorkPool.execute(work);
    }
}
