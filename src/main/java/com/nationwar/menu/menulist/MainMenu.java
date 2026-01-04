package com.nationwar.menu.menulist;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MainMenu {

    private final Inventory inv;

    public MainMenu(Player player) {
        inv = Bukkit.createInventory(null, 27, "메인 메뉴");

        inv.setItem(11, new ItemStack(Material.WHITE_BANNER)); // 팀
        inv.setItem(13, new ItemStack(Material.BEACON));       // 코어
        inv.setItem(15, new ItemStack(Material.BOOK));         // 정보
    }

    public Inventory getInventory() {
        return inv;
    }
}
