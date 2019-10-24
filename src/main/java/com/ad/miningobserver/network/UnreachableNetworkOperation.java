package com.ad.miningobserver.network;

import com.ad.miningobserver.SpringContextLookup;
import com.ad.miningobserver.network.control.NetworkJsonCreator;
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
     * Write to file the network message and date when the network was not available.
     */
    @Override
    public void run() {
        final NetworkError networkError = new NetworkError(this.networkMessage);
        final NetworkJsonCreator networkJsonCreator = this.getNetworkJsonCreator();
        networkJsonCreator.writeNetworkJson(networkError);
    }

    /**
     * Lookup {@link NetworkJsonCreator} from Spring bean context lookup.
     * 
     * @return {@link NetworkJsonCreator} bean representation
     */
    private NetworkJsonCreator getNetworkJsonCreator() {
        return SpringContextLookup.getBean(NetworkJsonCreator.class);
    }
}
