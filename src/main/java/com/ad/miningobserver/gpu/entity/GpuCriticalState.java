package com.ad.miningobserver.gpu.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GpuCriticalState extends GpuCurrentTime {
    
    private final String gpuUUID;

    public GpuCriticalState(@JsonProperty("gpuUUID") String gpuUUID) {
        super();
        this.gpuUUID = gpuUUID;
    }

    public String getGpuUUID() {
        return gpuUUID;
    }
}
