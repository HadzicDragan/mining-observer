package com.ad.miningobserver.gpu.control;

import com.ad.miningobserver.gpu.entity.GpuCard;
import com.ad.miningobserver.gpu.entity.GpuThermal;

/**
 * GpuCardToThermalMapper
 */
public class GpuCardToThermalMapper {

    public static GpuThermal cardToThermalInformation(final GpuCard card) {
        final GpuThermal gpuThermal = new GpuThermal();
        gpuThermal.index = card.getIndex();
        gpuThermal.uuuid = card.getUUID();
        gpuThermal.fanSpeed = card.getFanSpeed();
        gpuThermal.temperature = card.getTemperature();
        gpuThermal.reportedDate = card.getCurrentTimeDate();
        return gpuThermal;
    }
}