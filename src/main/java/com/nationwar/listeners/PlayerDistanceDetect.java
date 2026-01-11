package com.nationwar.listeners;

import com.google.gson.JsonObject;
import com.nationwar.NationWar;
import com.nationwar.core.CoreMain;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDistanceDetect implements Listener {

    private final NationWar plugin;
    private final CoreMain coreMain;

    // 플레이어별 보스바
    private final Map<UUID, BossBar> bossBars = new HashMap<>();

    public PlayerDistanceDetect(NationWar plugin, CoreMain coreMain) {
        this.plugin = plugin;
        this.coreMain = coreMain;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // 블록 이동 없으면 무시
        if (event.getFrom().getBlockX() == event.getTo().getBlockX()
                && event.getFrom().getBlockY() == event.getTo().getBlockY()
                && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }

        boolean inAnyCore = false;

        for (JsonObject core : coreMain.getCores().values()) {
            Location coreLoc = getCoreLocation(player.getWorld(), core);
            if (coreLoc == null) continue;

            double distance = player.getLocation().distance(coreLoc);

            if (distance <= 250) {
                inAnyCore = true;
                showBossBar(player, core);
                handleIntruder(player, core);
                break;
            }
        }

        if (!inAnyCore) {
            removeBossBar(player);
        }
    }

    private Location getCoreLocation(World world, JsonObject core) {
        if (!core.has("x") || !core.has("y") || !core.has("z")) return null;

        int x = core.get("x").getAsInt();
        int y = core.get("y").getAsInt();
        int z = core.get("z").getAsInt();

        if (y <= 0) return null;

        return new Location(world, x + 0.5, y + 1, z + 0.5);
    }

    private void showBossBar(Player player, JsonObject core) {
        BossBar bar = bossBars.get(player.getUniqueId());

        double hp = core.get("hp").getAsDouble();
        int id = core.get("id").getAsInt();

        if (bar == null) {
            bar = Bukkit.createBossBar(
                    "코어 " + id + " : [" + (int) hp + "/5000]",
                    BarColor.RED,
                    BarStyle.SOLID
            );
            bar.addPlayer(player);
            bossBars.put(player.getUniqueId(), bar);
        }

        bar.setTitle("코어 " + id + " : [" + (int) hp + "/5000]");
        bar.setProgress(Math.max(0, Math.min(1, hp / 5000.0)));
    }

    private void removeBossBar(Player player) {
        BossBar bar = bossBars.remove(player.getUniqueId());
        if (bar != null) {
            bar.removeAll();
        }
    }

    private void handleIntruder(Player player, JsonObject core) {
        String owner = core.get("owner").getAsString();

        // 아직 팀 시스템 전 → owner가 없음이 아닐 때만 처리
        if (owner.equals("없음")) return;

        // 타이틀
        player.sendTitle("§c적 코어 침입!", "§f경고: 적의 코어에 접근했습니다.", 5, 20, 5);

        // 발광 1초
        player.addPotionEffect(new PotionEffect(
                PotionEffectType.GLOWING,
                20,
                0,
                false,
                false
        ));

        // 팀 알림은 팀 시스템 이후에 연결
    }
}
