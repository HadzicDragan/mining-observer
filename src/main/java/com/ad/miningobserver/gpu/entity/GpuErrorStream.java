package com.ad.miningobserver.gpu.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class that will hold and store to file GPU errors
 */
public class GpuErrorStream extends GpuCurrentTime {
    
    private final List<String> errorList;
    private final List<String> uncommonErrors;

    public GpuErrorStream(
        @JsonProperty("errors") List<String> errorList, 
        @JsonProperty("uncommonErrors") List<String> uncommmonErrors) {
        super();
        this.errorList = errorList;
        this.uncommonErrors = uncommmonErrors;
    }

    public List<String> getErrorList() {
        return errorList;
    }

    public List<String> getUncommonErrors() {
        return this.uncommonErrors;
    }
}
