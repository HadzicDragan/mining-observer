package com.ad.miningobserver.gpu.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * GpuCardsUuids
 */
public class GpuCardsUuids {

    @JsonProperty(value = "uuids")
    public List<String> uuids;
}