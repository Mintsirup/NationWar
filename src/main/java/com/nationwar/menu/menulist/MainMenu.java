package com.nationwar.menu.menulist;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MainMenu {

    public void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "국가 전쟁 메뉴");

        inv.setItem(11, createItem(Material.PLAYER_HEAD, "팀 메뉴"));
        inv.setItem(13, createItem(Material.BEACON, "코어 메뉴"));
        inv.setItem(15, createItem(Material.BOOK, "정보 메뉴"));

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
