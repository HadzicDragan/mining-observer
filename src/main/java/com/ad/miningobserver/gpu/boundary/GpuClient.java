package com.ad.miningobserver.gpu.boundary;

import java.util.List;

import com.ad.miningobserver.client.AbstractClient;
import com.ad.miningobserver.gpu.control.GpuPath;
import com.ad.miningobserver.gpu.entity.GpuCardsUuids;
import com.ad.miningobserver.gpu.entity.GpuErrorStream;
import com.ad.miningobserver.gpu.entity.GpuThermal;

import org.springframework.stereotype.Component;

@Component
public class GpuClient extends AbstractClient {

    public boolean postGpuCardsUuids(final GpuCardsUuids gpuUuids) {
        final String endpoint = new GpuPath(super.containerPath)
            .buildGpuUuidsEndpoint(super.hostName);
        return super.postToEndpoint(endpoint, gpuUuids);
    }

    public boolean postGpuThermals(final List<GpuThermal> thermals) {
        final String endpoint = new GpuPath(super.containerPath)
            .buildGpuThermalsEndpoint(super.hostName);
        return super.postToEndpoint(endpoint, thermals);
    }

    public boolean postGpuCriticalThermal(final GpuThermal gpuThermal) {
        final String endpoint = new GpuPath(super.containerPath)
            .buildGpuCriticalThermal();
        return super.postToEndpoint(endpoint, gpuThermal);
    }

    public boolean batchCriticalThermals(final List<GpuThermal> gpuThermals) {
        final String endpoint = new GpuPath(super.containerPath)
            .buildGpuThermalsCriticalBatchEndpoint(super.hostName);
        return super.postToEndpoint(endpoint, gpuThermals);
    }

    public boolean postGpuError(final GpuErrorStream errorStream) {
        final String endpoint = new GpuPath(super.containerPath)
            .buildGpuErrorEndpoint(super.hostName);
        return super.postToEndpoint(endpoint, errorStream);
    }

    public boolean batchErrorStream(final List<GpuErrorStream> errorStreamList) {
        final String endpoint = new GpuPath(super.containerPath)
            .buildGpuErrorBatchEndpoint(super.hostName);
        return super.postToEndpoint(endpoint, errorStreamList);
    }
}
