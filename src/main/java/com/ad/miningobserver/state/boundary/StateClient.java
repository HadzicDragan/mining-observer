package com.ad.miningobserver.state.boundary;

import com.ad.miningobserver.client.AbstractClient;
import com.ad.miningobserver.network.control.LocalNetwork;
import com.ad.miningobserver.state.control.StatePath;
import com.ad.miningobserver.state.entity.MinerAddress;
import com.ad.miningobserver.state.entity.WorkerName;
import org.springframework.stereotype.Component;

@Component
public class StateClient extends AbstractClient {

    public boolean postSaveMiner(final MinerAddress minerAddress) {
        final String endpoint = new StatePath(super.containerPath)
                .buildSaveMinerEndpoint();
        // check how this will be propagaded, only boolean is valid
        // and on the service layer you should check what to do next
        return super.postToEndpoint(endpoint, minerAddress);
    }

    public boolean postSaveWorker(final String minerAddress) {
        final String endpoint = new StatePath(super.containerPath)
                .buildSaveWorkerEndpoint(minerAddress);
        // might have to package the hostname inside a object
        // need to validate this on the container :)
        WorkerName worker = new WorkerName();
        worker.workerName = super.hostName;

        return super.postToEndpoint(endpoint, worker);
    }
}
