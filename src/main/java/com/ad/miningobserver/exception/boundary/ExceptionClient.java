package com.ad.miningobserver.exception.boundary;

import com.ad.miningobserver.client.AbstractClient;
import com.ad.miningobserver.exception.control.ExceptionPath;
import com.ad.miningobserver.exception.entity.ExceptionError;

import org.springframework.stereotype.Component;

/**
 * Client interface for the exception package.
 */
@Component
public class ExceptionClient extends AbstractClient {

    /**
     * POST exception errors to the remote server.
     * 
     * @param exceptionError
     */
    public boolean postException(final ExceptionError exceptionError) {
        final String endpoint = new ExceptionPath(super.containerPath)
            .buildExceptionPath(super.hostName);
        return super.postToEndpoint(endpoint, exceptionError);
    }
}