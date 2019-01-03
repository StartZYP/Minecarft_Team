/*
 * Decompiled with CFR 0.138.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryView
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.qq44920040.miecarft.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Inventorys {
    private static TeamMain instance = TeamMain.getInstance();

    public static void teamsInv(Player p, int page) {
        Inventory inv = Bukkit.createInventory(null, (int)54, (String)"\u00a7c\u00a7l\u961f\u4f0d\u5217\u8868");
        List<String> list = instance.listTeams();
        if (list != null && !list.isEmpty()) {
            int index = (page - 1) * 45;
            for (int i = 0; i < 45; ++i) {
                if (i + index >= list.size()) continue;
                ItemStack item = Inventorys.item(list.get(i + index));
                inv.addItem(new ItemStack[]{item});
            }
            int size = list.size();
            if (size <= (page - 1) * 45) {
                p.sendMessage("\u00a7c\u6ca1\u6709\u4e0b\u4e00\u9875\u4e86");
                return;
            }
        } else {
            p.sendMessage("\u00a7c\u6ca1\u6709\u6570\u636e");
            return;
        }
        inv.setItem(45, Inventorys.up(page));
        inv.setItem(53, Inventorys.next(page));
        p.closeInventory();
        p.openInventory(inv);
    }

    private static ItemStack item(String teamName) {
        ItemStack item = new ItemStack(Material.SIGN);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("\u00a7c\u00a7l\u961f\u4f0d\u540d\u79f0: \u00a7a" + teamName);
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("\u00a7c\u00a7l\u961f\u957f\u540d\u79f0:\u00a7a" + instance.getTeamOwner(teamName));
        lore.add("");
        List<UUID> players = instance.listTeamPlayers(Bukkit.getPlayerExact((String)instance.getTeamOwner(teamName)));
        lore.add("\u00a7a\u961f\u5458\u5217\u8868:\u00a7e(\u00a77" + players.size() + "\u00a7e)");
        for (UUID uuid : players) {
            Player tar = Bukkit.getPlayer((UUID)uuid);
            lore.add(" \u00a77- \u00a7e" + tar.getName());
        }
        lore.add("");
        lore.add("\u00a77>>\u00a7a\u00a7l\u70b9\u51fb\u52a0\u5165\u961f\u4f0d\u00a77<<");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack up(int page) {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("\u00a7c\u00a7l\u4e0a\u4e00\u9875");
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("\u00a7a\u5f53\u524d\u00a7e" + page + "\u00a7a\u9875");
        lore.add("\u00a7c\u70b9\u51fb\u5207\u6362\u81f3\u4e0a\u4e00\u9875");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack next(int page) {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("\u00a7a\u00a7l\u4e0b\u4e00\u9875");
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("\u00a7a\u5f53\u524d\u00a7e" + page + "\u00a7a\u9875");
        lore.add("\u00a7a\u70b9\u51fb\u5207\u6362\u81f3\u4e0b\u4e00\u9875");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack request(String name) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("\u00a7c\u00a7l\u73a9\u5bb6\u540d\u79f0: \u00a7a" + name);
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("\u00a7a\u70b9\u51fb\u540c\u610f\u52a0\u5165\u961f\u4f0d");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static void requestInv(Player p, int page) {
        Inventory inv = Bukkit.createInventory(null, (int)54, (String)"\u00a7c\u00a7l\u8bf7\u6c42\u5217\u8868");
        List<UUID> list = instance.request(instance.getTeam(p));
        if (list != null && !list.isEmpty()) {
            int index = (page - 1) * 45;
            for (int i = 0; i < 45; ++i) {
                if (i + index >= list.size()) continue;
                Player tar = Bukkit.getPlayer((UUID)list.get(i + index));
                ItemStack item = Inventorys.request(tar.getName());
                inv.addItem(new ItemStack[]{item});
            }
            int size = list.size();
            if (size <= (page - 1) * 45) {
                p.sendMessage("\u00a7c\u6ca1\u6709\u4e0b\u4e00\u9875\u4e86");
                return;
            }
        } else {
            p.sendMessage("\u00a7c\u6ca1\u6709\u6570\u636e");
            return;
        }
        inv.setItem(45, Inventorys.up(page));
        inv.setItem(53, Inventorys.next(page));
        p.closeInventory();
        p.openInventory(inv);
    }
}

