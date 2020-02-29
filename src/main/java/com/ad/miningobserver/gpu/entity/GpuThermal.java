package com.ad.miningobserver.gpu.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * GpuThermals
 */
public class GpuThermal {
    @JsonProperty(value = "index")
    public int index;
    @JsonProperty(value = "uuid")
    public String uuuid;
    @JsonProperty(value = "fanSpeed")
    public int fanSpeed;
    @JsonProperty(value = "temperature")
    public int temperature;
    @JsonProperty(value = "reportedDate")
    public LocalDateTime reportedDate;
}