package com.ad.miningobserver;

import com.ad.miningobserver.file.Creator;
import com.ad.miningobserver.file.Finder;
import com.ad.miningobserver.file.Finder.ApplicationFile;
import com.ad.miningobserver.file.Writer;
import com.ad.miningobserver.util.ApplicationProcessHolder;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * Boot up Application settings
 */
@Component
@DependsOn(value = {
    NameReference.PROCESS_HANDLER_BEAN
})
public class BootInitiator {
    
    private final Finder fileFinder;
    private final ApplicationProcessHolder processManager;

    @Autowired
    public BootInitiator(
            Finder fileFinder, 
            ApplicationProcessHolder processManager) {
        this.fileFinder = fileFinder;
        this.processManager = processManager;
    }
    
    /**
     * Write the application process id to file.
     * File should be located inside the root directory of the application with
     * a file named "application_pid".
     */
    @PostConstruct
    private void initiate() throws IOException {
        final String systemAppDirectory = this.fileFinder.getApplicationDirectory();
        this.createdIfMissingDirectories(systemAppDirectory);
        String applicatioPIDFile = this.fileFinder
                .getApplicationFileLocation(ApplicationFile.PID_FILE);
        Writer.writePID(applicatioPIDFile, String.valueOf(this.processManager.getProcessId()));
        
        Creator.createFile(applicatioPIDFile, applicatioPIDFile);
        
        this.fileFinder.canReadMandatoryFiles();
    }
    
    private void createdIfMissingDirectories(final String startPath) throws IOException {
        final List<String> directories = this.fileFinder.directories();
        for (String directory : directories) {
            final StringBuilder builder = new StringBuilder();
            final String folder = builder
                    .append(startPath)
                    .append(File.separator)
                    .append(directory)
                    .toString();
            Creator.createApplicationDiretory(startPath, folder);
        }
    }
}
