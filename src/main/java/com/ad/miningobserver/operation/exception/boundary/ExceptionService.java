package com.ad.miningobserver.operation.exception.boundary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Service interface for the exception package.
 */
@Component
public class ExceptionService {

    @Autowired
    private ExceptionClient client;

    /**
     * #TODO add documentation
     * @param exceptionVal
     */
    public void publishException(final Object exceptionVal) {
        this.client.postException(exceptionVal);
    }
}