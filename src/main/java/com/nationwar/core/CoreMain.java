package com.nationwar.core;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;

public class CoreMain {

    private static final Map<Integer, Location> coreLocations = new HashMap<>();
    private static final Map<Integer, String> coreOwners = new HashMap<>();
    private static final Map<Integer, Integer> coreHealth = new HashMap<>();

    public static void generateCores(World world) {
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            boolean placed = false;
            Location loc = null;

            while (!placed) {
                int x = random.nextInt(14000) - 7000;
                int z = random.nextInt(14000) - 7000;
                int y = world.getHighestBlockYAt(x, z);

                loc = new Location(world, x, y, z);

                Block ground = world.getBlockAt(loc);
                if (ground.getType() == Material.WATER || ground.getType() == Material.LAVA) {
                    continue;
                }

                placed = true;
            }

            buildCore(loc);
            coreLocations.put(i, loc);
            coreOwners.put(i, "방랑자");
            coreHealth.put(i, 5000);
        }

        CoreGson.save();
    }

    private static void buildCore(Location loc) {
        World w = loc.getWorld();
        for (int x = 0; x < 4; x++) {
            for (int z = 0; z < 4; z++) {
                Block b = w.getBlockAt(loc.clone().add(new Vector(x, 0, z)));
                b.setType(Material.WHITE_CONCRETE);
            }
        }
    }

    public static Location getCoreLocation(int id) {
        return coreLocations.get(id);
    }

    public static String getCoreOwner(int id) {
        return coreOwners.get(id);
    }

    public static int getCoreHealth(int id) {
        return coreHealth.get(id);
    }

    public static void damageCore(int id, int amount, Player attacker, String team) {
        if (!coreHealth.containsKey(id)) {
            return;
        }

        int hp = coreHealth.get(id);
        hp = hp - amount;

        if (hp <= 0) {
            coreOwners.put(id, team);
            coreHealth.put(id, 5000);
            CoreGson.save();
            return;
        }

        coreHealth.put(id, hp);
        CoreGson.save();
    }

    public static void spawnDamageGhast(int id) {
        Location loc = coreLocations.get(id);
        if (loc == null) {
            return;
        }

        Ghast g = loc.getWorld().spawn(loc.clone().add(0.5, 3, 0.5), Ghast.class);
        g.setInvisible(true);
        g.setAI(false);
    }

    public static void resetAllHealth() {
        for (int id : coreHealth.keySet()) {
            coreHealth.put(id, 5000);
        }
        CoreGson.save();
    }

    public static Map<Integer, Location> getCoreLocations() {
        return coreLocations;
    }

    public static Map<Integer, String> getCoreOwners() {
        return coreOwners;
    }

    public static Map<Integer, Integer> getCoreHealthMap() {
        return coreHealth;
    }
}
