package cn.hyy.common.config;


import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 自定义队列
 * @author huyangyang
 */
public class TaskQueue<R extends Runnable> extends LinkedBlockingQueue<Runnable> {
    private static final long serialVersionUID = -6535853550281178697L;
    private MyThreadPoolExecutor myThreadPoolExecutor;

    public TaskQueue(int capacity) {
        super(capacity);
    }

    public void setMyThreadPoolExecutor(MyThreadPoolExecutor exec) {
        myThreadPoolExecutor = exec;
    }

    /**
     * 将任务提交到队列，返回成功失败，失败的时候会去创建线程
     * @param runnable 线程
     * @return 入队结果
     */
    @Override
    public boolean offer(Runnable runnable) {
        if (myThreadPoolExecutor == null) {
            throw new RejectedExecutionException("The task queue does not have executor!");
        }
        // 线程池的当前线程数
        int currentPoolThreadSize = myThreadPoolExecutor.getPoolSize();
        if (myThreadPoolExecutor.getSubmittedTaskCount() < currentPoolThreadSize) {
            // 已提交的任务数量小于当前线程数，意味着线程池中有空闲线程，直接扔进队列里，让线程去处理
            return super.offer(runnable);
        }

        // return false to let executor create new worker.
        if (currentPoolThreadSize < myThreadPoolExecutor.getMaximumPoolSize()) {
            // 重点： 当前线程数小于 最大线程数 ，返回false，暗含入队失败，让线程池去创建新的线程
            return false;
        }

        // 重点: 代码运行到此处，说明当前线程数 >= 最大线程数，需要真正的提交到队列中
        return super.offer(runnable);
    }

    public boolean retryOffer(Runnable o, long timeout, TimeUnit unit) throws InterruptedException {
        if (myThreadPoolExecutor.isShutdown()) {
            throw new RejectedExecutionException("Executor is shutdown!");
        }
        return super.offer(o, timeout, unit);
    }
}
