package com.ad.miningobserver.network.boundary;

import java.util.List;

import com.ad.miningobserver.client.AbstractClient;
import com.ad.miningobserver.network.control.NetworkPath;
import com.ad.miningobserver.network.entity.NetworkError;

import org.springframework.stereotype.Component;

/**
 * Client interface for the network package.
 */
@Component
public class NetworkClient extends AbstractClient {

    /**
     * Post network error to remote server.
     * 
     * @return {@code boolean} true if POST request was successful, else false
     */
    public boolean postNetworkError(final NetworkError networkError) {
        final String endpoint = new NetworkPath(super.containerPath)
            .buildNetworksEndpoint(super.hostName);
        return super.postToEndpoint(endpoint, networkError);
    }

    /**
     * POST network errors to remote server.
     * 
     * @param networkErrors 
     * @return {@code boolean} true if POST request was successful, else false
     */
    public boolean postNetworkErrors(final List<NetworkError> networkErrors) {
        final String endpoint = new NetworkPath(super.containerPath)
            .buildNetworkErrorBatchEndpoint(super.hostName);
        return super.postToEndpoint(endpoint, networkErrors);
    }
}
