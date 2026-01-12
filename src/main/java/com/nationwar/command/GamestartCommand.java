package com.nationwar.command;

import com.nationwar.NationWar;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.Random;

public class GamestartCommand implements CommandExecutor {
    private final NationWar plugin;
    public GamestartCommand(NationWar plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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
        Bukkit.broadcastMessage("§6[국가전쟁] §f게임이 시작되었습니다!");
        plugin.getCoreMain().LoadCores(); // 코어 생성 로직 호출
        return true;
    }
}