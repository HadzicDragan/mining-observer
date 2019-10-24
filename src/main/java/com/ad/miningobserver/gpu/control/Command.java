package com.ad.miningobserver.gpu.control;

import java.util.List;

import com.ad.miningobserver.gpu.InputStreamProcessParser;
import com.ad.miningobserver.gpu.boundary.GpuService;
import com.ad.miningobserver.gpu.entity.GpuCache;
import com.ad.miningobserver.gpu.entity.GpuCard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Command implements InputStreamProcessParser {
    
    private final GpuCache cache;
    private final GpuService service;

    @Autowired
    public Command(GpuCache cache, GpuService service) {
        this.cache = cache;
        this.service = service;
    }
    
    @Override
    public void commandLineOutput(final List<String> list) {
        final List<GpuCard> gpuCards = GpuCardParser.buildGPUs(list);
        this.cache.processGPUs(gpuCards);
        this.checkForCriticalGPUs(this.cache.getGpus());
        this.service.notifyGpuCards(this.cache.getGpus());
    }
    
    private void checkForCriticalGPUs(final List<GpuCard> gpus) {
        gpus.forEach(gpu -> {
            TemperatureController.criticalTemperature(gpu);
        });
    }
}
