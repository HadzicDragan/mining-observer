package com.ad.miningobserver.messaging.control;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WSEndpointBuilderTest {
    
    @Autowired
    private WebSocketEndpointBuilder endpointBuilder;
    
    @Test
    public void validEndpointTest() {
        final String endpointURI = "ws://127.0.0.1:8080/Container/socket";
        final String builtEndpoint = this.endpointBuilder.buildEndpoint();
        Assertions.assertThat(builtEndpoint)
                .isEqualTo(endpointURI);
    }
}
