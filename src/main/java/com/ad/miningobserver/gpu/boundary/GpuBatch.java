package com.ad.miningobserver.gpu.boundary;

import com.ad.miningobserver.operation.batch.Batch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * GpuBatch
 */
@Component
public class GpuBatch implements Batch {

    @Autowired
    private GpuService service;

    @Override
    public void process() {
        this.service.batchGpuThermals();
        this.service.batchGpuTemperatureStatus();
        this.service.batchGpuErrors();
    }
}

