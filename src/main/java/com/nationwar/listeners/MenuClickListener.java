package com.nationwar.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.entity.Player;

public class MenuClickListener implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!(event.getInventory().getHolder() instanceof GUIMenu menu)) return;

        event.setCancelled(true);

        menu.onClick(player, event.getSlot());
    }
}
