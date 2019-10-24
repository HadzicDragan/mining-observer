package com.ad.miningobserver.gpu;

import com.ad.miningobserver.SpringContextLookup;
import com.ad.miningobserver.gpu.boundary.GpuService;
import com.ad.miningobserver.operation.Operation;

public final class GpuErrorOperation extends Operation {
    
    private final String fileUUID;

    public GpuErrorOperation(OrderCode code, String fileUUID) {
        super(code);
        this.fileUUID = fileUUID;
    }
    
    @Override
    public void run() {
        this.getService().gpuErrorOperationActions(fileUUID);
    }
    
    private GpuService getService() {
        return SpringContextLookup.getBean(GpuService.class);
    }
}
