package com.bedrockcloud.bedrockcloud.player;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.server.proxy.ProxyServer;

public class CloudPlayer
{
    private final String playerName;
    private final String address;
    private final String uuid;
    private String currentServer;
    private final String xuid;
    private final String currentProxy;
    
    public CloudPlayer(final String playerName, final String address, final String uuid, final String xuid, final String currentServer, final String currentProxy) {
        this.playerName = playerName;
        this.address = address;
        this.uuid = uuid;
        this.xuid = xuid;
        this.currentServer = currentServer;
        this.currentProxy = currentProxy;
    }
    
    public String getPlayerName() {
        return this.playerName;
    }
    
    public String getAddress() {
        return this.address;
    }
    
    public String getCurrentProxy() {
        return this.currentProxy;
    }
    
    public String getCurrentServer() {
        return this.currentServer;
    }
    
    public String getUuid() {
        return this.uuid;
    }
    
    public String getXuid() {
        return this.xuid;
    }
    
    public void setCurrentServer(final String serverName) {
        this.currentServer = serverName;
    }
    
    public ProxyServer getProxy() {
        return BedrockCloud.getProxyServerProvider().getProxyServer(this.getCurrentProxy());
    }
    
    @Override
    public String toString() {
        return "CloudPlayer{playerName='" + this.playerName + '\'' + ", address='" + this.address + '\'' + ", uuid='" + this.uuid + '\'' + ", currentServer='" + this.currentServer + '\'' + ", xuid='" + this.xuid + '\'' + ", currentProxy='" + this.currentProxy + '\'' + '}';
    }
}
