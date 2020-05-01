package com.ad.miningobserver.client;

import com.ad.miningobserver.EventDesignator;
import com.ad.miningobserver.network.NetworkConnection;
import com.ad.miningobserver.network.RemoteServerStatus;
import com.ad.miningobserver.network.control.LocalNetwork;

import javax.annotation.PostConstruct;

import com.ad.miningobserver.exception.ExceptionOperationHandler;

import com.ad.miningobserver.state.boundary.StateManager.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Autowires {@code RestTemplate} and other application.properties values.
 * Additionally this class has helper methods to handle GET and POST client requests.
 */
public abstract class AbstractClient<T> implements EventDesignator<Status> {
    
    /**
     * Remote server URL and PORT number, value from application.properties 
     */
    protected String containerPath;

    /**
     * Worker name which is recognized by the remote server. 
     */
    protected String hostName;
    protected RestTemplate restTemplate;
    /**
     * Spring Event publisher, used to notify if the remote connection to the server is down.
     */
    private ApplicationEventPublisher eventPublisher;

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
     * If state is not initialized, endpoints will not be enabled.
     * When the state is initialized all the endpoints will be enabled
     * and the state endpoints will be closed until the application restarts.
     */
    private volatile boolean stateInitialized;

    /**
     * Saves the local network hostname, which is used for recognizing who is the client
     * on the remote server
     */
    @PostConstruct
    private void initHostName() {
        this.hostName = LocalNetwork.getHostName();
    }

    @EventListener(value = {Status.class})
    @Override
    public void handleEvent(Status obj) {
        if (Status.ESTABLISHED == obj) {
            this.stateInitialized = true;
        }
    }

    /**
     * Submit a POST request to the endpoint parameter.
     * 
     * @param endpoint to which the request will be made
     * @param body {@code Object} which will be served as payload for the request
     * @return {@link ResponseEntity} or null if an exception is thrown
     * @throws RestClientException if the request could not be made for some reason
     */
    protected ResponseEntity<Object> postObjectToUrl(final String endpoint, final Object body)
            throws RestClientException {
        if (this.notAvailableServer() || !this.stateInitialized) {
            return null;
        }

        return this.postCall(endpoint, body);
    }

    /**
     * Submit a POST request to the state endpoint parameter.
     *
     * @param endpoint to which the request will be made
     * @param body {@code Object} which will be served as payload for the request
     * @return {@link ResponseEntity} or null if an exception is thrown
     * @throws RestClientException if the request could not be made for some reason
     */
    protected ResponseEntity<Object> postObjectToUrlState(final String endpoint, final Object body)
            throws RestClientException {
        if (this.notAvailableServer() || this.stateInitialized) {
            return null;
        }

        return this.postCall(endpoint, body);
    }

    /**
     * Plain HTTP POST call to the requested endpoint. If an exception occurs it
     * will be delegated to the appropriate response.
     *
     * @param endpoint to which the request will be made
     * @param body {@code Object} which will be served as payload for the request
     * @return {@link ResponseEntity} or null if an exception is thrown
     */
    private ResponseEntity<Object> postCall(final String endpoint, final Object body) {
        try {
            return this.restTemplate.exchange(
                    endpoint,
                    HttpMethod.POST,
                    new HttpEntity(body, buildHeaders()),
                    Object.class);
        } catch (Exception ex) {
            return this.delegateException(ex, endpoint);
        }
    }

    /**
     * Check if the status code is valid (in this case {@code HttpStatus.FORBIDEN}), any
     * other {@link HttpStatus} aside from OK will be treated as invalid. In case of
     * {@link ResourceAccessException} event will be fired to notify the application to
     * close all the connections towards the remote server.
     * 
     * @param ex that will be check for specific types
     * @param endpoint representing the remote enpoint for this method call
     * @return {@code HttpStatus.OK} if the {@link Exception} is of type 
     * {@link HttpStatusCodeException} and the status code is {@code FORBIDEN},
     * else return null
     */
    private ResponseEntity delegateException(final Exception ex, final String endpoint) {
        if (ex instanceof HttpStatusCodeException) {
            HttpStatusCodeException statusCodeEx = (HttpStatusCodeException) ex;
            if (HttpStatus.LOCKED.equals(statusCodeEx.getStatusCode())) {
                return ResponseEntity.ok().build();
            }
            ExceptionOperationHandler.registerExceptionOperation(
                    this.getClass(), statusCodeEx, "postObjectToUrl()", endpoint);
            return null;
        } else if (ex instanceof ResourceAccessException) {
            this.eventPublisher.publishEvent(new NetworkConnection(false));
            return null;
        } else {
            ExceptionOperationHandler.registerExceptionOperation(
                    this.getClass(), ex, "postObjectToUrl()", endpoint);
            return null;
        }
    }

    /**
     * Submit a POST request to the endpoint parameter.
     * 
     * @param endpoint to which the request will be made
     * @param body {@code Object} which will be served as payload for the request
     * @return {@code boolean} true if the request was successful, else false
     * @throws RestClientException if the request could not be made for some reason
     */
    protected boolean postToEndpoint(final String endpoint, final Object body) {
        return (this.stateInitialized)
                ? this.isOkResponseStatus(this.postObjectToUrl(endpoint, body))
                : this.isOkResponseStatus(this.postObjectToUrlState(endpoint, body));
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
            return this.restTemplate.exchange(
                endpoint, 
                HttpMethod.GET, 
                new HttpEntity<>(this.buildHeaders()), 
                clazz);
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
        return (response == null) ? false : this.isValidResponse(response);
    }

    /**
     * Helper method to compare the {@code ResponseEntity} status code
     * is valid {@code HttpStatus.OK}.
     * @param response
     * @return {@code boolean} true if the {@code HttpStatus} is OK, else return false
     */
    private boolean isValidResponse(final ResponseEntity response) {
        return response.getStatusCode().equals(HttpStatus.OK);
    }

    /**
     * Build the {@link HttpHeaders} with the valid headers.
     * 
     * @return {@code HttpHeaders} with prefilled headers
     */
    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("auth", this.hostName);

        return headers;
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

    @Autowired
    protected final void setEventPublisher(ApplicationEventPublisher publisher) {
        this.eventPublisher = publisher;
    }
}
