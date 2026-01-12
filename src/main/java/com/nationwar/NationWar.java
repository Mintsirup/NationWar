package com.nationwar;

import com.nationwar.command.*;
import com.nationwar.core.CoreMain;
import com.nationwar.listeners.*;
import com.nationwar.team.TeamMain;
import com.nationwar.tpa.TpaMain;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class NationWar extends JavaPlugin {

    private static NationWar instance;

    private TeamMain teamMain;
    private CoreMain coreMain;
    private TpaMain tpaMain;

    @Override
    public void onEnable() {
        instance = this;

        // 폴더 생성
        saveDefaultConfig();
        getDataFolder().mkdirs();

        // 시스템 초기화
        this.teamMain = new TeamMain(this);
        this.coreMain = new CoreMain(this);
        this.tpaMain = new TpaMain(this);

        teamMain.loadTeams();
        coreMain.loadCores();

        // 커맨드 등록
        getCommand("메뉴").setExecutor(new MenuCommand(this));
        getCommand("gamestart").setExecutor(new GamestartCommand(this));
        getCommand("gamecontinue").setExecutor(new GameContinueCommand(this));
        getCommand("팀").setExecutor(new TeamCommand(this));
        getCommand("tpa").setExecutor(new TpaCommand(this));
        getCommand("국가창고").setExecutor(new TeamChestCommand(this));

        // 리스너 등록
        Bukkit.getPluginManager().registerEvents(new PvpListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDistanceDetect(this), this);
        Bukkit.getPluginManager().registerEvents(new CoreDamageListener(this), this);
        Bukkit.getPluginManager().registerEvents(new BlockProtection(this), this);
        Bukkit.getPluginManager().registerEvents(new MenuClickListener(this), this);

        getLogger().info("NationWar 플러그인 활성화 완료");
    }

    @Override
    public void onDisable() {
        teamMain.saveTeams();
        coreMain.saveCores();
        getLogger().info("NationWar 플러그인 비활성화 완료");
    }

    public static NationWar getInstance() {
        return instance;
    }

    public TeamMain getTeamMain() {
        return teamMain;
    }

    public CoreMain getCoreMain() {
        return coreMain;
    }

    public TpaMain getTpaMain() {
        return tpaMain;
    }
}
