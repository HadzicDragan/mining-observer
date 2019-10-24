package com.ad.miningobserver.mail.control;

import com.ad.miningobserver.client.ClientPath;

public class AccountPath extends ClientPath {
    
    private static final String PATH = "/actuator";

    public AccountPath(String hostName) {
        super(hostName);
    }
    
    public String buildOwnerPath() {
        return super.pathBuilder(PATH);
    }
}
