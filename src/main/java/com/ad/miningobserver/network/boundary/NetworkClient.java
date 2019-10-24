package com.ad.miningobserver.network.boundary;

import java.util.List;

import com.ad.miningobserver.client.AbstractClient;
import com.ad.miningobserver.network.control.NetworkPath;
import com.ad.miningobserver.network.entity.NetworkError;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Client interface for the network package.
 */
@Component
public class NetworkClient extends AbstractClient {

    /**
     * POST network errors to the remote server.
     * 
     * @param networkErrors 
     * @return {@code boolean} true if POST request was successful, else false
     */
    public boolean postNetworkErrors(final List<NetworkError> networkErrors) {
        if (!super.isRestEnabled) {
            return false;
        }
        if (super.notAvailableServer()) {
            return false;
        }
        final String endpoint = new NetworkPath(super.containerPath)
                .buildNetworksEndpoint();
        final ResponseEntity<Object> response = super.postObjectToUrl(
                endpoint, new HttpEntity<>(networkErrors));
        return super.isOkResponseStatus(response);
    }
}
