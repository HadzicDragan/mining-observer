package com.ad.miningobserver.gpu.control;

import java.util.ArrayList;
import java.util.List;

import com.ad.miningobserver.file.Finder;
import com.ad.miningobserver.gpu.entity.GpuErrorStream;
import com.ad.miningobserver.gpu.entity.GpuThermal;
import com.ad.miningobserver.gpu.entity.GpuThermals;
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
    public String writeGpuThermalJson(final Object value) {
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
     * Read the {@link GpuThermal} object from file.
     *
     * @param uuid of file without the extension
     * @return {@link GpuThermal}
     */
    public GpuThermal readCriticalGpuThermalJson(final String uuid) {
        final String jsonFile = super.jsonFilePath(this.fileFinder.getTemperatureDirectory(), uuid);
        return (GpuThermal) super.readJsonFile(jsonFile, GpuThermal.class);
    }
    
    /**
     * Read the {@link GpuThermal} object from file.
     *
     * @param uuid of file without the extension
     * @return {@link GpuThermal}
     */
    public GpuThermal readGpuListJson(final String uuid) {
        final String jsonFile = super.jsonFilePath(this.fileFinder.getGpuListDirectory(), uuid);
        return (GpuThermal) super.readJsonFile(jsonFile, GpuThermal.class);
    }
    
    /**
     * Read the {@link GpuThermals} object from file.
     *
     * @param uuid of file without the extension
     * @return {@link GpuThermals}
     */
    public GpuThermals readGpuThermalsJson(final String uuid) {
        final String jsonFile = super.jsonFilePath(this.fileFinder.getGpuListDirectory(), uuid);
        return (GpuThermals) super.readJsonFile(jsonFile, GpuThermals.class);
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
     * parsed {@code List} of GpuThermal objects, or empty {@code List} if the directory is empty
     */
    public FileAndObjectReference<GpuThermal> readGpuThermalFiles() {
        final List<String> files = this.fileFinder.getGpuListFiles();
        final List<GpuThermal> gpuThermalList = new ArrayList<>(files.size());
        for (String file : files) {
            final GpuThermals gpuThermals = (GpuThermals) 
                    super.readJsonFile(file, GpuThermals.class);
            if (gpuThermals != null) {
                for (GpuThermal thermal : gpuThermals.getGpuThermals()) {
                    gpuThermalList.add(thermal);
                }
            }
        }
        return new FileAndObjectReference<>(files, gpuThermalList);
    }

    /**
     * Read the temperature files and store the reference to {@link FileAndObjectReference} class.
     * If there are no temperature files, an empty {@code List} will be retured.
     * 
     * @return {@link FileAndObjectReference} object that holds the 
     * reference to {@code List} files and {@code List} gpu critical state;
     */
    public FileAndObjectReference<GpuThermal> readTemperatureFiles() {
        // #TODO this should use a tempate pattern
        // in the first iteration copy paste will be used
        // this method is the same as {@code readGpuListFiles()}
        final List<String> files = this.fileFinder.getTemperatureFiles();
        final List<GpuThermal> gpuCriticalList = new ArrayList<>(files.size());
        for (String file : files) {
            final GpuThermal gpuThermal = (GpuThermal) 
                    super.readJsonFile(file, GpuThermal.class);
            if (gpuThermal != null) {
                gpuCriticalList.add(gpuThermal);
            }
        }
        return new FileAndObjectReference<>(files, gpuCriticalList);
    }
}
