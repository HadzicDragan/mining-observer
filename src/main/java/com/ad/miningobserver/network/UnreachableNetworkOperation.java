package com.ad.miningobserver.network;

import com.ad.miningobserver.SpringContextLookup;
import com.ad.miningobserver.network.boundary.NetworkService;
import com.ad.miningobserver.network.entity.NetworkError;
import com.ad.miningobserver.operation.Operation;

/**
 * Network operation that writes to file if the network is not available.
 */
public final class UnreachableNetworkOperation extends Operation {
    
    private final String networkMessage;

    public UnreachableNetworkOperation(OrderCode code, String networkMessage) {
        super(code);
        this.networkMessage = networkMessage;
    }
    
    /**
     * Delegate to {@link NetworkService#publishNetworkError(NetworkError)}.
     * Service will try to post to remote server, if the request was not successful
     * will save the {@code NetworkError} to a file.
     */
    @Override
    public void run() {
        this.getNetworkService().publishNetworkError(new NetworkError(this.networkMessage));
    }

    /**
     * Lookup {@link NetworkService} from Spring bean context lookup.
     * 
     * @return {@link NetworkService} bean representation
     */
    private NetworkService getNetworkService() {
        return SpringContextLookup.getBean(NetworkService.class);
    }
}
