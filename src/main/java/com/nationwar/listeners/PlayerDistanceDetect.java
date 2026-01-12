package com.nationwar.listeners;

import com.nationwar.NationWar;
import com.nationwar.core.CoreGson;
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
    private final NationWar plugin;
    private final Map<UUID, BossBar> activeBars = new HashMap<>();

    public PlayerDistanceDetect(NationWar plugin) { this.plugin = plugin; }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        String playerTeam = plugin.getTeamMain().getPlayerTeam(player.getUniqueId());
        CoreGson.CoreInfo nearestCore = null;
        double minDistance = 251.0;

        for (CoreGson.CoreInfo core : plugin.getCoreMain().getCoreData().cores) {
            Location coreLoc = new Location(player.getWorld(), core.x, core.y, core.z);
            double dist = player.getLocation().distance(coreLoc);
            if (dist <= 250 && dist < minDistance) {
                minDistance = dist;
                nearestCore = core;
            }
        }

        if (nearestCore != null) {
            // 기준서: 범위 진입 시 상단에 보스바 표시
            showBossBar(player, nearestCore);

            // 기준서: 적군 코어 접근 시 타이틀 알림과 발광 효과 부여, 소유 팀원에게 경고 전송
            if (!nearestCore.owner.equals("없음") && !nearestCore.owner.equals(playerTeam)) {
                player.sendTitle("§c[!] 침입 알림", "§f적국 코어 중심부에 접근 중입니다.", 0, 20, 0);
                player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20, 0));

                // 소유 팀원에게 경고
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (plugin.getTeamMain().getPlayerTeam(p.getUniqueId()).equals(nearestCore.owner)) {
                        p.sendMessage("§e[경고] §f코어 " + nearestCore.id + "에 침입자가 발생했습니다!");
                    }
                }
            }
        } else {
            // 기준서: 코어 반경(250 블럭)을 벗어나면 코어와 관련된 모든 것을 없앤다.
            removeBossBar(player);
        }
    }

    private void showBossBar(Player p, CoreGson.CoreInfo core) {
        BossBar bar = activeBars.get(p.getUniqueId());
        String title = "코어 " + core.id + ": [" + (int)core.hp + "/5000]";
        if (bar == null) {
            bar = Bukkit.createBossBar(title, BarColor.RED, BarStyle.SOLID);
            bar.addPlayer(p);
            activeBars.put(p.getUniqueId(), bar);
        }
        bar.setTitle(title);
        bar.setProgress(core.hp / 5000.0);
    }

    private void removeBossBar(Player p) {
        BossBar bar = activeBars.remove(p.getUniqueId());
        if (bar != null) bar.removeAll();
    }
}