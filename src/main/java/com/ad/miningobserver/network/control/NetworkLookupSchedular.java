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
    
    /**
     * #TODO should be changed maybe to 5min
     * Every 2 minutes run the lookup job
     */
    private static final String SCHEDULE_EVERY_DAY_MIDNIGHT = "0 */1 * ? * *";
//    private static final String SCHEDULE_EVERY_DAY_MIDNIGHT = "0 */2 * ? * *";

    @Autowired
    private NetworkLookupTask networkLookup;

    @Bean(name = {NameReference.EXECUTOR_NETWORK_LOOKUP_SCHEDULAR})
    public Executor networkLookupThreadPoolSchedular() {
        return Executors.newScheduledThreadPool(1);
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar registrar) {
        registrar.addCronTask(this.networkLookup, SCHEDULE_EVERY_DAY_MIDNIGHT);
        registrar.setScheduler(this.networkLookupThreadPoolSchedular());
    }
}
