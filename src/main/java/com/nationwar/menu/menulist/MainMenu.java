package com.nationwar.menu.menulist;

import com.nationwar.menu.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class MainMenu {
    public static void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "§8메인 메뉴");
        inv.setItem(10, GUIManager.createItem(Material.NAME_TAG, "§a§l팀 메뉴", "§7팀 관리 및 정보를 확인합니다."));
        inv.setItem(13, GUIManager.createItem(Material.BEACON, "§b§l코어 메뉴", "§7코어 점령 상태 확인 및 이동."));
        inv.setItem(16, GUIManager.createItem(Material.BOOK, "§e§l정보 메뉴", "§7플러그인 및 점령 시간 안내."));
        player.openInventory(inv);
    }
}