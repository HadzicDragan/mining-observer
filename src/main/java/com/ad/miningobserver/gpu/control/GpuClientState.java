package com.ad.miningobserver.gpu.control;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Component;

/**
 * GpuClientState
 */
@Component
public class GpuClientState {

    private AtomicBoolean gpuUuidsPersisted = new AtomicBoolean(false);

    public boolean areGpuUuidsPersisted() {
        return this.gpuUuidsPersisted.get();
    }

    public void gpuUuidsPersisted() {
        this.gpuUuidsPersisted.set(true);
    }
}