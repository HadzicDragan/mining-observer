package com.ad.miningobserver.network.control;

import com.ad.miningobserver.NameReference;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * Configuration class that sets the Cron tab for {@link NetworkLookupTask} execution.
 */
@Configuration
@EnableScheduling
public class NetworkLookupSchedular implements SchedulingConfigurer {
    
    /** Cron job on 3 minutes */
    private static final String ON_TIME_LOOKUP = "0 */3 * * * *";

    @Autowired
    private NetworkLookupTask networkLookup;

    @Bean(name = {NameReference.EXECUTOR_NETWORK_LOOKUP_SCHEDULAR})
    public Executor networkLookupThreadPoolSchedular() {
        return Executors.newScheduledThreadPool(1);
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar registrar) {
        registrar.addCronTask(this.networkLookup, ON_TIME_LOOKUP);
        registrar.setScheduler(this.networkLookupThreadPoolSchedular());
    }
}
