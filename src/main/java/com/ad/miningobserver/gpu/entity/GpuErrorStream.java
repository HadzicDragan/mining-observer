package com.ad.miningobserver.gpu.entity;

import java.util.List;

/**
 * Class that will hold and store to file GPU errors
 */
public class GpuErrorStream extends GpuCurrentTime {
    
    private List<String> errorList;

    public GpuErrorStream() {
        super();
    }

    public List<String> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<String> errorList) {
        this.errorList = errorList;
    }
}
