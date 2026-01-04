package com.nationwar.tpa;

import com.nationwar.NationWar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TpaMain {
    // 수신자 UUID - 발신자 UUID 매핑
    private final Map<UUID, UUID> requests = new HashMap<>();
    // 요청 만료 시간을 관리하기 위한 맵 (선택 사항)
    private final Map<UUID, Long> requestTime = new HashMap<>();

    /**
     * TPA 요청 보내기
     */
    public void sendRequest(Player from, Player to) {
        if (from.getUniqueId().equals(to.getUniqueId())) {
            from.sendMessage("§c자기 자신에게는 요청할 수 없습니다.");
            return;
        }

        requests.put(to.getUniqueId(), from.getUniqueId());
        requestTime.put(to.getUniqueId(), System.currentTimeMillis());

        from.sendMessage("§e" + to.getName() + "§f님에게 이동 요청을 보냈습니다.");
        to.sendMessage("§e" + from.getName() + "§f님이 이동 요청을 보냈습니다.");
        to.sendMessage("§a/tpa 수락 §f또는 §c/tpa 거절§f을 입력하세요. (30초 후 만료)");

        // 30초 후 자동 만료 태스크
        Bukkit.getScheduler().runTaskLater(NationWar.getInstance(), () -> {
            if (requests.containsKey(to.getUniqueId()) && requests.get(to.getUniqueId()).equals(from.getUniqueId())) {
                requests.remove(to.getUniqueId());
                requestTime.remove(to.getUniqueId());
                from.sendMessage("§c" + to.getName() + "§f님에게 보낸 이동 요청이 만료되었습니다.");
            }
        }, 20 * 30L);
    }

    /**
     * TPA 요청 수락
     */
    public void acceptRequest(Player to) {
        if (!requests.containsKey(to.getUniqueId())) {
            to.sendMessage("§c현재 받은 이동 요청이 없습니다.");
            return;
        }

        UUID fromUUID = requests.remove(to.getUniqueId());
        requestTime.remove(to.getUniqueId());
        Player from = Bukkit.getPlayer(fromUUID);

        if (from != null && from.isOnline()) {
            from.teleport(to.getLocation());
            from.sendMessage("§a" + to.getName() + "§f님이 요청을 수락하여 이동되었습니다.");
            to.sendMessage("§a" + from.getName() + "§f님의 요청을 수락했습니다.");
        } else {
            to.sendMessage("§c요청을 보낸 플레이어가 오프라인 상태입니다.");
        }
    }

    /**
     * TPA 요청 거절
     */
    public void denyRequest(Player to) {
        if (!requests.containsKey(to.getUniqueId())) {
            to.sendMessage("§c현재 받은 이동 요청이 없습니다.");
            return;
        }

        UUID fromUUID = requests.remove(to.getUniqueId());
        requestTime.remove(to.getUniqueId());
        Player from = Bukkit.getPlayer(fromUUID);

        if (from != null && from.isOnline()) {
            from.sendMessage("§c" + to.getName() + "§f님이 이동 요청을 거절했습니다.");
        }
        to.sendMessage("§c이동 요청을 거절했습니다.");
    }
}