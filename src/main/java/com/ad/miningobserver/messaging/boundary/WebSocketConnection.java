package com.ad.miningobserver.messaging.boundary;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import javax.annotation.PostConstruct;

import com.ad.miningobserver.EventDesignator;
import com.ad.miningobserver.NameReference;
import com.ad.miningobserver.messaging.control.EntryHolder;
import com.ad.miningobserver.messaging.control.WebSocketEndpointBuilder;
import com.ad.miningobserver.network.Connection;
import com.ad.miningobserver.network.control.LocalNetwork;
import com.ad.miningobserver.operation.ExceptionOperationHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * WebSocketConnection handles all details related to the 
 * websocket connection with the remote server.
 */
@Component
@DependsOn(value = {
    NameReference.NETWORK_LOCAL_BEAN,
    NameReference.NETWORK_LOOKUP_TASK_BEAN
})
public class WebSocketConnection implements EventDesignator<Connection> {
    
    private final WebSocketMessageListener wsMessageListener;
    private final WebSocketEndpointBuilder endpointBuilder;
    private final LocalNetwork localNetwork;
    private WebSocket webSocket;

    @Value(value = "${rest.connector.enabled}")
    private boolean websocketEnabled;
    
    @Autowired
    public WebSocketConnection(
            WebSocketMessageListener wsListener, 
            WebSocketEndpointBuilder endpointBuilder, 
            LocalNetwork network) {
        this.wsMessageListener = wsListener;
        this.endpointBuilder = endpointBuilder;
        this.localNetwork = network;
    }
    
    /**
     * Initialize the websocket connection after the Spring bean has been created.
     */
    @PostConstruct
    private final void init() {
        if (!this.websocketEnabled) {
            return;
        }
        this.checkNetworkConnectionAndConnectToServer();
    }
    
    /**
     * When received, the event will set the status of the websocket connection.
     */
    @Override
    @EventListener(value = {Connection.class})
    public void handleEvent(Connection connection) {
        if (!this.websocketEnabled) {
            return;
        }
        if (connection.isConnected()) {
            this.reconnectToServer();
        } else {
            this.disconnected();
        }
    }
    
    /**
     * Reconnect the websocket connection to the remote server.
     */
    public void reconnectToServer() {
        this.checkNetworkConnectionAndConnectToServer();
    }
    
    /**
     * Convenient method to check if the websocket session is connected.
     * 
     * @return {@code boolean} true if websocket is connected, else false
     */
    public boolean isConnected() {
        return this.webSocket != null;
    }
    
    /**
     * Convenient method to check if the websocket session is not connected.
     * 
     * @return {@code boolean} true if websocket is not connected, else false
     */
    public boolean notConnected() {
        return this.webSocket == null;
    }

    /**
     * Caution, calling this method will break working WebSocket endpoints. 
     * This method is used to delete the reference when the server closes the connection,
     * or when there is no network available.
     */
    public void disconnected() {
        if (this.isConnected()) {
            this.webSocket.abort();
            this.webSocket = null;
        }
    }
    
    /**
     * Method to help with the exception handling while establishing 
     * the remote connection.
     */
    private void connectToServer() {
        try {
            this.connectToEndpoint();
        } catch (URISyntaxException ex) {
            ExceptionOperationHandler.registerExceptionOperation(
                    this.getClass(), ex, "connectToServer()", null);
        }
    }

    /**
     * Establish a websocket connection with the host name and valid worker name.
     * #TODO should get worker from some other method
     * 
     * @throws URISyntaxException
     */
    private void connectToEndpoint() throws URISyntaxException {
        final String canonicalHostName = LocalNetwork.getHostName();
        if (canonicalHostName == null) {
            ExceptionOperationHandler.registerUncommonOperation(
                this.getClass(), "connectToEndpoint", "Unable to get canonicalHostName");
            return;
        }
        final EntryHolder<String, String> entryHolder = 
                new EntryHolder<>("worker", canonicalHostName);
        this.connect(entryHolder, ChronoUnit.SECONDS.getDuration());
    }
    
    /**
     * Connect to the remote websocket server.
     * 
     * @param entryHolder
     * @param connectionTimeout
     * @throws URISyntaxException
     */
    private void connect(EntryHolder<String, String> entryHolder, 
            Duration connectionTimeout) throws URISyntaxException {
        if (this.webSocket != null) {
            return;
        }
        
        final HttpClient httpClient = HttpClient.newHttpClient();
        this.webSocket = httpClient.newWebSocketBuilder()
                .header(entryHolder.getFirst(), entryHolder.getSecond())
                .connectTimeout(connectionTimeout)
                .buildAsync(new URI(this.endpointBuilder.buildEndpoint()), this.wsMessageListener)
                .join();
    }
    
    /**
     * Determine if the local network is running and connect to 
     * websocket server if not connect, else if the local network 
     * is down remove the websocket reference and close the connection.
     */
    private void checkNetworkConnectionAndConnectToServer() {
        if (this.localNetwork.isOutgoingNetworkUp()) {
            if (this.notConnected()) {
                this.connectToServer();
            }
        } else {
            this.disconnected();
            if (this.webSocket != null) {
                this.webSocket = null;
            }
        }
    }
}
