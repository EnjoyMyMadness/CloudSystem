package com.bedrockcloud.bedrockcloud.console.shutdown;

import com.bedrockcloud.bedrockcloud.BedrockCloud;

public class ShutdownThread extends Thread {

    @Override
    public synchronized void start() {
        BedrockCloud.shutdown();
    }
}
