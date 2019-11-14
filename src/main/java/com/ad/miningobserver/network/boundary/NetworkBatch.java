package com.ad.miningobserver.network.boundary;

import com.ad.miningobserver.operation.batch.Batch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * NetworkBatch
 */
@Component
public class NetworkBatch implements Batch {

    @Autowired
    private NetworkService service;

    @Override
    public void process() {
        service.batchNetworkErrors();
    }
}