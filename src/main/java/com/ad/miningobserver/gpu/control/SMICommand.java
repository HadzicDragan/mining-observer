package com.ad.miningobserver.gpu.control;

import java.util.StringJoiner;

/**
 * Nvidia command helper class
 * @see <a href="https://developer.nvidia.com/nvidia-system-management-interface">nvidia-smi</a>
 */
public class SMICommand {

    /**
     * Nvidia-smi command string
     */
    public static String GPU_SMI_COMMAND_STRING = "nvidia-smi";
    
    /**
     * Format output string value
     */
    public static String GPU_FORMAT_STRING      = "--format=csv,noheader";
    
    /**
     * Query the GPU information
     */
    public static String GPU_QUERY_STRING       = "--query-gpu=";

    /**
     * Nvidia-smi query options
     * <li>{@link #NAME}</li>
     * <li>{@link #INDEX}</li>
     * <li>{@link #UUID}</li>
     * <li>{@link #VBIOS}</li>
     * <li>{@link #FAN_SPEED}</li>
     * <li>{@link #TEMPERATURE}</li>
     */
    public enum QueryOptions {

        /**
         * Name
         */
        NAME("name"),
        
        /**
         * Index
         */
        INDEX("index"),
        
        /**
         * Unique identifier
         */
        UUID("uuid"),
        
        /**
         * BIOS version
         */
        VBIOS("vbios_version"),
        
        /**
         * Fan speed
         */
        FAN_SPEED("fan.speed"),
        
        /**
         * Temperature
         */
        TEMPERATURE("temperature.gpu");
        
        private final String option;

        /**
         * Enumeration constructor which accepts a String as a parameter.
         * @param option query string option
         */
        private QueryOptions(String option) {
            this.option = option;
        }

        /**
         * String value of the query option.
         * @return string query option.
         */
        public String getOption() {
            return option;
        }
    }
    
    /**
     * Query only the specified values from the enumerated array.
     * @param options array of enumeration nvidia query options
     * @return array of joined string representation of the commands
     */
    public String[] queryGPUCard(QueryOptions[] options) {
        String[] smiCommand = {
            SMICommand.GPU_SMI_COMMAND_STRING,
            SMICommand.GPU_FORMAT_STRING,
            this.prepareQueryString(options)
        };
        GpuCardParser.commandCountValidator(smiCommand);
        return smiCommand;
    }

    /**
     * Query all the information of a graphics card.
     * @return array of joined string representation of the commands
     */
    public String[] queryAllInfoGPUs() {
        String[] smiCommand = {
            SMICommand.GPU_SMI_COMMAND_STRING,
            SMICommand.GPU_FORMAT_STRING,
            this.prepareQueryString(QueryOptions.values())
        };
        GpuCardParser.commandCountValidator(smiCommand);
        return smiCommand;
    }
    
    /**
     * Query the required options.
     * @return array of joined string representation of the commands
     */
    public String[] queryMendatoryInfoGPUs() {
        QueryOptions[] queryOptions = {
            QueryOptions.UUID,
            QueryOptions.VBIOS,
            QueryOptions.FAN_SPEED,
            QueryOptions.TEMPERATURE,
            QueryOptions.INDEX
        };
        
        String[] smiCommand = {
            SMICommand.GPU_SMI_COMMAND_STRING,
            SMICommand.GPU_FORMAT_STRING,
            this.prepareQueryString(queryOptions)
        };
        GpuCardParser.commandCountValidator(smiCommand);
        return smiCommand;
    }

    /**
     * Joins the enumerated query options with the standard query string
     * {@link #GPU_QUERY_STRING}.
     * @param queryOptions enumerated query options
     * @return string value of joined query options
     */
    private String prepareQueryString(QueryOptions... queryOptions) {
        StringJoiner joiner = new StringJoiner(",");
        for (QueryOptions option : queryOptions) {
            joiner.add(option.getOption());
        }

        return GPU_QUERY_STRING + joiner.toString();
    }
}
