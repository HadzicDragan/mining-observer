package com.ad.miningobserver.integration.client.monitoring.boundary;

import com.ad.miningobserver.client.monitoring.entity.ActuatorEntity;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@RestClientTest
public class RemoteActuatorClientTest {
    
    @Value("${integration.container.path.endpoint}")
    private String containerEndpoint;
    
    @Autowired
    private RestTemplate restTemplate;
    
    private String actuatorEndpoint;
    
    @Before
    public void setup() {
        StringBuilder builder = new StringBuilder();
        this.actuatorEndpoint = builder.append(this.containerEndpoint)
                .append("/Container")
                .append("/actuator")
                .append("/health")
                .toString();
    }
    
    @Test
    public void isEndpoint_accessable() {
        ResponseEntity<ActuatorEntity> exchange = this.restTemplate.exchange(
                this.actuatorEndpoint, HttpMethod.GET, null, ActuatorEntity.class);
        HttpStatus statusCode = exchange.getStatusCode();
        
        Assertions.assertThat(statusCode).isEqualByComparingTo(HttpStatus.OK);
    }
}
