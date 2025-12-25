package com.nationwar.commands;

import com.nationwar.NationWar;
import com.nationwar.core.CoreManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.Random;

public class GameStartCommand implements CommandExecutor {

    private final NationWar plugin;
    private final CoreManager coreManager;
    private final Random random = new Random();

    public GameStartCommand(NationWar plugin, CoreManager coreManager) {
        this.plugin = plugin;
        this.coreManager = coreManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        World world = Bukkit.getWorlds().get(0);
        if (world == null) {
            sender.sendMessage("No world found to start the game.");
            return true;
        }

        // 1) Set world border center 0,0 size 15000
        world.getWorldBorder().setCenter(0.0, 0.0);
        world.getWorldBorder().setSize(15000);
        sender.sendMessage("World border set to center 0,0 and size 15000.");

        // 2) Teleport all online players safely into a 2000x2000 area centered at 0,0
        // area bounds: -1000..1000 for both x and z
        for (Player player : Bukkit.getOnlinePlayers()) {
            Location safe = findSafeSpawn(world);
            if (safe != null) {
                player.teleport(safe);
                sender.sendMessage("Teleported " + player.getName() + " to " + safe.getBlockX() + "," + safe.getBlockZ());
            } else {
                sender.sendMessage("Failed to find safe spawn for " + player.getName() + ", leaving in place.");
            }

            // 3) Assign player to team '방랑자'
            assignPlayerToTeam(player, "방랑자");
        }

        // Ensure cores are (re)spawned and saved
        coreManager.spawnCores();
        coreManager.saveCores();

        sender.sendMessage("Game start completed.");
        return true;
    }

    private void assignPlayerToTeam(Player player, String teamName) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager == null) return;
        Scoreboard board = manager.getMainScoreboard();
        Team team = board.getTeam(teamName);
        if (team == null) {
            team = board.registerNewTeam(teamName);
            team.setDisplayName(teamName);
        }
        // Use player name as entry
        if (!team.hasEntry(player.getName())) team.addEntry(player.getName());
    }

    private Location findSafeSpawn(World world) {
        // Up to 200 attempts to find a random 4x4 lava-free spawn point in -1000..1000
        for (int attempt = 0; attempt < 200; attempt++) {
            int baseX = random.nextInt(2001) - 1000; // [-1000,1000]
            int baseZ = random.nextInt(2001) - 1000;

            // We will check a 4x4 square starting at baseX, baseZ
            boolean ok = true;
            int highestY = -1;
            for (int dx = 0; dx < 4 && ok; dx++) {
                for (int dz = 0; dz < 4; dz++) {
                    int checkX = baseX + dx;
                    int checkZ = baseZ + dz;
                    int y = world.getHighestBlockYAt(checkX, checkZ);
                    if (y <= 0) { ok = false; break; }
                    Material below = world.getBlockAt(checkX, y - 1, checkZ).getType();
                    Material at = world.getBlockAt(checkX, y, checkZ).getType();
                    Material above = world.getBlockAt(checkX, y + 1, checkZ).getType();
                    if (below == Material.LAVA || below == Material.SOUL_FIRE || below == Material.FIRE) { ok = false; break; }
                    if (at.isLiquid() || above.isLiquid()) { ok = false; break; }
                    // also avoid lava blocks inside area
                    if (below == Material.LAVA || at == Material.LAVA || above == Material.LAVA) { ok = false; break; }
                    highestY = Math.max(highestY, y);
                }
            }
            if (!ok) continue;

            // Ensure there is enough headroom (two air blocks)
            if (highestY < 1) continue;
            Location loc = new Location(world, baseX + 1.5, highestY + 1.0, baseZ + 1.5);
            // Final safety checks around the center
            if (world.getBlockAt(loc).getType().isSolid() || world.getBlockAt(loc.getBlockX(), loc.getBlockY() + 1, loc.getBlockZ()).getType().isSolid()) continue;
            return loc;
        }
        return null;
    }
}
