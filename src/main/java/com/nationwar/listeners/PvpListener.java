package com.nationwar.listeners;

import com.nationwar.team.TeamMain;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PvpListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity victim = event.getEntity();

        if (!(damager instanceof Player)) return;
        if (!(victim instanceof Player)) return;

        Player attacker = (Player) damager;
        Player target = (Player) victim;

        String teamA = TeamMain.getTeam(attacker);
        String teamB = TeamMain.getTeam(target);

        if (!teamA.equals(TeamMain.WANDERER) && teamA.equals(teamB)) {
            event.setCancelled(true);
            attacker.sendMessage("§c같은 팀원은 공격할 수 없습니다.");
        }
    }
}
