package com.nationwar.menu.menulist;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InfoMenu {

    private final Inventory inv;

    public InfoMenu(org.bukkit.entity.Player player) {
        inv = Bukkit.createInventory(null, 27, "정보");

        inv.setItem(13, new ItemStack(Material.BOOK));
    }

    public Inventory getInventory() {
        return inv;
    }
}
