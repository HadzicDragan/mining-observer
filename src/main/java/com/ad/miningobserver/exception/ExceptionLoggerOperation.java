package com.ad.miningobserver.exception;

import com.ad.miningobserver.SpringContextLookup;
import com.ad.miningobserver.operation.Operation;
import com.ad.miningobserver.exception.entity.ExceptionError;
import com.ad.miningobserver.exception.boundary.ExceptionService;

public final class ExceptionLoggerOperation extends Operation {
    
    private final ExceptionError exError;

    public ExceptionLoggerOperation(OrderCode code, ExceptionError exError) {
        super(code);
        this.exError = exError;
    }

    @Override
    public void run() {
        this.getExceptionService().publishException(exError);
    }

    private ExceptionService getExceptionService() {
        return SpringContextLookup.getBean(ExceptionService.class);
    }
}
