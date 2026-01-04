package com.nationwar.menu.menulist;

import com.nationwar.menu.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class TeamInviteListMenu {
    public static Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(null, 54, "§0팀 초대 목록");
        GUIManager.fillGui(inv, 48); // 9~44는 플레이어 헤드용 비워둠
        inv.setItem(48, GUIManager.createItem(Material.ARROW, "§7뒤로 가기"));
        return inv;
    }
}