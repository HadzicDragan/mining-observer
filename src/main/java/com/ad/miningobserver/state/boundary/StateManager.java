package com.ad.miningobserver.state.boundary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * StateManager
 */
@Component
public class StateManager {

    public enum Status {
        ESTABLISHED;
    }

    private final ApplicationEventPublisher eventPublisher;

    private final AtomicBoolean minerAddressEstablished;
    private final AtomicBoolean workerNameEstablished;
    private final AtomicBoolean stateInitialized;

    @Autowired
    public StateManager(ApplicationEventPublisher eventPublished) {
        this.eventPublisher = eventPublished;
        this.minerAddressEstablished = new AtomicBoolean(false);
        this.workerNameEstablished = new AtomicBoolean(false);
        this.stateInitialized = new AtomicBoolean(false);
    }

    /**
     * Returns the state of minerAddress.
     *
     * @return {@code boolean} true if the state was initialized properly,
     * else false
     */
    public boolean isMinerAddressEstablished() {
        return this.minerAddressEstablished.get();
    }

    /**
     * Set the minerAddress state. If every state is initialized an event
     * will be published to notify the container.
     *
     * @param saved {@code boolean} true if workerName was initialized,
     * else false
     */
    public void recordMinerAddress(final boolean saved) {
        if (this.stateInitialized.get()) {
           return;
        }
        this.minerAddressEstablished.set(saved);
        if (this.recordsEstablished()) {
            this.eventPublisher.publishEvent(Status.ESTABLISHED);
            this.stateInitialized.set(true);
        }
    }

    /**
     * Returns the state of workerName.
     *
     * @return {@code boolean} true if the state was initialized properly,
     * else false
     */
    public boolean isWorkerNameEstablished() {
        return this.workerNameEstablished.get();
    }

    /**
     * Set the workerName state. If every state is initialized an event
     * will be published to notify the container.
     *
     * @param saved {@code boolean} true if workerName was initialized,
     * else false
     */
    public void recordWorkerName(final boolean saved) {
        if (this.stateInitialized.get()) {
            return;
        }
        this.workerNameEstablished.set(saved);
        if (this.recordsEstablished()) {
            this.eventPublisher.publishEvent(Status.ESTABLISHED);
            this.stateInitialized.set(true);
        }
    }

    /**
     * Check if all the required states are initialized.
     *
     * @return {@code boolean} true if all the required state are initialized,
     * else false
     */
    public boolean recordsEstablished() {
        return this.isMinerAddressEstablished() && this.isWorkerNameEstablished();
    }
}
