package com.nationwar.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nationwar.NationWar;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.*;

public class CoreManager {
    private final NationWar plugin;
    private final List<Core> cores = new ArrayList<>();
    private final Map<UUID, Core> ghastToCore = new HashMap<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public CoreManager(NationWar plugin) {
        this.plugin = plugin;
    }

    public List<Core> getCores() { return cores; }

    public void loadCores() {
        try {
            File dataFolder = plugin.getDataFolder();
            if (!dataFolder.exists()) dataFolder.mkdirs();
            File coresFile = new File(dataFolder, "cores.json");
            if (!coresFile.exists()) {
                // create default cores (6 cores) centered around 0
                World world = Bukkit.getWorlds().get(0);
                int baseY = world.getHighestBlockYAt(0, 0) + 1;
                List<Core> defaults = new ArrayList<>();
                defaults.add(new Core(UUID.randomUUID(), world.getName(), 1000, baseY, 1000, 5000));
                defaults.add(new Core(UUID.randomUUID(), world.getName(), -1000, baseY, 1000, 5000));
                defaults.add(new Core(UUID.randomUUID(), world.getName(), 1000, baseY, -1000, 5000));
                defaults.add(new Core(UUID.randomUUID(), world.getName(), -1000, baseY, -1000, 5000));
                defaults.add(new Core(UUID.randomUUID(), world.getName(), 0, baseY, 1500, 5000));
                defaults.add(new Core(UUID.randomUUID(), world.getName(), 0, baseY, -1500, 5000));
                try (FileWriter writer = new FileWriter(coresFile)) {
                    gson.toJson(defaults, writer);
                }
            }

            try (FileReader reader = new FileReader(coresFile)) {
                Type listType = new TypeToken<List<Core>>(){}.getType();
                List<Core> loaded = gson.fromJson(reader, listType);
                cores.clear();
                if (loaded != null) cores.addAll(loaded);
                plugin.getLogger().info("Loaded " + cores.size() + " cores from cores.json");
            }
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to load cores.json: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveCores() {
        try {
            File dataFolder = plugin.getDataFolder();
            if (!dataFolder.exists()) dataFolder.mkdirs();
            File coresFile = new File(dataFolder, "cores.json");
            try (FileWriter writer = new FileWriter(coresFile)) {
                gson.toJson(cores, writer);
            }
            plugin.getLogger().info("Saved cores.json with " + cores.size() + " cores.");
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to save cores.json: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void spawnCores() {
        ghastToCore.clear();
        for (Core core : cores) {
            World world = Bukkit.getWorld(core.getWorldName());
            if (world == null) continue;
            // ensure core blocks exist
            buildCoreBlocks(world, core);
            // spawn ghast near core center
            spawnGhastForCore(world, core);
        }
    }

    private void buildCoreBlocks(World world, Core core) {
        int startX = core.getX();
        int startZ = core.getZ();
        int y = core.getY();
        for (int dx = 0; dx < 4; dx++) {
            for (int dz = 0; dz < 4; dz++) {
                int bx = startX + dx;
                int bz = startZ + dz;
                world.getBlockAt(bx, y, bz).setType(Material.WHITE_CONCRETE);
            }
        }
    }

    private void spawnGhastForCore(World world, Core core) {
        Location loc = new Location(world, core.getX() + 1.5, core.getY() + 3.0, core.getZ() + 1.5);
        // Remove old ghast if present
        if (core.getGhastUuid() != null) {
            try {
                UUID uuid = UUID.fromString(core.getGhastUuid());
                if (uuid != null) {
                    if (Bukkit.getEntity(uuid) != null) {
                        Bukkit.getEntity(uuid).remove();
                    }
                }
            } catch (IllegalArgumentException ignored) {}
        }

        Ghast ghast = (Ghast) world.spawnEntity(loc, EntityType.GHAST);
        ghast.setSilent(true);
        try { ghast.setInvisible(true); } catch (Throwable ignored) {}
        ghast.setCustomNameVisible(false);
        // store mapping
        core.setGhastUuid(ghast.getUniqueId().toString());
        ghastToCore.put(ghast.getUniqueId(), core);
    }

    public Core getCoreByGhastUuid(UUID ghastUuid) {
        return ghastToCore.get(ghastUuid);
    }

    public Core getNearestCore(Location loc, double maxDistance) {
        Core nearest = null;
        double best = Double.MAX_VALUE;
        for (Core c : cores) {
            if (!loc.getWorld().getName().equals(c.getWorldName())) continue;
            double dx = loc.getX() - (c.getX() + 1.5);
            double dz = loc.getZ() - (c.getZ() + 1.5);
            double dist = Math.sqrt(dx * dx + dz * dz);
            if (dist < best && dist <= maxDistance) {
                best = dist;
                nearest = c;
            }
        }
        return nearest;
    }

    public void damageCore(Core core, int amount) {
        if (core == null) return;
        core.damage(amount);
        plugin.getLogger().info("Core " + core.getId() + " damaged for " + amount + ", remaining HP=" + core.getHp());
        if (core.getHp() <= 0) {
            destroyCore(core);
        }
        saveCores();
    }

    private void destroyCore(Core core) {
        World world = Bukkit.getWorld(core.getWorldName());
        if (world == null) return;
        // remove blocks
        int startX = core.getX();
        int startZ = core.getZ();
        int y = core.getY();
        for (int dx = 0; dx < 4; dx++) {
            for (int dz = 0; dz < 4; dz++) {
                world.getBlockAt(startX + dx, y, startZ + dz).setType(Material.AIR);
            }
        }
        // remove ghast
        if (core.getGhastUuid() != null) {
            try {
                UUID uuid = UUID.fromString(core.getGhastUuid());
                if (Bukkit.getEntity(uuid) != null) Bukkit.getEntity(uuid).remove();
            } catch (IllegalArgumentException ignored) {}
        }

        plugin.getLogger().info("Core " + core.getId() + " destroyed.");
    }
}
