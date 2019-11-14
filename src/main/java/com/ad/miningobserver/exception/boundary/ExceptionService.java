package com.ad.miningobserver.exception.boundary;

import com.ad.miningobserver.exception.entity.ExceptionError;

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
     * 
     * 
     * @param exceptionError
     */
    public void publishException(final ExceptionError exceptionError) {
        this.client.postException(exceptionError);
    }
}