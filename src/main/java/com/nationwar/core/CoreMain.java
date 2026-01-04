package com.nationwar.core;

import com.nationwar.NationWar;
import org.bukkit.*;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Ghast;
import java.util.*;

public class CoreMain {
    public List<Location> coreLocations = new ArrayList<>();
    public Map<Integer, Double> coreHealth = new HashMap<>();
    public Map<Integer, String> coreOwners = new HashMap<>();
    public Map<Integer, BossBar> bossBars = new HashMap<>();
    public Map<Integer, Ghast> coreEntities = new HashMap<>();
    private final CoreGson coreGson = new CoreGson();

    public void spawnCores() {
        World world = Bukkit.getWorlds().get(0);
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            // ... (기존 스폰 로직 동일)
            int x = random.nextInt(15001) - 7500;
            int z = random.nextInt(15001) - 7500;
            int y = world.getHighestBlockYAt(x, z);
            Location loc = new Location(world, x, y, z);

            coreLocations.add(loc);
            coreHealth.put(i, 5000.0);
            coreOwners.put(i, "없음");

            // 코어 설치 및 엔티티 생성 로직 생략 (기존과 동일)
        }
        saveCores(); // 생성 후 저장
    }

    public void saveCores() {
        Map<String, Object> data = new HashMap<>();
        List<Map<String, Object>> coreList = new ArrayList<>();

        for (int i = 0; i < coreLocations.size(); i++) {
            Map<String, Object> coreInfo = new HashMap<>();
            Location loc = coreLocations.get(i);

            coreInfo.put("id", i);
            coreInfo.put("world", loc.getWorld().getName());
            coreInfo.put("x", loc.getX());
            coreInfo.put("y", loc.getY());
            coreInfo.put("z", loc.getZ());
            coreInfo.put("health", coreHealth.get(i));
            coreInfo.put("owner", coreOwners.get(i));

            coreList.add(coreInfo);
        }

        data.put("cores", coreList);
        coreGson.save(data);
    }

    // 코어 데미지 처리 시에도 saveCores()를 호출하도록 CoreDamageListener 등에서 연동 필요
}