package com.nationwar.menu.menulist;

import com.nationwar.menu.GUIManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TeamInviteConfirmMenu {
    public static void open(Player p, String teamName) {
        Inventory inv = GUIManager.createMenu("§0팀 초대 확인: " + teamName, 27);

        ItemStack accept = new ItemStack(Material.LIME_CONCRETE);
        ItemMeta am = accept.getItemMeta(); am.setDisplayName("§a§l수락"); accept.setItemMeta(am);

        ItemStack deny = new ItemStack(Material.RED_CONCRETE);
        ItemMeta dm = deny.getItemMeta(); dm.setDisplayName("§c§l거절"); deny.setItemMeta(dm);

        inv.setItem(11, accept);
        inv.setItem(15, deny);
        p.openInventory(inv);
    }
}