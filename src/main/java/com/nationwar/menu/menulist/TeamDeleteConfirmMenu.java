package com.nationwar.menu.menulist;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TeamDeleteConfirmMenu {

    private final Inventory inv;

    public TeamDeleteConfirmMenu(org.bukkit.entity.Player player) {
        inv = Bukkit.createInventory(null, 27, "팀 삭제 확인");

        inv.setItem(11, new ItemStack(Material.GREEN_WOOL)); // 확인
        inv.setItem(15, new ItemStack(Material.RED_WOOL));   // 취소
    }

    public Inventory getInventory() {
        return inv;
    }
}
