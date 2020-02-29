package com.ad.miningobserver.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.ad.miningobserver.util.ApplicationLogger;
import com.ad.miningobserver.util.DateReference;
import com.ad.miningobserver.util.TimeAsserter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Read/lookup actions for files.
 */
@Component
public class Finder {

    /**
     * Enumerated class that holds the relative path to the application files.
     */
    public enum ApplicationFile {

        /**
         * Application process ID which can be used to shutdown the application.
         */
        PID_FILE("/data/application_pid");

        private final String file;

        /**
         * Enumeration constructor which accepts a String as a parameter.
         *
         * @param file application file
         */
        private ApplicationFile(String file) {
            this.file = file;
        }

        /**
         * @return the relative path of the file
         */
        public String getFile() {
            return file;
        }
    }
    
    /**
     * Root location of the started application.
     */
    private static final String USER_DIR = "user.dir";
    
    @Value("#{systemProperties}")
    private Map<String, String> systemPropertiesMap;
    
    private String userDirLocation;
    
    private static final String ERROR_LOCATION = "/data/gpu/error/";
    private static final String NETWORK_LOCATION = "/data/network/";
    private static final String GPULIST_LOCATION = "/data/gpu/";
    private static final String EXCEPTION_LOCATION = "/data/error";
    private static final String TEMPERATURE_LOCATION = "/data/temperature/";
    
    /**
     * Get the application directory by looking up System properties.
     * This method will return always the directory from which the 
     * application has started from.
     * If the .jar file is not located inside proper location the 
     * application might not work properly.
     * @return application root directory
     */
    public String getApplicationDirectory() {
        if (this.userDirLocation != null) {
            return this.userDirLocation;
        }
        if (this.systemPropertiesMap.containsKey(USER_DIR)) {
            String property = this.systemPropertiesMap.get(USER_DIR);
            if (property == null || property.isEmpty()) {
                throw new RuntimeException(USER_DIR + " property not setup.");
            }
            
            this.userDirLocation = this.systemPropertiesMap.get(USER_DIR);
            return this.userDirLocation;
        }
        throw new RuntimeException("Unable to locate application location "
                    + "by System properties.");
    }
    
    /**
     * Checks if configuration files are readable. Iterates over the enumerated
     * values and checks if the file is able to be read. If the file is not
     * readable, a RuntimeException will be thrown.
     */
    public void canReadMandatoryFiles() {
        final String applicationDirectory = this.getApplicationDirectory();

        final ApplicationFile[] values = ApplicationFile.values();
        for (ApplicationFile value : values) {
            this.canReadFile(applicationDirectory + value.getFile());
        }
    }
    
    /**
     * @param applicationFile enumerated value
     * @return string value of the enumeration passed
     */
    public String getApplicationFileLocation(final ApplicationFile applicationFile) {
        return this.getApplicationDirectory()
                + applicationFile.getFile();
    }

    /**
     * Checks to see if the file can be read or else it throws a
     * {@link RuntimeException}.
     *
     * @param fileLocation
     * @throws RuntimeException
     */
    private void canReadFile(final String fileLocation) throws RuntimeException {
        final Path propertiesPath = Paths.get(fileLocation);
        if (!Files.isReadable(propertiesPath)) {
            final String errorMessage = "Unable to read file. "
                    + "Location : " + fileLocation;
            ApplicationLogger.errorLogger().error(errorMessage);

            throw new RuntimeException(errorMessage);
        }
    }
    
    /**
     * System directories that should be created on the project path if they do
     * not exist already.
     * @return List<String> of application directories that need to be present
     */
    public List<String> directories() {
        final List<String> directories = new ArrayList<>();
        directories.add("data");
        directories.add("config");
        directories.add("scripts");
        directories.add("data/error");
        directories.add("data/network");
        directories.add("data/gpu");
	    directories.add("data/gpu/error");
        directories.add("data/temperature");
        return directories;
    }
    
    public String getErrorDirectory() {
        return this.getApplicationDirectory() + ERROR_LOCATION;
    }
    
    public String getNetworkDirectory() {
        return this.getApplicationDirectory() + NETWORK_LOCATION;
    }
    
    public String getGpuListDirectory() {
        return this.getApplicationDirectory() + GPULIST_LOCATION;
    }
    
    public String getTemperatureDirectory() {
        return this.getApplicationDirectory() + TEMPERATURE_LOCATION;
    }
    
    public String getExceptionDirectory() {
        return this.getApplicationDirectory() + EXCEPTION_LOCATION;
    }
    
    public List<String> getErrorFiles() {
        return this.getFiles(this.getErrorDirectory());
    }
    
    public List<String> getNetworkFiles() {
        return this.getFiles(this.getNetworkDirectory());
    }
    
    public List<String> getGpuListFiles() {
        return this.getFiles(this.getGpuListDirectory());
    }
    
    public List<String> getTemperatureFiles() {
        return this.getFiles(this.getTemperatureDirectory());
    }
    
    public List<String> getExceptionFiles() {
	return this.getFiles(this.getExceptionDirectory());
    }

    private List<String> getFiles(final String directory) {
        final File file = new File(directory);
        return this.getFilesInDirectory(file);
    }

    private List<String> getFilesInDirectory(final File file) {
        return Arrays.stream(file.listFiles())
            .filter(File::isFile)
            .map(File::getPath)
            .collect(Collectors.toList());
    }
    
    public synchronized List<Path> getFilesByDate(final String directory, final DateReference dateRef) {
        final Instant reducedInstant = TimeAsserter.reducedInstantFromNow(dateRef);
        final Path path = Path.of(directory);
        try {
            return Files.list(path)
                    .filter(isCurrentFileOlder(reducedInstant))
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            ApplicationLogger.errorLogger().error(ex);
        }
        
        return Collections.emptyList();
    }
    
    private Predicate<Path> isCurrentFileOlder(final Instant instant) {
        return path -> this.compareInstantFromPath(path, instant);
    }

    private boolean compareInstantFromPath(final Path path, final Instant reducedInstant) {
        final Instant fileInstant = this.getCreatedDateInstantFromPath(path);
        if (fileInstant == null) {
            return false;
        }
        return reducedInstant.compareTo(fileInstant) == 1;
    }
    
    private Instant getCreatedDateInstantFromPath(final Path path) {
        try {
            return Files
                    .readAttributes(path, BasicFileAttributes.class)
                    .creationTime()
                    .toInstant();
        } catch (IOException ex) {
            ApplicationLogger.errorLogger().error(ex);
        }
        return null;
    }
}
