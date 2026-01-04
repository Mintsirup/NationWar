package com.nationwar.menu.menulist;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TeamInviteConfirmMenu {

    public static Inventory create(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "팀 초대 수락");

        ItemStack accept = new ItemStack(Material.LIME_WOOL);
        ItemMeta a = accept.getItemMeta();
        a.setDisplayName("§a초대 수락");
        accept.setItemMeta(a);

        ItemStack deny = new ItemStack(Material.RED_WOOL);
        ItemMeta d = deny.getItemMeta();
        d.setDisplayName("§c초대 거절");
        deny.setItemMeta(d);

        inv.setItem(11, accept);
        inv.setItem(15, deny);

        return inv;
    }
}
