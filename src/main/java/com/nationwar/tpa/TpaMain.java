package com.nationwar.tpa;

import com.nationwar.NationWar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class TpaMain {

    private final NationWar plugin;

    // 요청자 -> 요청 정보
    private final Map<UUID, TpaRequest> requests = new HashMap<>();
    // 쿨타임
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    private static final long COOLDOWN_TIME = 20 * 60 * 1000; // 20분
    private static final long EXPIRE_TIME = 60 * 1000; // 1분

    public TpaMain(NationWar plugin) {
        this.plugin = plugin;
    }

    /* ===================== 요청 ===================== */

    public boolean hasCooldown(Player player) {
        return cooldowns.containsKey(player.getUniqueId())
                && System.currentTimeMillis() - cooldowns.get(player.getUniqueId()) < COOLDOWN_TIME;
    }

    public long getRemainCooldown(Player player) {
        long passed = System.currentTimeMillis() - cooldowns.getOrDefault(player.getUniqueId(), 0L);
        return Math.max(0, (COOLDOWN_TIME - passed) / 1000);
    }

    public void sendRequest(Player from, Player to) {
        TpaRequest req = new TpaRequest(from.getUniqueId(), to.getUniqueId());
        requests.put(from.getUniqueId(), req);
        cooldowns.put(from.getUniqueId(), System.currentTimeMillis());

        to.sendMessage("§e" + from.getName() + "§f이(가) 당신에게 TPA 요청을 보냈습니다.");
        to.sendMessage("§7이 요청은 1분 후에 만료됩니다.");
        to.sendMessage("§a[수락] §c[거절] §7(/tpa accept | /tpa deny)");

        from.sendMessage("§aTPA 요청을 보냈습니다.");

        // 만료 처리
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!requests.containsKey(from.getUniqueId())) return;

                requests.remove(from.getUniqueId());
                from.sendMessage("§cTPA 요청이 시간 초과로 거절되었습니다.");
                to.sendMessage("§7TPA 요청이 만료되었습니다.");
            }
        }.runTaskLater(plugin, 20 * 60);
    }

    /* ===================== 수락 / 거절 ===================== */

    public boolean hasRequest(Player target) {
        return requests.values().stream()
                .anyMatch(r -> r.target().equals(target.getUniqueId()));
    }

    private Optional<TpaRequest> getRequestByTarget(Player target) {
        return requests.values().stream()
                .filter(r -> r.target().equals(target.getUniqueId()))
                .findFirst();
    }

    public void accept(Player target) {
        Optional<TpaRequest> opt = getRequestByTarget(target);
        if (opt.isEmpty()) return;

        TpaRequest req = opt.get();
        Player from = Bukkit.getPlayer(req.from());

        if (from == null) {
            target.sendMessage("§c요청자가 오프라인입니다.");
            requests.remove(req.from());
            return;
        }

        from.teleport(target.getLocation());
        from.sendMessage("§aTPA 요청이 수락되었습니다.");
        target.sendMessage("§aTPA 요청을 수락했습니다.");

        requests.remove(req.from());
    }

    public void deny(Player target) {
        Optional<TpaRequest> opt = getRequestByTarget(target);
        if (opt.isEmpty()) return;

        TpaRequest req = opt.get();
        Player from = Bukkit.getPlayer(req.from());

        if (from != null) {
            from.sendMessage("§cTPA 요청이 거절되었습니다.");
        }
        target.sendMessage("§7TPA 요청을 거절했습니다.");

        requests.remove(req.from());
    }

    /* ===================== 내부 레코드 ===================== */

    private record TpaRequest(UUID from, UUID target) {}
}
