package com.ad.miningobserver.file;

import com.ad.miningobserver.util.ApplicationLogger;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Writing to files should be done from this class.
 */
public class Writer {
    
    /**
     * Write the process id to "app_root/application_pid". Every new startup
     * overrides the previous pid.
     */
    public static void writePID(final String propertiesFile, final String content)  {
        try (FileWriter writer = new FileWriter(propertiesFile, false)) {
            writer.write(content);
        } catch (IOException ex) {
            ApplicationLogger.errorLogger().error(ex);
            throw new RuntimeException(ex.getCause());
        }
    }
}
