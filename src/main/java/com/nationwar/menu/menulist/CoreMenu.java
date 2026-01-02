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

        inv.setItem(10, createItem(Material.BARRIER, "코어 1"));
        inv.setItem(11, createItem(Material.BARRIER, "코어 2"));
        inv.setItem(12, createItem(Material.BARRIER, "코어 3"));

        // 가운데 13번 슬롯은 비움

        inv.setItem(14, createItem(Material.BARRIER, "코어 4"));
        inv.setItem(15, createItem(Material.BARRIER, "코어 5"));
        inv.setItem(16, createItem(Material.BARRIER, "코어 6"));

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
