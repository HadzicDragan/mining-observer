package com.ad.miningobserver.network;

/**
 * This interface should be used to listen on published events for {@code Connection}.
 */
public interface Connection {
    
    /**
     * #TODO add documentation
     * 
     * @return {@code boolean} true if connected, else false
     */
    boolean isConnected();
}
