package com.nationwar.menu.menulist;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TeamMenu {

    public void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "팀 메뉴");

        inv.setItem(11, createItem(Material.PLAYER_HEAD, "팀 초대"));
        inv.setItem(13, createItem(Material.RED_WOOL, "팀 색 설정"));
        inv.setItem(15, createItem(Material.BARRIER, "팀 삭제"));

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
