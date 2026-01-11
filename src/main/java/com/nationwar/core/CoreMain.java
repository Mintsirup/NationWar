package com.nationwar.core;

import com.nationwar.NationWar;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Ghast;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class CoreMain {

    // 코어 물리 구조 생성 (4x4 화이트 콘크리트)
    public static void buildCorePhysical(Location loc, int coreId) {
        World world = loc.getWorld();
        if (world == null) return;

        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();

        for (int x = 0; x < 4; x++) {
            for (int z = 0; z < 4; z++) {
                Block block = world.getBlockAt(cx + x, cy, cz + z);
                block.setType(Material.WHITE_CONCRETE);
            }
        }
        // 가스트는 4x4의 중앙(시작점+1.5) 지면에서 1칸 위에 소환
        spawnCoreGhast(new Location(world, cx + 1.5, cy + 1, cz + 1.5), coreId);
    }

    // 가스트 소환 및 고유 ID 부여
    public static void spawnCoreGhast(Location loc, int coreId) {
        Ghast ghast = loc.getWorld().spawn(loc, Ghast.class);
        ghast.setAI(false);
        ghast.setSilent(true);
        ghast.setGravity(false);
        ghast.setRemoveWhenFarAway(false); // 멀어져도 디스폰 방지 핵심!

        NamespacedKey key = new NamespacedKey(NationWar.getInstance(), "core_id");
        ghast.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, coreId);
        ghast.setCustomName("§f코어 " + coreId);
    }

    // 서버 재시작 후 코어 가스트 복구 로직
    public static boolean reloadCoresFromConfig() {
        try {
            List<CoreGson.CoreData> cores = CoreGson.getCores();
            World world = Bukkit.getWorlds().get(0);

            // 기존에 남아있을 수 있는 코어 가스트 제거
            world.getEntitiesByClass(Ghast.class).forEach(g -> {
                NamespacedKey key = new NamespacedKey(NationWar.getInstance(), "core_id");
                if (g.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
                    g.remove();
                }
            });

            // JSON 위치에 가스트 재소환
            for (CoreGson.CoreData data : cores) {
                if (data.x == 0 && data.y == 0 && data.z == 0) continue; // 설정 안 된 코어 스킵
                Location spawnLoc = new Location(world, data.x + 1.5, data.y + 1, data.z + 1.5);
                spawnCoreGhast(spawnLoc, data.id);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void startTimeChecker() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);
                int second = cal.get(Calendar.SECOND);

                // 20시 0분 0초가 되었을 때 초기화
                if (hour == 20 && minute == 0 && second == 0) {
                    for (CoreGson.CoreData core : CoreGson.getCores()) {
                        core.hp = 5000;
                    }
                    CoreGson.saveCores();
                    Bukkit.broadcastMessage("§e[!] 점령 시간이 종료되어 모든 코어 체력이 초기화되었습니다.");
                }
            }
        }.runTaskTimer(NationWar.getInstance(), 0L, 20L); // 1초마다 체크
    }

    public static boolean isCaptureTime() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
        int day = cal.get(Calendar.DAY_OF_WEEK);
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        boolean isCorrectDay = (day == Calendar.MONDAY || day == Calendar.WEDNESDAY || day == Calendar.FRIDAY);
        boolean isCorrectHour = (hour == 19); // 19:00 ~ 19:59

        return isCorrectDay && isCorrectHour;
    }
}