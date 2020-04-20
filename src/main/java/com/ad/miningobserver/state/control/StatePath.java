package com.ad.miningobserver.state.control;

import com.ad.miningobserver.client.ClientPath;

public class StatePath extends ClientPath {

    private static final String MINERS_PATH = "/miners";
    private static final String WORKERS_PATH = "/workers";

    public StatePath(String hostname) {
        super(hostname);
    }

    public String buildSaveMinerEndpoint() {
        return super.pathBuilder(
            MINERS_PATH
        );
    }

    public String buildSaveWorkerEndpoint(final String minerAddress) {
        return super.pathBuilder(
            WORKERS_PATH,
            ClientPath.PATH_SEPARATOR,
            MINERS_PATH,
            ClientPath.PATH_SEPARATOR,
            minerAddress
        );
    }
}
