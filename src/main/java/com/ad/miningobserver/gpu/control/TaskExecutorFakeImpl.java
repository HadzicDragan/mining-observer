package com.ad.miningobserver.gpu.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class should be removed when it is properly mocked in a test
 * when removing this class the Factory will change, this was initially setup
 * for testing, but mocking the instance is a better approach.
 */
@Component
public class TaskExecutorFakeImpl {
    
    private static final int INICIAL_VALUE = 0;
    private static final int THROW_ERROR_CAP = 2;
    private final AtomicInteger internalCounter = new AtomicInteger(INICIAL_VALUE);
    
    @Autowired
    private FakeCommand commandLine;
    @Autowired
    private FakeCommandError commandLineError;

    public void executeCommand(String[] commandArgs) {
        if (this.internalCounter.get() == THROW_ERROR_CAP) {
            this.internalCounter.set(INICIAL_VALUE);
            
            this.commandLineError.commandLineOutput(this.getErrorStream());
            return;
        }
        
        final String command = commandArgs[0];
        if (SMICommand.GPU_SMI_COMMAND_STRING.equals(command)) {
            this.commandLine.commandLineOutput(this.getGPUInputStream());
            this.internalCounter.incrementAndGet();
        }
    }
    
    private List<String> getErrorStream() {
        final List<String> errors = new ArrayList<>();
        errors.add("GPU has fallen off the bus");
        return errors;
    }
    
    private List<String> getGPUInputStream() {
        final String[] gpuArray = {
            "GPU-427efabf-7a18-cee7-04e4-52ee5945171b, 86.06.63.00.A5, 0 %, 47, 0",
            "GPU-56c49e13-5303-4d45-a3fb-cf17444ce6b5, 86.06.63.00.A5, 0 %, 55, 1",
            "GPU-b9301929-6489-4733-8f9f-809d5689786c, 86.06.63.00.A5, 0 %, 50, 2",
            "GPU-1f6a6ddd-beaa-4fe6-aec9-ed670169ce1e, 86.06.63.00.A5, 0 %, 60, 3",
            "GPU-1332a99e-47e5-4d33-9d9e-91fea7320ff2, 86.06.63.00.A5, 0 %, 38, 4",
            "GPU-c072ddb8-2d9e-4411-8b51-a0a64c6afaa0, 86.06.63.00.A5, 0 %, 49, 5",
        };
        
        return Arrays.asList(gpuArray);
    }
}
