package com.ad.miningobserver.gpu.control;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.ad.miningobserver.gpu.InputStreamProcessParser;
import com.ad.miningobserver.gpu.boundary.GpuService;
import com.ad.miningobserver.gpu.entity.GpuCache;
import com.ad.miningobserver.gpu.entity.GpuCard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FakeCommand implements InputStreamProcessParser {
    
    private final GpuCache cache;
    
    @Autowired
    public FakeCommand(GpuCache cache) {
        this.cache = cache;
    }
    
    @Autowired
    private GpuService service;
    
    private final AtomicInteger internalCounter = new AtomicInteger(0);

    @Override
    public void commandLineOutput(List<String> list) {
        final List<GpuCard> gpuCards = GpuCardParser.buildGPUs(list);
        this.cache.processGPUs(gpuCards);
        this.logGPUStatus();
        
        if(this.internalCounter.get() == 2) {
            
            this.service.notifyGpuCards(gpuCards);
            this.internalCounter.set(0);
        }
        System.out.println(this.cache.getGpus());
        this.internalCounter.getAndIncrement();
    }
    
    public void logGPUStatus() {
        List<GpuCard> gpus = this.cache.getGpus();
        gpus.forEach(gpu -> {

            // check if fanspeed needs to be updated.
            // #TODO maybe make a script that will update all the
            // cards at once, this will cut the process calls alot
            TemperatureController.criticalTemperature(gpu);
        });
    }
}
