package com.bedrockcloud.bedrockcloud;

import com.bedrockcloud.bedrockcloud.files.Startfiles;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;
import com.bedrockcloud.bedrockcloud.server.proxy.ProxyServer;

import java.io.IOException;

public class CloudStarter {
    public static void main(String args[]) {
        try {
            Class.forName("com.bedrockcloud.bedrockcloud.BedrockCloud");

            new Startfiles();
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                BedrockCloud.getLogger().exception(e);
            }
            new BedrockCloud();

        }catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
