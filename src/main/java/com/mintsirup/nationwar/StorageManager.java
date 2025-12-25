package com.mintsirup.nationwar;

import com.mintsirup.nationwar.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorageManager {
    private final Plugin plugin;

    public StorageManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void saveCores(List<Core> cores) {
        FileConfiguration cfg = plugin.getConfig();
        List<Map<String,Object>> serialized = new ArrayList<>();
        for (Core c : cores) serialized.add(c.serialize());
        cfg.set("cores", serialized);
        plugin.saveConfig();
    }

    @SuppressWarnings("unchecked")
    public List<Core> loadCores() {
        FileConfiguration cfg = plugin.getConfig();
        List<?> list = cfg.getList("cores");
        List<Core> out = new ArrayList<>();
        if (list == null) return out;
        for (Object o : list) {
            if (!(o instanceof Map)) continue;
            Map<String,Object> map = (Map<String,Object>) o;
            Core c = Core.deserialize(map);
            if (c != null) out.add(c);
        }
        return out;
    }
}
