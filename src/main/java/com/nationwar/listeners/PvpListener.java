package com.nationwar.listeners;

import com.nationwar.NationWar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PvpListener implements Listener {
    @EventHandler
    public void onPvp(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player attacker && e.getEntity() instanceof Player victim) {
            String teamA = NationWar.getInstance().getTeamMain().getPlayerTeam(attacker.getUniqueId());
            String teamV = NationWar.getInstance().getTeamMain().getPlayerTeam(victim.getUniqueId());

            if (!teamA.equals("방랑자") && teamA.equals(teamV)) {
                attacker.sendMessage("§c같은 팀원은 공격할 수 없습니다!");
                e.setCancelled(true);
            }
        }
    }
}