package com.ad.miningobserver.state.boundary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * StateService
 */
@Service
public class StateService {

    private final StateClient client;
    private final StateManager manager;

    @Autowired
    public StateService(StateClient client, StateManager manager) {
        this.client = client;
        this.manager = manager;
    }

    /**
     * POST minerAddress to the remote server.
     *
     */
    public void publishMinerAddress() {
        this.manager.recordMinerAddress(this.client.postSaveMiner());
    }

    /**
     * POST workerName to the remote server.
     *
     */
    public void publishWorkerName() {
        this.manager.recordWorkerName(this.client.postSaveWorker());
    }
}
