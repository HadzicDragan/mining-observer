package com.ad.miningobserver.network.boundary;

import javax.annotation.PostConstruct;

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
    private NetworkBatchInitiator batchInitiator;

    @PostConstruct
    private void initBatchProcessing() {
        this.batchInitiator.initBatchProcessing();
    }

    public void publishNetworkError(final NetworkError networkError) {
        final boolean isPosted = this.networkClient.postNetworkError(networkError);
        if (isPosted) {
            return;
        }

        jsonCreator.writeNetworkJson(networkError);
    }
    
    /**
     * Lookup if there are network error files and send them to the remote server.
     * If the request is successful remove the files from the server.
     *  
     */
    public void batchNetworkErrors() {
        final FileAndObjectReference<NetworkError> reference = 
                this.jsonCreator.readNetworkErrorFiles();
        if (reference.getObjects().isEmpty()) {
            return;
        }
        
        final boolean isPosted = this.networkClient.postNetworkErrors(reference.getObjects());
        if (isPosted) {
            Creator.removeFiles(reference.getFiles());
        }
    }
}
