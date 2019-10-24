package com.ad.miningobserver.gpu.control;

import com.ad.miningobserver.gpu.InputStreamProcessParser;
import com.ad.miningobserver.gpu.GpuErrorOperation;
import com.ad.miningobserver.operation.Operation;
import com.ad.miningobserver.operation.boundary.OperationRegister;
import com.ad.miningobserver.gpu.entity.GpuErrorStream;
import java.util.ArrayList;
import java.util.List;
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
        List<String> sortedErrorList = this.parseErrorStream(errorStreamList);
        if (!sortedErrorList.isEmpty()) {
            // #TODO check if this is coverd valid scenarios
            // MEANING if the comments below are valid
            // need the specific error codes and their messages
            // notify the with the email so I can see what happend
            final GpuErrorStream errorStream = new GpuErrorStream();
            errorStream.setErrorList(sortedErrorList);

            final String jsonUUID = this.jsonCreator.writeErrorJson(errorStream);
            final GpuErrorOperation operation = 
                    new GpuErrorOperation(Operation.OrderCode.CRITICAL, jsonUUID);
            this.register.addOperation(operation);
        }
    }
    
    public List<String> parseErrorStream(List<String> errorStream) {
        final List<String> errorList = new ArrayList<>(errorStream.size());
        errorStream.forEach((errorMessage) -> {
            GpuError.getErrorList().forEach(errorCode -> {
                if (errorMessage.contains(errorCode)) {
                    errorList.add(errorMessage);
                }
            });
        });
        return errorList;
    }
}
