package com.nationwar.command;

import com.nationwar.team.TeamMain;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Random;

public class GamestartCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof ConsoleCommandSender)) {
            return true;
        }

        World world = Bukkit.getWorlds().get(0);
        WorldBorder border = world.getWorldBorder();
        border.setCenter(0,0);
        border.setSize(15000);

        Random random = new Random();

        for (Player p : Bukkit.getOnlinePlayers()) {
            TeamMain.setPlayerTeam(p, "방랑자");

            boolean found = false;
            Location target = null;

            while (!found) {
                int x = random.nextInt(2000) - 1000;
                int z = random.nextInt(2000) - 1000;
                int y = world.getHighestBlockYAt(x, z);

                Location base = new Location(world, x, y, z);
                boolean lava = false;

                for (int dx = -2; dx <= 2; dx++) {
                    for (int dz = -2; dz <= 2; dz++) {
                        Block b = world.getBlockAt(base.clone().add(new Vector(dx, 0, dz)));
                        if (b.getType() == Material.LAVA) {
                            lava = true;
                        }
                    }
                }

                if (!lava) {
                    target = base.add(0.5,1,0.5);
                    found = true;
                }
            }

            p.teleport(target);
        }

        Bukkit.broadcastMessage("§a국가 전쟁 게임이 시작되었습니다!");

        return true;
    }
}
