package com.ad.miningobserver.network;

/**
 * NetworkConnection
 */
public class NetworkConnection implements Connection {
    
    private final boolean connected;

    public NetworkConnection(boolean connected) {
        this.connected = connected;
    }

    /**
     * Determine if the connection has established or not.
     */
    @Override
    public boolean isConnected() {
        return this.connected;
    }
}
