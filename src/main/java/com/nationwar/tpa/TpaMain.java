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
    // <수신자, 요청자>, <요청자, 쿨타임종료시간>
    private final Map<UUID, UUID> requests = new HashMap<>();
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    public TpaMain(NationWar plugin) { this.plugin = plugin; }

    public void sendRequest(Player sender, Player target) {
        UUID sId = sender.getUniqueId();
        UUID tId = target.getUniqueId();
        requests.put(tId, sId);

        // 기준서: 1분 이내에 요청을 받지 않으면 거절되었다고 요청자에게 알림
        new BukkitRunnable() {
            @Override
            public void run() {
                if (requests.containsKey(tId) && requests.get(tId).equals(sId)) {
                    requests.remove(tId);
                    if (Bukkit.getPlayer(sId) != null) {
                        Bukkit.getPlayer(sId).sendMessage("tpa요청이 거절되었습니다 (시간 초과)");
                    }
                }
            }
        }.runTaskLater(plugin, 1200L); // 1분

        cooldowns.put(sender.getUniqueId(), System.currentTimeMillis() + (20 * 60 * 1000));
    }

    public long getRemainCoolDown(UUID uuid) {
        if (!cooldowns.containsKey(uuid)) return 0;
        return cooldowns.get(uuid) - System.currentTimeMillis();
    }
}