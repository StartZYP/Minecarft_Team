/*
 * Decompiled with CFR 0.138.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.player.PlayerChangedWorldEvent
 *  org.bukkit.event.player.PlayerExpChangeEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.projectiles.ProjectileSource
 */
package com.qq44920040.miecarft.Team;

import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.projectiles.ProjectileSource;

public class Listeners
implements Listener {
    private TeamMain instance = TeamMain.getInstance();
    @EventHandler
    public void PlayerDeathevent(PlayerDeathEvent playerDeathEvent){

    }

    @EventHandler
    public void onExpChange(PlayerExpChangeEvent e) {
        Player p = e.getPlayer();
        if (e.getAmount() <= 0) {
            return;
        }
        if (this.instance.isInTeam(p)) {
            if (this.instance.listTeamPlayers(p).size() < 2) {
                return;
            }
            double doubly = this.instance.getConfig().getDouble("DoublyExp");
            int exp = (int)((double)e.getAmount() * doubly);
            e.setAmount(exp);
            p.sendMessage(this.instance.getConfig().getString("Message.ExpMessage").replace("<exp>", String.valueOf(exp)));
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            ProjectileSource shooter;
            Player entity = (Player)e.getEntity();
            Player damager = null;
            if (e.getDamager() instanceof Player) {
                damager = (Player)e.getDamager();
            } else if (e.getDamager() instanceof Projectile && (shooter = ((Projectile)e.getDamager()).getShooter()) instanceof Player) {
                damager = (Player)shooter;
            }else {
                return;
            }
            if (this.instance.isInSameTeam(entity, damager)) {
                e.setCancelled(true);
                damager.sendMessage("§c你不能攻击你的队友");
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (this.instance.isInTeam(p)) {
            if (this.instance.getTeamOwner(this.instance.getTeam(p)).equals(p.getName())) {
                List<UUID> players = this.instance.listTeamPlayers(p);
                for (UUID uuid : players) {
                    Player tar = Bukkit.getPlayer((UUID)uuid);
                    if (tar == null || !tar.isOnline()) continue;
                    tar.sendMessage("§c队伍被队长解散了");
                }
                this.instance.deleteTeam(this.instance.getTeam(p));
            } else {
                List<UUID> players = this.instance.listTeamPlayers(p);
                for (UUID uuid : players) {
                    Player tar = Bukkit.getPlayer((UUID)uuid);
                    tar.sendMessage("§e" + p.getName() + "§c退出了队伍");
                }
                this.instance.quitTeam(p);
            }
        }
        this.instance.removeRequest(p);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        block19 : {
            Player p;
            Inventory inv;
            block17 : {
                TeamItem titem;
                block20 : {
                    int slot;
                    block18 : {
                        p = (Player)e.getWhoClicked();
                        inv = e.getInventory();
                        if (!inv.getTitle().equals("§c§l队伍列表")) break block17;
                        e.setCancelled(true);
                        slot = e.getSlot();
                        if (slot != 45) break block18;
                        ItemStack item = inv.getItem(slot);
                        ItemMeta meta = item.getItemMeta();
                        String line = ChatColor.stripColor((String)((String)meta.getLore().get(0)));
                        line = line.split("当前")[1];
                        int page = Integer.parseInt(line = line.replace("页", ""));
                        if (page == 1) {
                            p.sendMessage("§c没有上一页了");
                            return;
                        }
                        Inventorys.teamsInv(p, page - 1);
                        break block19;
                    }
                    if (slot != 53) break block20;
                    ItemStack item = inv.getItem(slot);
                    ItemMeta meta = item.getItemMeta();
                    String line = ChatColor.stripColor((String)((String)meta.getLore().get(0)));
                    line = line.split("当前")[1];
                    line = line.replace("页", "");
                    int page = Integer.parseInt(line);
                    Inventorys.teamsInv(p, page + 1);
                    break block19;
                }
                ItemStack item = e.getCurrentItem();
                if (item == null || item.getType() != Material.SIGN || !(titem = new TeamItem(item)).isTeamItem()) break block19;
                String teamName = titem.getTeamName();
                if (this.instance.isInTeam(p)) {
                    p.sendMessage("§c你已经加入一个队伍了");
                    return;
                }
                if (!this.instance.hasTeam(teamName)) {
                    p.sendMessage("§c没有这个队伍");
                    return;
                }
                if (this.instance.isRequested(teamName, p)) {
                    p.sendMessage("§c你已经发送过请求了,请耐心等待");
                    return;
                }
                this.instance.sendJoinReQuest(teamName, p);
                p.sendMessage("§a请求发送成功");
                String ownerName = this.instance.getTeamOwner(teamName);
                Player owner = Bukkit.getPlayerExact((String)ownerName);
                if (owner == null || !owner.isOnline()) break block19;
                owner.sendMessage("§e" + p.getName() + "§a申请加入队伍,输入§e/team request§a来批准");
                break block19;
            }
            if (inv.getTitle().equalsIgnoreCase("§c§l请求列表")) {
                e.setCancelled(true);
                int slot = e.getSlot();
                if (slot == 45) {
                    ItemStack item = inv.getItem(slot);
                    ItemMeta meta = item.getItemMeta();
                    String line = ChatColor.stripColor((String)((String)meta.getLore().get(0)));
                    line = line.split("当前")[1];
                    int page = Integer.parseInt(line = line.replace("页", ""));
                    if (page == 1) {
                        p.sendMessage("§c没有上一页了");
                        return;
                    }
                    Inventorys.requestInv(p, page - 1);
                } else if (slot == 53) {
                    ItemStack item = inv.getItem(slot);
                    ItemMeta meta = item.getItemMeta();
                    String line = ChatColor.stripColor((String)((String)meta.getLore().get(0)));
                    line = line.split("当前")[1];
                    line = line.replace("页", "");
                    int page = Integer.parseInt(line);
                    Inventorys.requestInv(p, page + 1);
                } else {
                    RequestItem ritem;
                    ItemStack item = e.getCurrentItem();
                    if (item != null && item.getType() == Material.SKULL_ITEM && (ritem = new RequestItem(item)).isRequestItem()) {
                        String name = ritem.getRequestName();
                        Player tar = Bukkit.getPlayerExact((String)name);
                        p.closeInventory();
                        if (tar != null && tar.isOnline()) {
                            if (!this.instance.isInTeam(p)) {
                                p.sendMessage("§c你并没有加入一个队伍");
                                return;
                            }
                            if (!this.instance.getTeamOwner(this.instance.getTeam(p)).equals(p.getName())) {
                                p.sendMessage("§c你并不是队长");
                            }
                            if (this.instance.isInTeam(tar)) {
                                p.sendMessage("§c他已经加入一个队伍了");
                                return;
                            }
                            if (this.instance.listTeamPlayers(p).size() >= this.instance.getConfig().getInt("MaxCount")) {
                                p.sendMessage("§c队伍人数达到上限了");
                                return;
                            }
                            this.instance.removeRequest(this.instance.getTeam(p), tar);
                            this.instance.joinTeam(tar, this.instance.getTeam(p));
                            tar.sendMessage("§a你加入了队伍§e" + this.instance.getTeam(p));
                            List<UUID> players = this.instance.listTeamPlayers(p);
                            for (UUID uuid : players) {
                                Player tar2 = Bukkit.getPlayer((UUID)uuid);
                                tar2.sendMessage("§e" + tar.getName() + "§a加入了队伍");
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
		if (p.isOp()){
            return;
        }
        if (e.getTo().getWorld().getName().equals(e.getFrom().getWorld().getName())) {
            return;
        }
        Location to = e.getTo();
        String name = to.getWorld().getName();
        int count = this.instance.getConfig().getInt("Worlds." + name);
        if (count > 0) {
            if (!this.instance.isInTeam(p)) {
                p.sendMessage("§c你没有在队伍内,无法进入副本");
                e.setCancelled(true);
                return;
            }
            if (this.instance.getTeamOwner(this.instance.getTeam(p)).equals(p.getName()) && this.instance.listTeamPlayers(p).size() < count) {
                p.sendMessage(this.instance.getConfig().getString("Message.WorldMessage").replace("<count>", String.valueOf(count)));
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerWorldChanged(PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();
		if (p.isOp()){
            return;
        }
        List<UUID> players = this.instance.listTeamPlayers(p);
        if (players == null) {
            return;
        }
        World world = p.getWorld();
        for (UUID uuid : players) {
            Player tar = Bukkit.getPlayer((UUID)uuid);
            if (tar.getWorld().getName().equals(world.getName())) continue;
            tar.teleport(p.getLocation());
			tar.sendMessage("§c您所在的队伍中有一名玩家传送到了当前地图，因此您也跟随传送来了。");
        }
    }
}

