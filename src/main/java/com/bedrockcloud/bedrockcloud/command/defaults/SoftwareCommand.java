package com.bedrockcloud.bedrockcloud.command.defaults;

import com.bedrockcloud.bedrockcloud.SoftwareManager;
import com.bedrockcloud.bedrockcloud.console.Loggable;
import com.bedrockcloud.bedrockcloud.command.Command;

public class SoftwareCommand extends Command implements Loggable
{
    public SoftwareCommand() {
        super("software");
    }
    
    @Override
    protected void onCommand(final String[] args) {
        if (args.length != 0) {
            final String s = args[0];
            switch (s) {
                case "all": {
                    this.getLogger().debug("Check the server versions...");
                    this.getLogger().info("Downloading all required Server-Softwares..");
                    if (SoftwareManager.download("https://github.com/pmmp/PocketMine-MP/releases/latest/download/PocketMine-MP.phar", "./local/versions/pocketmine/PocketMine-MP.phar")) {
                        this.getLogger().info("Pocketmine Downloaded");
                    } else {
                        this.getLogger().error("Error, please check the URL!");
                    }
                    if (SoftwareManager.download("https://jenkins.waterdog.dev/job/Waterdog/job/WaterdogPE/job/master/lastSuccessfulBuild/artifact/target/Waterdog.jar", "./local/versions/waterdogpe/WaterdogPE.jar")) {
                        this.getLogger().info("WaterdogPE.jar Downloaded");
                        break;
                    }
                    break;
                }
                case "pocketmine": {
                    if (args.length != 1) {
                        this.getLogger().info("Check the server versions...");
                        this.getLogger().info("Downloading Pocketmine-MP...");
                        final String versionId = args[1];
                        if (SoftwareManager.download("https://github.com/pmmp/PocketMine-MP/releases/download/" + versionId + "/PocketMine-MP.phar", "./local/versions/pocketmine/PocketMine-MP.phar")) {
                            this.getLogger().info("PocketMine-MP.phar Downloaded");
                        } else {
                            this.getLogger().error("Download Server Offline!");
                        }
                        break;
                    }
                    break;
                }
                case "waterdogpe": {
                    this.getLogger().info("Check the server versions...");
                    this.getLogger().info("Downloading WaterdogPE...");
                    if (SoftwareManager.download("https://jenkins.waterdog.dev/job/Waterdog/job/WaterdogPE/job/master/lastSuccessfulBuild/artifact/target/Waterdog.jar", "./local/versions/waterdogpe/WaterdogPE.jar")) {
                        this.getLogger().info("WaterdogPE Downloaded");
                        break;
                    } else {
                        this.getLogger().error("Download Server Offline!");
                    }
                    break;
                }
            }
        }
    }
}
