package com.bedrockcloud.bedrockcloud.network;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

import com.bedrockcloud.bedrockcloud.console.Loggable;

public class NetworkManager implements Loggable
{
    public ServerSocket serverSocket;
    public Map<String, Socket> channelList;
    
    public NetworkManager(final int port) {
        try {
            this.getLogger().info("Connection on 127.0.0.1:" + port + " established successfully");
            this.serverSocket = new ServerSocket(port);
            this.channelList = new HashMap<String, Socket>();
        } catch (IOException e) {
            BedrockCloud.getLogger().exception(e);
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void start() {
        while (true) {
            if (!(this.serverSocket == null) && !this.serverSocket.isClosed()) {
                try {
                    Socket clientSocket = this.serverSocket.accept();
                    try {
                        clientSocket.setTcpNoDelay(true);
                        clientSocket.setKeepAlive(true);
                    } catch (SocketException e) {
                        BedrockCloud.getLogger().exception(e);
                    }
                    if (clientSocket.isConnected() && !clientSocket.isClosed()) {
                        ClientRequest request = new ClientRequest(clientSocket);
                        request.start();
                    }
                } catch (IOException e) {
                    BedrockCloud.getLogger().exception(e);
                }
            } else {
                BedrockCloud.getLogger().warning("§cServerSocket is null or closed.");
            }
        }
    }
}
