package com.ad.miningobserver.gpu.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class that will hold and store to file GPU errors
 */
public class GpuErrorStream extends GpuCurrentTime {
    
    private final List<String> errors;
    private final List<String> uncommonErrors;

    public GpuErrorStream(
        @JsonProperty("errors") List<String> errors, 
        @JsonProperty("uncommonErrors") List<String> uncommmonErrors) {
        super();
        this.errors = errors;
        this.uncommonErrors = uncommmonErrors;
    }

    public List<String> getErrors() {
        return errors;
    }

    public List<String> getUncommonErrors() {
        return this.uncommonErrors;
    }
}
