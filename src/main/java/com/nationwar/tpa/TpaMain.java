package com.nationwar.tpa;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TpaMain {

    private static final Map<UUID, UUID> requests = new HashMap<>();
    private static final Map<UUID, Long> requestTime = new HashMap<>();
    private static final Map<UUID, Long> cooldown = new HashMap<>();

    private static final long REQUEST_EXPIRE = 60 * 1000;
    private static final long COOLDOWN_TIME = 20 * 60 * 1000;

    public static boolean hasCooldown(Player player) {
        if (!cooldown.containsKey(player.getUniqueId())) return false;
        return System.currentTimeMillis() - cooldown.get(player.getUniqueId()) < COOLDOWN_TIME;
    }

    public static void sendRequest(Player from, Player to) {
        requests.put(to.getUniqueId(), from.getUniqueId());
        requestTime.put(to.getUniqueId(), System.currentTimeMillis());
        cooldown.put(from.getUniqueId(), System.currentTimeMillis());
    }

    public static boolean hasRequest(Player target) {
        if (!requests.containsKey(target.getUniqueId())) return false;
        long time = requestTime.get(target.getUniqueId());
        return System.currentTimeMillis() - time <= REQUEST_EXPIRE;
    }

    public static Player getRequester(Player target) {
        UUID uuid = requests.get(target.getUniqueId());
        if (uuid == null) return null;
        return Bukkit.getPlayer(uuid);
    }

    public static void accept(Player target) {
        Player from = getRequester(target);
        if (from == null) return;

        from.teleport(target.getLocation());
        from.sendMessage("TPA 요청이 수락되었습니다.");
        target.sendMessage("TPA 요청을 수락했습니다.");

        clear(target);
    }

    public static void deny(Player target) {
        Player from = getRequester(target);
        if (from != null) {
            from.sendMessage("TPA 요청이 거절되었습니다.");
        }
        target.sendMessage("TPA 요청을 거절했습니다.");
        clear(target);
    }

    public static void clear(Player target) {
        requests.remove(target.getUniqueId());
        requestTime.remove(target.getUniqueId());
    }
}
