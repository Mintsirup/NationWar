package com.nationwar.menu.menulist;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CoreMenu {

    public void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "코어 메뉴");

        for (int i = 10; i <= 15; i++) {
            inv.setItem(i, createItem(Material.BARRIER, "코어"));
        }

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
