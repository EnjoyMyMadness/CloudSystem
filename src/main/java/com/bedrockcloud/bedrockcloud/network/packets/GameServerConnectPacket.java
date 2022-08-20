package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;
import com.bedrockcloud.bedrockcloud.api.MessageAPI;
import com.bedrockcloud.bedrockcloud.server.proxy.ProxyServer;
import org.json.simple.JSONObject;

public class GameServerConnectPacket extends DataPacket
{
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String serverName = jsonObject.get("serverName").toString();
        final String serverPort = jsonObject.get("serverPort").toString();
        final String serverPid = jsonObject.get("serverPid").toString();
        final GameServer gameServer = BedrockCloud.getGameServerProvider().getGameServer(serverName);
        gameServer.setSocket(clientRequest.getSocket());
        gameServer.pid = Integer.parseInt(serverPid);
        gameServer.setAliveChecks(0);
        final VersionInfoPacket versionInfoPacket = new VersionInfoPacket();
        gameServer.pushPacket(versionInfoPacket);
        BedrockCloud.getLogger().info("Server " + serverName + " started successful");
        for (final String key : BedrockCloud.getProxyServerProvider().getProxyServerMap().keySet()) {
            final ProxyServer proxy = BedrockCloud.getProxyServerProvider().getProxyServer(key);
            final RegisterServerPacket packet = new RegisterServerPacket();
            packet.addValue("serverPort", serverPort);
            packet.addValue("serverName", serverName);
            proxy.pushPacket(packet);
        }

        String notifyMessage = MessageAPI.startedMessage.replace("%service", serverName);
        BedrockCloud.sendNotifyCloud(notifyMessage);

        gameServer.getTemplate().addServer(gameServer.getTemplate(), serverName);
    }
}