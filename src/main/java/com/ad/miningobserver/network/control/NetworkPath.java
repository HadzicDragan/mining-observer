package com.ad.miningobserver.network.control;

import com.ad.miningobserver.client.ClientPath;

/**
 * Client endpoints that are specific to the network package.
 */
public class NetworkPath extends ClientPath {
    
    private static final String NETWORK_ERROR = "/networks";

    public NetworkPath(String hostName) {
        super(hostName);
    }
    
    /**
     * Build the URL endpoint for network.
     * 
     * @return constructed URL endpoint
     */
    public String buildNetworksEndpoint() {
        return super.pathBuilder(NETWORK_ERROR);
    }

    public String buildNetworkErrorEndpoint(final String error) {
        return super.pathBuilder(NETWORK_ERROR, error);
    }
}
