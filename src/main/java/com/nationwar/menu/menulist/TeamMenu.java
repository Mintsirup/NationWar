package com.nationwar.menu.menulist;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TeamMenu {

    private final Inventory inv;

    public TeamMenu(Player player) {
        inv = Bukkit.createInventory(null, 27, "팀 메뉴");

        inv.setItem(11, new ItemStack(Material.PLAYER_HEAD)); // 초대
        inv.setItem(13, new ItemStack(Material.RED_DYE));     // 색
        inv.setItem(15, new ItemStack(Material.BARRIER));    // 삭제
    }

    public Inventory getInventory() {
        return inv;
    }
}
