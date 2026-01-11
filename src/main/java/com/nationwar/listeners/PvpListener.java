package com.nationwar.listeners;

import com.nationwar.team.TeamMain;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PvpListener implements Listener {

    private final TeamMain teamMain;

    public PvpListener(TeamMain teamMain) {
        this.teamMain = teamMain;
    }

    @EventHandler
    public void onPvp(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof Player victim)) return;
        if (!(event.getDamager() instanceof Player attacker)) return;

        if (teamMain.sameTeam(victim, attacker)) {
            event.setCancelled(true);
        }
    }
}
