package com.nationwar.listeners;

import com.nationwar.NationWar;
import com.nationwar.team.TeamMain;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PvpListener implements Listener {

    private final TeamMain teamMain;

    public PvpListener(NationWar plugin) {
        this.teamMain = plugin.getTeamMain();
    }

    @EventHandler
    public void onPvp(EntityDamageByEntityEvent event) {

        Entity damager = event.getDamager();
        Entity victim = event.getEntity();

        if (!(victim instanceof Player damaged)) return;

        Player attacker = null;

        // 직접 공격
        if (damager instanceof Player p) {
            attacker = p;
        }

        // 화살 같은 투사체
        if (damager instanceof org.bukkit.entity.Projectile proj
                && proj.getShooter() instanceof Player p) {
            attacker = p;
        }

        if (attacker == null) return;

        // 같은 팀이면 차단
        if (teamMain.sameTeam(attacker, damaged)) {
            event.setCancelled(true);
            event.setDamage(0);

            attacker.sendMessage("§c같은 팀원은 공격할 수 없습니다.");
        }
    }
}
