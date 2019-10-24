package com.ad.miningobserver.operation;

import com.ad.miningobserver.SpringContextLookup;
import com.ad.miningobserver.operation.entity.ExceptionError;
import com.ad.miningobserver.operation.exception.boundary.ExceptionService;

public class ExceptionLoggerOperation extends Operation {
    
    private final ExceptionError exError;

    public ExceptionLoggerOperation(OrderCode code, ExceptionError exError) {
        super(code);
        this.exError = exError;
    }

    @Override
    public void run() {
        
        final ExceptionService service = this.getExceptionService();
        service.publishException(exError);
        // System.out.println("Running...");
        // System.out.println(this.getClass().getSimpleName() + ".run()...");
        // System.out.println("Time that exception happend: " + this.exError.getCurrentDateTime();
        // System.out.println("Exception on class:" + this.exError.getClassName());
        // System.out.println("Method:" + this.exError.getMethodName());
        // System.out.println("Error message:" + this.exError.getErrorMessage());
        // System.out.println(this.getClass().getSimpleName() + " finis        - EmailServicehing task...");
    }

    private ExceptionService getExceptionService() {
        return SpringContextLookup.getBean(ExceptionService.class);
    }
}
