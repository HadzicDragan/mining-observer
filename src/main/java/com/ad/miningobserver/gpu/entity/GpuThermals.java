package com.ad.miningobserver.gpu.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * GpuThermals
 */
public class GpuThermals {

    private final List<GpuThermal> gpuThermals;

    public GpuThermals(@JsonProperty(value = "gpuThermals") List<GpuThermal> gpuThermals) {
        this.gpuThermals = gpuThermals;
    }

    public List<GpuThermal> getGpuThermals() {
        return this.gpuThermals;
    }
}