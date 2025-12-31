package com.mintsirup.nationwar.core;

import com.mintsirup.nationwar.StorageManager;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.stream.Collectors;

public class CoreManager {
    private final Plugin plugin;
    private final Map<UUID, Core> cores = new HashMap<>();

    public CoreManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void addCore(Core core) {
        cores.put(core.getId(), core);
        core.spawn(plugin);
    }

    public void removeCore(UUID id) {
        Core c = cores.remove(id);
        if (c != null) c.despawn();
    }

    public Core getCore(UUID id) {
        return cores.get(id);
    }

    public List<Core> getAllCores() {
        return new ArrayList<>(cores.values());
    }

    public void spawnAll() {
        for (Core c : cores.values()) c.spawn(plugin);
    }

    public void clearAll() {
        for (Core c : cores.values()) c.despawn();
        cores.clear();
    }

    public void saveToStorage(StorageManager storage) {
        storage.saveCores(getAllCores());
    }

    public void loadFromStorage(StorageManager storage) {
        List<Core> loaded = storage.loadCores();
        if (loaded == null) return;
        for (Core c : loaded) {
            cores.put(c.getId(), c);
        }
        spawnAll();
    }

    public List<Core> nearbyCores(org.bukkit.Location loc, double radius) {
        return cores.values().stream()
                .filter(c -> c.getLocation().getWorld() == loc.getWorld())
                .filter(c -> c.getLocation().distance(loc) <= radius)
                .collect(Collectors.toList());
    }
}
