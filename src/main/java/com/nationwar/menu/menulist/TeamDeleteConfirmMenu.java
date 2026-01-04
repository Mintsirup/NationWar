package com.nationwar.menu.menulist;

import com.nationwar.menu.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class TeamDeleteConfirmMenu {
    public static Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(null, 27, "§0팀 삭제 확인");
        GUIManager.fillGui(inv, 11, 13, 22);
        inv.setItem(11, GUIManager.createItem(Material.GRAY_STAINED_GLASS_PANE, "§7취소", "§7팀 삭제를 취소합니다."));
        inv.setItem(13, GUIManager.createItem(Material.BARRIER, "§4§l팀 삭제", "§c정말로 팀을 삭제하시겠습니까?", "§c이 작업은 되돌릴 수 없습니다.", "", "§e클릭하여 삭제"));
        inv.setItem(22, GUIManager.createItem(Material.ARROW, "§7뒤로 가기"));
        return inv;
    }
}