package com.nationwar.menu.menulist;

import com.nationwar.menu.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InfoMenu {
    public static void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "§8정보 메뉴");
        inv.setItem(10, GUIManager.createItem(Material.CLOCK, "§6점령 시간", "§f월, 수, 금 19:00 ~ 20:00"));
        inv.setItem(13, GUIManager.createItem(Material.IRON_SWORD, "§c코어 체력", "§f기본 체력: 5000 HP"));
        inv.setItem(16, GUIManager.createItem(Material.PAPER, "§f제작자", "§7Mintina_ / 2026.01.10"));
        player.openInventory(inv);
    }
}