package com.ad.miningobserver.network.control;

import com.ad.miningobserver.client.ClientPath;

/**
 * Client endpoints that are specific to the network package.
 */
public class NetworkPath extends ClientPath {
    
    private static final String NETWORK_ROOT_PATH = "/workers";
    private static final String NETWORK_ERROR = "/networks";
    private static final String NETWORK_BATCH = "/batch";

    public NetworkPath(String hostName) {
        super(hostName);
    }
    
    /**
     * Build the URL endpoint for network.
     * 
     * @return constructed URL endpoint
     */
    public String buildNetworksEndpoint(final String workerName) {
        return super.pathBuilder(
            NETWORK_ROOT_PATH, 
            ClientPath.PATH_SEPARATOR, 
            workerName, 
            NETWORK_ERROR);
    }

    public String buildNetworkErrorBatchEndpoint(final String workerName) {
        return super.pathBuilder(
            NETWORK_ROOT_PATH, 
            ClientPath.PATH_SEPARATOR, 
            workerName, 
            NETWORK_ERROR, 
            NETWORK_BATCH);
    }
}
