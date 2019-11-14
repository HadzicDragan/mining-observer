package com.ad.miningobserver.messaging.boundary;

import java.net.http.WebSocket;
import java.net.http.WebSocket.Listener;
import java.util.concurrent.CompletionStage;

import com.ad.miningobserver.network.NetworkConnection;
import com.ad.miningobserver.exception.ExceptionOperationHandler;
import com.ad.miningobserver.util.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * WebSocketMessageListener
 */
@Component
public class WebSocketMessageListener implements Listener {

    private String socketId;
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    @Override
    public void onOpen(WebSocket webSocket) {
        this.setSocketId();
        Listener.super.onOpen(webSocket);
    }

    @Override
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        this.eventPublisher.publishEvent(new NetworkConnection(false));
        this.removeSocketId();
        return Listener.super.onClose(webSocket, statusCode, reason);
    }
    
    @Override
    public void onError(WebSocket webSocket, Throwable error) {
        ExceptionOperationHandler.registerExceptionOperation(
                this.getClass(), error, "onError", this.socketId);
    }
    
    /**
     * Generate a unique id for the current websocket session.
     */
    private void setSocketId() {
        this.socketId = StringUtils.generateUUID();
    }

    /**
     * Remove the unique id from the current websocket session.
     */
    private void removeSocketId() {
        this.socketId = null;
    }
}
