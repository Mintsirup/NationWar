package com.nationwar.listeners;

import com.nationwar.core.CoreMain;
import com.nationwar.team.TeamMain;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;

public class PlayerDistanceDetect implements Listener {

    private final Set<String> notified = new HashSet<>();

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        for (int i = 1; i <= 6; i++) {
            Location core = CoreMain.getCoreLocation(i);
            if (core == null) continue;

            double distance = core.distance(player.getLocation());

            if (distance <= 250) {
                CoreMain.showBossBar(player, i);

                String owner = CoreMain.getCoreOwner(i);
                String playerTeam = TeamMain.getPlayerTeam(player);

                if (!owner.equals(playerTeam)) {
                    String key = player.getUniqueId() + ":" + i;
                    if (notified.contains(key)) return;

                    notified.add(key);

                    player.sendTitle(
                            "적 팀의 코어 주변에 접근했습니다!",
                            "발광이 적용됩니다",
                            10, 40, 10
                    );

                    player.addPotionEffect(new PotionEffect(
                            PotionEffectType.GLOWING,
                            20,
                            0,
                            false,
                            false
                    ));

                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (TeamMain.getPlayerTeam(p).equals(owner)) {
                            p.sendMessage("당신의 코어에 " + playerTeam + "팀의 " + player.getName() + "이(가) 접근했습니다!");
                        }
                    }
                }
            } else {
                CoreMain.hideBossBar(player, i);
                notified.remove(player.getUniqueId() + ":" + i);
            }
        }
    }
}
