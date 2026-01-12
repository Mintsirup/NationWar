package com.nationwar.core;

import com.nationwar.NationWar;
import org.bukkit.*;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CoreMain {

    private final NationWar plugin;
    private final CoreGson coreGson;

    private final List<CoreData> cores = new ArrayList<>();
    private final NamespacedKey CORE_ID_KEY;

    public CoreMain(NationWar plugin) {
        this.plugin = plugin;
        this.coreGson = new CoreGson(plugin);
        this.CORE_ID_KEY = new NamespacedKey(plugin, "core_id");
    }

    /* ===================== 로드 / 저장 ===================== */

    public void loadCores() {
        cores.clear();
        cores.addAll(coreGson.load());
    }

    public void saveCores() {
        coreGson.save(cores);
    }

    /* ===================== 게임 시작 시 코어 생성 ===================== */

    public void generateCores(World world) {
        cores.clear();

        for (int i = 0; i < 6; i++) {
            Location loc = getRandomGround(world);
            buildCorePlate(loc);
            spawnCoreGhast(loc, i);

            cores.add(new CoreData(i,
                    loc.getBlockX(),
                    loc.getBlockY() + 1,
                    loc.getBlockZ()));
        }

        saveCores();
    }

    /* ===================== 구조물 ===================== */

    public void buildCorePlate(Location center) {
        Location base = center.clone();

        for (int x = -1; x <= 2; x++) {
            for (int z = -1; z <= 2; z++) {
                base.clone().add(x, 0, z)
                        .getBlock()
                        .setType(Material.WHITE_CONCRETE);
            }
        }
    }

    public void spawnCoreGhast(Location loc, int coreId) {
        Ghast ghast = loc.getWorld().spawn(loc.clone().add(0.5, 1, 0.5), Ghast.class);
        ghast.setSilent(true);
        ghast.setAI(false);
        ghast.setInvulnerable(false);
        ghast.setCustomName("§c코어 " + coreId);
        ghast.setCustomNameVisible(true);

        ghast.getPersistentDataContainer()
                .set(CORE_ID_KEY, PersistentDataType.INTEGER, coreId);
    }

    /* ===================== 불러오기 (/gamecontinue) ===================== */

    public boolean reloadCoreFromConfig(World world) {
        if (cores.isEmpty()) return false;

        for (CoreData data : cores) {
            Location loc = new Location(world, data.x, data.y - 1, data.z);
            buildCorePlate(loc);
            spawnCoreGhast(loc, data.id);
        }
        return true;
    }

    /* ===================== 점령 시간 ===================== */

    public boolean isCaptureTime() {
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek day = now.getDayOfWeek();
        int hour = now.getHour();

        boolean validDay = (day == DayOfWeek.MONDAY
                || day == DayOfWeek.WEDNESDAY
                || day == DayOfWeek.FRIDAY);

        return validDay && hour >= 19 && hour < 20;
    }

    boolean isCore(EnderCrystal core) {
        return false;
    }

    String getCoreTeam(EnderCrystal core) {
        return null;
    }

    void damageCore(EnderCrystal core, double damage, Player attacker) {

    }


    /* ===================== 유틸 ===================== */

    private Location getRandomGround(World world) {
        Random r = new Random();
        int x = r.nextInt(15000) - 7500;
        int z = r.nextInt(15000) - 7500;

        int y = world.getHighestBlockYAt(x, z);
        return new Location(world, x, y, z);
    }

    public List<CoreData> getCores() {
        return cores;
    }

    public NamespacedKey getCoreIdKey() {
        return CORE_ID_KEY;
    }
}
