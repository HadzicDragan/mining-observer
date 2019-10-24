package com.ad.miningobserver.operation.exception.control;

import com.ad.miningobserver.client.ClientPath;

/**
 * Client endpoints that are specific to the exception package.
 */
public class ExceptionPath extends ClientPath {

    private static final String EXCEPTION_PATH = "/exceptions";

    public ExceptionPath(String hostName) {
        super(hostName);
    }

    /**
     * Build the URL endpoint for exception.
     * 
     * @return constructed URL endpoint
     */
    public String buildExceptionPath() {
        return super.pathBuilder(EXCEPTION_PATH);
    }
}