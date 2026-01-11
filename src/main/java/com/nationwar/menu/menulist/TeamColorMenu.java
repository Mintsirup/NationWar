package com.nationwar.menu.menulist;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TeamColorMenu {
    public static void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "TeamColorMenu");

        // 명세서 슬롯 구성
        inv.setItem(0, createColorItem(Material.RED_WOOL, "§c빨강 색"));
        inv.setItem(1, createColorItem(Material.ORANGE_WOOL, "§6주황 색"));
        inv.setItem(2, createColorItem(Material.YELLOW_WOOL, "§e노랑 색"));
        inv.setItem(3, createColorItem(Material.LIME_WOOL, "§a연두 색"));
        inv.setItem(4, createColorItem(Material.GREEN_WOOL, "§2초록 색"));
        inv.setItem(5, createColorItem(Material.LIGHT_BLUE_WOOL, "§b하늘 색"));
        inv.setItem(6, createColorItem(Material.BLUE_WOOL, "§9파랑 색"));
        inv.setItem(7, createColorItem(Material.PURPLE_WOOL, "§5보라 색"));
        inv.setItem(8, createColorItem(Material.BLACK_WOOL, "§0검정 색"));

        inv.setItem(13, createColorItem(Material.WHITE_WOOL, "§f하양 색"));

        player.openInventory(inv);
    }

    private static ItemStack createColorItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
}