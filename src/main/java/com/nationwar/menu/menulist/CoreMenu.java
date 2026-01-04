package com.nationwar.menu.menulist;

import com.nationwar.menu.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class CoreMenu {
    public static Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(null, 27, "§0코어 메뉴");
        GUIManager.fillGui(inv, 10, 11, 12, 14, 15, 16, 19);
        for (int i = 1; i <= 6; i++) {
            int slot = (i <= 3) ? 9 + i : 10 + i;
            inv.setItem(slot, GUIManager.createItem(Material.END_CRYSTAL, "§d§l코어 #" + i, "§7상태: §f확인 중", "§7점령 국가: §f없음", "", "§e클릭 시 상세 정보"));
        }
        inv.setItem(19, GUIManager.createItem(Material.ARROW, "§7뒤로 가기", "§7메인 메뉴로 이동합니다."));
        return inv;
    }
}