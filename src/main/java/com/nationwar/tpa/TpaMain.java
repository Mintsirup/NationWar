package com.nationwar.tpa;

import com.nationwar.NationWar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TpaMain {
    private final NationWar plugin;

    // <수락할사람UUID, 보낸사람UUID>
    private final Map<UUID, UUID> tpaRequests = new HashMap<>();
    // <보낸사람UUID, 마지막요청시간>
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    public TpaMain(NationWar plugin) {
        this.plugin = plugin;
    }

    // TPA 요청 저장 및 60초 자동 만료 설정
    public void sendRequest(Player requester, Player target) {
        UUID targetUUID = target.getUniqueId();
        UUID requesterUUID = requester.getUniqueId();

        tpaRequests.put(targetUUID, requesterUUID);
        cooldowns.put(requesterUUID, System.currentTimeMillis());

        // 60초(1200틱) 후 자동 만료 태스크
        new BukkitRunnable() {
            @Override
            public void run() {
                // 아직 만료되지 않았고, 현재 저장된 요청이 이 요청인 경우에만 삭제
                if (tpaRequests.containsKey(targetUUID) && tpaRequests.get(targetUUID).equals(requesterUUID)) {
                    tpaRequests.remove(targetUUID);

                    // 온라인일 경우에만 만료 알림 전송
                    Player r = Bukkit.getPlayer(requesterUUID);
                    Player t = Bukkit.getPlayer(targetUUID);
                    if (r != null && r.isOnline()) r.sendMessage("§c" + (t != null ? t.getName() : "상대방") + "님에게 보낸 TPA 요청이 만료되었습니다.");
                    if (t != null && t.isOnline()) t.sendMessage("§c" + (r != null ? r.getName() : "상대방") + "님의 TPA 요청이 만료되었습니다.");
                }
            }
        }.runTaskLater(plugin, 1200L); // 20틱 * 60초 = 1200틱
    }

    public UUID getRequest(UUID targetUUID) {
        return tpaRequests.get(targetUUID);
    }

    public void removeRequest(UUID targetUUID) {
        tpaRequests.remove(targetUUID);
    }

    public long getRemainCoolDown(UUID requesterUUID) {
        if (!cooldowns.containsKey(requesterUUID)) return 0;

        long timePassed = System.currentTimeMillis() - cooldowns.get(requesterUUID);
        long twentyMinutes = 20 * 60 * 1000L;

        if (timePassed >= twentyMinutes) {
            return 0;
        }
        return twentyMinutes - timePassed;
    }
}