package com.ad.miningobserver.operation.exception.boundary;

import com.ad.miningobserver.client.AbstractClient;
import com.ad.miningobserver.operation.exception.control.ExceptionPath;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Client interface for the exception package.
 */
@Component
public class ExceptionClient extends AbstractClient {

    /**
     * POST exception errors to the remote server.
     * 
     * @param exceptionVal
     */
    public void postException(final Object exceptionVal) {
        if (super.notAvailableServer()) {
            return;
        }
        if (!this.isRestEnabled) {
            return;
        }
        final String endpoint = new ExceptionPath(super.containerPath).buildExceptionPath();

        final HttpEntity entity = new HttpEntity<>(exceptionVal);
        final ResponseEntity<Object> response = super.postObjectToUrl(endpoint, entity);

        // #TODO - remove if not used => maybe return boolean, I do not do anything with it
        super.isOkResponseStatus(response);
    }
}