package com.nationwar.listeners;

import com.nationwar.NationWar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PvpListener implements Listener {
    private final NationWar plugin;

    public PvpListener(NationWar plugin) { this.plugin = plugin; }

    @EventHandler
    public void onPvp(EntityDamageByEntityEvent event) {
        // 기준서: 같은 팀원끼리는 서로 데미지를 입힐 수 없다.
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player victim = (Player) event.getEntity();
            Player attacker = (Player) event.getDamager();

            if (plugin.getTeamMain().sameTeam(victim, attacker)) {
                event.setCancelled(true);
            }
        }
    }
}