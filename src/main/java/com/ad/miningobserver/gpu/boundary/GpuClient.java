package com.ad.miningobserver.gpu.boundary;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.ad.miningobserver.client.AbstractClient;
import com.ad.miningobserver.gpu.control.GpuPath;
import com.ad.miningobserver.gpu.entity.GpuCard;
import com.ad.miningobserver.gpu.entity.GpuCriticalState;
import com.ad.miningobserver.gpu.entity.GpuErrorStream;
import com.ad.miningobserver.gpu.entity.GpuList;

import org.springframework.stereotype.Component;

@Component
public class GpuClient extends AbstractClient {
    
    private static final AtomicInteger simpleCounter = new AtomicInteger(0);
    
    // #TODO Add the implementation details
    // single POST
    public boolean postErrorStream(GpuErrorStream errorStream) {
        final String endpoint = new GpuPath(super.containerPath).buildEndpoint();
        
        if (simpleCounter.get() == 3) {
            simpleCounter.set(0);
            return false;
        }
        
        this.simpleCounter.incrementAndGet();
        return true;
    }

    public boolean postGpuCards(final GpuList gpuList) {
        // #TODO change endpoint path
        final String endpoint = new GpuPath(super.containerPath).buildGPUTemperaturesEndpoint();
        return super.postToEndpoint(endpoint, gpuList);
    }

    public boolean postCriticalTemperatureByUUID(final GpuCriticalState gpuState) {
        // #TODO change endpoint path
        final String endpoint = new GpuPath(super.containerPath).buildGPUTemperaturesEndpoint();
        return super.postToEndpoint(endpoint, gpuState);
    }

    public boolean batchGpuCards(final List<GpuList> gpuList) {
        // #TODO change endpoint path
        final String endpoint = new GpuPath(super.containerPath).buildGPUTemperaturesEndpoint();
        return super.postToEndpoint(endpoint, gpuList);
    }
    
    public boolean batchErrorStream() {
        final String endpoint = new GpuPath(super.containerPath).buildBatchEndpoint();
        return super.postToEndpoint(endpoint, null);
    }

    public boolean batchGpuTemperatures(final List<GpuCriticalState> gpuCriticalList) {
        // #TODO change endpoint path
        final String endpoint = new GpuPath(super.containerPath).buildGPUTemperaturesEndpoint();
        return super.postToEndpoint(endpoint, gpuCriticalList);
    }
}
