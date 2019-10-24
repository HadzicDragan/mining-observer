package com.ad.miningobserver.gpu.boundary;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.ad.miningobserver.client.AbstractClient;
import com.ad.miningobserver.gpu.control.GpuPath;
import com.ad.miningobserver.gpu.entity.GpuCriticalState;
import com.ad.miningobserver.gpu.entity.GpuErrorStream;
import com.ad.miningobserver.gpu.entity.GpuList;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class GpuClient extends AbstractClient {
    
    private static final AtomicInteger simpleCounter = new AtomicInteger(0);
    
    // #TODO Add the implementation details
    // single POST
    public boolean postErrorStream(GpuErrorStream errorStream) {
        if (super.notAvailableServer()) {
            return false;
        }
        final String endpoint = new GpuPath(super.containerPath).buildEndpoint();
        
        if (simpleCounter.get() == 3) {
            simpleCounter.set(0);
            return false;
        }
        
        this.simpleCounter.incrementAndGet();
        return true;
    }
    
    public boolean postGPUTemperatures() {
        if (super.notAvailableServer()) {
            return false;
        }
        final String endpoint = new GpuPath(super.containerPath).buildGPUTemperaturesEndpoint();
        if (!isRestEnabled) {
            return false;
//            throw new UnsupportedOperationException("Not supported yet.");
        }
        
        Object changeObject = new Object();
        final ResponseEntity<Object> response = super.postObjectToUrl(
                endpoint, new HttpEntity(changeObject));
        return super.isOkResponseStatus(response);
    }

    public boolean postGPUCards(GpuList gpuList) {
        if (super.notAvailableServer()) {
            return false;
        }
        // change endpoint path
        final String endpoint = new GpuPath(super.containerPath).buildGPUTemperaturesEndpoint();
        if (!isRestEnabled) {
            return false;
//            throw new UnsupportedOperationException("Not supported yet.");
        }
        
        final ResponseEntity<Object> response = super.postObjectToUrl(
                endpoint, new HttpEntity(gpuList));
        return super.isOkResponseStatus(response);
    }

    boolean postCriticalTemperatureByUUID(GpuCriticalState gpuState) {
        if (super.notAvailableServer()) {
            return false;
        }
        // change endpoint path
        final String endpoint = new GpuPath(super.containerPath).buildGPUTemperaturesEndpoint();
        if (!isRestEnabled) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
        final ResponseEntity<Object> response = super.postObjectToUrl(
                endpoint, new HttpEntity(gpuState));
        return super.isOkResponseStatus(response);
    }

    boolean batchGpuCards(List<GpuList> gpuList) {
        if (super.notAvailableServer()) {
            return false;
        }
        // change endpoint path
        final String endpoint = new GpuPath(super.containerPath).buildGPUTemperaturesEndpoint();
        if (!isRestEnabled) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
        final ResponseEntity<Object> response = super.postObjectToUrl(
                endpoint, new HttpEntity(gpuList));
        return super.isOkResponseStatus(response);
    }
    
    public boolean batchErrorStream() {
        if (super.notAvailableServer()) {
            return false;
        }
        final String endpoint = new GpuPath(super.containerPath).buildBatchEndpoint();
        
        if (!isRestEnabled) {
            return false;
        }
        
        Object changeObject = new Object();
        final ResponseEntity<Object> response = super.postObjectToUrl(
                endpoint, new HttpEntity(changeObject));
        return super.isOkResponseStatus(response);
    }

    boolean batchGpuTemperatures(List<GpuCriticalState> gpuCriticalList) {
        if (super.notAvailableServer()) {
            return false;
        }
        // change endpoint path
        final String endpoint = new GpuPath(super.containerPath).buildGPUTemperaturesEndpoint();
        if (!isRestEnabled) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
        final ResponseEntity<Object> response = super.postObjectToUrl(
                endpoint, new HttpEntity(gpuCriticalList));
        return super.isOkResponseStatus(response);
    }
}
