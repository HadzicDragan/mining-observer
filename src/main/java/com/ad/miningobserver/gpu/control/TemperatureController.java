package com.ad.miningobserver.gpu.control;

import com.ad.miningobserver.gpu.entity.GpuCard;
import com.ad.miningobserver.gpu.FanSpeedOperation;
import com.ad.miningobserver.operation.Operation.OrderCode;
import com.ad.miningobserver.operation.boundary.OperationRegister;

/**
 * Helper class that manages the fan speed of the graphic card
 */
public class TemperatureController {
    
    /**
     * Enumeration of fan options
     */
    public enum FanOptions {
        
        /**
         * Fan speed is turned off.
         */
        OFF(0), 
        
        /**
         * Fan speed is set at 25%.
         */
        LIGHT(25), 
        
        /**
         * Fan speed is set at 50%.
         */
        MEDIUM(50), 
        
        /**
         * Fan speed is set at 75%.
         */
        HEAVY(75), 
        
        /**
         * Fan speed is set at 100%.
         */
        FULL(100), 
        
        /**
         * Fan speed is in CRITICAL state, which means the mining application
         * should be turned off.
         */
        CRITICAL(-1);
        
        private final int fanSpeed;
        
        /**
         * Enumeration constructor which accepts a int as a parameter.
         */
        private FanOptions(final int fanSpeed) {
            this.fanSpeed = fanSpeed;
        }

        /**
         * Retrieves the fan speed of the current state.
         * @return int fan speed
         */
        public int getFanSpeed() {
            return fanSpeed;
        }
    }
    
    /**
     * Set the fan speed based upon the temperature. If the temperature is
     * higher than 70'C the program should be stopped.
     * @param temperature
     * @return 
     */
    public static FanOptions temperatureIntensity(final int temperature) {
        if (temperature <= 40) {
            return FanOptions.OFF;
        } else if ((temperature > 40) && (temperature < 45)) {
            return FanOptions.LIGHT;
        } else if ((temperature >= 45) && (temperature < 55)) {
            return FanOptions.MEDIUM;
        } else if ((temperature >= 55) && (temperature < 60)) {
            return FanOptions.HEAVY;
        } else if ((temperature >= 60) && (temperature < 70)) {
            return FanOptions.FULL;
        } else {
            return FanOptions.CRITICAL;
        }
    }
    
    /**
     * Check temperatue for {@code GpuCard} and register an 
     * {@link FanSpeedOperation} if the temperature is at 
     * {@code CRITICAL} state.
     * 
     * @param gpu {@link GpuCard} for which the temperature is checked
     */
    public static void criticalTemperature(final GpuCard gpu) {
        FanOptions fanOptions = TemperatureController.temperatureIntensity(gpu.getTemperature());
        if (TemperatureController.isFanSpeedCritical(fanOptions)) {
            final FanSpeedOperation fanSpeedOperation = 
                    new FanSpeedOperation(OrderCode.CRITICAL, fanOptions, gpu.getUUID());
            OperationRegister.getOperationRegister()
                    .addOperation(fanSpeedOperation);
        }
    }
    
    /**
     * Determine if fan speed is at CRITICAL state;
     *
     * @param fanOptions enumerated value of {@link FanOptions}
     */
    private static boolean isFanSpeedCritical(FanOptions fanOptions) {
        return (fanOptions == TemperatureController.FanOptions.CRITICAL);
    }
}
