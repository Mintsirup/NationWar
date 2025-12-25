package com.mintsirup.nationwar;

import com.mintsirup.nationwar.core.CoreListener;
import com.mintsirup.nationwar.core.CoreManager;
import org.bukkit.plugin.java.JavaPlugin;

public class NationWar extends JavaPlugin {
    private StorageManager storageManager;
    private CoreManager coreManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        storageManager = new StorageManager(this);
        coreManager = new CoreManager(this);

        // load persisted cores and spawn them
        coreManager.loadFromStorage(storageManager);

        // register listeners
        getServer().getPluginManager().registerEvents(new CoreListener(this), this);

        // other startup logic...
    }

    @Override
    public void onDisable() {
        // persist cores
        if (coreManager != null && storageManager != null) {
            coreManager.saveToStorage(storageManager);
        }
        // cleanup
        if (coreManager != null) coreManager.clearAll();
    }

    public StorageManager getStorageManager() { return storageManager; }
    public CoreManager getCoreManager() { return coreManager; }
}
