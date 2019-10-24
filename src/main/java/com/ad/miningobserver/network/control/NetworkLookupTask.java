package com.ad.miningobserver.network.control;

import com.ad.miningobserver.network.NetworkConnection;
import com.ad.miningobserver.network.boundary.NetworkService;
import com.ad.miningobserver.util.ApplicationLogger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * NetworkLookupTask responsible for checking and establishing local network 
 * connection back if it drops in some point in time.
 */
@Component
public class NetworkLookupTask implements Runnable {
    
    private static final String CLASSNAME = NetworkLookupTask.class.getSimpleName();
    
    @Autowired
    private LocalNetwork localNetwork;
    
    @Autowired
    private NetworkService service;
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    /**
     * Task that will be run by the {@link NetworkLookupSchedular}. 
     * Checks if the remote server is active and notifies other service.
     * Attempt to reconnect to local network is previously was down.
     */
    @Override
    public void run() {
        if (this.localNetwork.isOutgoingNetworkUp()) {
            if (this.service.isActiveServer()) {
                this.publishEvent(true);
                return;
            } else {
                this.publishEvent(false);
                return;
            }
        } else {
            // network is not reachable, close other resources
            this.publishEvent(false);
        }
        
        if (this.localNetwork.isReconnectNetworkSuccessful()) {
            if (this.service.isActiveServer()) {
                this.publishEvent(true);
                this.service.publishNetworkErrors();
            }
        } else {
            ApplicationLogger.infoLogger().info(CLASSNAME + ". Unable to reach server.");
        }
    }

    /**
     * Publish the {@link Connection} event to listeners.
     * 
     * @param connect flag true if connected, else false
     */
    private void publishEvent(final boolean connect) {
        this.eventPublisher.publishEvent(new NetworkConnection(connect));
    }
}
