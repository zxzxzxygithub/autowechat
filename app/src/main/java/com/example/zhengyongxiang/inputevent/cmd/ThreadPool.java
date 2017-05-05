package com.example.zhengyongxiang.inputevent.cmd;

import android.util.ArrayMap;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author zhengyx
 * @description 线程池处理
 * @date 2017/5/4
 */
public class ThreadPool {

    private static ThreadPool pool = new ThreadPool();

    private static final int nThreads = 5;
    private ExecutorService executor = null;
    private ArrayMap<Runnable, Future<?>> map = new ArrayMap<>();

    /**
     * @description 获取线程池实例
     * @author zhengyx
     * @date 2017/5/4
     */
    public static ThreadPool getThreadPool() {
        return pool;
    }

    private ThreadPool() {
        executor = Executors.newFixedThreadPool(nThreads);
    }

    /**
     * @description 添加任务并执行
     * @author zhengyx
     * @date 2017/5/3
     */
    public ThreadPool addTask(Runnable task) {
        Future<?> future = executor.submit(task);
        map.put(task, future);
        return this;
    }

    /**
     * @description 移除任务
     * @author zhengyx
     * @date 2017/5/3
     */
    public void removeTask(Runnable task) {
        Future<?> future = map.get(task);
        if (future != null) {
            future.cancel(true);
            map.remove(task);
        }
    }

    /**
     * @description 结束所有任务
     * @author zhengyx
     * @date 2017/5/3
     */
    public void shutdownAll() {
        executor.shutdown();
    }
}