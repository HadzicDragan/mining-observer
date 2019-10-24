package com.ad.miningobserver.gpu.entity;

import java.util.List;

/**
 * Class that holds the and stores the GPUs into a file
 */
public class GpuList extends GpuCurrentTime {

    private List<GpuCard> gpuCards;

    public GpuList() {
        super();
    }

    public List<GpuCard> getGPUCards() {
        return gpuCards;
    }

    public void setGPUCards(List<GpuCard> gpuCards) {
        this.gpuCards = gpuCards;
    }
}
