package com.ad.miningobserver.network.control;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;

import com.ad.miningobserver.network.UnreachableNetworkOperation;
import com.ad.miningobserver.operation.Operation.OrderCode;
import com.ad.miningobserver.operation.OperationRegister;
import com.ad.miningobserver.util.ApplicationLogger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class LocalNetwork {
    
    private static final String NETWORK_UNABLE_MESSAGE = "Unable to connect to network";
    private static final String RECONNECT_MESSAGE = "Reconnecting to network";
    private final AtomicBoolean isNetworkOperational = new AtomicBoolean(true);
    
    private NetworkInterface networkInterface;
    private OperationRegister register;

    @Autowired
    public LocalNetwork(OperationRegister register) {
        this.register = register;
    }

    @PostConstruct
    private void postInitializationNetworkInterfaceBinding() {
        this.initiateNetworkInterface(null);
    }
    
    private void initiateNetworkInterface(final String message) {
        try {
            this.networkInterface = this.getNetworkInterfaceByLink();
            if (this.networkInterface == null) {
                this.setOutgoingNetworkDown();
                this.registerNetworkOperation(this.networkMessage(message));
            } else {
                this.setOutgoingNetworkUp();
            }
        } catch (SocketException ex) {
            ApplicationLogger.errorLogger().error(ex);
            this.setOutgoingNetworkDown();
            this.registerNetworkOperation(this.networkMessage(message));
        }
    }
    
    public boolean isReconnectNetworkSuccessful() {
        if (this.isOutgoingNetworkAvailable()) {
            return true;
        }
        
        this.initiateNetworkInterface(RECONNECT_MESSAGE);
        return (this.networkInterface != null);
    }
    
    public boolean isOutgoingNetworkUp() {
        try {
            if (this.isOutgoingNetworkAvailable()) {
                if (this.networkInterfaceDown()) {
                    this.setOutgoingNetworkDown();
                    return false;
                }
                return true;  
            }
        } catch (SocketException ex) {
            ApplicationLogger.errorLogger().error(ex);
        }
        this.setOutgoingNetworkDown();
        return false;
    }
    
    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getCanonicalHostName();
        } catch (UnknownHostException ex) {
            ApplicationLogger.errorLogger().error(ex);
            return null;
        }
    }

    private NetworkInterface getNetworkInterfaceByLink() throws SocketException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterfaceEnum = networkInterfaces.nextElement();
            if (networkInterfaceContainsLinkAddress(networkInterfaceEnum)) {
                return networkInterfaceEnum;
            }
        }
        return null;
    }

    private boolean networkInterfaceContainsLinkAddress(NetworkInterface localNetworkInterface) {
        List<InterfaceAddress> interfaceAddresses = localNetworkInterface.getInterfaceAddresses();
        for (InterfaceAddress address : interfaceAddresses) {
            InetAddress inetAddress = address.getAddress();
            if (inetAddress.isLinkLocalAddress()) {
                return true;
            }
        }
        return false;
    }
    
    private String networkMessage(final String value) {
        return Optional
                .ofNullable(value)
                .orElse(NETWORK_UNABLE_MESSAGE);
    }
    
    private boolean networkInterfaceDown() throws SocketException {
        return !networkInterface.isUp();
    }
    
    private boolean isOutgoingNetworkAvailable() {
        return this.isNetworkOperational.get();
    }

    private void setOutgoingNetworkDown() {
        this.isNetworkOperational.set(false);
    }
    
    private void setOutgoingNetworkUp() {
        this.isNetworkOperational.set(true);
    }
    
    private void registerNetworkOperation(final String message) {
        final UnreachableNetworkOperation networkOperation =
                new UnreachableNetworkOperation(OrderCode.CRITICAL, message);
        this.register.addOperation(networkOperation);
    }
}
