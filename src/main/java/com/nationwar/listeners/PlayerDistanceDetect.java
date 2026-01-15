package com.nationwar.listeners;

import com.nationwar.NationWar;
import com.nationwar.core.CoreGson;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class PlayerDistanceDetect extends BukkitRunnable {
    private final NationWar plugin;
    private final Map<UUID, BossBar> activeBars = new HashMap<>();

    public PlayerDistanceDetect(NationWar plugin) { this.plugin = plugin; }

    @Override
    public void run() {
        for (Player invader : Bukkit.getOnlinePlayers()) {
            String invaderTeam = plugin.getTeamMain().getPlayerTeam(invader.getUniqueId());

            for (CoreGson.CoreInfo core : plugin.getCoreMain().getCoreData().cores) {
                // 1. 코어 주인이 없으면(방랑자/없음) 감지 안 함
                if (core.owner.equals("없음") || core.owner.equals("방랑자")) continue;

                // 2. 침입자가 코어 주인 팀과 같다면 감지 안 함
                if (invaderTeam.equals(core.owner)) continue;

                Location coreLoc = new Location(Bukkit.getWorld("world"), core.x, core.y, core.z);

                // 3. 월드가 같고 거리가 250m 이내인지 확인
                if (invader.getWorld().equals(coreLoc.getWorld())) {
                    double distance = invader.getLocation().distance(coreLoc);

                    if (distance <= 250) {
                        sendAlertToTeam(core.owner, invader, invaderTeam);
                    }
                }
            }
        }
    }

    private void sendAlertToTeam(String teamName, Player invader, String invaderTeam) {
        List<String> members = plugin.getTeamMain().getData().teams.get(teamName);
        if (members == null) return;

        for (String uuidStr : members) {
            Player teamMember = Bukkit.getPlayer(UUID.fromString(uuidStr));
            if (teamMember != null && teamMember.isOnline()) {
                // 화려한 경고 메시지 출력
                teamMember.sendMessage(" ");
                teamMember.sendMessage("§4§l[⚠] 침입자 경보: " + teamName + " 코어");
                teamMember.sendMessage("  §f적군 §c" + invader.getName() + "§7(" + invaderTeam + ")§f이 접근 중!");
                teamMember.playSound(teamMember.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 0.5f);
                teamMember.sendTitle("", "§c§l침입자 발생! §f(250m 이내)", 0, 20, 0);
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player intruder = event.getPlayer();
        String intruderTeam = plugin.getTeamMain().getPlayerTeam(intruder.getUniqueId());

        // 이동한 플레이어 주변 250블록 내의 코어 탐색
        for (CoreGson.CoreInfo core : plugin.getCoreMain().getCoreData().cores) {
            Location coreLoc = new Location(intruder.getWorld(), core.x, core.y, core.z);
            double dist = intruder.getLocation().distance(coreLoc);

            if (dist <= 250) {
                String ownerTeam = core.owner;

                // 1. [침입 알림] 다른 팀원이 우리 팀 코어에 접근했을 때
                if (!ownerTeam.equals("없음") && !ownerTeam.equals("방랑자") && !ownerTeam.equals(intruderTeam)) {
                    sendIntruderAlert(intruder, intruderTeam, ownerTeam, core.id);
                }

                // 2. [실시간 HP 액션바] 해당 코어의 소유 팀원들에게만 HP 표시
                sendCoreStatusToOwners(core);
            }
        }
    }

    private void sendIntruderAlert(Player intruder, String intruderTeam, String ownerTeam, int coreId) {
        String message = "§c§l[!] §e" + intruderTeam + "§f팀의 §e" + intruder.getName() + "§f이(가) 당신의 §6코어 " + coreId + "§f에 접근했습니다!";

        for (Player online : Bukkit.getOnlinePlayers()) {
            if (plugin.getTeamMain().getPlayerTeam(online.getUniqueId()).equals(ownerTeam)) {
                // 기준서: 소유 팀원 전원에게 메시지 및 사운드 전송
                online.sendMessage(message);
                online.playSound(online.getLocation(), Sound.ENTITY_WANDERING_TRADER_REAPPEARED, 1, 1);

                // 침입자 식별을 돕기 위해 타이틀 추가 송출
                online.sendTitle("", "§c코어 " + coreId + " 침입자 발생!", 0, 40, 10);
            }
        }

        // 침입자에게도 경고 및 발광 효과
        intruder.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 40, 0));
    }

    private void sendCoreStatusToOwners(CoreGson.CoreInfo core) {
        if (core.owner.equals("없음")) return;

        String hpBar = "§6§lCORE " + core.id + " §8| §f상태: " + getHpColor(core.hp) + (int)core.hp + " §7/ 5000";
        TextComponent textComponent = new TextComponent(hpBar);

        for (Player online : Bukkit.getOnlinePlayers()) {
            if (plugin.getTeamMain().getPlayerTeam(online.getUniqueId()).equals(core.owner)) {
                // 기준서: 소유 팀원들에게만 실시간 액션바 띄우기
                online.spigot().sendMessage(ChatMessageType.ACTION_BAR, textComponent);
            }
        }
    }



    private String getHpColor(double hp) {
        if (hp > 3500) return "§a";
        if (hp > 1500) return "§e";
        return "§c";
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