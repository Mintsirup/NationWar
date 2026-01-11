package com.nationwar.listeners;

import com.nationwar.NationWar;
import com.nationwar.core.CoreGson;
import com.nationwar.team.TeamMain;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.persistence.PersistentDataType;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class CoreDamageListener implements Listener {

    // 점령 가능 시간인지 확인 (월, 수, 금 19:00 ~ 19:59:59)
    private boolean isCaptureTime() {
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek day = now.getDayOfWeek();
        int hour = now.getHour();

        boolean isCorrectDay = (day == DayOfWeek.MONDAY || day == DayOfWeek.WEDNESDAY || day == DayOfWeek.FRIDAY);
        boolean isCorrectHour = (hour == 19);

        return isCorrectDay && isCorrectHour;
    }

    @EventHandler
    public void onCoreExplosion(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Ghast)) return;

        Ghast ghast = (Ghast) event.getEntity();
        NamespacedKey key = new NamespacedKey(NationWar.getInstance(), "core_id");
        if (!ghast.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) return;

        // 폭발 피해(ENTITY_EXPLOSION, BLOCK_EXPLOSION) 차단
        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION ||
                event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCoreDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Ghast) || !(event.getDamager() instanceof Player)) return;

        Player attacker = (Player) event.getDamager();

        // 1. 점령 시간 체크
        if (!isCaptureTime()) {
            attacker.sendMessage("§c[!] 지금은 코어 점령 시간이 아닙니다! (월/수/금 19:00~20:00)");
            event.setCancelled(true);
            return;
        }

        Ghast ghast = (Ghast) event.getEntity();
        NamespacedKey key = new NamespacedKey(NationWar.getInstance(), "core_id");
        if (!ghast.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) return;

        event.setCancelled(true);

        int coreId = ghast.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
        String attackerTeam = TeamMain.getPlayerTeam(attacker);
        CoreGson.CoreData core = CoreGson.getCore(coreId);

        if (attackerTeam.equals("방랑자") || attackerTeam.equals(core.owner)) return;

        core.hp -= event.getFinalDamage();
        if (core.hp <= 0) {
            core.hp = 5000;
            core.owner = attackerTeam;
            Bukkit.broadcastMessage("§6[!] §e코어 " + coreId + "번이 " + attackerTeam + " 팀에 점령되었습니다!");
        }
        CoreGson.saveCores();
    }
}