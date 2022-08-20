package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.network.DataPacket;

public class NodeServerMessagePacket extends DataPacket
{
    public String message;
    
    @Override
    public String getPacketName() {
        return "NodeServerMessagePacket";
    }
    
    @Override
    public String encode() {
        this.addValue("message", this.message);
        return super.encode();
    }
}
