package com.ad.miningobserver.gpu.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class that will execute a query to the underlying graphics card provider and
 * retrieve the statistics information.
 */
@Component
public class GpuQueryTask implements Runnable {

    private final TaskExecutorFakeImpl taskExecutor;
    
    @Autowired
    public GpuQueryTask(TaskExecutorFakeImpl taskExecutor) {
        this.taskExecutor = taskExecutor;
    }
    
    @Override
    public void run() {
        final String[] queryArgs = new SMICommand().queryMendatoryInfoGPUs();
        this.taskExecutor.executeCommand(queryArgs);
    }
}
