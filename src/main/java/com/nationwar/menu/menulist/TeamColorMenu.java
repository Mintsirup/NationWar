package com.nationwar.menu.menulist;

import com.nationwar.menu.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class TeamColorMenu {
    public static Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(null, 36, "§0팀 색 설정");
        GUIManager.fillGui(inv, 10, 11, 12, 13, 14, 15, 16, 31);
        inv.setItem(10, GUIManager.createItem(Material.RED_CONCRETE, "§f팀 색: §c빨강색", "§7해당 색상으로 팀 색을 변경합니다.", "", "§e클릭하여 선택"));
        inv.setItem(11, GUIManager.createItem(Material.ORANGE_CONCRETE, "§f팀 색: §6주황색", "§7해당 색상으로 팀 색을 변경합니다.", "", "§e클릭하여 선택"));
        inv.setItem(12, GUIManager.createItem(Material.YELLOW_CONCRETE, "§f팀 색: §e노랑색", "§7해당 색상으로 팀 색을 변경합니다.", "", "§e클릭하여 선택"));
        inv.setItem(13, GUIManager.createItem(Material.LIME_CONCRETE, "§f팀 색: §a초록색", "§7해당 색상으로 팀 색을 변경합니다.", "", "§e클릭하여 선택"));
        inv.setItem(14, GUIManager.createItem(Material.BLUE_CONCRETE, "§f팀 색: §9파랑색", "§7해당 색상으로 팀 색을 변경합니다.", "", "§e클릭하여 선택"));
        inv.setItem(15, GUIManager.createItem(Material.CYAN_CONCRETE, "§f팀 색: §3남색", "§7해당 색상으로 팀 색을 변경합니다.", "", "§e클릭하여 선택"));
        inv.setItem(16, GUIManager.createItem(Material.PURPLE_CONCRETE, "§f팀 색: §5보라색", "§7해당 색상으로 팀 색을 변경합니다.", "", "§e클릭하여 선택"));
        inv.setItem(31, GUIManager.createItem(Material.ARROW, "§7뒤로 가기"));
        return inv;
    }
}