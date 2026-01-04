package com.nationwar.tpa;

import com.nationwar.NationWar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.*;

public class TpaMain {
    private final Map<UUID, UUID> pendingRequests = new HashMap<>(); // 수락자 ID, 요청자 ID
    private final Map<UUID, Long> cooldowns = new HashMap<>(); // 요청자 ID, 마지막 요청 시간
    private final long COOLDOWN_TIME = 20 * 60 * 1000; // 20분
    private final long EXPIRATION_TIME = 60 * 1000; // 1분

    public void sendRequest(Player from, Player to) {
        UUID fromId = from.getUniqueId();
        UUID toId = to.getUniqueId();

        // 쿨타임 체크
        if (cooldowns.containsKey(fromId)) {
            long remaining = (cooldowns.get(fromId) + COOLDOWN_TIME) - System.currentTimeMillis();
            if (remaining > 0) {
                from.sendMessage("§c쿨타임이 " + (remaining / 1000 / 60) + "분 " + (remaining / 1000 % 60) + "초 남았습니다.");
                return;
            }
        }

        pendingRequests.put(toId, fromId);
        cooldowns.put(fromId, System.currentTimeMillis());

        to.sendMessage("§f" + from.getName() + "이(가) 당신에게 tpa요청을 보냈습니다. 이 요청은 1분 후에 만료됩니다.");
        to.sendMessage("§a[수락(수락 누를 시 명령 실행한 플레이어를 수락을 누른 플레이어에게 tp됨)]");
        to.sendMessage("§c[거절(거절을 누를 시 명령을 실행한 플레이어에게 \"tpa요청이 거절되었습니다\"전송)]");

        // 1분 후 만료 처리
        new BukkitRunnable() {
            @Override
            public void run() {
                if (pendingRequests.containsKey(toId) && pendingRequests.get(toId).equals(fromId)) {
                    pendingRequests.remove(toId);
                }
            }
        }.runTaskLater(NationWar.getInstance(), 1200L); // 20틱 * 60초
    }

    public void acceptRequest(Player to) {
        UUID toId = to.getUniqueId();
        if (!pendingRequests.containsKey(toId)) {
            to.sendMessage("§c진행 중인 tpa 요청이 없습니다.");
            return;
        }

        Player from = Bukkit.getPlayer(pendingRequests.get(toId));
        if (from != null && from.isOnline()) {
            from.teleport(to.getLocation());
            from.sendMessage("§a" + to.getName() + "님에게 이동되었습니다.");
            to.sendMessage("§a요청을 수락했습니다.");
        }
        pendingRequests.remove(toId);
    }

    public void denyRequest(Player to) {
        UUID toId = to.getUniqueId();
        if (!pendingRequests.containsKey(toId)) {
            to.sendMessage("§c진행 중인 tpa 요청이 없습니다.");
            return;
        }

        Player from = Bukkit.getPlayer(pendingRequests.get(toId));
        if (from != null && from.isOnline()) {
            from.sendMessage("tpa요청이 거절되었습니다");
        }
        to.sendMessage("§c요청을 거절했습니다.");
        pendingRequests.remove(toId);
    }
}