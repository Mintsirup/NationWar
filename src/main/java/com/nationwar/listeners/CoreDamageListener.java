package com.nationwar.listeners;

import com.nationwar.core.CoreMain;
import com.nationwar.team.TeamMain;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import java.time.LocalTime;
import java.time.ZoneId;

public class CoreDamageListener implements Listener {
    private final CoreMain coreMain;
    private final TeamMain teamMain;

    public CoreDamageListener(CoreMain coreMain, TeamMain teamMain) {
        this.coreMain = coreMain;
        this.teamMain = teamMain;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Ghast ghast && event.getDamager() instanceof Player player) {
            int coreIdx = -1;
            for (int i : coreMain.coreEntities.keySet()) {
                if (coreMain.coreEntities.get(i).getUniqueId().equals(ghast.getUniqueId())) {
                    coreIdx = i;
                    break;
                }
            }
            if (coreIdx == -1) return;
            event.setCancelled(true);

            String team = teamMain.playerTeams.getOrDefault(player.getUniqueId(), "방랑자");
            if (team.equals("방랑자")) return;

            LocalTime now = LocalTime.now(ZoneId.of("Asia/Seoul"));
            if (now.getHour() < 19 || now.getHour() >= 20) return;

            double health = coreMain.coreHealth.get(coreIdx) - event.getFinalDamage();
            if (health <= 0) {
                coreMain.coreHealth.put(coreIdx, 5000.0);
                coreMain.coreOwners.put(coreIdx, team);
                coreMain.bossBars.get(coreIdx).setProgress(1.0);
                coreMain.saveCores(); // 점령 팀 변경 시 저장
            } else {
                coreMain.coreHealth.put(coreIdx, health);
                // 체력 변경 시 실시간 저장은 성능을 위해 선택적 (필요 시 호출)
            }
        }
    }
}