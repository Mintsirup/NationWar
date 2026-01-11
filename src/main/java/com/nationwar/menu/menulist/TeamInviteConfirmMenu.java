package com.nationwar.menu.menulist;

import com.nationwar.menu.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TeamInviteConfirmMenu {
    public static void open(Player player, String targetName) {
        Inventory inv = Bukkit.createInventory(null, 45, "§8초대 확인: " + targetName);
        inv.setItem(20, GUIManager.createItem(Material.LIME_STAINED_GLASS_PANE, "§a초대 승인"));
        inv.setItem(24, GUIManager.createItem(Material.RED_STAINED_GLASS_PANE, "§c초대 취소"));
        player.openInventory(inv);
    }
}