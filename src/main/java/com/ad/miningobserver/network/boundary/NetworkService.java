package com.ad.miningobserver.network.boundary;

import com.ad.miningobserver.client.monitoring.boundary.ActuatorClient;
import com.ad.miningobserver.file.Creator;
import com.ad.miningobserver.network.control.NetworkJsonCreator;
import com.ad.miningobserver.network.entity.NetworkError;
import com.ad.miningobserver.util.FileAndObjectReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service interface for the network package.
 */
@Service
public class NetworkService {
    
    @Autowired
    private NetworkClient networkClient;
    
    @Autowired
    private NetworkJsonCreator jsonCreator;
    
    @Autowired
    private ActuatorClient actuatorClient;
    
    /**
     * Check the remote server is available.
     * 
     * @return {@code boolean} true if the remote server is available, else false.
     */
    public boolean isActiveServer() {
        return this.actuatorClient.isServerActive();
    }
    
    /**
     * Lookup if there are network error files and send them to the remote server.
     * If the request is successful remove the files from the server. If the remote server
     * is not reachable, email the client about the errors.
     * 
     */
    public void publishNetworkErrors() {
        final FileAndObjectReference<NetworkError> reference = 
                this.jsonCreator.readGpuErrorStreamFiles();
        if (reference.getObjects().isEmpty()) {
            return;
        }
        
        final boolean isPosted = this.networkClient.postNetworkErrors(reference.getObjects());
        if (isPosted) {
            Creator.removeFiles(reference.getFiles());
        }

        // #TODO mail should be sent to owner if remote server is down.
    }
}
