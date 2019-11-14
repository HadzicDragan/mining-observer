package com.ad.miningobserver.file;

import java.io.IOException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.ad.miningobserver.SpringContextLookup;
import com.ad.miningobserver.exception.ExceptionOperationHandler;
import com.ad.miningobserver.util.ApplicationLogger;
import com.ad.miningobserver.util.StringUtils;

/**
 * Create/delete actions for files.
 */
public class Creator {
    
    /**
     * User does not have permission to create directory
     */
    public static final String PERMISSION_DENIED_DIRECTORY = 
            "You don't have rights to create this directory : ";
    
    /**
     * Creates the file directory. If the user does not have the permissions for
     * this directory an RuntimeException is thrown.
     *
     * @param fileApplication
     * @throws IOException
     */
    public static void createApplicationDiretory(final String directory, final String fileApplication)
            throws IOException {
        final Path applicationPath = Paths.get(fileApplication);
        if (!Files.exists(applicationPath)) {
            if (Creator.hasWritePermission(directory)) {
                Files.createDirectory(applicationPath);
            } else {
                Creator.unableToCreate(applicationPath.toString());
            }
        }
    }
    
    /**
     * Create the file if there are write permissions and if the file does not
     * exist.
     * @param filename string representation of the file name
     * @throws IOException
     */
    public static void createFile(final String directory,final String filename) throws IOException {
        if (!Creator.hasWritePermission(directory)) {
            Creator.unableToCreate(filename);
        }

        Path filePath = Paths.get(filename);
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }
    }
    
    /**
     * Delete network files by parameter name.
     * 
     * @param uuid name of the file, without the extension
     * @return {@code boolean} true if the file is deleted, else false
     */
    public static boolean removeErrorFileByID(final String uuid) {
        final Finder fileFinder = Creator.getFinder();
        return Creator.isFileRemoved(fileFinder.getErrorDirectory(), uuid);
    }
    
    /**
     * Delete network files by parameter name.
     * 
     * @param uuid name of the file, without the extension
     * @return {@code boolean} true if the file is deleted, else false
     */
    public static boolean removeNetworkFileByID(final String uuid) {
        final Finder fileFinder = Creator.getFinder();
        return Creator.isFileRemoved(fileFinder.getNetworkDirectory(), uuid);
    }
    
    /**
     * Delete gpu list files by parameter name.
     * 
     * @param uuid name of the file, without the extension
     * @return {@code boolean} true if the file is deleted, else false
     */
    public static boolean removeGpuListFile(final String uuid) {
        final Finder fileFinder = Creator.getFinder();
        return Creator.isFileRemoved(fileFinder.getGpuListDirectory(), uuid);
    }
    
    /**
     * Delete gpu list files.
     */
    public static void removeGPUListFiles() {
        final Finder fileFinder = Creator.getFinder();
        Creator.removeFiles(fileFinder.getGpuListFiles());
    }
    
    /**
     * Delete network files.
     */
    public static void removeNetworkFiles() {
        final Finder fileFinder = Creator.getFinder();
        Creator.removeFiles(fileFinder.getNetworkFiles());
    }
    
    /**
     * {@code List} files that will be deleted.
     * 
     * @param files path names represented in {@code String} format
     */
    public static void removeFiles(final List<String> files) {
        files.forEach(Creator::removeFile);
    }
    
    /**
     * {@code List} files that will be deleted.
     * 
     * @param files path names represented in {@code Path} format
     */
    public static void removePathFiles(final List<Path> paths) {
        paths.forEach(path -> {
            try {
                Files.deleteIfExists(path);
            } catch (IOException ex) {
                ExceptionOperationHandler.registerExceptionOperation(
                    Creator.class, ex, "removeFile(final List<Path> paths)", path.toString());
            }
        });
    }
    
    /**
     * Check if the directory is writable.
     * 
     * @param applicationPath application root path
     * @return {@code boolean} true is the path is writable, else false
     */
    private static boolean hasWritePermission(final String applicationPath) {
        try {
            Path path = Paths.get(applicationPath);
            return Files.isWritable(path);
        } catch (FileSystemNotFoundException | SecurityException ex) {
            ApplicationLogger.errorLogger().error(ex);
            return false;
        }
    }

    /**
     * Throw {@code RuntimeException} if the user does not have create rights.
     * 
     * @param applicationPath path to the application
     * @throws RuntimeException if the user does not have rights to create directory
     */
    private static void unableToCreate(final String applicationPath) throws RuntimeException {
        throw new RuntimeException(PERMISSION_DENIED_DIRECTORY + applicationPath);
    }
    
    /**
     * Convenient method for deleting files in {@code directory} 
     * and by name {@code uuid}.
     * 
     * @param directory 
     * @param uuid file name without the extension
     * @return {@code boolean} true if the file is deleted, else false
     */
    private static boolean isFileRemoved(final String directory, final String uuid) {
        final String errorJson = StringUtils.buildString(
                directory, uuid, FileExtensions.JSON_EXTENSION);
        return Creator.removeFile(errorJson);
    }
    
    /**
     * Delete file from the system by the provided {@code filePath}.
     * 
     * @param filePath
     * @return {@code boolean} true if file is deleted, else false
     */
    private static boolean removeFile(final String filePath) {
        Path path = Path.of(filePath);
        try {
            return Files.deleteIfExists(path);
        } catch (IOException ex) {
            ExceptionOperationHandler.registerExceptionOperation(
                    Creator.class, ex, "removeFile(final String filePath)", filePath);
        }
        return false;
    }
    
    /**
     * Lookup {@link Finder} from Spring bean context lookup.
     * 
     * @return {@link Finder} bean representation
     */
    private static Finder getFinder() {
        return SpringContextLookup.getBean(Finder.class);
    }
}
