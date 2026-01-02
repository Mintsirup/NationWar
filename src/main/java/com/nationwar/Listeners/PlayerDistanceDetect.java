package com.nationwar.Listeners;

import com.nationwar.core.CoreMain;
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

    private static final Map<Player, BossBar> bossBars = new HashMap<>();
    private static final Set<Player> alerted = new HashSet<>();
    private static final Set<String> coreAlerted = new HashSet<>();

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Location loc = p.getLocation();

        boolean nearCore = false;

        for (int id : CoreMain.getCoreLocations().keySet()) {
            Location core = CoreMain.getCoreLocation(id);
            if (core == null) {
                continue;
            }

            double d = loc.distance(core);
            if (d <= 250) {
                nearCore = true;

                int hp = CoreMain.getCoreHealth(id);
                int max = 5000;
                double progress = (double) hp / max;
                if (progress < 0) {
                    progress = 0;
                }
                if (progress > 1) {
                    progress = 1;
                }

                BossBar bar = bossBars.get(p);
                if (bar == null) {
                    bar = Bukkit.createBossBar("코어 체력: " + hp + " / 5000", BarColor.WHITE, BarStyle.SOLID);
                    bar.addPlayer(p);
                    bossBars.put(p, bar);
                }
                bar.setTitle("코어 체력: " + hp + " / 5000");
                bar.setProgress(progress);

                String owner = CoreMain.getCoreOwner(id);
                String team = TeamMain.getTeam(p);

                if (!owner.equals("방랑자") && !owner.equals(team)) {
                    if (!alerted.contains(p)) {
                        p.sendTitle("적 팀의 코어 주변에 접근했습니다!", "발광이 적용됩니다", 10, 40, 10);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20, 0));
                        alerted.add(p);
                    }

                    String key = owner + "_" + p.getName() + "_" + id;
                    if (!coreAlerted.contains(key)) {
                        for (Player t : Bukkit.getOnlinePlayers()) {
                            if (TeamMain.getTeam(t).equals(owner)) {
                                t.sendMessage("당신의 코어에 " + team + "팀의 " + p.getName() + "님이 접근했습니다!");
                            }
                        }
                        coreAlerted.add(key);
                    }
                }

                break;
            }
        }

        if (!nearCore) {
            if (bossBars.containsKey(p)) {
                BossBar bar = bossBars.remove(p);
                bar.removeAll();
            }
            alerted.remove(p);
        }
    }
}
