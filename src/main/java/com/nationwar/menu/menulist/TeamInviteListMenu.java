package com.nationwar.menu.menulist;

import com.nationwar.team.TeamGson;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class TeamInviteListMenu {
    public static void open(Player leader) {
        Inventory inv = Bukkit.createInventory(null, 54, "§8팀 초대 메뉴");
        int slot = 0;
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (TeamGson.getPlayerTeam(target.getUniqueId()).equals("방랑자")) {
                ItemStack head = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta meta = (SkullMeta) head.getItemMeta();
                if (meta != null) {
                    meta.setOwningPlayer(target);
                    meta.setDisplayName("§f" + target.getName());
                    head.setItemMeta(meta);
                }
                inv.setItem(slot++, head);
            }
        }
        leader.openInventory(inv);
    }
}