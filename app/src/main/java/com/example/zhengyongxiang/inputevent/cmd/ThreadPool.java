package com.example.zhengyongxiang.inputevent.cmd;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ThreadPool {

    private static ThreadPool pool = new ThreadPool();

    private static final int nThreads = 5;
    private ExecutorService executor = null;

    public static ThreadPool getThreadPool() {
        return pool;
    }

    //创建固定大小的线程池,线程池中的线程会不断被重用（当空闲时）
    private ThreadPool() {
        /**
         * 始终维护固定数量的线程，线程在执行过程中由于错误导致终止，将会自动创建新的线程进行替补
         */
        executor = Executors.newFixedThreadPool(nThreads);
    }

    /**
     * @description 添加任务并执行
     * @author zhengyx
     * @date 2017/5/3
     */
    public ThreadPool addTask(Task task) {
        executor.execute(task);
        return this;
    }

    /**
     * @description 移除任务
     * @author zhengyx
     * @date 2017/5/3
     */
    public void removeTask(Task task) {
        task.shutDown();
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