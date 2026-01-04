package com.nationwar.menu.menulist;

import com.nationwar.menu.GUIManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TeamDeleteConfirmMenu {
    public static void open(Player p) {
        Inventory inv = GUIManager.createMenu("§0팀 삭제 확인", 27);

        ItemStack confirm = new ItemStack(Material.NETHER_WART_BLOCK);
        ItemMeta cm = confirm.getItemMeta(); cm.setDisplayName("§4§l해체 확정"); confirm.setItemMeta(cm);

        ItemStack cancel = new ItemStack(Material.BARRIER);
        ItemMeta cam = cancel.getItemMeta(); cam.setDisplayName("§f취소"); cancel.setItemMeta(cam);

        inv.setItem(11, confirm);
        inv.setItem(15, cancel);
        p.openInventory(inv);
    }
}