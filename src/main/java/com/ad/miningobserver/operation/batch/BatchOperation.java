package com.ad.miningobserver.operation.batch;

import java.util.ArrayList;
import java.util.List;

import com.ad.miningobserver.NameReference;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * BatchOperation
 */
@Component
public class BatchOperation {

    private final List<Batch> batchJobs = new ArrayList<>();

    public BatchOperation runJobs() {
        this.batchJobs.forEach(Batch::process);
        return this;
    }

    public BatchOperation registerBatchJob(final Batch batch) {
        this.batchJobs.add(batch);
        return this;
    }

    @Async(value = NameReference.EXECUTOR_ASYNC_TASK_THREAD_POOL)
    public void runAsync() {
        this.runJobs();
    }
}