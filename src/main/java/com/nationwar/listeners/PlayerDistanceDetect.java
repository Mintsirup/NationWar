package com.nationwar.listeners;

import com.nationwar.core.CoreGson;
import com.nationwar.team.TeamMain;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class PlayerDistanceDetect implements Listener {
    private final HashMap<UUID, BossBar> activeBars = new HashMap<>();
    private final HashMap<UUID, Set<Integer>> notifiedCores = new HashMap<>();

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        String playerTeam = TeamMain.getPlayerTeam(player);
        Location pLoc = player.getLocation();
        boolean inAnyCoreRange = false;

        for (CoreGson.CoreData core : CoreGson.getCores()) {
            if (core.x == 0 && core.y == 0) continue;

            Location cLoc = new Location(player.getWorld(), core.x, core.y, core.z);
            if (pLoc.getWorld().equals(cLoc.getWorld()) && pLoc.distance(cLoc) <= 250) {
                inAnyCoreRange = true;

                // 1. 보스바
                BossBar bar = activeBars.computeIfAbsent(player.getUniqueId(), k -> Bukkit.createBossBar("", BarColor.GREEN, BarStyle.SOLID));
                bar.setTitle("§e[코어 " + core.id + "] §f소유: §b" + core.owner + " §7| §cHP: " + (int)core.hp);
                bar.setProgress(Math.max(0, Math.min(1.0, core.hp / 5000.0)));
                bar.addPlayer(player);

                // 2. 적 팀 코어 진입 시 (알림 + 발광)
                if (!core.owner.equals("없음") && !core.owner.equals(playerTeam)) {
                    Set<Integer> notifications = notifiedCores.computeIfAbsent(player.getUniqueId(), k -> new HashSet<>());
                    if (!notifications.contains(core.id)) {
                        player.sendTitle("§c적 팀의 코어 접근!", "§e발광이 적용됩니다", 10, 40, 10);

                        // 소유 팀에게 알림
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (TeamMain.getPlayerTeam(p).equals(core.owner)) {
                                p.sendMessage("§6[!] §e코어에 " + playerTeam + "팀의 " + player.getName() + " 접근!");
                            }
                        }
                        notifications.add(core.id);
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 30, 0, false, false));
                }
                break;
            }
        }

        if (!inAnyCoreRange) {
            if (activeBars.containsKey(player.getUniqueId())) {
                activeBars.get(player.getUniqueId()).removePlayer(player);
                activeBars.remove(player.getUniqueId());
            }
            notifiedCores.remove(player.getUniqueId());
        }
    }
}