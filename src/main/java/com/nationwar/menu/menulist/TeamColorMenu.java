package com.nationwar.menu.menulist;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TeamColorMenu {

    public void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "팀 색 설정");

        inv.setItem(10, createItem(Material.RED_WOOL, "빨강"));
        inv.setItem(12, createItem(Material.BLUE_WOOL, "파랑"));
        inv.setItem(14, createItem(Material.GREEN_WOOL, "초록"));
        inv.setItem(16, createItem(Material.YELLOW_WOOL, "노랑"));

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
