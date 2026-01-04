package com.nationwar.listeners;

import com.nationwar.NationWar;
import com.nationwar.core.CoreMain;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerDistanceDetect implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        CoreMain coreMain = NationWar.getInstance().getCoreMain();

        coreMain.getCores().forEach((id, data) -> {
            Location coreLoc = new Location(player.getWorld(), data.x, data.y, data.z);
            if (player.getWorld().equals(coreLoc.getWorld()) && player.getLocation().distance(coreLoc) <= 50) {
                coreMain.getBossBars().get(id).addPlayer(player);
            } else {
                coreMain.getBossBars().get(id).removePlayer(player);
            }
        });
    }
}