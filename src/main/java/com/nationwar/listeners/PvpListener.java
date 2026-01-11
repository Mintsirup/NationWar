package com.nationwar.listeners;

import com.nationwar.team.TeamMain;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PvpListener implements Listener {

    @EventHandler
    public void onPvp(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Player victim = (Player) event.getEntity();

            String damagerTeam = TeamMain.getPlayerTeam(damager);
            String victimTeam = TeamMain.getPlayerTeam(victim);

            // 같은 팀이고 방랑자가 아닐 경우 (방랑자는 서로 때리기 가능 혹은 설정에 따라 변경)
            if (!damagerTeam.equals("방랑자") && damagerTeam.equals(victimTeam)) {
                damager.sendMessage("§c[!] 같은 팀원은 공격할 수 없습니다.");
                event.setCancelled(true);
            }
        }
    }
}