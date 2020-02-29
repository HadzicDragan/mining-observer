package com.ad.miningobserver.gpu.entity;

import java.time.LocalDateTime;

import com.ad.miningobserver.util.CurrentTime;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * GpuCurrentTime
 */
public abstract class GpuCurrentTime {

    protected final LocalDateTime currentDateTime;

    public GpuCurrentTime() {
        this.currentDateTime = CurrentTime.currentTimeUTC();
    }

    @JsonProperty(value = "reportedDate")
    protected LocalDateTime getCurrentDateTime() {
        return currentDateTime;
    }
}