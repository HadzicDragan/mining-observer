package com.ad.miningobserver.operation;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.ad.miningobserver.NameReference;
import com.ad.miningobserver.exception.ExceptionOperationHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

/**
 * OperationSchedular
 */
@Component
public class ScheduledOperation implements Runnable {

    private static final int MAX_QUEUE_CAP = 50;
    private final Queue<Operation> operations = new ArrayBlockingQueue<>(MAX_QUEUE_CAP);

    @Override
    public void run() {
        if (this.operations.isEmpty()) {
            return;
        }
        final Operation operation = this.operations.poll();
        operation.run();
    }

    public ScheduledOperation queueOperation(final Operation operation) {
        if (this.operations.size() == MAX_QUEUE_CAP) {
            ExceptionOperationHandler.registerUncommonOperation(
                this.getClass(), "queueOperation", "MAX_QUEUE_CAP reached.");
        }
        this.operations.add(operation);
        return this;
    }

    @Configuration
    @EnableScheduling
    public class OperationSchedular implements SchedulingConfigurer {

        /** Cron job on 10 seconds */
        private static final String ON_TIME = "*/10 * * * * *";

        @Autowired
        private ScheduledOperation scheduledOp;

        @Bean(name = {NameReference.EXECUTOR_OPERATION_SCHEDULAR})
        public Executor networkLookupThreadPoolSchedular() {
            return Executors.newScheduledThreadPool(1);
        }

        @Override
        public void configureTasks(ScheduledTaskRegistrar registrar) {
            registrar.addCronTask(scheduledOp, ON_TIME);
            registrar.setScheduler(this.networkLookupThreadPoolSchedular());
        }
    }
}