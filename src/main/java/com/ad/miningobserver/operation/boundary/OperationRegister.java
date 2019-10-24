package com.ad.miningobserver.operation.boundary;

import com.ad.miningobserver.operation.Operation;
import com.ad.miningobserver.NameReference;
import com.ad.miningobserver.SpringContextLookup;
import com.ad.miningobserver.operation.Operation.OrderCode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public final class OperationRegister {

    private final List<Operation> operations = new ArrayList<>();
    
    @Autowired
    @Qualifier(value = NameReference.EXECUTOR_OPERATION_THREAD_POOL)
    private ThreadPoolTaskExecutor executor;
    private boolean isAsleep;

    public synchronized void addOperation(Operation operation) {
        if (OrderCode.CRITICAL.equals(operation.getCode())) {
            this.submitNow(operation);
            return;
        }
        this.operations.add(operation);
        this.setAwake();
    }
    
    private void submitNow(Operation operation) {
        this.executor.execute(operation);
    }

    /**
     * Maybe can use a scheduled executor to handle requests
     */
    protected synchronized void runOperation() {
        if (this.isAsleep) {
            return;
        }

        // always get the first Operation
        final Operation operation = this.getOperation();

        // when task is submitted
        Future<?> submit = this.executor.submit(operation);
//        submit. 
        // remove it from the list
        this.removeOperation(operation);

        if (this.operations.isEmpty()) {
            this.setSleep();
        }
    }
    
    private void sortOperationByType() {
        
        //  List<String> errorFiles = this.finder.getErrorLocation();
        //  List<String> networkFiles = this.finder.getNetworkLocation();
    }

    private void removeOperation(final Operation operation) {
        this.operations.remove(operation);
    }

    private Operation getOperation() {
        return this.operations.get(0);
    }

    private void setSleep() {
        this.isAsleep = true;
    }

    private void setAwake() {
        this.isAsleep = false;
    }
    
    public static OperationRegister getOperationRegister() {
        return SpringContextLookup.getBean(OperationRegister.class);
    }
}
