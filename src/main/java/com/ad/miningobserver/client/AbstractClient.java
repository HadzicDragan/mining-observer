package com.ad.miningobserver.client;

import com.ad.miningobserver.network.RemoteServerStatus;
import com.ad.miningobserver.operation.ExceptionOperationHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Autowires {@code RestTemplate} and other application.properties values.
 * Additionally this class has helper methods to handle GET and POST client requests.
 */
public abstract class AbstractClient<T> {
    
    /**
     * Remote server URL and PORT number, value from application.properties 
     */
    protected String containerPath;
    /**
     * Is REST endpoint enabled for the application, value from application.properties 
     */
    protected boolean isRestEnabled;
    protected RestTemplate restTemplate;

    /**
     * Reference to {@link RemoteServerStatus} which tell the clients if the remote server is
     * available or not.
     */
    protected RemoteServerStatus serverStatus;

    /**
     * Remote server is not available.
     * 
     * @return {@code boolean} false if the Remote server is down
     */
    protected boolean notAvailableServer() {
        return !this.serverStatus.isServerAvailable();
    }
    
    /**
     * Submit a POST request to the endpoint parameter.
     * 
     * @param endpoint to which the request will be made
     * @param entity {@code HttpEntity} which will be served as payload for the request
     * @return {@link ResponseEntity} or null if an exception is thrown
     * @throws RestClientException if the request could not be made for some reason
     */
    protected ResponseEntity<Object> postObjectToUrl(final String endpoint, final HttpEntity entity) 
            throws RestClientException {
        try {
            return this.restTemplate.exchange(endpoint, HttpMethod.POST, entity, Object.class);
        } catch (RestClientException ex) {
            ExceptionOperationHandler.registerExceptionOperation(
                    this.getClass(), ex, "postObjectToUrl()", endpoint);
            return null;
        }
    }
    
    /**
     * Submit a GET request to the endpoint parameter.
     * 
     * @param endpoint to which the request will be made
     * @param clazz what type of response will be returned in the {@link ResponseEntity}
     * @return {@link ResponseEntity} or null if an exception is thrown
     * @throws RestClientException if the request could not be made for some reason
     */
    protected ResponseEntity<?> getObjectFromUrl(final String endpoint, Class<?> clazz) 
            throws RestClientException {
        try {
            return this.restTemplate.exchange(endpoint, HttpMethod.GET, null, clazz);
        } catch (RestClientException ex) {
            ExceptionOperationHandler.registerExceptionOperation(
                    this.getClass(), ex, "getObjectFromUrl()", endpoint);
            return null;
        }
    }
    
    /**
     * Check if {@link ResponseEntity} status code is OK.
     * 
     * @param response {@code ResponseEntity} which will be tested
     * @return {@code boolean} true if the {@code ResponseEntity} is not null
     * or {@link HttpStatus} is OK, else return false
     */
    protected boolean isOkResponseStatus(final ResponseEntity response) {
        return (response == null) ? false : response.getStatusCode().equals(HttpStatus.OK);
    }
    
    @Autowired
    protected final void setIsRestEnabled(@Value(value = "${rest.connector.enabled}") 
            boolean isRestEnabled) {
        this.isRestEnabled = isRestEnabled;
    }

    @Autowired
    protected final void setContainerPath(@Value("${container.path.endpoint}") 
            String containerPath) {
        this.containerPath = containerPath;
    }

    @Autowired
    protected final void setServerStatus(RemoteServerStatus serverStatus) {
        this.serverStatus = serverStatus;
    }

    @Autowired
    protected final void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

}
