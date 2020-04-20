package com.ad.miningobserver.state.boundary;

import com.ad.miningobserver.state.control.MinerAddressLookup;
import com.ad.miningobserver.state.entity.MinerAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StateService {

    private final MinerAddressLookup minerLookup;
    private final StateClient client;

    @Autowired
    public StateService(MinerAddressLookup minerLookup, StateClient client) {
        this.minerLookup = minerLookup;
        this.client = client;
    }

    public void publishMinerAddress() {
        MinerAddress miner = new MinerAddress();
        miner.minerAddress = this.minerLookup.getMinerAddress();
        this.client.postSaveMiner(miner);
    }

    public void publishWorkerName() {
        this.client.postSaveWorker(this.minerLookup.getMinerAddress());
    }

}
