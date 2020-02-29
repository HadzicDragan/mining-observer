package com.ad.miningobserver.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Application logger
 */
public class ApplicationLogger {
    
    public static final String INFO = "com.ad.miningobserver.InfoLevelLogger";
    
    /**
     * Get the INFO Logger of the application;
     * @return {@link Logger} 
     */
    public static Logger infoLogger() {
        return LogManager.getLogger(INFO);
    }
    
    /**
     * Get the ERROR Logger of the application.
     * @return {@link Logger} 
     */
    public static Logger errorLogger() {
        return LogManager.getLogger(ApplicationLogger.class);
    }

    public static void saveError(final Throwable ex) {
        ApplicationLogger.errorLogger().error(ex.getLocalizedMessage(), ex);
    }
}
