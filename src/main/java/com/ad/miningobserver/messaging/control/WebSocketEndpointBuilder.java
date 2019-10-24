package com.ad.miningobserver.messaging.control;

import com.ad.miningobserver.PathLocation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * #TODO refactor or remove
 */
@Component
@Scope("prototype")
public class WebSocketEndpointBuilder {
    
    private static final String PROTOCOL = "ws";
    private static final String PORT = "8080"; 
    
    @Value(value = "${container.ip.address}")
    private String containerIPAddress;
    
    public String buildEndpoint() {
        StringBuilder builder = new StringBuilder();
        return builder.append(PROTOCOL)
                .append("://")
                .append(this.containerIPAddress)
                .append(":")
                .append(PORT)
                .append(PathLocation.CONTAINER)
                .append(PathLocation.WEBSOCKET_ENDPOINT)
                .toString();
    }
}
