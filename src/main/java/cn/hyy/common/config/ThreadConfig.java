package cn.hyy.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 举个例子，代码里这么用
 * 这个线程池核心线程数2个，最大线程数10个，队列10000个
 * 在任务来的时候，核心线程->最大线程->入队->拒绝策略(报错)
 * @author hyy
 */
@Component
public class ThreadConfig {
    private final AtomicInteger mThreadNum = new AtomicInteger();

    @Bean
    public ThreadPoolExecutor mediateThreadPool() {
        return new MyThreadPoolExecutor(2, 10, 10, TimeUnit.SECONDS, new TaskQueue<>(10000),
                t -> new Thread(t, "mediatePublic-" + mThreadNum.getAndIncrement()));
    }
}
