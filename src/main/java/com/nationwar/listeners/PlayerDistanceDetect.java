package com.nationwar.listeners;

import com.nationwar.NationWar;
import com.nationwar.core.CoreGson;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class PlayerDistanceDetect extends BukkitRunnable {
    private final NationWar plugin;

    // 알림 중복 방지용: <침입자UUID, 점유팀이름>
    // 이미 알림을 보낸 침입자 상태를 기억합니다.
    private final Map<UUID, String> alertedInvaders = new HashMap<>();

    private final Map<UUID, BossBar> playerBossBars = new HashMap<>();

    public PlayerDistanceDetect(NationWar plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            UUID uuid = p.getUniqueId();
            String playerTeam = plugin.getTeamMain().getPlayerTeam(uuid);
            CoreGson.CoreInfo nearestCore = null;
            double minDistance = plugin.getConfig().getDouble("core.alert-distance", 250.0);

            // 1. 250블록 내에서 가장 가까운 코어 찾기 (팀 상관 없음)
            for (CoreGson.CoreInfo core : plugin.getCoreMain().getCoreData().cores) {
                Location coreLoc = new Location(p.getWorld(), core.x, core.y, core.z);
                if (!p.getWorld().equals(coreLoc.getWorld())) continue;

                double dist = p.getLocation().distance(coreLoc);
                if (dist <= minDistance) {
                    minDistance = dist;
                    nearestCore = core;
                }
            }

            // 2. 코어 근처(250m)라면 무조건 보스바 표시
            if (nearestCore != null) {
                updateBossBar(p, nearestCore);

                // [추가 로직] 적팀 코어일 경우에만 침입 알림 및 발광 효과 적용
                if (!nearestCore.owner.equals(playerTeam) && !nearestCore.owner.equals("없음")) {
                    if (!alertedInvaders.containsKey(uuid)) {
                        sendAlertToTeam(nearestCore.owner, p, playerTeam);
                        alertedInvaders.put(uuid, nearestCore.owner);
                        String invTitle    = plugin.getConfig().getString("invader-message.title",    "§4§l[!] 침입");
                        String invSubtitle = plugin.getConfig().getString("invader-message.subtitle", "§c적팀 코어 구역에 진입했습니다!");
                        p.sendTitle(invTitle, invSubtitle, 10, 40, 10);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 0, false, false));
                    }
                }
            } else {
                // 250m 밖으로 나가면 보스바 및 침입 효과 제거
                removeBossBar(p);
                if (alertedInvaders.containsKey(uuid)) {
                    alertedInvaders.remove(uuid);
                    p.removePotionEffect(PotionEffectType.GLOWING);
                    p.sendMessage(plugin.getConfig().getString("invader-message.leave", "§a[!] 구역을 벗어나 보스바와 발광 효과가 해제되었습니다."));
                }
            }
        }
    }

    private void updateBossBar(Player p, CoreGson.CoreInfo core) {
        BossBar bar = playerBossBars.get(p.getUniqueId());

        // 보스바가 없으면 생성
        if (bar == null) {
            bar = Bukkit.createBossBar("§e§l" + core.owner + " 팀의 코어", BarColor.RED, BarStyle.SOLID);
            bar.addPlayer(p);
            playerBossBars.put(p.getUniqueId(), bar);
        }

        // 보스바 내용 업데이트 (코어 체력 반영)
        double maxHp = plugin.getConfig().getDouble("core.hp", 5000);
        double healthPercent = core.hp / maxHp;
        if (healthPercent < 0) healthPercent = 0;
        if (healthPercent > 1) healthPercent = 1;

        bar.setProgress(healthPercent);
        String barTitle = plugin.getConfig().getString("format.bossbar-title", "§6§l{team} §f팀의 코어 §7| §c{hp} HP")
                .replace("{team}", core.owner)
                .replace("{hp}", String.valueOf((int) core.hp));
        bar.setTitle(barTitle);

        // 체력에 따라 색상 변경 (디테일)
        if (healthPercent > 0.6) bar.setColor(BarColor.GREEN);
        else if (healthPercent > 0.3) bar.setColor(BarColor.YELLOW);
        else bar.setColor(BarColor.RED);
    }

    void removeBossBar(Player p) {
        BossBar bar = playerBossBars.remove(p.getUniqueId());
        if (bar != null) {
            bar.removeAll();
        }
    }

    private void sendAlertToTeam(String teamName, Player invader, String invaderTeam) {
        List<String> members = plugin.getTeamMain().getData().teams.get(teamName);
        if (members == null) return;

        for (String uuidStr : members) {
            Player teamMember = Bukkit.getPlayer(UUID.fromString(uuidStr));
            if (teamMember != null && teamMember.isOnline()) {
                String alert    = plugin.getConfig().getString("invader-message.team-alert",    "§4§l[⚠] 침입자 경보!");
                String detail   = plugin.getConfig().getString("invader-message.team-detail",   "  §f적군 §c{invader} §7({team} 팀)§f이 코어 주변에 나타났습니다.")
                        .replace("{invader}", invader.getName()).replace("{team}", invaderTeam);
                String subtitle = plugin.getConfig().getString("invader-message.team-subtitle", "§c§l침입자 발생!");
                teamMember.sendMessage(" ");
                teamMember.sendMessage(alert);
                teamMember.sendMessage(detail);
                teamMember.playSound(teamMember.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 0.5f);
                teamMember.sendTitle("", subtitle, 0, 40, 0);
            }
        }
    }
}