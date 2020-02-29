package com.ad.miningobserver.network.entity;

import com.ad.miningobserver.util.CurrentTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class NetworkError {
    
    private final String reason;
    private final LocalDateTime currentDateTime;

    public NetworkError(@JsonProperty("reason") String reason) {
        this.reason = reason;
        this.currentDateTime = CurrentTime.currentTimeUTC();
    }

    public String getReason() {
        return reason;
    }

    @JsonProperty(value = "reportedDate")
    public LocalDateTime getCurrentDateTime() {
        return currentDateTime;
    }
}
