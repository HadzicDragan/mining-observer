package com.ad.miningobserver.network;

/**
 * This interface should be used to listen on published events for {@code Connection}.
 */
public interface Connection {
    
    /**
     * 
     * @return {@code boolean} true if connected, else false
     */
    boolean isConnected();
}
