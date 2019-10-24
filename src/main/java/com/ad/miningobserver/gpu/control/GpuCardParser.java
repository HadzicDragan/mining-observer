package com.ad.miningobserver.gpu.control;

import com.ad.miningobserver.gpu.entity.GpuCard;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class which parses the GPU information
 */
public class GpuCardParser {
    
    /**
     * Build GPU objects from the provided String values.
     * @param gpuInfoList
     * @return list of parsed GPU objects
     */
    public static List<GpuCard> buildGPUs(final List<String> gpuInfoList) {
        final List<GpuCard> gpuCards = new ArrayList<>(gpuInfoList.size());
        gpuInfoList.forEach(gpu -> {
            
            final GpuCard.GPUCardBuilder cardBuilder = new GpuCard.GPUCardBuilder();
            final String[] gpuParameters = gpu.split(",");
            for (int i = 0; i < gpuParameters.length; i++) {
                final String parameter = gpuParameters[i].trim();
                switch (i) {
                    case 0:
                        cardBuilder.uuid(parameter);
                        break;
                    case 1:
                        cardBuilder.biosVersion(parameter);
                        break;
                    case 2:
                        cardBuilder.fanSpeed(GpuCardParser.parseFanSpeedValue(parameter));
                        break;
                    case 3:
                        cardBuilder.temperature(Integer.valueOf(parameter));
                        break;
                    case 4:
                        cardBuilder.index(Integer.valueOf(parameter));
                        break;
                    default:
                        break;
                }
            }
            
            final GpuCard gpuCard = cardBuilder.build();
            if (gpuCard.getUUID() != null && !gpuCard.getUUID().isEmpty()) {
                gpuCards.add(gpuCard);
            }
        });
        
        return gpuCards;
    }
    
    /**
     * Parse the string value of the fan speed and convert it to a int value.
     * @param fanSpeedValue string value of fan speed
     * @return int value of fan speed
     */
    public static int parseFanSpeedValue(final String fanSpeedValue) {
        if (!GpuCardParser.containsValidCharactersFanSpeed(fanSpeedValue)) {
            // maybe change this exception type to a custom one
            throw new IllegalArgumentException(
                    "Type of value passed does not match the required");
        }
        final String parsedFanSpeed = fanSpeedValue.substring(0, 
                fanSpeedValue.length() -1).trim();
        return Integer.valueOf(parsedFanSpeed);
    }
    
    private static boolean containsValidCharactersFanSpeed(final String value) {
        if (!value.endsWith("%")) {
            return false;
        }
        int counter = 0;
        for (int i = 0; i < value.length(); i++) {
            // value can't be more than 100
            if (counter > 3) {
                return false;
            }
            char character = value.charAt(i);
            if (Character.isWhitespace(character)) {
                continue;
            } else if (character == '%') {
                continue;
            }
            if (Character.isDigit(character)) {
                counter++;
            } else {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Checks the validity of the passed array. If the array count is 0 or less
     * then the minimal length which is 2 an {@link IllegalArgumentException}
     * will be thrown.
     * @param commands command line argument commands
     */
    public static void commandCountValidator(final String[] commands) {
        final int commandLenght = commands.length;
        if (commandLenght == 0) {
            throw new IllegalArgumentException("Command length can "
                    + "not be " + commandLenght);
        } else if (commandLenght <= 2) {
            throw new IllegalArgumentException("No query options selected.");
        } 
    }
}
