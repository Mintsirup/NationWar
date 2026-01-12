package com.nationwar.listeners;

import com.nationwar.NationWar;
import com.nationwar.core.CoreMain;
import com.nationwar.team.TeamMain;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CoreDamageListener implements Listener {

    private final CoreMain coreMain;
    private final TeamMain teamMain;

    public CoreDamageListener(NationWar plugin) {
        this.coreMain = plugin.getCoreMain();
        this.teamMain = plugin.getTeamMain();
    }

    @EventHandler
    public void onCoreDamage(EntityDamageByEntityEvent event) {

        Entity entity = event.getEntity();

        // 코어가 아니면 패스
        if (!(entity instanceof EnderCrystal core)) return;

        // 공격자 확인 (근접 + 원거리)
        Player attacker = null;

        if (event.getDamager() instanceof Player p) {
            attacker = p;
        } else if (event.getDamager() instanceof org.bukkit.entity.Projectile proj
                && proj.getShooter() instanceof Player p) {
            attacker = p;
        }

        if (attacker == null) {
            event.setCancelled(true);
            return;
        }

        // 팀 없는 플레이어 차단
        if (!teamMain.hasTeam(attacker)) {
            attacker.sendMessage("§c팀에 소속되어야 코어를 공격할 수 있습니다.");
            event.setCancelled(true);
            return;
        }

        // 코어 정보 불러오기
        if (!coreMain.isCore(core)) {
            event.setCancelled(true);
            return;
        }

        String coreTeam = coreMain.getCoreTeam(core);
        String attackerTeam = teamMain.getTeam(attacker);

        // 같은 팀 코어 공격 차단
        if (coreTeam.equals(attackerTeam)) {
            attacker.sendMessage("§c자기 팀 코어는 공격할 수 없습니다.");
            event.setCancelled(true);
            return;
        }

        // 기본 데미지 무효화 (우리가 체력 관리)
        event.setCancelled(true);

        double damage = event.getDamage();
        coreMain.damageCore(core, damage, attacker);
    }
}
