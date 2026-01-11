package com.nationwar.menu;

import com.nationwar.core.CoreGson;
import com.nationwar.NationWar;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class CoreTeleportHandler {
    private static final HashMap<UUID, BukkitTask> teleportTasks = new HashMap<>();

    // 텔레포트 시작
    public static void startTeleport(Player player, CoreGson.CoreData core) {
        if (core == null) return;

        cancelTeleport(player, false); // 기존 작업이 있다면 취소

        player.sendMessage("§e[!] 10초 후 코어로 이동합니다. 움직이면 취소됩니다!");

        BukkitTask task = new BukkitRunnable() {
            int count = 10;
            @Override
            public void run() {
                if (count <= 0) {
                    player.teleport(new Location(player.getWorld(), core.x, core.y + 1, core.z));
                    player.sendMessage("§a[!] 코어로 이동되었습니다.");
                    teleportTasks.remove(player.getUniqueId());
                    this.cancel();
                    return;
                }
                player.sendMessage("§7[!] 이동까지 " + count + "초...");
                count--;
            }
        }.runTaskTimer(NationWar.getInstance(), 0L, 20L);

        teleportTasks.put(player.getUniqueId(), task);
    }

    // 에러 해결: 텔레포트 중인지 확인
    public static boolean isTeleporting(Player player) {
        return teleportTasks.containsKey(player.getUniqueId());
    }

    // 에러 해결: 텔레포트 취소
    public static void cancelTeleport(Player player, boolean message) {
        if (teleportTasks.containsKey(player.getUniqueId())) {
            teleportTasks.get(player.getUniqueId()).cancel();
            teleportTasks.remove(player.getUniqueId());
            if (message) player.sendMessage("§c[!] 움직임이 감지되어 이동이 취소되었습니다.");
        }
    }
}