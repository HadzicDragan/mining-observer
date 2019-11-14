package com.ad.miningobserver.gpu.control;

import com.ad.miningobserver.gpu.InputStreamProcessParser;
import com.ad.miningobserver.gpu.GpuErrorOperation;
import com.ad.miningobserver.operation.Operation;
import com.ad.miningobserver.operation.OperationRegister;
import com.ad.miningobserver.gpu.entity.GpuErrorStream;
import com.ad.miningobserver.util.EntryHolder;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandError implements InputStreamProcessParser {
    
    private final OperationRegister register;
    private final GpuJsonCreator jsonCreator;

    @Autowired
    public CommandError(
            OperationRegister register, 
            GpuJsonCreator jsonCreator) {
        this.register = register;
        this.jsonCreator = jsonCreator;
    }

    @Override
    public void commandLineOutput(final List<String> errorStreamList) {
        final EntryHolder<List<String>, List<String>> entry = this.parseErrorStream(errorStreamList);
        if (!entry.getFirst().isEmpty() || entry.getSecond().isEmpty()) {
            final GpuErrorStream errorStream = new GpuErrorStream(
                entry.getFirst(), entry.getSecond());

            final String jsonUUID = this.jsonCreator.writeErrorJson(errorStream);
            final GpuErrorOperation operation = 
                    new GpuErrorOperation(Operation.OrderCode.CRITICAL, jsonUUID);
            this.register.addOperation(operation);
        }
    }
    
    public EntryHolder<List<String>, List<String>> parseErrorStream(final List<String> errorStream) {
        final List<String> errorList = errorStream.stream()
            .filter(val -> GpuError.getErrorList().contains(val))
            .collect(Collectors.toList());
        
        final List<String> uncommonErrorList = errorStream.stream()
            .filter(val -> !errorList.contains(val))
            .collect(Collectors.toList());
        return new EntryHolder<>(errorList, uncommonErrorList);
    }
}
