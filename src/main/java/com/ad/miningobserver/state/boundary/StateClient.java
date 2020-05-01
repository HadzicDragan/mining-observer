package com.ad.miningobserver.state.boundary;

import com.ad.miningobserver.client.AbstractClient;
import com.ad.miningobserver.state.control.MinerAddressLookup;
import com.ad.miningobserver.state.control.StatePath;
import com.ad.miningobserver.state.entity.MinerAddress;
import com.ad.miningobserver.state.entity.WorkerName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * StateClient
 */
@Component
public class StateClient extends AbstractClient {

    private final MinerAddressLookup minerLookup;

    @Autowired
    public StateClient(MinerAddressLookup minerLookup) {
        this.minerLookup = minerLookup;
    }

    /**
     * Post the remote server which minerAddress is used by this observer.
     *
     * @return {@code boolean} true if the request was successful, else false
     */
    public boolean postSaveMiner() {
        final String endpoint = new StatePath(super.containerPath)
                .buildSaveMinerEndpoint();
        MinerAddress miner = new MinerAddress();
        miner.minerAddress = this.minerLookup.getMinerAddress();
        return super.postToEndpoint(endpoint, miner);
    }

    /**
     * Post to the remote server which workerName is used by this observer.
     *
     * @return {@code boolean} true if the request was successful, else false
     */
    public boolean postSaveWorker() {
        final String endpoint = new StatePath(super.containerPath)
                .buildSaveWorkerEndpoint(this.minerLookup.getMinerAddress());
        WorkerName worker = new WorkerName();
        worker.workerName = super.hostName;
        return super.postToEndpoint(endpoint, worker);
    }
}
