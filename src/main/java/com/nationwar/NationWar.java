package com.nationwar;

import com.nationwar.command.GamestartCommand;
import com.nationwar.command.TpaCommand;
import com.nationwar.listeners.CoreListener;
import com.nationwar.listeners.PvpListener;
import com.nationwar.util.StorageManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class NationWar extends JavaPlugin {

    private static NationWar instance;
    private StorageManager storageManager;

    @Override
    public void onEnable() {
        instance = this;

        // 데이터 폴더 생성
        getDataFolder().mkdirs();

        // StorageManager (Gson 기반)
        storageManager = new StorageManager(this);
        storageManager.loadAll();

        // 커맨드 등록
        getCommand("gamestart").setExecutor(new GamestartCommand(this));
        getCommand("tpa").setExecutor(new TpaCommand(this));
        getCommand("tpa").setTabCompleter(new TpaCommand(this)); // 간단히 같은 객체 사용

        // 리스너 등록
        getServer().getPluginManager().registerEvents(new PvpListener(this), this);
        getServer().getPluginManager().registerEvents(new CoreListener(this), this);

        getLogger().info("NationWar 활성화 (Paper 1.21.10, Java 21)");
    }

    @Override
    public void onDisable() {
        // 저장
        storageManager.saveAll();
        getLogger().info("NationWar 비활성화 - 데이터 저장 완료");
    }

    public static NationWar getInstance() {
        return instance;
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }
}
