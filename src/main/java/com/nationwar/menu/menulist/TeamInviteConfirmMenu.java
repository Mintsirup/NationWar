package com.nationwar.menu.menulist;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TeamInviteConfirmMenu {

    public void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "팀 초대 확인");

        inv.setItem(11, createItem(Material.LIME_WOOL, "초대 수락"));
        inv.setItem(15, createItem(Material.RED_WOOL, "초대 거절"));

        player.openInventory(inv);
    }

    private ItemStack createItem(Material mat, String name) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
}
