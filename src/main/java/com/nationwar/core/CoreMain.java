package com.nationwar.core;

import com.nationwar.NationWar;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.io.Serializable;
import java.util.*;

public class CoreMain {
    private final Map<Integer, CoreData> cores = new HashMap<>();
    private final Map<Integer, BossBar> bossBars = new HashMap<>();
    private boolean captureTime = false;
    private final CoreGson coreGson;

    public CoreMain() {
        this.coreGson = new CoreGson();
    }

    // 코어 데이터 클래스
    public static class CoreData implements Serializable {
        public double x, y, z;
        public String worldName;
        public String ownerTeam;
        public double hp;
        public transient Location loc;

        public CoreData(double x, double y, double z, String worldName, String ownerTeam, double hp) {
            this.x = x; this.y = y; this.z = z;
            this.worldName = worldName;
            this.ownerTeam = ownerTeam;
            this.hp = hp;
            this.loc = new Location(Bukkit.getWorld(worldName), x, y, z);
        }
    }

    public void spawnCores() {
        World world = Bukkit.getWorlds().get(0);
        Random r = new Random();
        clearExistingCores();
        cores.clear();

        for (int i = 1; i <= 6; i++) {
            int x = r.nextInt(15001) - 7500;
            int z = r.nextInt(15001) - 7500;
            int y = world.getHighestBlockYAt(x, z);
            if (y < -60) y = 64;

            // 4x4x4 콘크리트 배치
            for (int dy = 0; dy < 4; dy++) {
                for (int dx = 0; dx < 4; dx++) {
                    for (int dz = 0; dz < 4; dz++) {
                        world.getBlockAt(x + dx, y + dy, z + dz).setType(Material.WHITE_CONCRETE);
                    }
                }
            }

            // 가스트 히트박스 소환
            Location ghastLoc = new Location(world, x + 2.0, y + 2.0, z + 2.0);
            Ghast ghast = (Ghast) world.spawnEntity(ghastLoc, EntityType.GHAST);
            ghast.setAI(false);
            ghast.setInvisible(true);
            ghast.setSilent(true);
            ghast.setPersistent(true);
            ghast.setMetadata("core_hitbox", new FixedMetadataValue(NationWar.getInstance(), true));

            CoreData data = new CoreData(x, y, z, world.getName(), "없음", 5000);
            cores.put(i, data);

            BossBar bar = Bukkit.createBossBar("코어 " + i + " (소유: 없음)", BarColor.WHITE, BarStyle.SOLID);
            bossBars.put(i, bar);
        }
        coreGson.saveCores(cores);
    }

    public void damageCore(int id, double damage, Player damager) {
        CoreData data = cores.get(id);
        String team = NationWar.getInstance().getTeamMain().getPlayerTeam(damager.getUniqueId());

        if (team.equals("방랑자")) return;

        data.hp -= damage;
        if (bossBars.containsKey(id)) {
            bossBars.get(id).setProgress(Math.max(0, data.hp / 5000.0));
        }

        if (data.hp <= 0) {
            data.ownerTeam = team;
            data.hp = 5000;
            Bukkit.broadcastMessage("§8[§6!§8] §b" + team + "§f 팀이 §e" + id + "번 코어§f를 점령했습니다!");

            if (bossBars.containsKey(id)) {
                bossBars.get(id).setTitle("코어 " + id + " (소유: " + team + ")");
                bossBars.get(id).setColor(BarColor.BLUE);
            }
        }
    }

    public void checkWinner() {
        Map<String, Integer> score = new HashMap<>();
        for (CoreData d : cores.values()) {
            if (!d.ownerTeam.equals("없음")) {
                score.put(d.ownerTeam, score.getOrDefault(d.ownerTeam, 0) + 1);
            }
        }

        for (Map.Entry<String, Integer> entry : score.entrySet()) {
            if (entry.getValue() == 6) {
                broadcastVictory(entry.getKey());
                return;
            }
        }
        Bukkit.broadcastMessage("§7[!] 모든 코어를 점령한 팀이 없어 승리팀 없이 전쟁이 종료되었습니다.");
    }

    private void broadcastVictory(String team) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendTitle("§b§l" + team + " §f팀이 §6§l승리§f하셨습니다!", "§e모두 수고하셨습니다.", 10, 100, 20);
            p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);

            Firework fw = p.getWorld().spawn(p.getLocation(), Firework.class);
            FireworkMeta fm = fw.getFireworkMeta();
            fm.addEffect(FireworkEffect.builder().withColor(Color.BLUE).withFade(Color.YELLOW).with(FireworkEffect.Type.BALL_LARGE).build());
            fw.setFireworkMeta(fm);
        }
    }

    public void clearExistingCores() {
        Bukkit.getWorlds().get(0).getEntitiesByClass(Ghast.class).forEach(g -> {
            if (g.hasMetadata("core_hitbox")) g.remove();
        });
        bossBars.values().forEach(BossBar::removeAll);
        bossBars.clear();
    }

    public Map<Integer, CoreData> getCores() { return cores; }
    public Map<Integer, BossBar> getBossBars() { return bossBars; }
    public boolean isCaptureTime() { return captureTime; }
    public void setCaptureTime(boolean b) { this.captureTime = b; }
}