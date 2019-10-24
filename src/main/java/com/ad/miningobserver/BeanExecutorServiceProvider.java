package com.ad.miningobserver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class BeanExecutorServiceProvider {

    /**
     * #TODO check if these thread are enough Thread pool for running
     * application operation tasks
     */
    @Bean(name = {NameReference.EXECUTOR_OPERATION_THREAD_POOL})
    public ThreadPoolTaskExecutor operationThreadPoolExecutor() {
        final ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
        poolTaskExecutor.setCorePoolSize(2);
        poolTaskExecutor.setMaxPoolSize(2);
        poolTaskExecutor.initialize();
        return poolTaskExecutor;
    }

    /**
     * Thread pool for running command line tasks
     */
    @Bean(name = {NameReference.EXECUTOR_TASK_THREAD_POOL})
    public ThreadPoolTaskExecutor taskThreadPoolExecutor() {
        final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(6);
        taskExecutor.setMaxPoolSize(6);
        taskExecutor.initialize();
        return taskExecutor;
    }
}
