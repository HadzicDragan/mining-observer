package com.ad.miningobserver.client.monitoring.control;

import com.ad.miningobserver.client.ClientPath;

public class ActuatorPath extends ClientPath {
    
    private static final String PATH = "/actuator";
    private static final String HEALTH_ENDPOINT = "/health";

    public ActuatorPath(String hostName) {
        super(hostName);
    }
    
    public String buildHealthEndpoint() {
        return super.pathBuilder(PATH, HEALTH_ENDPOINT);
    }
}
