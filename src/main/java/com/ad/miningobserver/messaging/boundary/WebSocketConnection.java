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
import com.ad.miningobserver.PathLocation;
import com.ad.miningobserver.network.Connection;
import com.ad.miningobserver.network.control.LocalNetwork;
import com.ad.miningobserver.exception.ExceptionOperationHandler;
import com.ad.miningobserver.util.EntryHolder;

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
    private final LocalNetwork localNetwork;
    private WebSocket webSocket;

    @Value(value = "${container.ip.address}")
    private String containerIPAddress;
    @Value(value = "${websocket.connector.protocol}")
    private String wsProtocol;
    @Value(value = "${websocket.connector.port}")
    private String portNumber;
    @Value(value = "${websocket.connector.enabled}")
    private boolean websocketEnabled;
    
    private String wsEndpointURL;
    
    @Autowired
    public WebSocketConnection(WebSocketMessageListener wsListener, LocalNetwork network) {
        this.wsMessageListener = wsListener;
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

        this.wsEndpointURL = this.buildEndpoint();
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
        final EntryHolder<String, String> entry = new EntryHolder<>("worker", canonicalHostName);
        this.connect(entry, ChronoUnit.SECONDS.getDuration());
    }
    
    /**
     * Connect to the remote websocket server.
     * 
     * @param entry
     * @param connectionTimeout
     * @throws URISyntaxException
     */
    private void connect(EntryHolder<String, String> entry, Duration connectionTimeout) 
        throws URISyntaxException {
        if (this.webSocket != null) {
            return;
        }
        
        final HttpClient httpClient = HttpClient.newHttpClient();
        this.webSocket = httpClient.newWebSocketBuilder()
                .header(entry.getFirst(), entry.getSecond())
                .connectTimeout(connectionTimeout)
                .buildAsync(new URI(this.wsEndpointURL), this.wsMessageListener)
                .join();
    }

    private String buildEndpoint() {
        StringBuilder builder = new StringBuilder();
        return builder.append(wsProtocol)
                .append("://")
                .append(this.containerIPAddress)
                .append(":")
                .append(portNumber)
                .append(PathLocation.CONTAINER)
                .append(PathLocation.WEBSOCKET_ENDPOINT)
                .toString();
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
