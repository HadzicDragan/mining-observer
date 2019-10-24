package com.ad.miningobserver.file;

import java.nio.file.Path;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.ad.miningobserver.util.DateReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Scheduled class for execution by {@link FileCleanupSchedular} 
 * for deleting unused files.
 */
@Component
public class ScheduledFile implements Runnable {
    
    /**
     * #TODO add documentation
     * 
     * <li>{@link #GPU}</li>
     * <li>{@link #NETWORK}</li>
     * <li>{@link #ERROR}</li>
     * <li>{@link #TEMPERATURE}</li>
     */
    public enum FileCleanup {
        /**
         * 
         */
        GPU(new DateReference(2, ChronoUnit.DAYS)),
        /**
         * 
         */
        NETWORK(new DateReference(5, ChronoUnit.DAYS)),
        /**
         * 
         */
        ERROR(new DateReference(5, ChronoUnit.DAYS)),
        /**
         * 
         */
        TEMPERATURE(new DateReference(7, ChronoUnit.DAYS));
        
        private final DateReference dateRef;
        
        private FileCleanup(final DateReference dateRef) {
            this.dateRef = dateRef;
        }

        public DateReference getDateRef() {
            return dateRef;
        }
    }
    
    @Autowired
    private Finder fileFinder;
    
    /**
     * Remove the files by their respected time periods.
     * Every type of file has it's own time difference when they should be deleted.
     * Check {@link FileCleanup} for reference by type of file.
     */
    @Override
    public void run() {
        this.cleanupGpuListDirectory();
        this.cleanupNetworkDirectory();
        this.cleanupErrorDirectory();
        this.cleanupTemperatureDirectory();
    }
    
    /**
     * Cleanup the gpu list directory.
     */
    private void cleanupGpuListDirectory() {
        this.cleanupFiles(this.fileFinder.getGpuListDirectory(), FileCleanup.GPU);
    }

    /**
     * Cleanup the network directory.
     */
    private void cleanupNetworkDirectory() {
        this.cleanupFiles(this.fileFinder.getNetworkDirectory(), FileCleanup.NETWORK);
    }

    /**
     * Cleanup the error directory.
     */
    private void cleanupErrorDirectory() {
        this.cleanupFiles(this.fileFinder.getErrorDirectory(), FileCleanup.ERROR);
    }
    
    /**
     * Cleanup the temperature directory.
     */
    private void cleanupTemperatureDirectory() {
        this.cleanupFiles(this.fileFinder.getTemperatureDirectory(), FileCleanup.TEMPERATURE);
    }

    /**
     * Delete the files in the directory by {@link FileCleanup}
     * 
     * @param directory that will be checked and cleaned
     * @param file type of file by {@link DateReference}
     */
    private void cleanupFiles(final String directory, FileCleanup file) {
        final List<Path> paths = this.fileFinder.getFilesByDate(
                directory, file.getDateRef());
        Creator.removePathFiles(paths);
    }
}
