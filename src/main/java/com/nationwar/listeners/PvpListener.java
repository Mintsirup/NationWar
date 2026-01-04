package com.nationwar.listeners;

import com.nationwar.team.TeamMain;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PvpListener implements Listener {

    @EventHandler
    public void onPvp(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity target = event.getEntity();

        if (!(damager instanceof Player)) return;
        if (!(target instanceof Player)) return;

        Player attacker = (Player) damager;
        Player victim = (Player) target;

        String teamA = TeamMain.getPlayerTeam(attacker);
        String teamB = TeamMain.getPlayerTeam(victim);

        if (teamA.equals(teamB)) {
            event.setCancelled(true);
            attacker.sendMessage("같은 팀의 플레이어는 공격할 수 없습니다.");
        }
    }
}
