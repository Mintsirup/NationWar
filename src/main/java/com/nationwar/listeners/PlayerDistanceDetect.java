package com.nationwar.listeners;

import com.nationwar.NationWar;
import com.nationwar.core.CoreGson;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
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

    public PlayerDistanceDetect(NationWar plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player invader : Bukkit.getOnlinePlayers()) {
            UUID invaderUUID = invader.getUniqueId();
            String invaderTeam = plugin.getTeamMain().getPlayerTeam(invaderUUID);

            boolean isInsideAnyCore = false; // 현재 어떤 코어라도 근처에 있는지 체크

            for (CoreGson.CoreInfo core : plugin.getCoreMain().getCoreData().cores) {
                // 주인이 없거나 자기 팀 코어라면 무시
                if (core.owner.equals("없음") || core.owner.equals("방랑자")) continue;
                if (invaderTeam.equals(core.owner)) continue;

                Location coreLoc = new Location(invader.getWorld(), core.x, core.y, core.z);

                // 월드가 다르면 거리 계산 불가하므로 패스
                if (!invader.getWorld().equals(coreLoc.getWorld())) continue;

                double distance = invader.getLocation().distance(coreLoc);

                if (distance <= 250) {
                    isInsideAnyCore = true;

                    // 1. 처음 진입했을 때만 팀원들에게 알림 전송
                    if (!alertedInvaders.containsKey(invaderUUID) || !alertedInvaders.get(invaderUUID).equals(core.owner)) {
                        sendAlertToTeam(core.owner, invader, invaderTeam);
                        alertedInvaders.put(invaderUUID, core.owner);

                        // 2. 침입자 본인에게 타이틀 및 효과 부여
                        invader.sendTitle("§4§l[!] 경고", "§c적팀의 코어 반경(250m)에 진입했습니다!", 10, 40, 10);
                        invader.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 0, false, false));
                        invader.playSound(invader.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 0.5f, 1.5f);
                    }
                }
            }

            // 3. 만약 어떤 코어 주변(250m)에도 있지 않다면 상태 초기화 및 발광 제거
            if (!isInsideAnyCore && alertedInvaders.containsKey(invaderUUID)) {
                alertedInvaders.remove(invaderUUID);
                invader.removePotionEffect(PotionEffectType.GLOWING);
                invader.sendMessage("§a§l[!] §f적팀 코어의 감지 범위에서 벗어났습니다.");
            }
        }
    }

    private void sendAlertToTeam(String teamName, Player invader, String invaderTeam) {
        List<String> members = plugin.getTeamMain().getData().teams.get(teamName);
        if (members == null) return;

        for (String uuidStr : members) {
            Player teamMember = Bukkit.getPlayer(UUID.fromString(uuidStr));
            if (teamMember != null && teamMember.isOnline()) {
                teamMember.sendMessage(" ");
                teamMember.sendMessage("§4§l[⚠] 침입자 경보!");
                teamMember.sendMessage("  §f적군 §c" + invader.getName() + " §7(" + invaderTeam + " 팀)§f이 코어 주변에 나타났습니다.");
                teamMember.playSound(teamMember.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 0.5f);
                teamMember.sendTitle("", "§c§l침입자 발생!", 0, 40, 0);
            }
        }
    }
}