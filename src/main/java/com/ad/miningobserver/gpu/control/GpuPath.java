package com.ad.miningobserver.gpu.control;

import com.ad.miningobserver.client.ClientPath;

public class GpuPath extends ClientPath {
    
    private static final String FIRST_ENDPOINT_RENAME = "/endpoint";
    private static final String BATCH_ENDPOINT_RENAME = "/batch";
    private static final String GPU_TEMPERATURES = "/temperatures";

    public GpuPath(String hostName) {
        super(hostName);
    }
    
    public String buildEndpoint() {
        return super.pathBuilder(FIRST_ENDPOINT_RENAME);
    }

    public String buildBatchEndpoint() {
        return super.pathBuilder(BATCH_ENDPOINT_RENAME);
    }
    
    public String buildGPUTemperaturesEndpoint() {
        return super.pathBuilder(GPU_TEMPERATURES);
    }
}
