package com.ad.miningobserver.gpu;

import com.ad.miningobserver.SpringContextLookup;
import com.ad.miningobserver.gpu.boundary.GpuService;
import com.ad.miningobserver.operation.Operation;

public final class FanSpeedOperation extends Operation {
    
    private final String fileUUID;
    
    public FanSpeedOperation(OrderCode code, final String fileUUID) {
        super(code);
        this.fileUUID = fileUUID;
    }
    
    @Override
    public void run() {
        this.getGpuService().publishCriticalTemperature(this.fileUUID);
    }
    
    private GpuService getGpuService() {
        return SpringContextLookup.getBean(GpuService.class);
    }
}
