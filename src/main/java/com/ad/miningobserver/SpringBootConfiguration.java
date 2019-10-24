package com.ad.miningobserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SpringBootConfiguration extends SpringBootServletInitializer {

    @Value(value = "${remote.client.timeout.limit}")
    private int timeoutLimit;
    
    // deployment needs to be run inside a servlet container
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SpringBootConfiguration.class);
    }

    // -jar execution with this setup you can set the context path
    // if deployed as a servlet instance, this configuration will not work.
    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>
            webServerFactoryCustomizer() {
        return factory -> factory.setContextPath(PathLocation.CONTEXT_PATH);
    }
            
    @Bean
    public RestTemplate configureRestTemplate() {
        final int timeoutLimitConfig = this.timeoutLimit * 1000;
        final SimpleClientHttpRequestFactory clientFactory = new SimpleClientHttpRequestFactory();
        clientFactory.setReadTimeout(timeoutLimitConfig);
        clientFactory.setConnectTimeout(timeoutLimitConfig);
        return new RestTemplate(clientFactory);
    }
}
