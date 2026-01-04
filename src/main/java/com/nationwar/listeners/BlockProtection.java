package com.nationwar.listeners;

import com.nationwar.core.CoreMain;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockProtection implements Listener {
    private final CoreMain coreMain;

    public BlockProtection(CoreMain coreMain) {
        this.coreMain = coreMain;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        for (Location loc : coreMain.coreLocations) {
            if (event.getBlock().getLocation().distance(loc) < 5) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        for (Location loc : coreMain.coreLocations) {
            if (event.getBlock().getLocation().distance(loc) < 5) {
                event.setCancelled(true);
                return;
            }
        }
    }
}