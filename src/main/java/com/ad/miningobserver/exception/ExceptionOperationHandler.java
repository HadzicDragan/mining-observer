package com.ad.miningobserver.exception;

import com.ad.miningobserver.operation.Operation.OrderCode;
import com.ad.miningobserver.operation.OperationRegister;
import com.ad.miningobserver.exception.entity.ExceptionError;
import com.ad.miningobserver.util.ApplicationLogger;

/**
 * Utility class to register {@code Throwable} Operations.
 */
public class ExceptionOperationHandler {
    
    /**
     * Register a {@code Throwable} Operation that will be ran at some point. Additionally the 
     * {@code Throwable} is saved to the log file.
     * 
     * @param clazz where the {@code Throwable} was thrown
     * @param ex {@code Throwable} that was thrown in the calling method
     * @param methodName name of the method from which the {@code Throwable} was thrown
     * @param value any additional information that needs to be passed
     */
    public static void registerExceptionOperation(
            final Class clazz, 
            final Throwable ex, 
            final String methodName, 
            final Object value) {
        final ExceptionError exceptionError = new ExceptionError.Builder()
                .className(clazz.getSimpleName())
                .methodName(methodName)
                .errorMessage(ex.getMessage())
                .objValue(value)
                .build();
        final ExceptionLoggerOperation exceptionOperation = 
                new ExceptionLoggerOperation(OrderCode.LIGHT, exceptionError);
        OperationRegister.getOperationRegister()
                .addOperation(exceptionOperation);
        ApplicationLogger.errorLogger().error(ex.getLocalizedMessage(), ex);
    }

    /**
     * Register a Operation that will be ran at some point. This operation is not saved to the log
     * files.
     * 
     * @param clazz where {@code ExceptionOperationHandler} was called
     * @param methodName name of the method where the {@code ExceptionOperationHandler} was called
     * @param value any additional information that needs to be passed
     */
    public static void registerUncommonOperation(
            final Class clazz,
            final String methodName, 
            final Object value) {
        final ExceptionError exceptionError = new ExceptionError.Builder()
                .className(clazz.getSimpleName())
                .methodName(methodName)
                .objValue(value)
                .build();
        final ExceptionLoggerOperation exceptionOperation = 
                new ExceptionLoggerOperation(OrderCode.LIGHT, exceptionError);
        OperationRegister.getOperationRegister()
                .addOperation(exceptionOperation);
    }
}
