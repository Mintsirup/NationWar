package com.nationwar.listeners;

import com.nationwar.team.TeamMain;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PvpListener implements Listener {
    @EventHandler
    public void onPvp(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Player victim = (Player) event.getEntity();

            String team1 = TeamMain.getPlayerTeam(damager);
            String team2 = TeamMain.getPlayerTeam(victim);

            if (!team1.equals("방랑자") && team1.equals(team2)) {
                event.setCancelled(true);
                damager.sendMessage("§c[!] 같은 팀원은 공격할 수 없습니다.");
            }
        }
    }
}