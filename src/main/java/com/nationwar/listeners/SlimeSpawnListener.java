package com.nationwar.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class SlimeSpawnListener implements Listener {
    @EventHandler
    public void onSpawn(CreatureSpawnEvent e) {
        if (e.getEntityType() == EntityType.SLIME || e.getEntityType() == EntityType.GHAST) {
            // 플러그인이 소환한(CUSTOM) 것이 아니면 스폰 취소
            if (e.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM) {
                e.setCancelled(true);
            }
        }
    }
}