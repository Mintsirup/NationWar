package com.nationwar;

import com.nationwar.command.*;
import com.nationwar.listeners.*;
import com.nationwar.team.TeamMain;
import com.nationwar.core.CoreMain;
import com.nationwar.team.TeamGson;
import com.nationwar.core.CoreGson;
import com.nationwar.team.TeamChest;
import org.bukkit.plugin.java.JavaPlugin;

public class NationWar extends JavaPlugin {

    private static NationWar instance;

    @Override
    public void onEnable() {

        instance = this;

        // 1. 폴더 생성 여부 먼저 확인
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        // 2. 데이터 로드 (JSON 파일들)
        TeamGson.loadTeams();
        CoreGson.loadCores();
        TeamChest.loadChests();

        // 2. 명령어 등록
        registerCommands();

        // 3. 리스너(이벤트) 등록
        registerEvents();

        getLogger().info("NationWar 플러그인이 활성화되었습니다!");
    }

    @Override
    public void onDisable() {
        // 데이터 저장 (서버 꺼질 때 데이터 유실 방지)
        TeamGson.saveTeams();
        CoreGson.saveCores();
        TeamChest.saveChests();

        getLogger().info("NationWar 플러그인이 비활성화되었습니다.");
    }

    private void registerCommands() {
        getCommand("메뉴").setExecutor(new MenuCommand());
        getCommand("gamestart").setExecutor(new GamestartCommand());
        getCommand("팀").setExecutor(new TeamCommand());
        getCommand("tpa").setExecutor(new TpaCommand());
        getCommand("국가창고").setExecutor(new TeamChestCommand());
        getCommand("gamecontinue").setExecutor(new GameContinueCommand());
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerDistanceDetect(), this);
        getServer().getPluginManager().registerEvents(new MenuClickListener(), this);
        getServer().getPluginManager().registerEvents(new BlockProtection(), this);
        getServer().getPluginManager().registerEvents(new CoreDamageListener(), this);
        getServer().getPluginManager().registerEvents(new SlimeSpawnListener(), this);
        getServer().getPluginManager().registerEvents(new PvpListener(), this);
    }

    public static NationWar getInstance() {
        return instance;
    }
}