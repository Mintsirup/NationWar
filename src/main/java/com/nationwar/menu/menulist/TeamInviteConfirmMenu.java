package com.nationwar.menu.menulist;

import com.nationwar.menu.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class TeamInviteConfirmMenu {
    public static Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(null, 27, "§0팀 초대 확인");
        GUIManager.fillGui(inv, 11, 13, 15, 22);
        inv.setItem(11, GUIManager.createItem(Material.RED_STAINED_GLASS_PANE, "§c거절", "§7팀 초대를 거절합니다."));
        inv.setItem(13, GUIManager.createItem(Material.PAPER, "§a§l팀 초대", "§7초대한 팀: §f정보 없음", "§7초대한 플레이어: §f정보 없음"));
        inv.setItem(15, GUIManager.createItem(Material.LIME_STAINED_GLASS_PANE, "§a수락", "§7팀 초대를 수락합니다."));
        inv.setItem(22, GUIManager.createItem(Material.ARROW, "§7뒤로 가기"));
        return inv;
    }
}