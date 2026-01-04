package com.nationwar.command;

import com.nationwar.core.CoreMain;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import java.util.Random;

public class GamestartCommand implements CommandExecutor {
    private final CoreMain coreMain;

    public GamestartCommand(CoreMain coreMain) {
        this.coreMain = coreMain;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // 지시사항: Console Only
        if (!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage("이 명령어는 콘솔에서만 실행 가능합니다.");
            return true;
        }

        World world = Bukkit.getWorlds().get(0); // 기본 월드
        if (world == null) return true;

        // 월드 보더 설정
        WorldBorder border = world.getWorldBorder();
        border.setCenter(0, 0);
        border.setSize(15000);

        Random random = new Random();
        for (Player player : Bukkit.getOnlinePlayers()) {
            Location loc = null;
            boolean safe = false;
            int attempts = 0;

            while (!safe && attempts < 100) {
                attempts++;
                int x = random.nextInt(2001) - 1000;
                int z = random.nextInt(2001) - 1000;
                int y = world.getHighestBlockYAt(x, z);
                loc = new Location(world, x + 0.5, y + 1, z + 0.5);

                safe = true;
                // 주변 4x4x4 용암 체크
                for (int dx = -2; dx <= 2; dx++) {
                    for (int dy = -2; dy <= 2; dy++) {
                        for (int dz = -2; dz <= 2; dz++) {
                            if (loc.clone().add(dx, dy, dz).getBlock().getType() == Material.LAVA) {
                                safe = false;
                                break;
                            }
                        }
                    }
                }
            }
            if (loc != null) player.teleport(loc);
        }

        coreMain.spawnCores();
        Bukkit.broadcastMessage("국가 전쟁 게임이 시작되었습니다!");
        return true;
    }
}