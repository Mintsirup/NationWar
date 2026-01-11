package com.nationwar.command;

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
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) return false;

        World world = Bukkit.getWorlds().get(0);

        // 1. 월드보더 설정 (중심 0,0 / 크기 15000)
        world.getWorldBorder().setCenter(0, 0);
        world.getWorldBorder().setSize(15000);

        Random random = new Random();
        for (Player player : Bukkit.getOnlinePlayers()) {
            Location loc = findSafeLocation(world, random);
            player.teleport(loc);
        }

        Bukkit.broadcastMessage("§6§l[!] 국가 전쟁 게임이 시작되었습니다!");
        return true;
    }

    private Location findSafeLocation(World world, Random random) {
        int x, z, y;
        Location loc;
        while (true) {
            // 2000x2000 범위 내 무작위 좌표
            x = random.nextInt(2001) - 1000;
            z = random.nextInt(2001) - 1000;
            y = world.getHighestBlockYAt(x, z);
            loc = new Location(world, x, y + 1, z);

            // 주변 4x4x4 용암 체크
            boolean hasLava = false;
            for (int dx = -2; dx <= 2; dx++) {
                for (int dy = -2; dy <= 2; dy++) {
                    for (int dz = -2; dz <= 2; dz++) {
                        if (loc.clone().add(dx, dy, dz).getBlock().getType() == Material.LAVA) {
                            hasLava = true;
                            break;
                        }
                    }
                }
            }
            if (!hasLava) return loc;
        }
    }
}