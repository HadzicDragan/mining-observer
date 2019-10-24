package com.ad.miningobserver;

import com.ad.miningobserver.gpu.control.SMICommand;
import com.ad.miningobserver.gpu.control.SMICommand.QueryOptions;
import java.util.Arrays;
import java.util.List;

public class MockGPU {
    
    public static List<String> mockGPUStringList() {
        String[] gpuArray = {
            "GPU-427efabf-7a18-cee7-04e4-52ee5945171b, 86.06.63.00.A5, 0 %, 47, 0",
            "GPU-56c49e13-5303-4d45-a3fb-cf17444ce6b5, 86.06.63.00.A5, 0 %, 55, 1",
            "GPU-b9301929-6489-4733-8f9f-809d5689786c, 86.06.63.00.A5, 0 %, 50, 2",
            "GPU-1f6a6ddd-beaa-4fe6-aec9-ed670169ce1e, 86.06.63.00.A5, 0 %, 60, 3",
            "GPU-1332a99e-47e5-4d33-9d9e-91fea7320ff2, 86.06.63.00.A5, 0 %, 38, 4",
            "GPU-c072ddb8-2d9e-4411-8b51-a0a64c6afaa0, 86.06.63.00.A5, 0 %, 49, 5",
        };
        
        return Arrays.asList(gpuArray);
    }
    
    public static String[] mockCommands() {
        final String[] commands = {
            SMICommand.GPU_SMI_COMMAND_STRING, 
            SMICommand.GPU_FORMAT_STRING, 
            SMICommand.GPU_QUERY_STRING + QueryOptions.UUID
        };
        
        return commands;
    }
}
