package com.ad.miningobserver.operation;

public abstract class Operation implements Runnable {

    /**
     * Runtime code to handle errors.
     * <li>{@link #LIGHT}</li>
     * <li>{@link #MEDIUM}</li>
     * <li>{@link #CRITICAL}</li>
     */
    public enum OrderCode {
        /**
         * Application should continue working
         */
        LIGHT,
        /**
         * Application should check the message and upon the message use the
         * appropriate observer class
         */
        MEDIUM,
        /**
         * Application should shutdown with closing of all the hooks.
         *
         * @see com.ad.miningobserver.boot.boundary.Application
         */
        CRITICAL;
    }
    
    private final OrderCode code;

    public Operation(OrderCode code) {
        this.code = code;
    }

    public OrderCode getCode() {
        return code;
    }
}
