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
    
    public void start() {
        while (true) {
            try {
                final Socket socket = this.serverSocket.accept();
                try {
                    socket.setTcpNoDelay(true);
                    socket.setKeepAlive(true);
                } catch (SocketException e){
                    BedrockCloud.getLogger().exception(e);
                }
                if (socket.isConnected() && !socket.isClosed()) {
                    ClientRequest request = new ClientRequest(socket);
                    request.start();
                }
            } catch (IOException e) {
                BedrockCloud.getLogger().exception(e);
            }
        }
    }
}
