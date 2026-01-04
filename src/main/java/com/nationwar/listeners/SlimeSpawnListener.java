package com.nationwar.listeners;

import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.SlimeSplitEvent;

public class SlimeSpawnListener implements Listener {
    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        if (event.getEntity() instanceof Slime) event.setCancelled(true);
    }

    @EventHandler
    public void onSplit(SlimeSplitEvent event) {
        event.setCancelled(true);
    }
}