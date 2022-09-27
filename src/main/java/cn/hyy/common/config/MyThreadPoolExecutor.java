package cn.hyy.common.config;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 自定义线程池，主要为了优先使用最大线程数
 * @author huyangyang
 */
public class MyThreadPoolExecutor extends ThreadPoolExecutor {

    /**
     * 定义一个成员变量，用于记录当前线程池中已提交的任务数量
     */
    private final AtomicInteger submittedTaskCount = new AtomicInteger(0);

    public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                                TimeUnit unit, TaskQueue<Runnable> workQueue,ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,threadFactory);
        workQueue.setMyThreadPoolExecutor(this);
    }

    public int getSubmittedTaskCount() {
        return submittedTaskCount.get();
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        submittedTaskCount.decrementAndGet();
    }

    @Override
    public void execute( Runnable command) {
        // 将池中已提交的任务数 + 1
        submittedTaskCount.incrementAndGet();
        try {
            super.execute(command);
        } catch (RejectedExecutionException rx) {
            // retry to offer the task into queue.
            final TaskQueue queue = (TaskQueue) super.getQueue();
            try {
                if (!queue.retryOffer(command, 0, TimeUnit.MILLISECONDS)) {
                    submittedTaskCount.decrementAndGet();
                    throw new RejectedExecutionException("Queue capacity is full.", rx);
                }
            } catch (InterruptedException x) {
                submittedTaskCount.decrementAndGet();
                throw new RejectedExecutionException(x);
            }
        } catch (Throwable t) {
            submittedTaskCount.decrementAndGet();
            throw t;
        }
    }
}
