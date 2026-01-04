package com.nationwar.core;

import com.nationwar.NationWar;
import com.nationwar.team.TeamMain;
import org.bukkit.*;
import org.bukkit.boss.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

public class CoreMain implements Listener {

    private final NationWar plugin;
    private final Map<Integer, Location> coreLocations = new HashMap<>();
    private final Map<Integer, Integer> coreHp = new HashMap<>();
    private final Map<Integer, String> coreOwner = new HashMap<>();
    private final Map<Integer, BossBar> bossBars = new HashMap<>();

    private boolean gameEnded = false;

    private static final int MAX_HP = 5000;

    public CoreMain(NationWar plugin) {
        this.plugin = plugin;
    }

    public void generateCores(World world) {
        Random random = new Random();

        for (int i = 1; i <= 6; i++) {
            int x = random.nextInt(14000) - 7000;
            int z = random.nextInt(14000) - 7000;
            int y = world.getHighestBlockYAt(x, z) + 1;

            Location base = new Location(world, x, y, z);

            for (int dx = 0; dx < 4; dx++)
                for (int dy = 0; dy < 4; dy++)
                    for (int dz = 0; dz < 4; dz++)
                        base.clone().add(dx, dy, dz).getBlock().setType(Material.WHITE_CONCRETE);

            coreLocations.put(i, base);
            coreHp.put(i, MAX_HP);
            coreOwner.put(i, "NONE");

            BossBar bar = Bukkit.createBossBar(
                    "코어 #" + i,
                    BarColor.WHITE,
                    BarStyle.SEGMENTED_10
            );
            bar.setVisible(true);
            bossBars.put(i, bar);
        }
    }

    private void completeCapture(int coreId) {
        String team = TeamMain.getNearestTeam(coreLocations.get(coreId));
        if (team == null) return;

        coreOwner.put(coreId, team);
        coreHp.put(coreId, MAX_HP);
        bossBars.get(coreId).setProgress(1.0);

        Bukkit.broadcastMessage("§a[국가전쟁] §f코어 #" + coreId + "가 §e" + team + " §f팀에게 점령되었습니다.");

        checkWinCondition(team);
    }
    private void checkWinCondition(String team) {
        if (gameEnded) return;

        for (String owner : coreOwner.values()) {
            if (!team.equals(owner)) return;
        }

        endGame(team);
    }

    private void endGame(String winner) {
        gameEnded = true;

        for (Player player : Bukkit.getOnlinePlayers()) {
            String team = TeamMain.getPlayerTeam(player);

            boolean isWinner = winner.equals(team);

            int fireworkCount = isWinner ? 3 : 1;

            for (int i = 0; i < fireworkCount; i++) {
                Firework fw = player.getWorld().spawn(player.getLocation(), Firework.class);
                FireworkMeta meta = fw.getFireworkMeta();

                meta.addEffect(
                        FireworkEffect.builder()
                                .with(FireworkEffect.Type.BALL_LARGE)
                                .withColor(isWinner ? Color.YELLOW : Color.WHITE)
                                .withFade(Color.WHITE)
                                .trail(true)
                                .flicker(true)
                                .build()
                );

                meta.setPower(1);
                fw.setFireworkMeta(meta);
            }

            if (isWinner) {
                player.sendTitle(
                        "§6승리하셨습니다!",
                        "§f" + winner + " 팀이 전쟁에서 승리했습니다",
                        10, 80, 20
                );

                player.sendMessage("§a[국가전쟁] §f축하드립니다! 당신의 팀이 전쟁에서 승리했습니다!");
            } else {
                player.sendTitle(
                        "§c게임이 종료되었습니다",
                        "§f수고하셨습니다",
                        10, 80, 20
                );
            }
        }

        Bukkit.broadcastMessage("§a[국가전쟁] §f게임이 종료되었습니다. 수고하셨습니다.");

        Bukkit.getWorlds().forEach(world -> world.setPVP(false));
    }

    @EventHandler
    public void onGhastDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Ghast)) return;

        for (int id : coreLocations.keySet()) {
            Location core = coreLocations.get(id);
            if (event.getEntity().getLocation().distance(core) > 3) continue;

            int hp = coreHp.get(id) - 50;
            coreHp.put(id, hp);

            BossBar bar = bossBars.get(id);
            bar.setProgress(Math.max(0, hp / (double) MAX_HP));

            if (hp <= 0) {
                startCapture(id);
            }
        }
    }

    private void startCapture(int coreId) {
        LocalTime now = LocalTime.now(ZoneId.of("Asia/Seoul"));
        if (now.isBefore(LocalTime.of(19, 0)) || now.isAfter(LocalTime.of(20, 0))) return;

        new BukkitRunnable() {
            int time = 30;

            @Override
            public void run() {
                if (time-- <= 0) {
                    completeCapture(coreId);
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void completeCapture(int coreId) {
        String team = TeamMain.getNearestTeam(coreLocations.get(coreId));
        if (team == null) return;

        coreOwner.put(coreId, team);
        coreHp.put(coreId, MAX_HP);
        bossBars.get(coreId).setProgress(1.0);

        Bukkit.broadcastMessage("§a[국가전쟁] §f코어 #" + coreId + "가 §e" + team + " §f팀에게 점령되었습니다.");
    }
}
