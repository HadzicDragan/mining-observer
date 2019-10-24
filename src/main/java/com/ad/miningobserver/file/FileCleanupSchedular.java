package com.ad.miningobserver.file;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.ad.miningobserver.NameReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * Configuration class that sets the Cron tab for {@link ScheduledFile} execution.
 */
@Configuration
@EnableScheduling
public class FileCleanupSchedular implements SchedulingConfigurer {

    /**
     * EVERY DAY AT 12PM
     */
    private static final String SCHEDULE_EVERY_DAY_MIDNIGHT = "0 0 0 * * ?";

    @Autowired
    private ScheduledFile scheduledFile;

    @Bean(name = {NameReference.EXECUTOR_FILE_CLEANER_SCHEDULAR})
    public Executor fileCleanerThreadPoolSchedular() {
        return Executors.newScheduledThreadPool(1);
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar registrar) {
        registrar.addCronTask(this.scheduledFile, SCHEDULE_EVERY_DAY_MIDNIGHT);
        registrar.setScheduler(this.fileCleanerThreadPoolSchedular());
    }
}
