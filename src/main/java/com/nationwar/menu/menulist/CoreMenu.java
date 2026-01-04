package com.nationwar.menu.menulist;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CoreMenu {

    private final Inventory inv;

    public CoreMenu(org.bukkit.entity.Player player) {
        inv = Bukkit.createInventory(null, 27, "코어 목록");

        for (int i = 0; i < 6; i++) {
            inv.addItem(new ItemStack(Material.BARRIER));
        }
    }

    public Inventory getInventory() {
        return inv;
    }
}
