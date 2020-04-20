package com.ad.miningobserver.state.control;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MinerAddressLookup {

    @Value(value = "${miner.address.default}")
    private String defaultMinerAddress;

    @Value(value = "${miner.address.file.location}")
    private String minerAddressFileLocation;

    private boolean minerAddressFileUnavailable() {
        return this.minerAddressFileLocation.isBlank();
    }

    public String getMinerAddress() {
        return (this.minerAddressFileUnavailable())
                ? this.defaultMinerAddress
                : this.readFile();
    }

    // TODO add this implementation
    private String readFile() {
        // should read from this.minerAddressFileLocation
        // when you look it up, save it inside this state
        // because there is no need to re check every time
        // an argument can be made because this will be done
        // only on startup that you do not need to save it locally
        return null;
    }
}
