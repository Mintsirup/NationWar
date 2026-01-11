package com.nationwar.menu.menulist;

import com.nationwar.menu.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TeamDeleteConfirmMenu {
    public static void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 45, "§8팀 삭제 확인");
        inv.setItem(22, GUIManager.createItem(Material.TNT, "§c§l팀 삭제 실행", "§7클릭 시 팀이 즉시 해체됩니다."));
        inv.setItem(40, GUIManager.createItem(Material.BARRIER, "§f돌아가기"));
        player.openInventory(inv);
    }
}