package com.ad.miningobserver.network.control;

import java.util.ArrayList;
import java.util.List;

import com.ad.miningobserver.file.Finder;
import com.ad.miningobserver.network.entity.NetworkError;
import com.ad.miningobserver.util.FileAndObjectReference;
import com.ad.miningobserver.util.JsonCreator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Creates/Reads JSON files for the network package.
 */
@Component
public class NetworkJsonCreator extends JsonCreator {
    
    @Autowired
    protected Finder fileFinder;
    
    /**
     * Write JSON object to network directory file.
     *
     * @param value object that will be written to JSON file
     * @return UUID of file without the extension
     */
    public String writeNetworkJson(Object value) {
        return super.writeToFile(value, this.fileFinder.getNetworkDirectory());
    }
    
    /**
     * Read the {@code NetworkError} object from file.
     *
     * @param uuid of file without the extension
     * @return {@link NetworkError}
     */
    public NetworkError readNetworkJson(String uuid) {
        final String jsonFile = super.jsonFilePath(this.fileFinder.getNetworkDirectory(), uuid);
        return (NetworkError) super.readJsonFile(jsonFile, NetworkError.class);
    }
    
    /**
     * Read the network files and store the reference to {@link FileAndObjectReference} class.
     * If there are no network files, an empty {@code List} will be retured.
     *
     * @return {@link FileAndObjectReference} object that holds the parsed 
     * {@code List} of GpuErrorStream objects, or empty {@code List} if the directory is empty
     */
    public FileAndObjectReference<NetworkError> readNetworkErrorFiles() {
        final List<String> files = this.fileFinder.getNetworkFiles();
        final List<NetworkError> errorStreamList = new ArrayList<>(files.size());
        for (String file : files) {
            final NetworkError networkError = (NetworkError) 
                    super.readJsonFile(file, NetworkError.class);
            if (networkError != null) {
                errorStreamList.add(networkError);
            }
        }
        return new FileAndObjectReference<>(files, errorStreamList);
    }
}
