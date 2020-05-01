package com.ad.miningobserver.state.control;

import com.ad.miningobserver.EventDesignator;
import com.ad.miningobserver.NameReference;
import com.ad.miningobserver.state.boundary.StateManager.Status;
import com.ad.miningobserver.state.boundary.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.IntervalTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * StateSchedular
 */
@Configuration
@EnableScheduling
public class StateSchedular implements SchedulingConfigurer, EventDesignator<Status> {

    private static final int QUERY_TIME = 20 * 1000; // 10sec
    private static final int DELAY_TIME = 5 * 1000; // 5sec

    private final StateService service;
    private final AtomicBoolean initialized;

    @Autowired
    public StateSchedular(StateService service) {
        this.service = service;
        this.initialized = new AtomicBoolean(false);
    }

    /**
     * Scheduled thread pool for running State initialization
     */
    @Bean(name = {NameReference.EXECUTOR_STATE_SCHEDULAR})
    public Executor stateThreadPoolSchedular() {
        return Executors.newScheduledThreadPool(1);
    }

    /**
     * When the application is ready, it will try to initialize
     * the required state properties.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initTask() {
        this.initializeState();
    }

    @EventListener(value = {Status.class})
    @Override
    public void handleEvent(Status status) {
        if (Status.ESTABLISHED == status && !this.initialized.get()) {
            this.initialized.set(true);
        }
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar registrar) {
        IntervalTask intervalTask =
                new IntervalTask(this::initializeState, QUERY_TIME, DELAY_TIME);
        registrar.addFixedDelayTask(intervalTask);
        registrar.setScheduler(this.stateThreadPoolSchedular());
        // TODO check out
        // https://stackoverflow.com/questions/44644141/how-to-stop-a-scheduled-task-that-was-started-using-scheduled-annotation/50216003#50216003
        // find out if you can close/destroy the
        // scheduledexecutor if state is initialized
    }

    /**
     * Helper method to initialize application state.
     */
    private void initializeState() {
        if (this.initialized.get()) {
            return;
        }
        this.service.publishMinerAddress();
        this.service.publishWorkerName();
    }
}
