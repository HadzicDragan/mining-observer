package com.ad.miningobserver.network;

import java.util.concurrent.atomic.AtomicBoolean;

import com.ad.miningobserver.EventDesignator;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * RemoteServerStatus
 */
@Component
public class RemoteServerStatus implements EventDesignator<Connection> {

    private final AtomicBoolean available = new AtomicBoolean(true);

    /**
     * Describes if the remote server is available or not
     * 
     * @return {@code boolean} true if server is available, else false
     */
    public boolean isServerAvailable() {
        return this.available.get();
    }

    /**
     * When received, the event will set the status of the remote server.
     */
    @EventListener(value = {Connection.class})
    @Override
    public void handleEvent(Connection connection) {
        this.setServerStatus(connection.isConnected());        
    }

    /**
     * Set the remote server availability status.
     * 
     * @param available {@code boolean} true if server is available, else false
     */
    private void setServerStatus(final boolean available) {
        this.available.set(available);
    }
}