package com.ad.miningobserver.network.boundary;

import com.ad.miningobserver.operation.batch.BatchOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * NetworkBatchInitiator
 */
@Component
@DependsOn(value = {"networkBatch"})
public class NetworkBatchInitiator {
    
    @Autowired
    private BatchOperation batchOperation;

    @Autowired
    private NetworkBatch networkBatch;

    public void initBatchProcessing() {
        this.batchOperation.registerBatchJob(networkBatch);
    }
}