package com.ad.miningobserver.gpu;

import com.ad.miningobserver.SpringContextLookup;
import com.ad.miningobserver.gpu.boundary.GpuService;
import com.ad.miningobserver.gpu.control.TemperatureController.FanOptions;
import com.ad.miningobserver.operation.Operation;

public class FanSpeedOperation extends Operation {
    
    private final FanOptions fanOptions;
    private final String gpuUUID;
    
    public FanSpeedOperation(OrderCode code, final FanOptions options, final String gpuUUID) {
        super(code);
        this.fanOptions = options;
        this.gpuUUID = gpuUUID;
    }
    
    @Override
    public void run() {
        if (this.isFanSpeedCritical()) {
            this.getGpuService().notifyCriticalTemperature(this.gpuUUID);
        }
    }
    
    private boolean isFanSpeedCritical() {
        return this.fanOptions.equals(FanOptions.CRITICAL);
    }
    
    private GpuService getGpuService() {
        return SpringContextLookup.getBean(GpuService.class);
    }
}
