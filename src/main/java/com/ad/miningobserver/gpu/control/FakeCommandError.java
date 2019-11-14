package com.ad.miningobserver.gpu.control;

import com.ad.miningobserver.gpu.InputStreamProcessParser;
import com.ad.miningobserver.gpu.GpuErrorOperation;
import com.ad.miningobserver.operation.Operation.OrderCode;
import com.ad.miningobserver.operation.OperationRegister;
import com.ad.miningobserver.gpu.entity.GpuErrorStream;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FakeCommandError implements InputStreamProcessParser {
    
    private final OperationRegister register;
    private final GpuJsonCreator jsonCreator;

    @Autowired
    public FakeCommandError(OperationRegister register, GpuJsonCreator jsonCreator) {
        this.register = register;
        this.jsonCreator = jsonCreator;
    }
    
    @Override
    public void commandLineOutput(List<String> list) {
        final GpuErrorStream errorStream = new GpuErrorStream(list, null);
        
        final String jsonUUID = this.jsonCreator.writeErrorJson(errorStream);
        final GpuErrorOperation operation = 
                new GpuErrorOperation(OrderCode.CRITICAL, jsonUUID);
        this.register.addOperation(operation);
    }
}
