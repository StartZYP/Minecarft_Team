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

public class RequestItem {
    private boolean requestItem = false;
    private String requestName;

    public RequestItem(ItemStack item) {
        String name;
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && (name = item.getItemMeta().getDisplayName()).startsWith("\u00a7c\u00a7l\u73a9\u5bb6\u540d\u79f0: \u00a7a")) {
            this.requestItem = true;
            this.requestName = name.split("\u00a7c\u00a7l\u73a9\u5bb6\u540d\u79f0: \u00a7a")[1];
        }
    }

    public boolean isRequestItem() {
        return this.requestItem;
    }

    public String getRequestName() {
        return this.requestName;
    }
}

