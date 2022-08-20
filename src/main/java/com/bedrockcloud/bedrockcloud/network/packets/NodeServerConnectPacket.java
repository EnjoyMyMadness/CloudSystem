package com.bedrockcloud.bedrockcloud.network.packets;

import java.io.IOException;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;

import java.net.Socket;

import org.json.simple.JSONObject;
import com.bedrockcloud.bedrockcloud.console.Loggable;

public class NodeServerConnectPacket extends DataPacket implements Loggable
{
    @Override
    public String getPacketName() {
        return "NodeServerConnectPacket";
    }
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        this.getLogger().info("§4StimoNode-1 has connected to the cloud");
        try {
            final Integer socketPort = (Integer) jsonObject.get("socketPort");
            final Socket s = new Socket("127.0.0.1", socketPort);
            BedrockCloud.setSocket(s);
        }
        catch (IOException e) {
            BedrockCloud.getLogger().exception(e);
        }
    }
}
