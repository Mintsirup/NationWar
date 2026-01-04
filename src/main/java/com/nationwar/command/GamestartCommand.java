package com.nationwar.command;

import com.nationwar.core.CoreMain;
import com.nationwar.team.TeamMain;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

public class GamestartCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) return true;

        World world = Bukkit.getWorlds().get(0);

        WorldBorder border = world.getWorldBorder();
        border.setCenter(0, 0);
        border.setSize(15000);

        Random random = new Random();

        for (Player player : Bukkit.getOnlinePlayers()) {
            int x;
            int z;
            Location loc;

            while (true) {
                x = random.nextInt(2000) - 1000;
                z = random.nextInt(2000) - 1000;
                int y = world.getHighestBlockYAt(x, z) + 1;

                loc = new Location(world, x, y, z);

                if (loc.getBlock().getType() != Material.LAVA) break;
            }

            player.teleport(loc);
            TeamMain.setPlayerTeam(player, "방랑자");
        }

        CoreMain.createCores(world);

        Bukkit.broadcastMessage("국가 전쟁 게임이 시작되었습니다!");

        return true;
    }
}
