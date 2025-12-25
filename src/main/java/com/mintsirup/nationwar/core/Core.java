package com.mintsirup.nationwar.core;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Core {
    private final UUID id;
    private final Location location;
    private int health;
    private ArmorStand stand;

    public Core(Location location, int health) {
        this.id = UUID.randomUUID();
        this.location = location.clone();
        this.health = health;
    }

    public Core(UUID id, Location location, int health) {
        this.id = id;
        this.location = location.clone();
        this.health = health;
    }

    public UUID getId() { return id; }
    public Location getLocation() { return location; }
    public int getHealth() { return health; }

    public void spawn(Plugin plugin) {
        // If already spawned, skip
        if (stand != null && !stand.isDead()) return;

        World world = location.getWorld();
        if (world == null) return;

        stand = (ArmorStand) world.spawnEntity(location, EntityType.ARMOR_STAND);
        stand.setCustomName("Core-" + id.toString());
        stand.setCustomNameVisible(true);
        stand.setGravity(false);
        stand.setVisible(false);
        stand.setPersistent(true);
        stand.setInvulnerable(false); // allow us to control damage handling
    }

    public void despawn() {
        if (stand != null && !stand.isDead()) {
            stand.remove();
        }
        stand = null;
    }

    public void takeDamage(int amount) {
        this.health -= amount;
        if (this.health <= 0) {
            destroy();
        } else {
            updateName();
        }
    }

    private void updateName() {
        if (stand != null && !stand.isDead()) {
            stand.setCustomName("Core-" + id.toString() + " [" + health + "]");
        }
    }

    private void destroy() {
        if (stand != null && !stand.isDead()) {
            stand.getWorld().createExplosion(stand.getLocation(), 0F, false, false);
        }
        despawn();
    }

    public Map<String, Object> serialize() {
        Map<String,Object> m = new HashMap<>();
        m.put("id", id.toString());
        m.put("world", location.getWorld() == null ? "" : location.getWorld().getName());
        m.put("x", location.getX());
        m.put("y", location.getY());
        m.put("z", location.getZ());
        m.put("yaw", location.getYaw());
        m.put("pitch", location.getPitch());
        m.put("health", health);
        return m;
    }

    public static Core deserialize(Map<String,Object> data) {
        try {
            String idStr = (String) data.get("id");
            String worldName = (String) data.get("world");
            double x = ((Number) data.get("x")).doubleValue();
            double y = ((Number) data.get("y")).doubleValue();
            double z = ((Number) data.get("z")).doubleValue();
            float yaw = ((Number) data.getOrDefault("yaw", 0)).floatValue();
            float pitch = ((Number) data.getOrDefault("pitch", 0)).floatValue();
            int health = ((Number) data.getOrDefault("health", 100)).intValue();

            World w = Bukkit.getWorld(worldName);
            if (w == null) return null;
            Location loc = new Location(w, x, y, z, yaw, pitch);
            return new Core(UUID.fromString(idStr), loc, health);
        } catch (Exception e) {
            return null;
        }
    }
}
