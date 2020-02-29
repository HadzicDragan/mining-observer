package com.ad.miningobserver.exception.control;

import com.ad.miningobserver.client.ClientPath;

/**
 * Client endpoints that are specific to the exception package.
 */
public class ExceptionPath extends ClientPath {

    private static final String EXCEPTION_ROOT_PATH = "/workers";
    private static final String EXCEPTION_PATH = "/exceptions";

    public ExceptionPath(String hostName) {
        super(hostName);
    }

    /**
     * Build the URL endpoint for exception.
     * 
     * @return constructed URL endpoint
     */
    public String buildExceptionPath(String workerName) {
        return super.pathBuilder(
            EXCEPTION_ROOT_PATH, 
            ClientPath.PATH_SEPARATOR,
            workerName, 
            EXCEPTION_PATH);
    }
}