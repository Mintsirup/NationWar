package com.nationwar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ghast;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

/**
 * CoreManager responsible for loading/saving cores and spawning them.
 * Note: This class keeps package com.nationwar as requested.
 */
public class CoreManager {
    private final JavaPlugin plugin;
    private final List<Core> cores = new ArrayList<>();
    private final Gson gson = new Gson();

    public CoreManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadCores();
    }

    public List<Core> getCores() {
        return cores;
    }

    // Keep existing saveCores behavior: persist to data folder / cores.json
    public void saveCores() {
        try {
            File dataFolder = plugin.getDataFolder();
            if (!dataFolder.exists()) dataFolder.mkdirs();
            File out = new File(dataFolder, "cores.json");
            try (FileWriter writer = new FileWriter(out)) {
                gson.toJson(cores, writer);
            }
        } catch (Exception ex) {
            plugin.getLogger().log(Level.WARNING, "Failed to save cores.json", ex);
        }
    }

    // Load cores if present (best-effort)
    public void loadCores() {
        try {
            File dataFolder = plugin.getDataFolder();
            if (!dataFolder.exists()) dataFolder.mkdirs();
            File in = new File(dataFolder, "cores.json");
            if (!in.exists()) return;
            try (FileReader reader = new FileReader(in)) {
                Type listType = new TypeToken<List<Core>>(){}.getType();
                List<Core> loaded = gson.fromJson(reader, listType);
                if (loaded != null) {
                    cores.clear();
                    cores.addAll(loaded);
                }
            }
        } catch (Exception ex) {
            plugin.getLogger().log(Level.WARNING, "Failed to load cores.json", ex);
        }
    }

    // Existing spawnCores behavior: place blocks and spawn ghasts for each core
    public void spawnCores() {
        World world = Bukkit.getWorlds().get(0);
        for (Core core : cores) {
            placeCoreBlocks(world, core.getX(), core.getY(), core.getZ());
            spawnGhastForCore(core);
        }
    }

    private void placeCoreBlocks(World world, int x, int y, int z) {
        for (int dx = 0; dx < 4; dx++) {
            for (int dz = 0; dz < 4; dz++) {
                Block b = world.getBlockAt(x + dx, y, z + dz);
                b.setType(Material.WHITE_CONCRETE);
            }
        }
    }

    // Keep existing spawnGhastForCore behavior - here we spawn a ghast and tag it via metadata on the Core object
    public void spawnGhastForCore(Core core) {
        World world = Bukkit.getWorlds().get(0);
        Location loc = new Location(world, core.getX() + 2.0, core.getY() + 1.0, core.getZ() + 2.0);
        Ghast ghast = (Ghast) world.spawnEntity(loc, EntityType.GHAST);
        // Optionally set metadata or custom name
        ghast.setCustomName("Core Ghast");
        ghast.setCustomNameVisible(false);
        core.setGhastUUID(ghast.getUniqueId());
    }

    /**
     * Generate random cores within the server's main world's world border.
     * Places 4x4 WHITE_CONCRETE plates for each core and spawns a ghast.
     * Retries up to 500 attempts per core. Persists cores by calling saveCores() at the end.
     */
    public void generateRandomCores(int count, double minDistance) {
        World world = Bukkit.getWorlds().get(0);
        if (world == null) {
            plugin.getLogger().warning("No world available to generate cores.");
            return;
        }

        // World border center and size
        Location center = world.getWorldBorder().getCenter();
        double size = world.getWorldBorder().getSize();

        double half = size / 2.0;
        double margin = 2; // small margin for 4x4 placement
        double minX = center.getX() - half + margin;
        double maxX = center.getX() + half - margin - 3; // subtract 3 to ensure 4-wide fits
        double minZ = center.getZ() - half + margin;
        double maxZ = center.getZ() + half - margin - 3;

        Random rand = new Random();
        int placed = 0;

        outer:
        for (int i = 0; i < count; i++) {
            boolean ok = false;
            int attempts = 0;
            int tryX = 0, tryY = 0, tryZ = 0;
            while (!ok && attempts < 500) {
                attempts++;
                double rx = minX + rand.nextDouble() * Math.max(0.0, (maxX - minX));
                double rz = minZ + rand.nextDouble() * Math.max(0.0, (maxZ - minZ));
                int x = (int) Math.floor(rx);
                int z = (int) Math.floor(rz);
                int y = world.getHighestBlockYAt(x, z);

                // Check 4x4 area at (x,y,z)
                boolean areaSafe = true;
                for (int dx = 0; dx < 4 && areaSafe; dx++) {
                    for (int dz = 0; dz < 4; dz++) {
                        Block b = world.getBlockAt(x + dx, y, z + dz);
                        Material type = b.getType();
                        if (type == Material.LAVA || b.isLiquid()) {
                            areaSafe = false;
                            break;
                        }

                        Block above1 = b.getRelative(0, 1, 0);
                        Block above2 = b.getRelative(0, 2, 0);
                        if (above1.isLiquid() || above2.isLiquid()) {
                            areaSafe = false;
                            break;
                        }
                        if (!(above1.getType() == Material.AIR && above2.getType() == Material.AIR)) {
                            // need at least two blocks of air for player space
                            areaSafe = false;
                            break;
                        }
                    }
                }

                if (!areaSafe) continue;

                // enforce minDistance from existing placed cores
                boolean farEnough = true;
                for (Core c : cores) {
                    double dx = c.getX() - x;
                    double dz = c.getZ() - z;
                    double dist = Math.sqrt(dx * dx + dz * dz);
                    if (dist < minDistance) {
                        farEnough = false;
                        break;
                    }
                }
                if (!farEnough) continue;

                // All checks passed
                ok = true;
                tryX = x;
                tryY = y;
                tryZ = z;
            }

            if (!ok) {
                plugin.getLogger().log(Level.WARNING, "Failed to place core {0} after {1} attempts", new Object[]{i, 500});
                continue; // may produce fewer than requested cores
            }

            Core newCore = new Core(tryX, tryY, tryZ, 5000);
            cores.add(newCore);
            // Place blocks and spawn ghast
            placeCoreBlocks(world, tryX, tryY, tryZ);
            spawnGhastForCore(newCore);
            placed++;
        }

        plugin.getLogger().info("Placed " + placed + " cores (requested " + count + ").");
        saveCores();
    }
}
