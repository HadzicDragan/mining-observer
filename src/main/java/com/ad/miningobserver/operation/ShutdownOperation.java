package com.ad.miningobserver.operation;

/**
 * #TODO 
 * This will be implemented in future versions.
 * The idea is to make a Operation that will shutdown the entire application
 * and send the required information to the Server.
 */
public final class ShutdownOperation extends Operation {

    public ShutdownOperation(OrderCode code) {
        super(code);
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
