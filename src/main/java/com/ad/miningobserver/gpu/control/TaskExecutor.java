package com.ad.miningobserver.gpu.control;

import java.io.IOException;

import com.ad.miningobserver.operation.ExceptionOperationHandler;
import com.ad.miningobserver.util.RunCLITask;
import com.ad.miningobserver.util.RunCLITask.ProcessResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class that logs the GPU temperature and updates it accordingly if needed.
 */
@Component
public class TaskExecutor {

    private final RunCLITask cliTask;
    private final Command commandLine;
    private final CommandError commandLineError;
    
    @Autowired
    public TaskExecutor(
            RunCLITask cliTask, 
            Command commandLine, 
            CommandError commandLineError) {
        this.cliTask = cliTask;
        this.commandLine = commandLine;
        this.commandLineError = commandLineError;
    }

    /**
     * Run the argument commands in a seprate process. If they produce an 
     * error stream it will be pushed to the {@link CommandError} 
     * else normal flow will continue in {@link Command}.
     * 
     * @param commandArgs arguments that will be run in a seperate process.
     */
    public void executeCommand(String[] commandArgs) {
        try {
            ProcessResult processResult = this.cliTask.submitCommand(commandArgs);
            if (!processResult.getErrorStream().isEmpty()) {
                this.commandLineError.commandLineOutput(processResult.getErrorStream());
                return;
            }
            
            this.commandLine.commandLineOutput(processResult.getInputStream());
        } catch (IOException ex) {
            ExceptionOperationHandler.registerExceptionOperation(
                    this.getClass(), ex, "executeComman(String[] commandArgs)", commandArgs);
        }
    }
}
