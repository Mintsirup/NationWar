package com.nationwar.core;

import com.nationwar.NationWar;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

public class CoreMain {
    public List<Location> coreLocations = new ArrayList<>();
    public Map<Integer, Double> coreHealth = new HashMap<>();
    public Map<Integer, String> coreOwners = new HashMap<>();
    public Map<Integer, BossBar> bossBars = new HashMap<>();
    public Map<Integer, Ghast> coreEntities = new HashMap<>();

    public void spawnCores() {
        World world = Bukkit.getWorlds().get(0);
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            int x = random.nextInt(15001) - 7500;
            int z = random.nextInt(15001) - 7500;
            int y = world.getHighestBlockYAt(x, z);
            Location loc = new Location(world, x, y, z);

            coreLocations.add(loc);
            coreHealth.put(i, 5000.0);
            coreOwners.put(i, "없음");

            for (int dx = 0; dx < 4; dx++) {
                for (int dz = 0; dz < 4; dz++) {
                    loc.clone().add(dx, 0, dz).getBlock().setType(Material.WHITE_CONCRETE);
                }
            }

            BossBar bar = Bukkit.createBossBar("§d코어 #" + (i + 1), BarColor.PINK, BarStyle.SOLID);
            bossBars.put(i, bar);

            Ghast ghast = world.spawn(loc.clone().add(2, 1, 2), Ghast.class);
            ghast.setInvisible(true);
            ghast.setAI(false);
            ghast.setSilent(true);
            coreEntities.put(i, ghast);
        }
        startCaptureTimer();
    }

    private void startCaptureTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                LocalTime now = LocalTime.now(ZoneId.of("Asia/Seoul"));
                if (now.getHour() == 20 && now.getMinute() == 0 && now.getSecond() == 0) {
                    for (int i = 0; i < 6; i++) {
                        coreHealth.put(i, 5000.0);
                        if (bossBars.get(i) != null) bossBars.get(i).setProgress(1.0);
                    }
                    checkWinCondition();
                }
            }
        }.runTaskTimer(NationWar.getInstance(), 0L, 20L);
    }

    private void checkWinCondition() {
        String firstOwner = coreOwners.get(0);
        if (firstOwner.equals("없음")) return;

        for (int i = 1; i < 6; i++) {
            if (!coreOwners.get(i).equals(firstOwner)) return;
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendTitle("§6" + firstOwner + " 팀이 승리했습니다", "", 10, 70, 20);
            p.getWorld().spawn(p.getLocation(), org.bukkit.entity.Firework.class);
        }
    }
}