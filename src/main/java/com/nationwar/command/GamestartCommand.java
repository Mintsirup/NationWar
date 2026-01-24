package com.nationwar.command;

import com.nationwar.NationWar;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.Random;

public class GamestartCommand implements CommandExecutor {
    private final NationWar plugin;
    public GamestartCommand(NationWar plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
          // OP이거나 해당 권한이 있어야만 실행 가능
          if (!sender.hasPermission("nationwar.admin")) {
            sender.sendMessage(TextComponent.fromLegacyText("§c§l[!] §c권한이 부족합니다."));
            return true;
          
          }
        
        
        World world = Bukkit.getWorlds().get(0);
        // 기준서: 15000 x 15000 사이즈의 월드보더 설정
        world.getWorldBorder().setCenter(0, 0);
        world.getWorldBorder().setSize(15000);

        Random random = new Random();
        for (Player p : Bukkit.getOnlinePlayers()) {
            Location loc;
            while (true) {
                // 기준서: 2000 x 2000 범위 내 랜덤 텔레포트
                int x = random.nextInt(2001) - 1000;
                int z = random.nextInt(2001) - 1000;
                int y = world.getHighestBlockYAt(x, z);
                loc = new Location(world, x + 0.5, y + 1, z + 0.5);

                // 기준서: 주변 4칸 이내에 용암이 없는 안전한 곳
                boolean safe = true;
                for(int dx=-2; dx<=2; dx++) {
                    for(int dz=-2; dz<=2; dz++) {
                        if(loc.clone().add(dx, -1, dz).getBlock().getType() == Material.LAVA) safe = false;
                    }
                }
                if (safe) break;
            }
            p.teleport(loc);
        }
        startCaptureEvent();
        plugin.getCoreMain().LoadCores(); // 코어 생성 로직 호출
        return true;
    }

    public void startCaptureEvent() {
        new BukkitRunnable() {
            int count = 5;

            @Override
            public void run() {
                if (count > 0) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendTitle("§c§l" + count, "§f전쟁 시작까지...", 0, 21, 0);
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 1.0f);
                    }
                } else {
                    // 전쟁 시작 실제 연출
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendTitle("§4§lWAR BEGINS", "§e지금부터 모든 코어의 보호막이 해제됩니다!", 10, 70, 20);
                        p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.8f);
                        p.playSound(p.getLocation(), Sound.EVENT_RAID_HORN, 1.0f, 1.0f);
                    }

                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage("§8§l[ §4§l! §8§l] §f------------------------------------------ §8§l[ §4§l! §8§l]");
                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage("   §c§l▣ 국가전쟁 점령 시간 ▣");
                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage("   §7지금 이 순간부터 8시까지 점령시간이 활성화되었습니다.");
                    Bukkit.broadcastMessage("   §7자신의 코어를 지키고, 적의 코어를 꿰뚫으십시오.");
                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage("§8§l[ §4§l! §8§l] §f------------------------------------------ §8§l[ §4§l! §8§l]");
                    Bukkit.broadcastMessage("");

                    plugin.getCoreMain().setGameStarted(true); // 게임 상태 변경
                    this.cancel();
                }
                count--;
            }
        }.runTaskTimer(plugin, 0L, 20L); // 1초 간격 실행
    }

}