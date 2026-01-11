package com.nationwar;

import com.nationwar.command.*;
import com.nationwar.core.CoreGson;
import com.nationwar.listeners.*;
import com.nationwar.team.TeamGson;
import com.nationwar.team.TeamChest;
import org.bukkit.plugin.java.JavaPlugin;

public class NationWar extends JavaPlugin {
    private static NationWar instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        // 데이터 로드
        TeamGson.load();
        CoreGson.load();
        TeamChest.load();

        // 명령어 등록
        getCommand("메뉴").setExecutor(new MenuCommand());
        getCommand("gamestart").setExecutor(new GamestartCommand());
        getCommand("gamecontinue").setExecutor(new GameContinueCommand());
        getCommand("팀").setExecutor(new TeamCommand());
        getCommand("tpa").setExecutor(new TpaCommand());
        getCommand("국가창고").setExecutor(new TeamChestCommand());

        // 리스너 등록
        getServer().getPluginManager().registerEvents(new MenuClickListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDistanceDetect(), this);
        getServer().getPluginManager().registerEvents(new CoreDamageListener(), this);
        getServer().getPluginManager().registerEvents(new PvpListener(), this);
        getServer().getPluginManager().registerEvents(new BlockProtection(), this);
    }

    @Override
    public void onDisable() {
        TeamGson.save();
        CoreGson.save();
        TeamChest.save();
    }

    public static NationWar getInstance() { return instance; }
}