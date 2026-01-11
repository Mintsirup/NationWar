package com.nationwar.listeners;

import com.nationwar.core.CoreGson;
import com.nationwar.team.TeamMain;
import org.bukkit.Bukkit;
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
    private final Map<UUID, BossBar> bars = new HashMap<>();
    private final Set<UUID> alerted = new HashSet<>();

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        String team = TeamMain.getPlayerTeam(p);
        CoreGson.CoreData nearest = null;

        for (CoreGson.CoreData core : CoreGson.getCores()) {
            double dist = p.getLocation().distance(new org.bukkit.Location(p.getWorld(), core.x, core.y, core.z));
            if (dist <= 250) { nearest = core; break; }
        }

        if (nearest != null) {
            updateBar(p, nearest);
            if (!nearest.owner.equals("없음") && !nearest.owner.equals(team)) {
                if (!alerted.contains(p.getUniqueId())) {
                    p.sendTitle("§c적 팀의 코어 주변에 접근했습니다!", "§e발광이 적용됩니다", 10, 40, 10);
                    alertOwnerTeam(nearest.owner, team, p.getName());
                    alerted.add(p.getUniqueId());
                }
                p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 30, 0, false, false));
            }
        } else {
            removeBar(p);
            alerted.remove(p.getUniqueId());
        }
    }

    private void updateBar(Player p, CoreGson.CoreData c) {
        BossBar bar = bars.computeIfAbsent(p.getUniqueId(), k -> Bukkit.createBossBar("", BarColor.RED, BarStyle.SOLID));
        bar.setTitle("코어 " + c.id + ": [" + (int)c.hp + "/5000]");
        bar.setProgress(Math.max(0, c.hp / 5000.0));
        bar.addPlayer(p);
    }

    private void removeBar(Player p) {
        BossBar bar = bars.remove(p.getUniqueId());
        if (bar != null) bar.removeAll();
    }

    private void alertOwnerTeam(String owner, String intruderTeam, String name) {
        String msg = "§c[알림] 당신의 코어에 " + intruderTeam + "팀의 " + name + "이(가) 접근했습니다!";
        Bukkit.getOnlinePlayers().forEach(p -> {
            if (TeamMain.getPlayerTeam(p).equals(owner)) p.sendMessage(msg);
        });
    }
}