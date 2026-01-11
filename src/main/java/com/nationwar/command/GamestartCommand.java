package com.nationwar.command;

import com.nationwar.core.CoreMain;
import com.nationwar.core.CoreGson;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.Random;

public class GamestartCommand implements CommandExecutor {
    private final Random random = new Random();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            sender.sendMessage("§c이 명령어는 콘솔 전용입니다.");
            return true;
        }

        World world = Bukkit.getWorlds().get(0);
        world.getWorldBorder().setCenter(0, 0);
        world.getWorldBorder().setSize(15000);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.teleport(findSafeLoc(world, 2000));
        }

        // 코어 6개 랜덤 배치
        for (int i = 0; i < 6; i++) {
            int x = random.nextInt(14000) - 7000;
            int z = random.nextInt(14000) - 7000;
            int y = world.getHighestBlockYAt(x, z);
            CoreGson.CoreData data = CoreGson.getCore(i);
            data.x = x; data.y = y; data.z = z;
            CoreMain.buildCorePhysical(new Location(world, x, y, z), i);
        }
        CoreGson.saveCores();
        Bukkit.broadcastMessage("§6§l국가 전쟁 게임이 시작되었습니다!");
        return true;
    }

    private Location findSafeLoc(World w, int range) {
        while (true) {
            int x = random.nextInt(range) - (range / 2);
            int z = random.nextInt(range) - (range / 2);
            int y = w.getHighestBlockYAt(x, z);
            Location loc = new Location(w, x, y + 1, z);
            if (loc.getBlock().getType() != Material.LAVA) return loc;
        }
    }
}