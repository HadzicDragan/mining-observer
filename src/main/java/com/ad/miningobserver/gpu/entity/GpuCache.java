package com.ad.miningobserver.gpu.entity;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;

/**
 * Graphic cards cache object
 */
@Component
public class GpuCache {

    private final List<GpuCard> gpus = new CopyOnWriteArrayList<>();

    /**
     * Retrieve all the GPUs.
     * @return {@link List} of {@link GpuCard} synchronized objects
     */
    public List<GpuCard> getGpus() {
        return this.gpus;
    }

    /**
     * Add a GPU to the synchronized list.
     * @param gpuCard {@link GpuCard}
     * @return true if the GPU is added to the list, else false
     */
    public boolean addGPU(GpuCard gpuCard) {
        return this.gpus.add(gpuCard);
    }
    
    /**
     * Parse every GPU from the list and add it the application GPU cache.
     * @param gpus
     */
    public void processGPUs(List<GpuCard> gpus) {
        if (this.gpus.isEmpty()) {
            this.gpus.addAll(gpus);
            return;
        }
        
        gpus.forEach(gpu -> {
            this.updateGPU(gpu);
        });
    }
    
    /**
     * Update the fan speed and temperature of GPU. Checks if the list contains
     * the passed {@link GpuCard} parameter and updates, else it just adds to
     * the list.
     * @param gpuCard {@link GpuCard}
     */
    public void updateGPU(GpuCard gpuCard) {
        if (gpus.contains(gpuCard)) {
            gpus.forEach(gpu -> {
                if (gpu.equals(gpuCard)) {
                    gpu.setFanSpeed(gpuCard.getFanSpeed());
                    gpu.setTemperature(gpuCard.getTemperature());
                }
            });
        } else {
            gpus.add(gpuCard);
        }
    }
}
