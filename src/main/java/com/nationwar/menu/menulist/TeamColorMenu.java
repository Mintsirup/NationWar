package com.nationwar.menu.menulist;

import com.nationwar.menu.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TeamColorMenu {
    public static void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 18, "§8팀 색 설정 메뉴");
        Material[] wools = {Material.RED_WOOL, Material.ORANGE_WOOL, Material.YELLOW_WOOL, Material.LIME_WOOL,
                Material.GREEN_WOOL, Material.LIGHT_BLUE_WOOL, Material.BLUE_WOOL, Material.PURPLE_WOOL, Material.WHITE_WOOL};
        String[] names = {"빨강", "주황", "노랑", "연두", "초록", "하늘", "파랑", "보라", "하양"};

        for (int i = 0; i < wools.length; i++) {
            inv.setItem(i < 8 ? i : 13, GUIManager.createItem(wools[i], "§f" + names[i] + " 색상 선택"));
        }
        player.openInventory(inv);
    }
}