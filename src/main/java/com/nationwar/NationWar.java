package com.nationwar;

import com.nationwar.commands.GameStartCommand;
import com.nationwar.core.CoreManager;
import com.nationwar.listeners.CoreProximityListener;
import com.nationwar.listeners.GhastDamageListener;
import org.bukkit.plugin.java.JavaPlugin;

public class NationWar extends JavaPlugin {

    private static NationWar instance;
    private CoreManager coreManager;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        // Initialize CoreManager (loads cores and spawns Ghasts)
        coreManager = new CoreManager(this);
        coreManager.loadCores();
        coreManager.spawnCores();

        // Register command(s)
        this.getCommand("gamestart").setExecutor(new GameStartCommand(this, coreManager));

        // Register listeners
        getServer().getPluginManager().registerEvents(new GhastDamageListener(coreManager), this);
        getServer().getPluginManager().registerEvents(new CoreProximityListener(coreManager), this);

        getLogger().info("NationWar enabled: core system and gamestart command initialized.");
    }

    @Override
    public void onDisable() {
        if (coreManager != null) coreManager.saveCores();
        instance = null;
    }

    public static NationWar getInstance() {
        return instance;
    }
}
