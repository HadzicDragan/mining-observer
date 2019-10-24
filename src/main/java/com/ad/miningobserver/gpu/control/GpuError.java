package com.ad.miningobserver.gpu.control;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles errors when error stream is not empty.
 * @see <a href="https://docs.nvidia.com/deploy/xid-errors/index.html">XID Errors</a>
 */
public class GpuError {
    
    /**
     * XID: 8
     * Failure: "GPU stopped processing"
     * ErrorType: Thermal
     */
    private static final String GPU_STOP_PROCESSING = "GPU stopped processing";
    
    /**
     * XID: 13
     * Failure: "Graphics Engine Exception"
     * ErrorType: Thermal
     */
    private static final String DRIVER_ERROR_GPU_EXCEPTION = "Graphics Engine Exception";
    
    /**
     * XID: 24
     * Failure: "GPU semaphore timeout"
     * ErrorType: Thermal 
     */
    private static final String SEMAPHOR_TIMEOUT = "GPU semaphore timeout";
    
    /**
     * XID: 32
     * Failure: "Invalid or corrupted push buffer stream"
     * ErrorType: Thermal
     */
    private static final String INVALID_PUSH_BUFFER = "Invalid or corrupted push buffer stream";
    
    /**
     * XID: 62
     * Failure: "Internal micro-controller halt"
     * ErrorType: Thermal
     */
    private static final String MICRO_CONTROLER_HALT = "Internal micro-controller halt";
    
    /**
     * XID: 79
     * Failure: "GPU has fallen off the bus"
     * ErrorType: Thermal
     */
    private static final String GPU_FALLEN_OFF_BUS = "GPU has fallen off the bus";
    
    private final static List<String> ERROR_LIST = new ArrayList<>();
    
    static {
        ERROR_LIST.add(SEMAPHOR_TIMEOUT);
        ERROR_LIST.add(INVALID_PUSH_BUFFER);
        ERROR_LIST.add(GPU_STOP_PROCESSING);
        ERROR_LIST.add(DRIVER_ERROR_GPU_EXCEPTION);
        ERROR_LIST.add(MICRO_CONTROLER_HALT);
        ERROR_LIST.add(GPU_FALLEN_OFF_BUS);
    }
    
    public static List<String> getErrorList() {
        return ERROR_LIST;
    }
}
