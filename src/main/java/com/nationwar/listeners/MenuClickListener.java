package com.nationwar.listeners;

import com.nationwar.menu.GUIManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuClickListener implements Listener {
    private final GUIManager guiManager;

    public MenuClickListener(GUIManager guiManager) {
        this.guiManager = guiManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getView().getTitle().startsWith("§")) {
            event.setCancelled(true);
            if (event.getWhoClicked() instanceof Player player && event.getCurrentItem() != null) {
                guiManager.handleMenuClick(player, event.getView().getTitle(), event.getRawSlot(), event.getCurrentItem());
            }
        }
    }
}