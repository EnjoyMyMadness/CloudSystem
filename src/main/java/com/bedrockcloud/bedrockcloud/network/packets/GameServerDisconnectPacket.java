package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;
import com.bedrockcloud.bedrockcloud.server.port.PortValidator;
import com.bedrockcloud.bedrockcloud.api.MessageAPI;
import com.bedrockcloud.bedrockcloud.server.privategameserver.PrivateGameServer;
import com.bedrockcloud.bedrockcloud.server.proxy.ProxyServer;
import com.bedrockcloud.bedrockcloud.templates.Template;

import java.io.File;
import java.util.Objects;

import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import org.json.simple.JSONObject;
import com.bedrockcloud.bedrockcloud.network.DataPacket;

public class GameServerDisconnectPacket extends DataPacket
{
    @Override
    public String getPacketName() {
        return "GameServerDisconnectPacket";
    }
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String serverName = jsonObject.get("serverName").toString();
        final boolean isPrivate = Boolean.parseBoolean(jsonObject.get("isPrivate").toString());

        if (!isPrivate) {
            final GameServer gameServer = BedrockCloud.getGameServerProvider().getGameServer(serverName);
            gameServer.setAliveChecks(0);
            final Template template = gameServer.getTemplate();
            for (final String key : BedrockCloud.getProxyServerProvider().getProxyServerMap().keySet()) {
                final ProxyServer proxy = BedrockCloud.getProxyServerProvider().getProxyServer(key);
                final UnregisterServerPacket packet = new UnregisterServerPacket();
                packet.addValue("serverName", serverName);
                proxy.pushPacket(packet);
            }

            String notifyMessage = MessageAPI.stoppedMessage.replace("%service", serverName);
            BedrockCloud.sendNotifyCloud(notifyMessage);
            BedrockCloud.getLogger().warning(notifyMessage);

            final ProcessBuilder builder = new ProcessBuilder(new String[0]);
            try {
                builder.command("/bin/sh", "-c", "screen -X -S " + serverName + " kill").directory(new File("/root")).start();
            } catch (Exception ignored) {
            }
            try {
                builder.command("/bin/sh", "-c", "kill " + gameServer.pid).directory(new File("/root")).start();
            } catch (Exception ignored) {
            }

            gameServer.getTemplate().removeServer(gameServer.getServerName());
            BedrockCloud.getGameServerProvider().deleteServer(new File("./temp/" + serverName), serverName);

            if (BedrockCloud.getTemplateProvider().isTemplateRunning(template)) {
                final Template tmp = template;
                int b = 0;
                for (final String servername : BedrockCloud.getGameServerProvider().gameServerMap.keySet()) {
                    if (Objects.equals(BedrockCloud.getGameServerProvider().getGameServer(servername).getTemplate().getName(), template.getName())) {
                        ++b;
                    }
                }
                int minRunning = 0;
                minRunning = tmp.getMinRunningServer();
                for (int i = b; i < minRunning; ++i) {
                    new GameServer(template);
                }
            }
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            final PrivateGameServer gameServer = BedrockCloud.getPrivateGameServerProvider().getGameServer(serverName);
            gameServer.setAliveChecks(0);
            final Template template = gameServer.getTemplate();
            for (final String key : BedrockCloud.getProxyServerProvider().getProxyServerMap().keySet()) {
                final ProxyServer proxy = BedrockCloud.getProxyServerProvider().getProxyServer(key);
                final UnregisterServerPacket packet = new UnregisterServerPacket();
                packet.addValue("serverName", serverName);
                proxy.pushPacket(packet);
            }

            String notifyMessage = MessageAPI.stoppedMessage.replace("%service", serverName);
            BedrockCloud.sendNotifyCloud(notifyMessage);
            BedrockCloud.getLogger().warning(notifyMessage);

            final ProcessBuilder builder = new ProcessBuilder(new String[0]);
            try {
                builder.command("/bin/sh", "-c", "screen -X -S " + serverName + " kill").directory(new File("/root")).start();
            } catch (Exception ignored) {
            }
            try {
                builder.command("/bin/sh", "-c", "kill " + gameServer.pid).directory(new File("/root")).start();
            } catch (Exception ignored) {
            }

            gameServer.getTemplate().removeServer(gameServer.getServerName());
            BedrockCloud.getPrivateGameServerProvider().deleteServer(new File("./temp/" + serverName), serverName);

            if (BedrockCloud.getTemplateProvider().isTemplateRunning(template)) {
                final Template tmp = template;
                int b = 0;
                for (final String servername : BedrockCloud.getPrivateGameServerProvider().gameServerMap.keySet()) {
                    if (Objects.equals(BedrockCloud.getPrivateGameServerProvider().getGameServer(servername).getTemplate().getName(), template.getName())) {
                        ++b;
                    }
                }
                int minRunning = 0;
                minRunning = tmp.getMinRunningServer();
                for (int i = b; i < minRunning; ++i) {
                    new GameServer(template);
                }
            }
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
