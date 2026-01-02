package com.nationwar.tpa;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class TpaMain {

    private static final Map<Player, Player> requestMap = new HashMap<>();
    private static final Map<Player, Long> cooldown = new HashMap<>();

    public static void sendRequest(Player from, String name) {
        Player target = Bukkit.getPlayerExact(name);
        if (target == null) {
            return;
        }

        long now = System.currentTimeMillis();
        if (cooldown.containsKey(from)) {
            long remain = cooldown.get(from) - now;
            if (remain > 0) {
                return;
            }
        }

        cooldown.put(from, now + (20 * 60 * 1000));

        requestMap.put(target, from);

        target.sendMessage(from.getName() + "님이 당신에게 tpa요청을 보냈습니다. 이 요청은 1분 후에 만료됩니다.");
        target.sendMessage("§a/수락 §f또는 §c/거절 §f을 입력해 주십시오.");

        Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("NationWar"), () -> {
            requestMap.remove(target);
        }, 20 * 60);
    }

    public static void accept(Player target) {
        if (!requestMap.containsKey(target)) return;
        Player from = requestMap.remove(target);
        from.teleport(target.getLocation());
    }

    public static void deny(Player target) {
        if (!requestMap.containsKey(target)) return;
        Player from = requestMap.remove(target);
        from.sendMessage("tpa요청이 거절되었습니다.");
    }
}
