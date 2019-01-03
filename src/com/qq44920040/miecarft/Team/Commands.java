/*
 * Decompiled with CFR 0.138.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Player
 */
package com.qq44920040.miecarft.Team;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands
implements CommandExecutor {
    private TeamMain instance = TeamMain.getInstance();
    private Map<UUID, String> inviteMap = new HashMap<UUID, String>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("team")) {
            Player p = (Player)sender;
            if (args.length == 0) {
                p.sendMessage("\u00a7c\u00a7l\u521b\u5efa\u961f\u4f0d: \u00a7a/team create <\u961f\u4f0d\u540d\u5b57>");
                p.sendMessage("\u00a7c\u00a7l\u7533\u8bf7\u52a0\u5165\u961f\u4f0d: \u00a7a/team join <\u961f\u4f0d\u540d\u5b57>");
                p.sendMessage("\u00a7c\u00a7l\u9000\u51fa\u961f\u4f0d: \u00a7a/team quit");
                p.sendMessage("\u00a7c\u00a7l\u8f6c\u8ba9\u961f\u957f: \u00a7a/team owner <\u961f\u5458\u540d\u5b57>");
                p.sendMessage("\u00a7c\u00a7l\u6253\u5f00\u52a0\u5165\u8bf7\u6c42\u83dc\u5355: \u00a7a/team request");
                p.sendMessage("\u00a7c\u00a7l\u8e22\u51fa\u961f\u5458: \u00a7a/team kick <\u961f\u5458\u540d\u5b57>");
                p.sendMessage("\u00a7c\u00a7l\u89e3\u6563\u961f\u4f0d: \u00a7a/team delete");
                p.sendMessage("\u00a7c\u00a7l\u7ed9\u6240\u6709\u961f\u5458\u53d1\u9001\u6d88\u606f: \u00a7a/team mess <\u6d88\u606f>");
                p.sendMessage("\u00a7c\u00a7l\u9080\u8bf7\u73a9\u5bb6\u8fdb\u5165\u961f\u4f0d: \u00a7a/team invite <\u73a9\u5bb6>");
                p.sendMessage("\u00a7c\u00a7l\u540c\u610f\u5165\u961f\u9080\u8bf7: \u00a7a/team accept");
                p.sendMessage("\u00a7c\u00a7l\u67e5\u8be2\u6211\u6240\u5728\u7684\u961f\u4f0d\u7684\u4fe1\u606f: \u00a7a/team me");
                p.sendMessage("\u00a7c\u00a7l\u67e5\u770b\u961f\u4f0d\u5217\u8868: \u00a7a/team list");
                if (p.isOp()) {
                    p.sendMessage("\u00a7c\u00a7l\u91cd\u8f7d\u914d\u7f6e\u6587\u4ef6: \u00a7a/team reload");
                }
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("quit")) {
                    if (this.instance.isInTeam(p)) {
                        if (this.instance.getTeamOwner(this.instance.getTeam(p)).equals(p.getName())) {
                            p.sendMessage("\u00a7c\u961f\u957f\u65e0\u6cd5\u76f4\u63a5\u9000\u51fa\u961f\u4f0d,\u4f46\u53ef\u4ee5\u89e3\u6563\u961f\u4f0d");
                            return false;
                        }
                        List<UUID> players = this.instance.listTeamPlayers(p);
                        for (UUID uuid : players) {
                            Player tar = Bukkit.getPlayer((UUID)uuid);
                            tar.sendMessage("\u00a7e" + p.getName() + "\u00a7c\u9000\u51fa\u4e86\u961f\u4f0d");
                        }
                        this.instance.quitTeam(p);
                    } else {
                        p.sendMessage("\u00a7c\u4f60\u6ca1\u6709\u52a0\u5165\u4e00\u4e2a\u961f\u4f0d");
                        p.teleport(Bukkit.getWorld("zhucheng").getSpawnLocation());
                    }
                } else if (args[0].equalsIgnoreCase("delete")) {
                    if (this.instance.isInTeam(p)) {
                        if (!this.instance.getTeamOwner(this.instance.getTeam(p)).equals(p.getName())) {
                            p.sendMessage("\u00a7c\u53ea\u6709\u961f\u957f\u53ef\u4ee5\u89e3\u6563\u961f\u4f0d");
                            return false;
                        }
                        List<UUID> players = this.instance.listTeamPlayers(p);
                        for (UUID uuid : players) {
                            Player tar = Bukkit.getPlayer((UUID)uuid);
                            if (tar == null || !tar.isOnline()) continue;
                            tar.sendMessage("\u00a7c\u961f\u4f0d\u88ab\u961f\u957f\u89e3\u6563\u4e86");
                        }
                        this.instance.deleteTeam(this.instance.getTeam(p));
                    } else {
                        p.sendMessage("\u00a7c\u4f60\u6ca1\u6709\u52a0\u5165\u4e00\u4e2a\u961f\u4f0d");
                    }
                } else if (args[0].equalsIgnoreCase("me")) {
                    if (this.instance.isInTeam(p)) {
                        p.sendMessage("\u00a7a\u4f60\u6240\u5728\u7684\u961f\u4f0d\u540d\u5b57\u662f:\u00a7e " + this.instance.getTeam(p));
                        List<UUID> players = this.instance.listTeamPlayers(p);
                        p.sendMessage("\u00a7a\u961f\u5458\u5217\u8868:\u00a7e(\u00a77" + players.size() + "\u00a7e)");
                        for (UUID uuid : players) {
                            Player tar = Bukkit.getPlayer((UUID)uuid);
                            p.sendMessage("\u00a77 - \u00a7e" + tar.getName());
                        }
                    } else {
                        p.sendMessage("\u00a7c\u4f60\u5e76\u6ca1\u6709\u52a0\u5165\u4e00\u4e2a\u961f\u4f0d");
                    }
                } else if (args[0].equalsIgnoreCase("list")) {
                    Inventorys.teamsInv(p, 1);
                } else if (args[0].equalsIgnoreCase("reload") && p.isOp()) {
                    this.instance.reloadConfig();
                    p.sendMessage("\u00a7a\u914d\u7f6e\u6587\u4ef6\u5df2\u91cd\u8f7d");
                } else if (args[0].equalsIgnoreCase("request")) {
                    if (this.instance.isInTeam(p)) {
                        if (this.instance.getTeamOwner(this.instance.getTeam(p)).equals(p.getName())) {
                            Inventorys.requestInv(p, 1);
                        } else {
                            p.sendMessage("\u00a7c\u53ea\u6709\u961f\u957f\u624d\u53ef\u4ee5\u6253\u5f00");
                        }
                    } else {
                        p.sendMessage("\u00a7c\u4f60\u5e76\u6ca1\u6709\u52a0\u5165\u4e00\u4e2a\u961f\u4f0d");
                    }
                } else if (args[0].equalsIgnoreCase("accept")) {
                    if (this.inviteMap.containsKey(p.getUniqueId())) {
                        String team = this.inviteMap.get(p.getUniqueId());
                        if (!this.instance.hasTeam(team)) {
                            p.sendMessage("\u00a7c\u961f\u4f0d\u4e0d\u5b58\u5728");
                            return false;
                        }
                        if (this.instance.isInTeam(p)) {
                            p.sendMessage("\u00a7c\u4f60\u5df2\u7ecf\u5728\u4e00\u4e2a\u961f\u4f0d\u4e86");
                            return false;
                        }
                        if (this.instance.listTeamPlayers(team).size() >= this.instance.getConfig().getInt("MaxCount")) {
                            p.sendMessage("\u00a7c\u961f\u4f0d\u4eba\u6570\u8fbe\u5230\u4e0a\u9650\u4e86");
                            return false;
                        }
                        this.instance.joinTeam(p, team);
                        List<UUID> players = this.instance.listTeamPlayers(p);
                        for (UUID uuid : players) {
                            Player tar = Bukkit.getPlayer((UUID)uuid);
                            if (tar == null || !tar.isOnline()) continue;
                            tar.sendMessage("\u00a7e" + p.getName() + "\u00a7a\u88ab\u9080\u8bf7\u8fdb\u4e86\u961f\u4f0d");
                        }
                    } else {
                        p.sendMessage("\u00a7c\u4f60\u5e76\u6ca1\u6709\u6536\u5230\u5165\u961f\u9080\u8bf7");
                    }
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("create")) {
                    if (this.instance.isInTeam(p)) {
                        p.sendMessage("\u00a7c\u4f60\u5df2\u7ecf\u5728\u4e00\u4e2a\u961f\u4f0d\u4e86");
                        return false;
                    }
                    String name = args[1];
                    if (name.length() > 10 || name.length() < 2) {
                        p.sendMessage("\u00a7c\u961f\u4f0d\u540d\u79f0\u957f\u5ea6\u5fc5\u987b\u57282-10\u4e4b\u95f4");
                        return false;
                    }
                    if (this.instance.isTeamExist(name)) {
                        p.sendMessage("\u00a7c\u540d\u4e3a\u00a7e" + name + "\u00a7a\u7684\u961f\u4f0d\u5df2\u7ecf\u5b58\u5728\u4e86");
                        return false;
                    }
                    this.instance.createTeam(name, p);
                    p.sendMessage("\u00a7a\u961f\u4f0d\u00a7e" + name + "\u00a7a\u521b\u5efa\u6210\u529f");
                } else if (args[0].equalsIgnoreCase("join")) {
                    String name = args[1];
                    if (this.instance.isInTeam(p)) {
                        p.sendMessage("\u00a7c\u4f60\u5df2\u7ecf\u52a0\u5165\u4e00\u4e2a\u961f\u4f0d\u4e86");
                        return false;
                    }
                    if (!this.instance.hasTeam(name)) {
                        p.sendMessage("\u00a7c\u6ca1\u6709\u8fd9\u4e2a\u961f\u4f0d");
                        return false;
                    }
                    if (this.instance.isRequested(name, p)) {
                        p.sendMessage("\u00a7c\u4f60\u5df2\u7ecf\u53d1\u9001\u8fc7\u8bf7\u6c42\u4e86,\u8bf7\u8010\u5fc3\u7b49\u5f85");
                        return false;
                    }
                    this.instance.sendJoinReQuest(name, p);
                    String owner = this.instance.getTeamOwner(name);
                    Player tar = Bukkit.getPlayerExact((String)owner);
                    if (tar != null && tar.isOnline()) {
                        tar.sendMessage("\u00a7e" + p.getName() + "\u00a7a\u7533\u8bf7\u52a0\u5165\u961f\u4f0d,\u8f93\u5165\u00a7e/team request\u00a7a\u6765\u6279\u51c6");
                    }
                    p.sendMessage("\u00a7a\u8bf7\u6c42\u53d1\u9001\u6210\u529f");
                } else if (args[0].equalsIgnoreCase("owner")) {
                    if (!this.instance.isInTeam(p)) {
                        p.sendMessage("\u00a7c\u4f60\u5e76\u6ca1\u6709\u52a0\u5165\u4e00\u4e2a\u961f\u4f0d");
                        return false;
                    }
                    if (!this.instance.getTeamOwner(this.instance.getTeam(p)).equals(p.getName())) {
                        p.sendMessage("\u00a7c\u4f60\u5e76\u4e0d\u662f\u961f\u957f");
                        return false;
                    }
                    String name = args[1];
                    Player tar = Bukkit.getPlayerExact((String)name);
                    if (tar != null && tar.isOnline()) {
                        if (!this.instance.isInSameTeam(p, tar)) {
                            p.sendMessage("\u00a7e" + name + "\u00a7c\u548c\u4f60\u4e0d\u662f\u5728\u540c\u4e00\u4e2a\u961f\u4f0d");
                            return false;
                        }
                        this.instance.setTeamOwner(this.instance.getTeam(p), tar);
                        p.sendMessage("\u00a7a\u961f\u957f\u8f6c\u8ba9\u6210\u529f");
                        tar.sendMessage("\u00a7e" + p.getName() + "\u00a7a\u5c06\u961f\u957f\u8f6c\u8ba9\u7ed9\u4e86\u4f60");
                    } else {
                        p.sendMessage("\u00a7e" + name + "\u00a7c\u4e0d\u5728\u7ebf");
                    }
                } else if (args[0].equalsIgnoreCase("kick")) {
                    if (!this.instance.isInTeam(p)) {
                        p.sendMessage("\u00a7c\u4f60\u5e76\u6ca1\u6709\u52a0\u5165\u4e00\u4e2a\u961f\u4f0d");
                        return false;
                    }
                    if (!this.instance.getTeamOwner(this.instance.getTeam(p)).equals(p.getName())) {
                        p.sendMessage("\u00a7c\u4f60\u5e76\u4e0d\u662f\u961f\u957f");
                        return false;
                    }
                    String name = args[1];
                    Player tar = Bukkit.getPlayerExact((String)name);
                    if (tar != null && tar.isOnline()) {
                        if (!this.instance.isInSameTeam(p, tar)) {
                            p.sendMessage("\u00a7e" + name + "\u00a7c\u548c\u4f60\u4e0d\u662f\u5728\u540c\u4e00\u4e2a\u961f\u4f0d");
                            return false;
                        }
                        this.instance.quitTeam(tar);
                        p.sendMessage("\u00a7c\u8e22\u51fa\u6210\u529f");
                        tar.sendMessage("\u00a7c\u4f60\u88ab\u8e22\u51fa\u4e86\u961f\u4f0d");
                    } else {
                        p.sendMessage("\u00a7e" + name + "\u00a7c\u4e0d\u5728\u7ebf");
                    }
                } else if (args[0].equalsIgnoreCase("mess")) {
                    if (!this.instance.isInTeam(p)) {
                        p.sendMessage("\u00a7c\u4f60\u5e76\u6ca1\u6709\u52a0\u5165\u4e00\u4e2a\u961f\u4f0d");
                        return false;
                    }
                    String mess = args[1];
                    List<UUID> players = this.instance.listTeamPlayers(p);
                    for (UUID uuid : players) {
                        Player tar = Bukkit.getPlayer((UUID)uuid);
                        tar.sendMessage("\u00a7a\u961f\u4f0d\u6d88\u606f>> \u00a7a" + p.getName() + ": \u00a7e" + mess);
                    }
                } else if (args[0].equalsIgnoreCase("invite")) {
                    if (!this.instance.isInTeam(p)) {
                        p.sendMessage("\u00a7c\u4f60\u5e76\u6ca1\u6709\u52a0\u5165\u4e00\u4e2a\u961f\u4f0d");
                        return false;
                    }
                    String name = args[1];
                    Player tar = Bukkit.getPlayerExact((String)name);
                    if (tar != null && tar.isOnline()) {
                        if (this.instance.isInTeam(tar)) {
                            p.sendMessage("\u00a7e" + name + "\u00a7c\u5df2\u7ecf\u5728\u4e00\u4e2a\u961f\u4f0d\u5185\u4e86");
                            return false;
                        }
                        if (this.instance.listTeamPlayers(p).size() >= this.instance.getConfig().getInt("MaxCount")) {
                            p.sendMessage("\u00a7c\u961f\u4f0d\u4eba\u6570\u8fbe\u5230\u4e0a\u9650\u4e86");
                            return false;
                        }
                        tar.sendMessage("\u00a7e" + p.getName() + "\u00a7a\u9080\u8bf7\u4f60\u8fdb\u5165\u4ed6\u7684\u961f\u4f0d");
                        tar.sendMessage("\u00a7a\u8f93\u5165\u00a7e/team accept\u00a7a\u6765\u540c\u610f");
                        p.sendMessage("\u00a7a\u9080\u8bf7\u6210\u529f");
                        this.inviteMap.put(tar.getUniqueId(), this.instance.getTeam(p));
                    } else {
                        p.sendMessage("\u00a7e" + name + "\u00a7c\u4e0d\u5728\u7ebf");
                    }
                }
            }
        }
        return false;
    }
}

