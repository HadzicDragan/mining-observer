package com.ad.miningobserver.operation;

import com.ad.miningobserver.NameReference;
import com.ad.miningobserver.SpringContextLookup;
import com.ad.miningobserver.operation.Operation.OrderCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public final class OperationRegister {

    @Autowired
    private ScheduledOperation scheduledOp;
    
    @Autowired
    @Qualifier(value = NameReference.EXECUTOR_OPERATION_THREAD_POOL)
    private ThreadPoolTaskExecutor executor;

    public OperationRegister addOperation(final Operation operation) {
        if (OrderCode.CRITICAL.equals(operation.getCode())) {
            this.executor.execute(operation);
            return this;
        }
        this.scheduledOp.queueOperation(operation);
        return this;
    }

    public static OperationRegister getOperationRegister() {
        return SpringContextLookup.getBean(OperationRegister.class);
    }
}
