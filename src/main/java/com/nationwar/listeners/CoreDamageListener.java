package com.nationwar.listeners;

import com.nationwar.NationWar;
import com.nationwar.core.CoreGson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CoreDamageListener implements Listener {
    private final NationWar plugin;

    public CoreDamageListener(NationWar plugin) { this.plugin = plugin; }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Ghast) || !(event.getDamager() instanceof Player)) return;

        // 기준서: 한국 시간 기준 월, 수, 금 19:00 ~ 20:00에만 코어를 공격할 수 있습니다.
        if (!plugin.getCoreMain().isCaptureTime()) {
            event.setCancelled(true);
            return;
        }

        Player damager = (Player) event.getDamager();
        String teamName = plugin.getTeamMain().getPlayerTeam(damager.getUniqueId());

        // 기준서: 방랑자는 코어를 점령할 수 없습니다.
        if (teamName.equals("방랑자")) {
            event.setCancelled(true);
            return;
        }

        Ghast ghast = (Ghast) event.getEntity();
        String name = ghast.getCustomName();
        if (name == null || !name.contains("코어")) return;

        int coreId = Integer.parseInt(name.replaceAll("[^0-9]", ""));
        CoreGson.CoreInfo core = plugin.getCoreMain().getCoreData().cores.get(coreId);

        // 데미지 처리
        core.hp -= event.getFinalDamage();

        if (core.hp <= 0) {
            // 기준서: 가스트의 체력 5000을 깎아 마지막 타격(막타)을 가한 팀이 소유권을 가져감
            core.owner = teamName;
            core.hp = 5000.0; // 기준서: 체력은 즉시 5000으로 초기화됩니다.

            Bukkit.broadcastMessage("§6[국가전쟁] §f" + teamName + " 팀이 " + coreId + "번 코어를 점령했습니다!");
            plugin.getCoreMain().saveCores();

            // 기준서: 한 팀이 6개의 코어를 모두 점령하면 게임 종료 (CoreMain에서 체크)
            plugin.getCoreMain().checkVictory();
        }
    }
}