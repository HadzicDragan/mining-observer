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
     * #TODO change the {@link NetworkPath} method
     * Post network error to remote server.
     * 
     * @return {@code boolean} true if POST request was successful, else false
     */
    public boolean postNetworkError(NetworkError networkError) {
        // #TODO add implementation on the remote server.
        return false;
        /*
        final String endpoint = new NetworkPath(super.containerPath)
                .buildNetworkErrorEndpoint("single");
        return super.postToEndpoint(endpoint, networkError);
        */
    }

    /**
     * POST network errors to remote server.
     * 
     * @param networkErrors 
     * @return {@code boolean} true if POST request was successful, else false
     */
    public boolean postNetworkErrors(final List<NetworkError> networkErrors) {
        final String endpoint = new NetworkPath(super.containerPath)
                .buildNetworksEndpoint();
        return super.postToEndpoint(endpoint, networkErrors);
    }
}
