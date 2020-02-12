package com.ad.miningobserver.file;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.nio.file.Path;
import java.time.temporal.ChronoUnit;

import com.ad.miningobserver.NameReference;
import com.ad.miningobserver.util.DateReference;

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

    /** Every day at 12PM */
    private static final String SCHEDULE_EVERY_DAY_MIDNIGHT = "0 0 0 * * ?";

    private ScheduledFiles scheduledFiles;

    public FileCleanupSchedular() {
        this.scheduledFiles = new ScheduledFiles();
    }

    @Autowired
    private Finder fileFinder;

    @Bean(name = {NameReference.EXECUTOR_FILE_CLEANER_SCHEDULAR})
    public Executor fileCleanerThreadPoolSchedular() {
        return Executors.newScheduledThreadPool(1);
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar registrar) {
        registrar.addCronTask(this.scheduledFiles, SCHEDULE_EVERY_DAY_MIDNIGHT);
        registrar.setScheduler(this.fileCleanerThreadPoolSchedular());
    }

    private class ScheduledFiles implements Runnable {

        private final DateReference GPU = new DateReference(2, ChronoUnit.DAYS);
        private final DateReference NETWORK = new DateReference(5, ChronoUnit.DAYS);
        private final DateReference ERROR = new DateReference(5, ChronoUnit.DAYS);
        private final DateReference TEMPERATURE = new DateReference(7, ChronoUnit.DAYS);
        
        /**
         * Remove the files by their respected time periods.
         * Every type of file has it's own time difference when they should be deleted.
         */
        @Override
        public void run() {
            this.cleanupGpuListDirectory();
            this.cleanupNetworkDirectory();
            // #TODO add this cleanup after added implemenation to container
            // this.cleanupErrorDirectory();
            this.cleanupTemperatureDirectory();
        }
        
        /**
         * Cleanup the gpu list directory.
         */
        private void cleanupGpuListDirectory() {
            this.cleanupFiles(fileFinder.getGpuListDirectory(), GPU);
        }

        /**
         * Cleanup the network directory.
         */
        private void cleanupNetworkDirectory() {
            this.cleanupFiles(fileFinder.getNetworkDirectory(), NETWORK);
        }

        /**
         * Cleanup the error directory.
         */
        private void cleanupErrorDirectory() {
            this.cleanupFiles(fileFinder.getErrorDirectory(), ERROR);
        }
        
        /**
         * Cleanup the temperature directory.
         */
        private void cleanupTemperatureDirectory() {
            this.cleanupFiles(fileFinder.getTemperatureDirectory(), TEMPERATURE);
        }

        /**
         * Delete the files in the directory by {@link FileCleanup}
         * 
         * @param directory that will be checked and cleaned
         * @param file type of file by {@link DateReference}
         */
        private void cleanupFiles(final String directory, DateReference dateRef) {
            final List<Path> paths = fileFinder.getFilesByDate(
                    directory, dateRef);
            Creator.removePathFiles(paths);
        }
    }
}
