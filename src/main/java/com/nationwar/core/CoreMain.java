package com.nationwar.core;

import com.nationwar.NationWar;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ghast;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

public class CoreMain {
    private final NationWar plugin;
    private final File coreFile;
    private CoreGson.CoreData coreData;
    private boolean gameStarted = false;

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
        // [중요] .clone()을 사용하여 원본 데이터(core.json 좌표)가 수정되는 것을 방지합니다.
        // .add(0.5, 1, 0.5)를 통해 블록 정중앙 위쪽에 소환합니다.
        Location spawnLoc = loc.clone().add(0.5, 1.0, 0.5);

        Ghast ghast = (Ghast) spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.GHAST);

        ghast.setCustomName("§f코어 " + id);
        ghast.setCustomNameVisible(true);
        ghast.setAI(false);
        ghast.setSilent(true);
        ghast.setRemoveWhenFarAway(false);
        ghast.setInvulnerable(false);

        // 가스트 체력 설정 (엔진 한계치 1024)
        if (ghast.getAttribute(Attribute.MAX_HEALTH) != null) {
            ghast.getAttribute(Attribute.MAX_HEALTH).setBaseValue(1024.0);
        }
        ghast.setHealth(1024.0);

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

    // CoreMain 클래스 안에 추가/수정
    public void stopGame(String winnerTeam) {
        // 1. 게임 시작 상태 끄기 (이제 아무도 코어를 못 때림)
        setGameStarted(false);

        // 2. 모든 코어 가스트 제거 (서버 깔끔하게)
        removeAllCoreGhasts();

        // 3. 승리 팀에게 자동 보상 (선택 사항)
        // 예: 모든 팀원에게 돈이나 아이템 지급 로직을 여기에 넣으면 됩니다.

        Bukkit.getLogger().info("[NationWar] 게임이 종료되었습니다. 우승팀: " + winnerTeam);
    }

    public void buildCorePlate(Location loc) {
        // 기준서: 5x5 화이트 콘크리트
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                loc.clone().add(x, 0, z).getBlock().setType(Material.WHITE_CONCRETE);
            }
        }
    }

    public void removeAllCoreGhasts() {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntitiesByClass(Ghast.class)) {
                if (entity.hasMetadata("core_id")) entity.remove();
            }
        }
    }

    public boolean isGameStarted() { return gameStarted; }

    public void setGameStarted(boolean started) {
        this.gameStarted = started;
        plugin.getLogger().info("[Core] 보호막 상태 변경: " + (started ? "해제(공격가능)" : "가동(공격불가)"));
    }

    public void respawnAllCores() {
        removeAllCoreGhasts(); // 혹시 남아있을 가스트 중복 방지

        for (CoreGson.CoreInfo core : getCoreData().cores) {
            Location loc = new Location(Bukkit.getWorld("world"), core.x, core.y, core.z); // 월드 이름 확인 필요
            spawnCoreGhast(core.id, loc);
        }
        Bukkit.broadcastMessage("§6§l[!] §f모든 코어 가스트가 성공적으로 재생성되었습니다.");
    }

    public void startTimeChecker() {
        new BukkitRunnable() {
            @Override
            public void run() {
                LocalDateTime now = LocalDateTime.now();

                // 저녁 8시 정각에 점령전 강제 종료 및 정산
                if (now.getHour() == 11 && now.getMinute() == 0 && now.getSecond() == 0) {
                    if (isGameStarted()) {
                        determineWinnerByCount(); // 가장 많이 점령한 팀 찾기
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void determineWinnerByCount() {
        Map<String, Integer> score = new HashMap<>();

        // 각 팀별 점령 개수 계산
        for (CoreGson.CoreInfo core : coreData.cores) {
            if (!core.owner.equals("없음")) {
                score.put(core.owner, score.getOrDefault(core.owner, 0) + 1);
            }
        }

        // 가장 높은 점수를 가진 팀 찾기
        String winner = score.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("없음");

        if (winner.equals("없음")) {
            Bukkit.broadcastMessage("§c§l[!] §f점령 시간이 종료되었습니다.");
            stopGame("없음");
        } else {
            plugin.coreDamageListener.announceVictory(winner); // Listener에 있는 메서드를 호출하거나 CoreMain으로 옮겨서 호출
        }
    }

    public void startCaptureEvent() {
        new BukkitRunnable() {
            int count = 5;
            @Override
            public void run() {
                if (count <= 0) {

                    // 핵심: 게임 시작 상태로 전환!
                    plugin.getCoreMain().setGameStarted(true);

                    this.cancel();
                }
                count--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public boolean isCaptureTime() {
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek day = now.getDayOfWeek();
        int hour = now.getHour();

        // 월, 수, 금, 일 체크
        boolean isDay = (day == DayOfWeek.MONDAY || day == DayOfWeek.WEDNESDAY ||
                day == DayOfWeek.FRIDAY || day == DayOfWeek.SUNDAY);
        boolean isHour = (hour == 10); // 19:00 ~ 19:59

        return isDay && isHour;
    }

    public void saveCores() { CoreGson.save(coreFile, coreData); }

}