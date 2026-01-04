package com.nationwar.menu.menulist;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TeamColorMenu {

    private final Inventory inv;

    public TeamColorMenu(org.bukkit.entity.Player player) {
        inv = Bukkit.createInventory(null, 27, "팀 색 설정");

        inv.setItem(11, new ItemStack(Material.RED_DYE));
        inv.setItem(13, new ItemStack(Material.BLUE_DYE));
        inv.setItem(15, new ItemStack(Material.GREEN_DYE));
    }

    public Inventory getInventory() {
        return inv;
    }
}
