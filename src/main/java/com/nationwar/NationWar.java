package com.nationwar;

import org.bukkit.plugin.java.JavaPlugin;

public class NationWar extends JavaPlugin {

    private static NationWar instance;
    public static NationWar getInstance() { return instance; }

    private GameState gameState = GameState.WAITING;
    public GameState getGameState() { return gameState; }
    public void setGameState(GameState state) { this.gameState = state; }

    @Override
    public void onEnable() {
        instance = this;

        // TODO: 명령어 / 리스너 등록 예정
        getLogger().info("NationWar enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("NationWar disabled");
    }
}
