package com.nationwar.listeners;

import com.nationwar.NationWar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.Player;

/**
 * 기본: 같은 팀이면 PVP 취소 (팀 시스템은 StorageManager 등 이후 구현 필요)
 * (현재는 틀만 제공)
 */
public class PvpListener implements Listener {

    private final NationWar plugin;

    public PvpListener(NationWar plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player victim && e.getDamager() instanceof Player attacker) {
            // TODO: 팀 비교 로직 - 같은 팀이면 e.setCancelled(true);
            // 예) if (TeamManager.getTeam(victim).equals(TeamManager.getTeam(attacker)) ) e.setCancelled(true);
        }
    }
}
