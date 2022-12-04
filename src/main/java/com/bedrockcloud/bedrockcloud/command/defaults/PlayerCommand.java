package com.bedrockcloud.bedrockcloud.command.defaults;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.player.CloudPlayer;

import java.util.Objects;
import com.bedrockcloud.bedrockcloud.network.packets.PlayerTextPacket;
import com.bedrockcloud.bedrockcloud.command.Command;

public class PlayerCommand extends Command
{
    public PlayerCommand() {
        super("player");
    }
    
    @Override
    protected void onCommand(final String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("list")) {
                this.getLogger().info("§e»§r §7There are currently " + BedrockCloud.getCloudPlayerProvider().cloudPlayerMap.size() + " Player connected! §e«");
                for (final CloudPlayer cloudPlayer : BedrockCloud.getCloudPlayerProvider().cloudPlayerMap.values()) {
                    this.getLogger().info("§c➤ §rPlayer: " + cloudPlayer.getPlayerName() + " (" + cloudPlayer.getAddress() + ") | (" + cloudPlayer.getXuid() + ") ᐅ " + cloudPlayer.getCurrentServer());
                }
            } else if (args[0].equalsIgnoreCase("message") && args.length != 2) {
                final String value = args[1];
                final String playerName = args[2];
                if (BedrockCloud.getCloudPlayerProvider().existsPlayer(playerName)) {
                    final PlayerTextPacket playerTextPacket = new PlayerTextPacket();
                    playerTextPacket.playerName = playerName;
                    Objects.requireNonNull(playerTextPacket);
                    playerTextPacket.type = 0;
                    playerTextPacket.value = value;
                    BedrockCloud.getCloudPlayerProvider().getCloudPlayer(playerName).getProxy().pushPacket(playerTextPacket);
                }
            }
        }
    }
}
