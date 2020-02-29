package com.ad.miningobserver.operation.batch;

import java.util.concurrent.atomic.AtomicInteger;

import com.ad.miningobserver.EventDesignator;
import com.ad.miningobserver.network.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * BatchCoordinator
 */
@Component
public class BatchCoordinator implements EventDesignator<Connection> {

    private final AtomicInteger attemptCount = new AtomicInteger(0);

    @Autowired
    private BatchOperation batchOperation;
    
    @Override
    @EventListener(value = {Connection.class})
    public void handleEvent(Connection connection) {
        if (connection.isConnected() && attemptCount.get() > 4) {
            this.batchOperation.runAsync();
            this.attemptCount.set(0);
            return;
        }
        this.attemptCount.incrementAndGet();
    }
}