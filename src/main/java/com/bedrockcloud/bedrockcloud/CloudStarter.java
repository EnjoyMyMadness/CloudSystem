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
            addShutdownHook();

        }catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public static void addShutdownHook(){
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            BedrockCloud.setRunning(false);
            boolean allServicesStopped = false;
            do {
                try {
                    if (BedrockCloud.networkManager.serverSocket != null && !BedrockCloud.networkManager.serverSocket.isClosed()){
                        BedrockCloud.networkManager.serverSocket.close();
                        BedrockCloud.getLogger().warning("§cServerSocket was closed.");
                    }
                } catch (IOException ignored) {}

                for (final String templateName : BedrockCloud.getTemplateProvider().templateMap.keySet()) {
                    BedrockCloud.getTemplateProvider().removeRunningGroup(templateName);
                }

                for (final GameServer gameServer : BedrockCloud.getGameServerProvider().gameServerMap.values()) {
                    gameServer.stopServer();
                }

                for (final String proxy : BedrockCloud.getProxyServerProvider().proxyServerMap.keySet()) {
                    final ProxyServer proxyServer = BedrockCloud.getProxyServerProvider().getProxyServer(proxy);
                    proxyServer.stopServer();
                }

                int gameServerCount = BedrockCloud.getGameServerProvider().gameServerMap.size();
                int privateServerCount = BedrockCloud.getPrivateGameServerProvider().gameServerMap.size();
                int proxyServerCount = BedrockCloud.getProxyServerProvider().proxyServerMap.size();

                if (gameServerCount <= 0 && privateServerCount <= 0 && proxyServerCount <= 0){
                    allServicesStopped = true;
                    BedrockCloud.getLogger().info("§aAll services were stopped.");
                }

                final ProcessBuilder builder = new ProcessBuilder();
                try {
                    if (allServicesStopped) {
                        Thread.sleep(2000);
                        builder.command("/bin/sh", "-c", "killall -9 php").start();
                        builder.command("/bin/sh", "-c", "killall -9 java").start();
                    }
                } catch (IOException | InterruptedException e) {
                    BedrockCloud.getLogger().exception(e);
                }
            } while (!BedrockCloud.isRunning());
        }));
    }
}
