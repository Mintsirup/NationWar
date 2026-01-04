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
        if (event.getDamager() instanceof Player p1 && event.getEntity() instanceof Player p2) {
            String team1 = teamMain.playerTeams.getOrDefault(p1.getUniqueId(), "방랑자");
            String team2 = teamMain.playerTeams.getOrDefault(p2.getUniqueId(), "방랑자");
            if (!team1.equals("방랑자") && team1.equals(team2)) {
                event.setCancelled(true);
            }
        }
    }
}