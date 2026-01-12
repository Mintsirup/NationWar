package com.nationwar.core;

import com.nationwar.NationWar;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ghast;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Random;

public class CoreMain {
    private final NationWar plugin;
    private final File coreFile;
    private CoreGson.CoreData coreData;

    public CoreMain(NationWar plugin) {
        this.plugin = plugin;
        this.coreFile = new File(plugin.getDataFolder(), "core.json");
        this.coreData = CoreGson.load(coreFile);
    }

    public CoreGson.CoreData getCoreData() { return coreData; }

    public boolean reloadCoreFromConfig() { // 추가됨
        this.coreData = CoreGson.load(coreFile);
        if (this.coreData == null || this.coreData.cores.isEmpty()) return false;

        for (CoreGson.CoreInfo info : coreData.cores) {
            Location loc = new Location(Bukkit.getWorlds().get(0), info.x, info.y, info.z);
            buildCorePlate(loc);
            spawnCoreGhast(info.id, loc);
        }
        return true;
    }

    public void spawnCoreGhast(int id, Location loc) {
        Ghast ghast = (Ghast) loc.getWorld().spawnEntity(loc.add(0.5, 1, 0.5), EntityType.GHAST);
        ghast.setCustomName("§f코어 " + id);
        ghast.setAI(false);
        // 리스너에서 인식할 수 있도록 메타데이터 부여
        ghast.setMetadata("core_id", new FixedMetadataValue(plugin, id));
    }

    public void checkVictory() { // private -> public 수정
        // 승리 판정 로직
    }

    public void LoadCores() {
        // 기준서: 15000x15000 내 무작위 위치 지면 생성
        World world = Bukkit.getWorlds().get(0);
        Random random = new Random();
        coreData = new CoreGson.CoreData();

        for (int i = 0; i < 6; i++) {
            int x = random.nextInt(15001) - 7500;
            int z = random.nextInt(15001) - 7500;
            int y = world.getHighestBlockYAt(x, z);

            CoreGson.CoreInfo info = new CoreGson.CoreInfo();
            info.id = i;
            info.x = x; info.y = y; info.z = z;
            coreData.cores.add(info);

            Location loc = new Location(world, x, y, z);
            buildCorePlate(loc);
            spawnCoreGhast(i, loc);
        }
        saveCores();
    }

    public void buildCorePlate(Location loc) {
        // 기준서: 5x5 화이트 콘크리트
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                loc.clone().add(x, 0, z).getBlock().setType(Material.WHITE_CONCRETE);
            }
        }
    }

    public void startTimeChecker() {
        new BukkitRunnable() {
            @Override
            public void run() {
                LocalDateTime now = LocalDateTime.now();
                // 기준서: 20:00가 되면 모든 코어 체력 5000으로 자동 초기화
                if (now.getHour() == 20 && now.getMinute() == 0 && now.getSecond() == 0) {
                    for (CoreGson.CoreInfo core : coreData.cores) {
                        core.hp = 5000.0;
                    }
                    saveCores();
                    checkVictory(); // 승리 조건 체크
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public boolean isCaptureTime() {
        // 기준서: 월, 수, 금 19:00 ~ 20:00
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek day = now.getDayOfWeek();
        boolean isDay = (day == DayOfWeek.MONDAY || day == DayOfWeek.WEDNESDAY || day == DayOfWeek.FRIDAY);
        return isDay && (now.getHour() == 19);
    }

    public void saveCores() { CoreGson.save(coreFile, coreData); }

}