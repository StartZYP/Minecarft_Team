/*
 * Decompiled with CFR 0.138.
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.qq44920040.miecarft.Team;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TeamItem {
    private boolean teamItem = false;
    private String teamName;

    public TeamItem(ItemStack item) {
        String name;
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && (name = item.getItemMeta().getDisplayName()).startsWith("\u00a7c\u00a7l\u961f\u4f0d\u540d\u79f0: \u00a7a")) {
            this.teamItem = true;
            this.teamName = name.split("\u00a7c\u00a7l\u961f\u4f0d\u540d\u79f0: \u00a7a")[1];
        }
    }

    public boolean isTeamItem() {
        return this.teamItem;
    }

    public String getTeamName() {
        return this.teamName;
    }
}

