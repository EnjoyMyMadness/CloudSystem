package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.server.port.PortValidator;
import com.bedrockcloud.bedrockcloud.server.proxy.ProxyServer;
import com.bedrockcloud.bedrockcloud.templates.Template;

import java.io.File;

import org.json.simple.JSONObject;

public class ProxyServerDisconnectPacket extends DataPacket
{
    @Override
    public String getPacketName() {
        return "ProxyServerDisconnectPacket";
    }
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String serverName = jsonObject.get("serverName").toString();
        final ProxyServer proxyServer = BedrockCloud.getProxyServerProvider().getProxyServer(serverName);
        if (proxyServer != null) {
            final Template template = proxyServer.getTemplate();

            BedrockCloud.getProxyServerProvider().removeServer(serverName);
            BedrockCloud.getProxyServerProvider().deleteServer(new File("./temp/" + serverName), serverName);
            proxyServer.stopServer();
            if (BedrockCloud.getTemplateProvider().isTemplateRunning(template)) {
                if (template.getRunningTemplateServers().size() < template.getMinRunningServer()) {
                    new ProxyServer(template);
                }
            }
        }
    }
}
