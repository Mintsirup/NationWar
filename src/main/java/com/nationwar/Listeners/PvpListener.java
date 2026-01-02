package com.nationwar.Listeners;

import com.nationwar.team.TeamMain;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PvpListener implements Listener {

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (!(e.getDamager() instanceof Player)) return;

        Player a = (Player) e.getDamager();
        Player b = (Player) e.getEntity();

        String ta = TeamMain.getTeam(a);
        String tb = TeamMain.getTeam(b);

        if (ta.equals(tb)) {
            e.setCancelled(true);
        }
    }
}
