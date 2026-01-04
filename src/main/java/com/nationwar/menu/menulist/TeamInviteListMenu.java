package com.nationwar.menu.menulist;

import com.nationwar.menu.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class TeamInviteListMenu {
    public static void open(Player p) {
        Inventory inv = GUIManager.createMenu("§0팀 초대 하기", 54);
        int slot = 0;
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.equals(p)) continue;
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setOwningPlayer(online);
            meta.setDisplayName("§e" + online.getName());
            skull.setItemMeta(meta);
            inv.setItem(slot++, skull);
        }
        p.openInventory(inv);
    }
}