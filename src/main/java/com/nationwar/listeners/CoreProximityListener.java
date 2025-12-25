package com.nationwar.listeners;

import com.nationwar.core.Core;
import com.nationwar.core.CoreManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashSet;
import java.util.Set;

public class CoreProximityListener implements Listener {

    private final CoreManager coreManager;
    private final Set<String> notified = new HashSet<>();

    public CoreProximityListener(CoreManager coreManager) {
        this.coreManager = coreManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        for (Core core : coreManager.getCores()) {
            if (!player.getWorld().getName().equals(core.getWorldName())) continue;
            double cx = core.getX() + 1.5;
            double cz = core.getZ() + 1.5;
            double dx = player.getLocation().getX() - cx;
            double dz = player.getLocation().getZ() - cz;
            double dist = Math.sqrt(dx * dx + dz * dz);
            String key = player.getUniqueId().toString() + ":" + core.getId().toString();
            if (dist <= 30.0) {
                // Player is near a core — placeholder for bossbar/title/glow
                if (!notified.contains(key)) {
                    player.sendMessage("You are near a core (" + core.getId() + ") — bossbar/title/glow to be implemented.");
                    notified.add(key);
                }
            } else {
                if (notified.contains(key)) notified.remove(key);
            }
        }
    }
}
