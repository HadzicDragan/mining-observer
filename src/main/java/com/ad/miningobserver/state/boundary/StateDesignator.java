package com.ad.miningobserver.state.boundary;

import com.ad.miningobserver.EventDesignator;
import com.ad.miningobserver.network.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * StateDesignator
 */
@Component
public class StateDesignator implements EventDesignator<Connection> {

    private final StateService service;
    private final StateManager manager;

    @Autowired
    public StateDesignator(StateService service, StateManager manager) {
        this.service = service;
        this.manager = manager;
    }

    @Override
    @EventListener
    public void handleEvent(Connection connection) {
        if (!connection.isConnected() || this.manager.recordsEstablished()) {
            return;
        }
        if (connection.isConnected()) {
            this.publishMinerIfNotEstablished();
            this.publishWorkerIfNotEstablished();
        }
    }

    /**
     * If the miner address was not created/established on the remote server,
     * publish the address.
     *
     */
    private void publishMinerIfNotEstablished() {
        if (!this.manager.isMinerAddressEstablished()) {
            this.service.publishMinerAddress();
        }
    }

    /**
     * If the worker name was not created/established on the remote server,
     * publish the name.
     *
     */
    private void publishWorkerIfNotEstablished() {
        if (!this.manager.isWorkerNameEstablished()) {
            this.service.publishWorkerName();
        }
    }
}
