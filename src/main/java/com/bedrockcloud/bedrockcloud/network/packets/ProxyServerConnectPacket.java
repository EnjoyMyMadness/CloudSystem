package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.api.GroupAPI;
import com.bedrockcloud.bedrockcloud.console.Loggable;
import com.bedrockcloud.bedrockcloud.files.json.json;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;
import com.bedrockcloud.bedrockcloud.server.proxy.ProxyServer;
import com.bedrockcloud.bedrockcloud.tasks.KeepALiveTask;
import com.bedrockcloud.bedrockcloud.templates.Template;

import java.io.IOException;
import java.util.HashMap;
import java.net.Socket;

import org.json.simple.JSONObject;

public class ProxyServerConnectPacket extends DataPacket implements Loggable
{
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String serverName = jsonObject.get("serverName").toString();

        this.getLogger().info("Proxy '" + serverName + "' is registered!");

        final ProxyServer proxyServer = BedrockCloud.getProxyServerProvider().getProxyServer(serverName);
        final Object socketPort = jsonObject.get("socketPort");
        try {
            final Socket s = new Socket("127.0.0.1", Integer.parseInt(socketPort.toString()));
            proxyServer.setSocket(s);
            for (final String name : GroupAPI.getGroups()) {
                try {
                    final HashMap<String, Object> stats = (HashMap<String, Object>) json.get(name, 9);
                    if (Integer.parseInt(stats.get("type").toString()) == 1) {
                        final Template group = BedrockCloud.getTemplateProvider().getTemplate(name);
                        if (group != null) {
                            if (!BedrockCloud.getTemplateProvider().isTemplateRunning(group)) {
                                group.start();
                            }
                        }
                    }
                } catch (IOException e) {
                    BedrockCloud.getLogger().exception(e);
                }
            }

            for (final String key : BedrockCloud.getProxyServerProvider().getProxyServerMap().keySet()) {
                final ProxyServer proxy = BedrockCloud.getProxyServerProvider().getProxyServer(key);
                final RegisterServerPacket packet = new RegisterServerPacket();
                for (GameServer server : BedrockCloud.getGameServerProvider().gameServerMap.values()) {
                    if (server != null) {
                        packet.addValue("serverPort", server.getServerPort());
                        packet.addValue("serverName", server.getServerName());
                        proxy.pushPacket(packet);
                    }
                }
            }
        } catch (IOException e2) {
            BedrockCloud.getLogger().exception(e2);
        }
    }
}
