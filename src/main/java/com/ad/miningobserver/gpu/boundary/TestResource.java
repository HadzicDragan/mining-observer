package com.ad.miningobserver.gpu.boundary;

import com.ad.miningobserver.client.monitoring.boundary.ActuatorClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
        path = {"/batch"},
        produces = {MediaType.APPLICATION_JSON_VALUE},
        consumes = {MediaType.APPLICATION_JSON_VALUE}
)
public class TestResource {
    
    @Autowired
    private GpuService service;
    
    @RequestMapping(
            method = {RequestMethod.GET}
    )
    public ResponseEntity getGPUList() {
        this.service.batchGpuCardsStatus();
        
        return ResponseEntity.ok().build();
    }
    
    @Autowired
    private ActuatorClient actuatorClient;
    
    @RequestMapping(
            method = {RequestMethod.GET},
            path = {"/actuator"}
    )
    public ResponseEntity getActuator() {
        boolean serverActive = this.actuatorClient.isServerActive();
        if (serverActive) {
            System.out.println("Actuator is working as intended :D");
        } else {
            System.out.println("Actuator broke down.... fix this somehow ;(");
        }
        
        return ResponseEntity.ok().build();
    }
}
