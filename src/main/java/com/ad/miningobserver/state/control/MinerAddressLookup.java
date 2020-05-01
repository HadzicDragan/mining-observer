package com.ad.miningobserver.state.control;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * MinerAddressLookup
 */
@Component
public class MinerAddressLookup {

    @Value(value = "${miner.address.default}")
    private String defaultMinerAddress;

    @Value(value = "${miner.address.file.location}")
    private String minerAddressFileLocation;

    private boolean minerAddressFileUnavailable() {
        return this.minerAddressFileLocation.isBlank();
    }

    /**
     * Read the minerAddress from application.properties.
     * If the property file can be found and read that value
     * will be used, else the default value from
     * application.properties.
     *
     * @return minerAddress from properties file
     */
    public String getMinerAddress() {
        return (this.minerAddressFileUnavailable())
                ? this.defaultMinerAddress
                : this.readFile();
    }

    /**
     * Should be added in the next release.
     * TODO add this implementation
     *
     * @return minerAddress from the file
     */
    private String readFile() {
        // should read from this.minerAddressFileLocation
        // when you look it up, save it inside this state
        // because there is no need to re check every time
        // an argument can be made because this will be done
        // only on startup that you do not need to save it locally
        return null;
    }
}
