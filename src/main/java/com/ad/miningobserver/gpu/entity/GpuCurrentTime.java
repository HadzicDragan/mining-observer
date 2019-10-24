package com.ad.miningobserver.gpu.entity;

import java.time.LocalDateTime;

import com.ad.miningobserver.util.CurrentTime;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * GpuCurrentTime
 */
public abstract class GpuCurrentTime implements CurrentTime {

    protected final LocalDateTime currentDateTime;

    public GpuCurrentTime() {
        this.currentDateTime = this.currentTimeUTC();
    }

    @JsonProperty(value = "dateTime")
    protected LocalDateTime getCurrentDateTime() {
        return currentDateTime;
    }
}