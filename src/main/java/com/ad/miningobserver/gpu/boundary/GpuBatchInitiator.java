package com.ad.miningobserver.gpu.boundary;

import com.ad.miningobserver.operation.batch.BatchOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * GpuBatchInitiator
 */
@Component
@DependsOn(value = {"gpuBatch"})
public class GpuBatchInitiator {

    @Autowired
    private BatchOperation batchOperation;

    @Autowired
    private GpuBatch gpuBatch;

    public void initBatchProcessing() {
        this.batchOperation.registerBatchJob(gpuBatch);
    }
}