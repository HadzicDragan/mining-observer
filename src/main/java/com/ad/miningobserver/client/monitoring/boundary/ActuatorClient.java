package com.ad.miningobserver.client.monitoring.boundary;

import com.ad.miningobserver.client.AbstractClient;
import com.ad.miningobserver.client.monitoring.control.ActuatorPath;
import com.ad.miningobserver.client.monitoring.entity.ActuatorEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ActuatorClient extends AbstractClient {
    
    public boolean isServerActive() {
        final String healthEndpoint = new ActuatorPath(this.containerPath)
                .buildHealthEndpoint();
        final ResponseEntity<ActuatorEntity> response = super.getObjectFromUrl(
                healthEndpoint, ActuatorEntity.class);
        if (response == null || (response.getStatusCode() != HttpStatus.OK)) {
            return false;
        }
        
        final ActuatorEntity actuatorEntity = response.getBody();
        if (actuatorEntity == null) {
            return false;
        }
        return (actuatorEntity.getStatus().equals(ActuatorEntity.STATUS_UP));
    }
}
