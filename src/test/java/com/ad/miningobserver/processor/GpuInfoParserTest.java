package com.ad.miningobserver.processor;

import com.ad.miningobserver.gpu.control.GpuCardParser;
import com.ad.miningobserver.MockGPU;
import com.ad.miningobserver.gpu.entity.GpuCard;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class GpuInfoParserTest {
    
    private static final String ERROR_MESSAGE = 
                "Type of value passed does not match the required";
    
    @Test
    public void parseGPUStringRepresentionToGPUInfoTest() {
        final List<String> gpuStringList = MockGPU.mockGPUStringList();
        final List<GpuCard> gpuCards = GpuCardParser.buildGPUs(gpuStringList);
        Assertions.assertThat(gpuCards)
                .isNotEmpty();
        Assertions.assertThat(gpuCards)
                .hasSize(6);
    }
    
    @Test
    public void parseGPUStringRepresentionToGPUInfoNoGPUsTest() {
        final List<String> empty = new ArrayList<>();
        final List<GpuCard> gpuCards = GpuCardParser.buildGPUs(empty);
        Assertions.assertThat(gpuCards)
                .isEmpty();
    }
    
    @Test
    public void parseFanSpeedConverterTest() {
        final int expectedValue = 60;
        final String fanSpeedValue = " 60 %";
        final int parsedFanSpeed = GpuCardParser.parseFanSpeedValue(fanSpeedValue);
        Assertions.assertThat(parsedFanSpeed)
                .isEqualTo(expectedValue);
    }
    
    @Test
    public void parseFanSpeedConverterInvalidValuePassedTest() {
        final String invalidFanSpeedValue = " dd 45555 -%";
        final Throwable exception = Assertions.catchThrowable(
                () -> GpuCardParser.parseFanSpeedValue(invalidFanSpeedValue));
        Assertions.assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThat(exception)
                .hasMessageContaining(ERROR_MESSAGE);
    }
    
    @Test
    public void parseFanSpeedConverterInvalidIntegerValueTest() {
        final String invalidFanSpeedValue = "9999%";
        final Throwable exception = Assertions.catchThrowable(
                () -> GpuCardParser.parseFanSpeedValue(invalidFanSpeedValue));
        Assertions.assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThat(exception)
                .hasMessageContaining(ERROR_MESSAGE);
    }
    
    @Test
    public void validNumberOfCommandsPassedTest() {
        final String[] commands = MockGPU.mockCommands();
        GpuCardParser.commandCountValidator(commands);
    }
    
    @Test
    public void invalidNumberOfCommandsPassedTest() {
        final String errorMessage = "No query options selected.";
        final String[] commands = {"nvidia-smi"};
        final Throwable exception = Assertions.catchThrowable(
                () -> GpuCardParser.commandCountValidator(commands));
        Assertions.assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThat(exception)
                .hasMessageContaining(errorMessage);
    }
    
    @Test
    public void emptyNumberOfCommandsPassedTest() {
        final String errorMessage = "Command length can "
                    + "not be 0";
        final String[] commands = {};
        final Throwable exception = Assertions.catchThrowable(
                () -> GpuCardParser.commandCountValidator(commands));
        Assertions.assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThat(exception)
                .hasMessageContaining(errorMessage);
    }
}
