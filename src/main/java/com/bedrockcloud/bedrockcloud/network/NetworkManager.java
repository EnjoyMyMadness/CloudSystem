package com.bedrockcloud.bedrockcloud.network;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;

import java.io.IOException;
import java.util.HashMap;
import java.net.Socket;
import java.util.Map;
import java.net.ServerSocket;
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
    
    public void start() {
        while (true) {
            try {
                final Socket socket = this.serverSocket.accept();
                if (socket.isConnected()) {
                    ClientRequest request = new ClientRequest(socket);
                    request.setDaemon(true);
                    request.start();
                }
            } catch (IOException e) {
                BedrockCloud.getLogger().exception(e);
            }
        }
    }
}
