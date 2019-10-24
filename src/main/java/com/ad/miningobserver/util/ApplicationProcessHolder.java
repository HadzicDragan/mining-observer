package com.ad.miningobserver.util;

import org.springframework.stereotype.Component;

/**
 * Holds the process ID of the running application.
 */
@Component
public class ApplicationProcessHolder {
    
    private final long pid;

    public ApplicationProcessHolder() {
        this.pid = ProcessHandle.current().pid();
    }
    
    /**
     * @return process ID of the application
     */
    public long getProcessId() {
        return this.pid;
    }
}
