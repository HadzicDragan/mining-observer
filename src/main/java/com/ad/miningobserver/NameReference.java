package com.ad.miningobserver;

/**
 * Class that stores Spring bean name references for easier lookup
 */
public class NameReference {
    
    /**
     * Applies to class {@link BootInitiator}
     */
    public static final String BOOT_INITIATOR_BEAN = "bootInitiator";
    /**
     * Applies to class {@link ApplicationProcessHolder}
     */
    public static final String PROCESS_HANDLER_BEAN = "applicationProcessHolder";
    /**
     * Applies to class {@link NetworkLookupTask}
     */
    public static final String NETWORK_LOOKUP_TASK_BEAN = "networkLookupTask";
    /**
     * Applies to class {@link LocalNetwork}
     */
    public static final String NETWORK_LOCAL_BEAN = "localNetwork";
    /**
     * Applies to class {@link ScheduledFile}
     */
    public static final String FILE_CLEANUP_SCHEDULAR_BEAN = "fileCleanupSchedular";
    
    
    
    /**
     * ScheduledExecutor that queries the underlying GPUs on interval.
     */
    public static final String EXECUTOR_GPU_SCHEDULAR = "gpuTaskSchedularBean";
    /**
     * ScheduledExecutor on a CRON job that will cleanup the files.
     */
    public static final String EXECUTOR_OPERATION_SCHEDULAR = "operationSchedularBean";
    /**
     * ScheduledExecutor on a CRON job that will cleanup the files.
     */
    public static final String EXECUTOR_FILE_CLEANER_SCHEDULAR = "fileCleanerSchedularBean";
    /**
     * ScheduledExecutor on a CRON job that will check if the outbound network is available.
     */
    public static final String EXECUTOR_NETWORK_LOOKUP_SCHEDULAR = "networkLookupSchedularzz";
    /**
     * Executor which will be used only for operation processing.
     */
    public static final String EXECUTOR_OPERATION_THREAD_POOL = "operationThreadPoolExecutorBean";
    /**
     * Executor which will be used only for running command line processing.
     */
    public static final String EXECUTOR_TASK_THREAD_POOL = "taskThreadPoolExecutorBean";
    /**
     * Executor which will be used only for async processing.
     */
    public static final String EXECUTOR_ASYNC_TASK_THREAD_POOL = "asyncThreadPoolExecutorBean";
}
