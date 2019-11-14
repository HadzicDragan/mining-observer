package com.ad.miningobserver.gpu.control;

import java.util.ArrayList;
import java.util.List;

import com.ad.miningobserver.file.Finder;
import com.ad.miningobserver.gpu.entity.GpuCriticalState;
import com.ad.miningobserver.gpu.entity.GpuErrorStream;
import com.ad.miningobserver.gpu.entity.GpuList;
import com.ad.miningobserver.util.FileAndObjectReference;
import com.ad.miningobserver.util.JsonCreator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Creates/Reads JSON files for the gpu package.
 */
@Component
public class GpuJsonCreator extends JsonCreator {
    
    @Autowired
    protected Finder fileFinder;
    
    /**
     * Write JSON object to error directory file.
     *
     * @param value object that will be written to JSON file
     * @return UUID of file without the extension
     */
    public String writeErrorJson(final Object value) {
        return super.writeToFile(value, this.fileFinder.getErrorDirectory());
    }
    
    /**
     * Write JSON object to gpu directory file.
     *
     * @param value object that will be written to JSON file
     * @return UUID of file without the extension
     */
    public String writeGpuListJson(final Object value) {
        return super.writeToFile(value, this.fileFinder.getGpuListDirectory());
    }
    
    /**
     * Write JSON object to temperature directory file.
     *
     * @param value object that will be written to JSON file
     * @return UUID of file without the extension
     */
    public String writeCriticalTemperatureJson(final Object value) {
        return super.writeToFile(value, this.fileFinder.getTemperatureDirectory());
    }
    
    /**
     * Read the {@link GpuErrorStream} object from file.
     *
     * @param uuid of file without the extension
     * @return {@link GpuErrorStream}
     */
    public GpuErrorStream readErrorJson(final String uuid) {
        final String jsonFile = super.jsonFilePath(this.fileFinder.getErrorDirectory(), uuid);
        return (GpuErrorStream) super.readJsonFile(jsonFile, GpuErrorStream.class);
    }

    /**
     * Read the {@link GpuCriticalState} object from file.
     *
     * @param uuid of file without the extension
     * @return {@link GpuCriticalState}
     */
    public GpuCriticalState readCriticalStateJson(final String uuid) {
        final String jsonFile = super.jsonFilePath(this.fileFinder.getTemperatureDirectory(), uuid);
        return (GpuCriticalState) super.readJsonFile(jsonFile, GpuCriticalState.class);
    }
    
    /**
     * Read the {@link GpuList} object from file.
     *
     * @param uuid of file without the extension
     * @return {@link GpuList}
     */
    public GpuList readGpuListJson(final String uuid) {
        final String jsonFile = super.jsonFilePath(this.fileFinder.getGpuListDirectory(), uuid);
        return (GpuList) super.readJsonFile(jsonFile, GpuList.class);
    }

    /**
     * Read the gpu error files and store the reference to {@link FileAndObjectReference} class.
     * If there are no gpu error files, an empty {@code List} will be retured. 
     * 
     * @return {@link FileAndObjectReference} object that holds the parsed 
     * {@code List} of GPUErrorStream objects, or null if the directory is empty
     */
    public FileAndObjectReference<GpuErrorStream> readGpuErrorStreamFiles() {
        final List<String> files = this.fileFinder.getErrorFiles();
        final List<GpuErrorStream> errorStreamList = new ArrayList<>(files.size());
        for (String file : files) {
            final GpuErrorStream gpuErrorStream = (GpuErrorStream) 
                    super.readJsonFile(file, GpuErrorStream.class);
            if (gpuErrorStream != null) {
                errorStreamList.add(gpuErrorStream);
            }
        }
        return new FileAndObjectReference<>(files, errorStreamList);
    }

    /**
     * Read the gpu files and store the reference to {@link FileAndObjectReference} class.
     * If there are no gpu files, an empty {@code List} will be retured.
     * 
     * @return {@link FileAndObjectReference} object that holds the 
     * parsed {@code List} of GpuList objects, or empty {@code List} if the directory is empty
     */
    public FileAndObjectReference<GpuList> readGpuListFiles() {
        final List<String> files = this.fileFinder.getGpuListFiles();
        final List<GpuList> gpuCardList = new ArrayList<>(files.size());
        for (String file : files) {
            final GpuList gpuCards = (GpuList) 
                    super.readJsonFile(file, GpuList.class);
            if (gpuCards != null) {
                gpuCardList.add(gpuCards);
            }
        }
        return new FileAndObjectReference<>(files, gpuCardList);
    }

    /**
     * Read the temperature files and store the reference to {@link FileAndObjectReference} class.
     * If there are no temperature files, an empty {@code List} will be retured.
     * 
     * @return {@link FileAndObjectReference} object that holds the 
     * reference to {@code List} files and {@code List} gpu critical state;
     */
    public FileAndObjectReference<GpuCriticalState> readTemperatureFiles() {
        final List<String> files = this.fileFinder.getTemperatureFiles();
        final List<GpuCriticalState> gpuCriticalList = new ArrayList<>(files.size());
        for (String file : files) {
            final GpuCriticalState gpuCriticalState = (GpuCriticalState) 
                    super.readJsonFile(file, GpuCriticalState.class);
            if (gpuCriticalState != null) {
                gpuCriticalList.add(gpuCriticalState);
            }
        }
        return new FileAndObjectReference<>(files, gpuCriticalList);
    }
}
