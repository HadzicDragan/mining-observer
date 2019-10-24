package com.ad.miningobserver.util;

import com.ad.miningobserver.NameReference;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * Class that executes command line actions.
 */
@Component
public class RunCLITask {
    
    @Autowired
    @Qualifier(value = NameReference.EXECUTOR_TASK_THREAD_POOL)
    private ThreadPoolTaskExecutor executor;
    
    /**
     * Result of the command line action parsed in list of strings.
     */
    public class ProcessResult {
        
        private final List<String> inputStream;
        private final List<String> errorStream;

        public ProcessResult(
                List<String> inputStream, 
                List<String> errorStream) {
            this.inputStream = inputStream;
            this.errorStream = errorStream;
        }

        public List<String> getInputStream() {
            return inputStream;
        }

        public List<String> getErrorStream() {
            return errorStream;
        }
    }

    /**
     * Executes the given command line commands. 
     * @param command array of string commands
     * @return {@link ProcessResult} if the command is executed and parsed
     * correctly
     * @throws java.io.IOException
     */
    public ProcessResult submitCommand(String[] command) throws IOException {
        final ProcessBuilder processBuilder = new ProcessBuilder(command);
        final Process process = processBuilder.start();
        
        final Future<List<String>> inputStreamFuture = this.executor.submit(() -> {
            return this.readInputStream(process.getInputStream());
        });
        
        final Future<List<String>> errorStreamFuture = this.executor.submit(() -> {
            return this.readInputStream(process.getErrorStream());
        });
        
        List<String> inputStreamList = new ArrayList<>();
        try {
            inputStreamList = (!inputStreamFuture.isDone()) ? 
                    inputStreamFuture.get(30, TimeUnit.SECONDS) : 
                    inputStreamFuture.get();
        } catch (InterruptedException | 
                ExecutionException | 
                TimeoutException ex) {
            ApplicationLogger.errorLogger().error(ex);
        }
        
        List<String> errorStreamList = new ArrayList<>();
        try {
            errorStreamList = (!errorStreamFuture.isDone()) ? 
                    errorStreamFuture.get(30, TimeUnit.SECONDS) : 
                    errorStreamFuture.get();
        } catch (InterruptedException | 
                ExecutionException | 
                TimeoutException ex) {
            ApplicationLogger.errorLogger().error(ex);
        }
        
        if (process.isAlive()) {
            try {
                if(!process.waitFor(1, TimeUnit.MINUTES)) {
                    // process didn't exit
                    // timed out.
                    // notify listeners if any
                    // gracefully close and try to reopen the operation several times
                    // if the number of times exceeds maybe just down the application
                }
            } catch (InterruptedException ex) {
                ApplicationLogger.errorLogger().error(ex);
            }
        }
        
        return new ProcessResult(inputStreamList, errorStreamList);
    }

    /**
     * Read line by line and store it into a {@link List} of Strings.
     * @param inputStream
     * @return parsed InputStream as a list of strings.
     * @throws IOException when trying to close the input stream
     */
    private List<String> readInputStream(final InputStream inputStream) 
            throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream))) {
            return bufferedReader.lines().collect(Collectors.toList());
        } catch (Exception ex) {
            ApplicationLogger.errorLogger().error(ex);
            return Collections.emptyList();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}
