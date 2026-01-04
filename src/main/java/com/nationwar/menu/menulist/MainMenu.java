package com.nationwar.menu.menulist;

import com.nationwar.menu.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class MainMenu {
    public static Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(null, 27, "§0메인 메뉴");
        GUIManager.fillGui(inv, 10, 13, 16, 22);
        inv.setItem(10, GUIManager.createItem(Material.BEACON, "§c§l코어 관리", "§7국가의 코어 상태를 확인합니다.", "§7점령 및 전쟁 정보 포함", "", "§e클릭하여 이동"));
        inv.setItem(13, GUIManager.createItem(Material.BOOK, "§b§l국가 정보", "§7국가 상태를 확인합니다.", "§7전쟁 진행 여부 및 시간", "", "§e클릭하여 이동"));
        inv.setItem(16, GUIManager.createItem(Material.PLAYER_HEAD, "§a§l팀 관리", "§7팀 관련 설정을 관리합니다.", "", "§e클릭하여 이동"));
        inv.setItem(22, GUIManager.createItem(Material.BARRIER, "§c닫기", "§7메뉴를 닫습니다."));
        return inv;
    }
}