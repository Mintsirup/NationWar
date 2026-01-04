package com.nationwar.menu.menulist;

import com.nationwar.menu.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class InfoMenu {
    public static Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(null, 27, "§0정보 메뉴");
        GUIManager.fillGui(inv, 10, 11, 12, 19);
        inv.setItem(10, GUIManager.createItem(Material.WRITABLE_BOOK, "§b§l국가 정보", "§7소속 국가 정보", "§7팀 수, 플레이어 수"));
        inv.setItem(11, GUIManager.createItem(Material.COMPASS, "§c§l전쟁 상태", "§7현재 전쟁 여부", "§7전쟁 시간"));
        inv.setItem(12, GUIManager.createItem(Material.CLOCK, "§e§l점령 시간", "§7점령 가능 시간", "§7UTC+9 기준"));
        inv.setItem(19, GUIManager.createItem(Material.ARROW, "§7뒤로 가기"));
        return inv;
    }
}