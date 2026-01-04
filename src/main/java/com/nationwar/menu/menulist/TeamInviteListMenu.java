package com.nationwar.menu.menulist;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TeamInviteListMenu {

    private final Inventory inv;

    public TeamInviteListMenu(Player player) {
        inv = Bukkit.createInventory(null, 54, "팀 초대");

        for (Player p : Bukkit.getOnlinePlayers()) {
            inv.addItem(p.getPlayerProfile().getTextures().getSkin() == null
                    ? null
                    : Bukkit.createInventory(null, 9).getItem(0));
        }
    }

    public Inventory getInventory() {
        return inv;
    }
}
