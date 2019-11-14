package com.ad.miningobserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * BeanAsyncExecutorProvider
 */
@Configuration
@EnableAsync
public class BeanAsyncExecutorProvider {

    @Value(value = "${executor.thread.core.pool.size}")
    private int corePoolSize;
    @Value(value = "${executor.thread.max.pool.size}")
    private int maxPoolSize;

    @Bean(name = {NameReference.EXECUTOR_ASYNC_TASK_THREAD_POOL})
    public ThreadPoolTaskExecutor asyncThreadPoolExecutor() {
        final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(corePoolSize);
        taskExecutor.setMaxPoolSize(maxPoolSize);
        taskExecutor.initialize();
        return taskExecutor;
    }
}