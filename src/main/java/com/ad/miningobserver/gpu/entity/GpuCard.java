package com.ad.miningobserver.gpu.entity;

import java.util.Objects;

/**
 * 
 */
public class GpuCard {
    
    private static final String SEPARATOR = ", ";
    private static final String PERCENT_SIGN = "%";
    
    private int index;
    private String uuid;
    private String biosVersion;
    private int fanSpeed;
    private int temperature;

    public GpuCard() {
    }

    public GpuCard(int index, String uuid, String biosVersion) {
        this.index = index;
        this.uuid = uuid;
        this.biosVersion = biosVersion;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + Objects.hashCode(this.uuid);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final GpuCard other = (GpuCard) obj;
        return Objects.equals(this.uuid, other.uuid);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getUUID() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getBiosVersion() {
        return biosVersion;
    }

    public void setBiosVersion(String biosVersion) {
        this.biosVersion = biosVersion;
    }

    public int getFanSpeed() {
        return fanSpeed;
    }

    public void setFanSpeed(int fanSpeed) {
        this.fanSpeed = fanSpeed;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GPU ")
                .append(this.index)
                .append(": uuid=")
                .append(this.uuid)
                .append(SEPARATOR)
                .append("fan=")
                .append(this.fanSpeed)
                .append(PERCENT_SIGN)
                .append(SEPARATOR)
                .append("temp=")
                .append(this.temperature)
                .append(PERCENT_SIGN);
        
        return builder.toString();
    }
    
    /**
     * 
     */
    public static class GPUCardBuilder {
        
        private int index;
        private String uuid;
        private String biosVersion;
        private int fanSpeed;
        private int temperature;
        
        public GPUCardBuilder index(int index) {
            this.index = index;
            return this;
        }
        
        public GPUCardBuilder uuid(String uuid) {
            this.uuid = uuid;
            return this;
        }
        
        public GPUCardBuilder biosVersion(String biosVersion) {
            this.biosVersion = biosVersion;
            return this;
        }
        
        public GPUCardBuilder fanSpeed(int fanSpeed) {
            this.fanSpeed = fanSpeed;
            return this;
        }
        
        public GPUCardBuilder temperature(int temperature) {
            this.temperature = temperature;
            return this;
        }
        
        public GpuCard build() {
            
            GpuCard gpuCard = new GpuCard(this.index, this.uuid, this.biosVersion);
            gpuCard.setFanSpeed(this.fanSpeed);
            gpuCard.setTemperature(this.temperature);
            return gpuCard;
        }
    }
}
