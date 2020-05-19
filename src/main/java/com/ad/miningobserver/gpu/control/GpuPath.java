package com.ad.miningobserver.gpu.control;

import com.ad.miningobserver.client.ClientPath;

public class GpuPath extends ClientPath {
    
    private static final String WORKERS_PATH = "/workers";
    private static final String GPUS_PATH = "/gpus";
    private static final String THERMALS_PATH = "/thermals";
    private static final String GPU_TEMPERATURES = "/temperatures";
    private static final String GPU_CRITICAL = "/critical";
    private static final String GPU_ERRORS = "/errors";
    private static final String BATCH_JOB = "/batch";

    public GpuPath(String hostName) {
        super(hostName);
    }

    public String buildGpuUuidsEndpoint(final String workerName) {
        return super.pathBuilder(
            WORKERS_PATH, 
            ClientPath.PATH_SEPARATOR, 
            workerName, 
            GPUS_PATH
        );
    }

    public String buildGpuThermalsEndpoint(final String workerName) {
        return super.pathBuilder(
            GPUS_PATH, 
            WORKERS_PATH, 
            ClientPath.PATH_SEPARATOR, 
            workerName, 
            THERMALS_PATH
        );
    }

    public String buildGpuErrorEndpoint(final String workerName) {
        return super.pathBuilder(
            WORKERS_PATH, 
            ClientPath.PATH_SEPARATOR,
            workerName, 
            GPU_ERRORS
        );
    }
    
    public String buildGpuErrorBatchEndpoint(final String workerName) {
        return super.pathBuilder(
            WORKERS_PATH, 
            ClientPath.PATH_SEPARATOR,
            workerName,
            GPU_ERRORS,
            BATCH_JOB
        );
    }

    public String buildGpuCriticalThermal() {
        return super.pathBuilder(
            GPUS_PATH, 
            GPU_TEMPERATURES, 
            GPU_CRITICAL
        );
    }

    public String buildGpuThermalsCriticalBatchEndpoint(final String workerName) {
        return super.pathBuilder(
            GPUS_PATH,
            ClientPath.PATH_SEPARATOR,
            workerName,
            GPU_TEMPERATURES, 
            GPU_CRITICAL, 
            BATCH_JOB
        );
    }
}
