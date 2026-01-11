package com.nationwar.tpa;

import com.nationwar.NationWar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TpaMain {

    private final NationWar plugin;

    // 요청자 → 대상
    private final Map<UUID, UUID> requests = new HashMap<>();
    // 요청자 → 만료 태스크
    private final Map<UUID, BukkitTask> expireTasks = new HashMap<>();
    // 요청자 → 마지막 사용 시간
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    private static final long COOLDOWN = 20 * 60 * 1000L; // 20분
    private static final long EXPIRE = 60 * 20L; // 1분 (틱)

    public TpaMain(NationWar plugin) {
        this.plugin = plugin;
    }

    public boolean hasCooldown(Player player) {
        if (!cooldowns.containsKey(player.getUniqueId())) return false;
        return System.currentTimeMillis() - cooldowns.get(player.getUniqueId()) < COOLDOWN;
    }

    public long getRemainCooldown(Player player) {
        return (COOLDOWN - (System.currentTimeMillis() - cooldowns.get(player.getUniqueId()))) / 1000;
    }

    public void sendRequest(Player from, Player to) {
        requests.put(from.getUniqueId(), to.getUniqueId());
        cooldowns.put(from.getUniqueId(), System.currentTimeMillis());

        BukkitTask task = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (requests.containsKey(from.getUniqueId())) {
                requests.remove(from.getUniqueId());
                from.sendMessage("§cTPA 요청이 만료되었습니다.");
            }
        }, EXPIRE);

        expireTasks.put(from.getUniqueId(), task);
    }

    public void accept(Player to, Player from) {
        requests.remove(from.getUniqueId());
        cancelExpire(from);

        from.teleport(to.getLocation());
        from.sendMessage("§aTPA 요청이 수락되었습니다.");
        to.sendMessage("§aTPA 요청을 수락하셨습니다.");
    }

    public void deny(Player to, Player from) {
        requests.remove(from.getUniqueId());
        cancelExpire(from);

        from.sendMessage("§cTPA 요청이 거절되었습니다.");
        to.sendMessage("§eTPA 요청을 거절하셨습니다.");
    }

    public boolean hasRequest(Player from, Player to) {
        return requests.containsKey(from.getUniqueId())
                && requests.get(from.getUniqueId()).equals(to.getUniqueId());
    }

    private void cancelExpire(Player from) {
        BukkitTask task = expireTasks.remove(from.getUniqueId());
        if (task != null) task.cancel();
    }
}
