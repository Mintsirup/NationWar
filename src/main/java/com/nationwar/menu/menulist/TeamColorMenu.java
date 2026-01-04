package com.nationwar.menu.menulist;

import com.nationwar.menu.GUIManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TeamColorMenu {
    public static void open(Player p) {
        Inventory inv = GUIManager.createMenu("§0팀 색상 설정", 27);
        Material[] colors = {Material.RED_WOOL, Material.ORANGE_WOOL, Material.YELLOW_WOOL, Material.LIME_WOOL, Material.BLUE_WOOL, Material.PURPLE_WOOL};
        for (int i = 0; i < colors.length; i++) inv.setItem(10 + i, new ItemStack(colors[i]));
        p.openInventory(inv);
    }
}