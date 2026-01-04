package com.nationwar.menu.menulist;

import com.nationwar.menu.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class TeamMenu {
    public static Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(null, 27, "§0팀 메뉴");
        GUIManager.fillGui(inv, 10, 11, 12, 19);
        inv.setItem(10, GUIManager.createItem(Material.CYAN_DYE, "§d§l팀 색 설정", "§7팀 색상을 변경합니다.", "", "§e클릭하여 이동"));
        inv.setItem(11, GUIManager.createItem(Material.PAPER, "§a§l팀 초대", "§7받은 팀 초대를 확인합니다.", "", "§e클릭하여 이동"));
        inv.setItem(12, GUIManager.createItem(Material.RED_DYE, "§c§l팀 삭제", "§c팀을 완전히 삭제합니다.", "§c되돌릴 수 없습니다.", "", "§e클릭하여 진행"));
        inv.setItem(19, GUIManager.createItem(Material.ARROW, "§7뒤로 가기"));
        return inv;
    }
}