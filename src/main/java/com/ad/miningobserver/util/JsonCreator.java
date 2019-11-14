package com.ad.miningobserver.util;

import java.io.File;
import java.io.IOException;

import com.ad.miningobserver.file.FileExtensions;
import com.ad.miningobserver.exception.ExceptionOperationHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class that does JSON operations.
 */
@Component
public abstract class JsonCreator {

    @Autowired
    protected ObjectMapper mapper;    

    /**
     * Reads the JSON file by type.
     * 
     * @param jsonFile {@code String} representation of the {@link File} where the JSON is located
     * @param clazz type of {@code Class} for which the JSON file represents.
     * @return {@code Object} of the type passed or null if {@link IOException} thrown
     */
    protected Object readJsonFile(String jsonFile, Class<?> clazz) {
        try {
            return this.mapper.readValue(new File(jsonFile), clazz);
        } catch (IOException ex) {
            catchError(ex, "readErrorJson(String value)", jsonFile);
        }
        return null;
    }

    /**
     * Build a path with the directory and filename.
     *
     * @param directory where the files are located
     * @param uuid name of the file without the extension
     * @return built path to the JSON file
     */
    protected String jsonFilePath(final String directory, final String uuid) {
        final String jsonFile = StringUtils.buildString(
                directory, uuid, FileExtensions.JSON_EXTENSION);
        return jsonFile;
    }

    /**
     * Write the {@code Object} to JSON file.
     * 
     * @param value {@code Object} to be written to file
     * @param directory {@link Path} location where JSON will be stored
     * @return {@code String} name of the file, no extension or path included
     */
    protected String writeToFile(final Object value, final String directory) {
        final String uuid = StringUtils.generateUUID();
        final String jsonFile = this.jsonFilePath(directory, uuid);
        return this.isWriteToJsonFile(value, jsonFile) ? uuid : null;
    }

    /**
     * Write the {@code Object} to JSON file.
     * 
     * @param value {@code Object} to be written to file
     * @param jsonFile {@code String} representation of the {@link File} where the JSON is located
     * @return {code boolean} true if the JSON is written to file, else false.
     */
    private boolean isWriteToJsonFile(Object value, String jsonFile) {
        final ObjectWriter objectWritter = this.mapper.writer();
        try {
            objectWritter.forType(value.getClass()).writeValue(new File(jsonFile), value);
            return true;
        } catch (IOException ex) {
            catchError(ex, "writeJsonToFile(Object value)", jsonFile);
        }
        return false;
    }
    
    /**
     * Save the {@link IOException} to error log and register exception 
     * operation to be run at some point
     * 
     * @param ex {@link IOException} thrown when reading/writing to file
     * @param methodName name where the exception occured
     * @param jsonFile path where the JSON file is located
     */
    private void catchError(IOException ex, String methodName, String jsonFile) {
        ExceptionOperationHandler.registerExceptionOperation(getClass(), ex, methodName, jsonFile);
        ApplicationLogger.errorLogger().error(ex);
    }
}
