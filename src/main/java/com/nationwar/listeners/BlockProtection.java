package com.nationwar.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.block.Block;
import java.util.Iterator;

public class BlockProtection implements Listener {
    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.WHITE_CONCRETE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        Iterator<Block> blocks = event.blockList().iterator();
        while (blocks.hasNext()) {
            if (blocks.next().getType() == Material.WHITE_CONCRETE) blocks.remove();
        }
    }
}