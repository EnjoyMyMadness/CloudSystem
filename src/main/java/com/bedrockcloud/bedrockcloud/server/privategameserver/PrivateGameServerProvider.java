package com.bedrockcloud.bedrockcloud.server.privategameserver;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.config.Config;
import com.bedrockcloud.bedrockcloud.config.FileUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PrivateGameServerProvider
{
    public Map<String, PrivateGameServer> gameServerMap;

    public PrivateGameServerProvider() {
        this.gameServerMap = new HashMap<String, PrivateGameServer>();
    }
    
    public Map<String, PrivateGameServer> getGameServerMap() {
        return this.gameServerMap;
    }
    
    public void addGameServer(final PrivateGameServer gameServer) {
        this.gameServerMap.put(gameServer.getServerName(), gameServer);
    }
    
    public void removeServer(final PrivateGameServer gameServer) {
        this.gameServerMap.remove(gameServer.getServerName());
    }
    
    public void removeServer(final String name) {
        this.gameServerMap.remove(name);
    }
    
    public PrivateGameServer getGameServer(final String name) {
        return this.gameServerMap.get(name);
    }
    
    public boolean existServer(final String name) {
        return this.gameServerMap.get(name) != null;
    }
    
    public int getFreeNumber(final String path) {
        boolean found = false;
        int i = 1;
        while (!found) {
            final File file = new File(path + "-" + i);
            if (!file.exists()) {
                found = true;
                --i;
            }
            ++i;
        }
        return i;
    }
    
    public void copy(final File src, final File dest) {
        try {
            if (src.isDirectory()) {
                if (!dest.exists()) {
                    dest.mkdir();
                }
                final String[] list;
                final String[] files = list = src.list();
                for (final String file : list) {
                    final File srcFile = new File(src, file);
                    final File destFile = new File(dest, file);
                    this.copy(srcFile, destFile);
                }
            } else {
                InputStream in = null;
                try {
                    in = new FileInputStream(src);
                    final OutputStream out = new FileOutputStream(dest);
                    final byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                    in.close();
                    out.close();
                } catch (IOException e) {
                    BedrockCloud.getLogger().exception(e);
                }
            }
        } catch (Exception ignored) {}
    }
    
    public void createProperties(final PrivateGameServer gameServer) {
        final String serverName = gameServer.getServerName();
        final int port = gameServer.getServerPort();
        final StringBuilder sb = new StringBuilder();
        Objects.requireNonNull(gameServer);
        final String filePath = sb.append("./temp/").append(serverName).append("/server.properties").toString();
        Config prop = new Config(filePath, Config.PROPERTIES);
        try {
            prop.set("server-port", Integer.toString(port));
            prop.set("language", "deu");
            prop.set("motd", serverName);
            prop.set("white-list", "false");
            prop.set("announce-player-achievements", "off");
            prop.set("spawn-protection", "0");
            prop.set("max-players", Integer.toString(gameServer.getTemplate().getMaxPlayers()));
            prop.set("online-players", "0");
            prop.set("gamemode", "0");
            prop.set("force-gamemode", "off");
            prop.set("hardcore", "off");
            prop.set("pvp", "on");
            prop.set("difficulty", "1");
            prop.set("enable-query", "on");
            prop.set("enable-rcon", "off");
            prop.set("rcon.password", "gayorso");
            prop.set("auto-save", "off");
            prop.set("view-distance", "8");
            prop.set("xbox-auth", "off");
            prop.set("enable-ipv6", "off");
            prop.set("template", gameServer.getTemplate().getName());
            prop.set("cloud-port", String.valueOf(BedrockCloud.getConfig().getDouble("port")));
            prop.set("cloud-password", BedrockCloud.getConfig().getString("password"));
            prop.set("cloud-path", BedrockCloud.getCloudPath());
            prop.set("pserver-owner", gameServer.getServerOwner());
            prop.save();
        } catch (Throwable ignored) {}
    }
    
    public void deleteServer(final File file, final String serverName) {
        /*try {
            Thread.sleep(500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            final File Crashfile = new File("./temp/" + serverName + "/crashdumps/");
            final File dest_lib = new File("./archive/crashdumps/" + serverName + "/");
            dest_lib.mkdirs();
            FileUtils.copyFile(Crashfile, dest_lib);
            //StimoCloud.getGameServerProvider().copy(Crashfile, dest_lib);
            if (file.isDirectory()) {
                final String[] fileList = file.list();
                if (fileList.length == 0) {
                    file.delete();
                } else {
                    for (final String fileName : fileList) {
                        final String fullPath = file.getPath() + "/" + fileName;
                        final File fileOrFolder = new File(fullPath);
                        this.delete(fileOrFolder);
                    }
                    this.delete(file);
                }
            } else {
                file.delete();
            }
        } catch (NullPointerException | IOException ignored){}*/

        try {
            final File Crashfile = new File("./temp/" + serverName + "/crashdumps/");
            final File dest_lib = new File("./archive/crashdumps/" + serverName + "/");
            dest_lib.mkdirs();
            BedrockCloud.getGameServerProvider().copy(Crashfile, dest_lib);
            FileUtils.copyFile(Crashfile, dest_lib);
        } catch (IOException e) {
            //e.printStackTrace();
        }

        try {
            if (file.isDirectory()) {
                final String[] fileList = file.list();
                if (fileList.length == 0) {
                    file.delete();
                } else {
                    for (final String fileName : fileList) {
                        final String fullPath = file.getPath() + "/" + fileName;
                        final File fileOrFolder = new File(fullPath);
                        this.delete(fileOrFolder);
                    }
                    this.delete(file);
                }
            } else {
                file.delete();
            }
        } catch (NullPointerException ignored){}
    }
    
    public void delete(final File file) {
        try {
            if (file.isDirectory()) {
                final String[] fileList = file.list();
                if (fileList.length == 0) {
                    file.delete();
                } else {
                    for (final String fileName : fileList) {
                        final String fullPath = file.getPath() + "/" + fileName;
                        final File fileOrFolder = new File(fullPath);
                        this.delete(fileOrFolder);
                    }
                    this.delete(file);
                }
            } else {
                file.delete();
            }
        } catch (NullPointerException ignored){}
    }
}
