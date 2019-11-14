package com.ad.miningobserver.network.boundary;

import javax.annotation.PostConstruct;

import com.ad.miningobserver.file.Creator;
import com.ad.miningobserver.network.control.NetworkJsonCreator;
import com.ad.miningobserver.network.entity.NetworkError;
import com.ad.miningobserver.operation.batch.BatchOperation;
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
    private BatchOperation batchOperation;

    @Autowired
    private NetworkBatch networkBatch;

    @PostConstruct
    private void initBatchProcessing() {
        this.batchOperation.registerBatchJob(networkBatch);
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
     * If the request is successful remove the files from the server. If the remote server
     * is not reachable, email the client about the errors.
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
            return;
        }

        // #TODO mail should be sent to owner if remote server is down.
    }
}
