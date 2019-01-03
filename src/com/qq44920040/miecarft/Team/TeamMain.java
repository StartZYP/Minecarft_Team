/*
 * Decompiled with CFR 0.138.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.PluginCommand
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package com.qq44920040.miecarft.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TeamMain
extends JavaPlugin {
    private static TeamMain instance;
    private Map<String, List<UUID>> teams;
    private Map<String, UUID> teamOwner;
    private Map<String, List<UUID>> req;

    public void onEnable() {
        this.saveDefaultConfig();
        instance = this;
        this.teams = new HashMap<String, List<UUID>>();
        this.teamOwner = new HashMap<String, UUID>();
        this.req = new HashMap<String, List<UUID>>();
        this.getCommand("team").setExecutor((CommandExecutor)new Commands());
        Bukkit.getPluginManager().registerEvents((Listener)new Listeners(), (Plugin)this);
        this.task();
    }

    public static TeamMain getInstance() {
        return instance;
    }

    public boolean hasTeam(String team) {
        return this.teams.keySet().contains(team);
    }

    public List<UUID> listTeamPlayers(Player p) {
        if (this.isInTeam(p)) {
            String team = this.getTeam(p);
            return this.teams.get(team);
        }
        return null;
    }

    public List<UUID> listTeamPlayers(String team) {
        return this.teams.get(team);
    }

    public void setTeamOwner(String team, Player p) {
        this.teamOwner.put(team, p.getUniqueId());
    }

    public String getTeamOwner(String team) {
        UUID uuid = this.teamOwner.get(team);
        return uuid != null ? Bukkit.getOfflinePlayer((UUID)uuid).getName() : null;
    }

    public boolean isTeamExist(String team) {
        return this.teams.containsKey(team);
    }

    public List<String> listTeams() {
        Set<String> key = this.teams.keySet();
        return key == null ? null : new ArrayList<String>(key);
    }

    public String getTeam(Player p) {
        for (String next : this.teams.keySet()) {
            List<UUID> list = this.teams.get(next);
            if (!list.contains(p.getUniqueId())) continue;
            return next;
        }
        return null;
    }

    public boolean isInTeam(Player p) {
        return this.getTeam(p) != null;
    }

    public boolean isInSameTeam(Player p1, Player p2) {
        return this.getTeam(p1) != null && this.getTeam(p2) != null && this.getTeam(p1).equals(this.getTeam(p2));
    }

    public boolean joinTeam(Player p, String teamName) {
        if (this.teams.containsKey(teamName)) {
            List<UUID> list = this.teams.get(teamName);
            list.add(p.getUniqueId());
            return true;
        }
        return false;
    }

    public boolean quitTeam(Player p) {
        for (List<UUID> next : this.teams.values()) {
            if (!next.contains(p.getUniqueId())) continue;
            next.remove(p.getUniqueId());
            return true;
        }
        return false;
    }

    public void deleteTeam(String team) {
        if (this.teams.containsKey(team)) {
            this.teams.remove(team);
        }
        if (this.teamOwner.containsKey(team)) {
            this.teamOwner.remove(team);
        }
        if (this.req.containsKey(team)) {
            this.req.remove(team);
        }
    }

    public void createTeam(String team, Player p) {
        ArrayList<UUID> list = new ArrayList<UUID>();
        list.add(p.getUniqueId());
        this.teams.put(team, list);
        this.teamOwner.put(team, p.getUniqueId());
    }

    public void sendJoinReQuest(String team, Player p) {
        if (this.req.containsKey(team)) {
            this.req.get(team).add(p.getUniqueId());
        } else {
            ArrayList<UUID> list = new ArrayList<UUID>();
            list.add(p.getUniqueId());
            this.req.put(team, list);
        }
    }

    public boolean isRequested(String team, Player p) {
        return this.req.containsKey(team) && this.req.get(team).contains(p.getUniqueId());
    }

    public void removeRequest(String team, Player p) {
        if (this.isRequested(team, p)) {
            this.req.get(team).remove(p.getUniqueId());
        }
    }

    public void removeRequest(Player p) {
        for (String next : this.req.keySet()) {
            if (!this.req.get(next).contains(p.getUniqueId())) continue;
            this.req.get(next).remove(p.getUniqueId());
        }
    }

    public List<UUID> request(String team) {
        return this.req.containsKey(team) ? this.req.get(team) : null;
    }

    private int countTeammate(Player p) {
        int count = 0;
        List<Player> players = p.getWorld().getPlayers();
        for (Player tar : players) {
            if (!this.isInSameTeam(p, tar)) continue;
            ++count;
        }
        return count;
    }

    private void task() {
        BukkitRunnable task = new BukkitRunnable(){
            public void run() {
                Set<String> keys = getConfig().getConfigurationSection("Worlds").getKeys(false);
                if (keys != null) {
                    for (String next : keys) {
                        List<Player> players;
                        final int minCount = getConfig().getInt("Worlds." + next);
                        World world = Bukkit.getWorld((String)next);
                        if (world == null || (players = world.getPlayers()).size() == 0) continue;
                        for (Player p : players) {
                            if (p.isOp()){
                                continue;
                            }
                            new BukkitRunnable(){
                                public void run() {
                                    if (isInTeam(p)) {
                                        if (countTeammate(p) < minCount) {
                                            List<UUID> uuids = listTeamPlayers(p);
                                            for (UUID uuid : uuids) {
                                                Player tar = Bukkit.getPlayer((UUID)uuid);
                                                tar.sendMessage(getConfig().getString("Message.KickMessage"));
                                                tar.teleport(Bukkit.getWorld((String)getConfig().getString("World")).getSpawnLocation());
                                            }
                                        }
                                    } else {
                                        p.teleport(Bukkit.getWorld((String)getConfig().getString("World")).getSpawnLocation());
                                    }
                                }
                            }.runTaskLater(TeamMain.getInstance(), 5L);
                        }
                    }
                }
            }

        };
        task.runTaskTimerAsynchronously((Plugin)this, 10L, 20L);
    }

}

