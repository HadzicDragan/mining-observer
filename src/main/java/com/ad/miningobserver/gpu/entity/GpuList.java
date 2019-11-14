package com.ad.miningobserver.gpu.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class that holds the and stores the GPUs into a file
 */
public class GpuList extends GpuCurrentTime {

    private List<GpuCard> gpuCards;

    public GpuList(@JsonProperty("gpuCards") List<GpuCard> gpuCards) {
        super();
        this.gpuCards = gpuCards;
    }

    public List<GpuCard> getGPUCards() {
        return gpuCards;
    }
}
