package com.ad.miningobserver.client;

import com.ad.miningobserver.PathLocation;

public abstract class ClientPath {
    
    protected final String hostName;

    public ClientPath(String hostName) {
        this.hostName = hostName;
    }
    
    protected String pathBuilder(String... path) {
        final StringBuilder builder = this.hostPrefix();
        for (String pathValue : path) {
            builder.append(pathValue);
        }
        return builder.toString();
    }

    private StringBuilder hostPrefix() {
        final StringBuilder builder = new StringBuilder();
        return builder.append(this.hostName)
                .append(PathLocation.CONTAINER);
    }
}
