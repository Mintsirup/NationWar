package com.nationwar.listeners;

import com.nationwar.NationWar;
import com.nationwar.core.CoreMain;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CoreDamageListener implements Listener {
    private final CoreMain coreMain;
    public CoreDamageListener(CoreMain cm) { this.coreMain = cm; }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCoreDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Ghast ghast && event.getDamager() instanceof Player player) {
            if (!ghast.hasMetadata("core_hitbox")) return;

            if (!coreMain.isCaptureTime()) {
                player.sendMessage("§c지금은 전쟁 시간이 아닙니다! (전쟁시간 : 19:00 ~ 20:00)");
                event.setCancelled(true);
                return;
            }

            double damage = event.getFinalDamage();
            if (damage <= 0) damage = 1.0;

            for (int id : coreMain.getCores().keySet()) {
                CoreMain.CoreData data = coreMain.getCores().get(id);
                if (ghast.getLocation().distance(data.loc) < 5) { // 좌표 근접 확인
                    coreMain.damageCore(id, damage, player);
                    event.setDamage(0);
                    ghast.setHealth(ghast.getMaxHealth()); // 가스트 죽음 방지
                    return;
                }
            }
        }
    }
}