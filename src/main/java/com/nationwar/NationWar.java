package com.nationwar;

import com.nationwar.command.*;
import com.nationwar.core.CoreMain;
import com.nationwar.listeners.*;
import com.nationwar.team.TeamMain;
import com.nationwar.tpa.TpaMain;
import com.nationwar.menu.GUIManager; // 추가
import org.bukkit.plugin.java.JavaPlugin;

public class NationWar extends JavaPlugin {
    private static NationWar instance;
    private TeamMain teamMain;
    private CoreMain coreMain;
    private TpaMain tpaMain;
    private GUIManager guiManager; // 추가

    @Override
    public void onEnable() {
        instance = this;

        // 기준서: 데이터 폴더 생성 확인
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        this.teamMain = new TeamMain(this);
        this.coreMain = new CoreMain(this);
        this.tpaMain = new TpaMain(this);
        this.guiManager = new GUIManager(this);

        getCommand("메뉴").setExecutor(new MenuCommand(this));
        getCommand("gamestart").setExecutor(new GamestartCommand(this));
        getCommand("팀").setExecutor(new TeamCommand(this));
        getCommand("tpa").setExecutor(new TpaCommand(this));
        getCommand("국가창고").setExecutor(new TeamChestCommand(this));
        getCommand("gamecontinue").setExecutor(new GameContinueCommand(this));

        getServer().getPluginManager().registerEvents(new PlayerDistanceDetect(this), this);
        getServer().getPluginManager().registerEvents(new MenuClickListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockProtection(), this);
        getServer().getPluginManager().registerEvents(new CoreDamageListener(this), this);
        getServer().getPluginManager().registerEvents(new PvpListener(this), this);

        coreMain.startTimeChecker();
    }

    public static NationWar getInstance() { return instance; }
    public TeamMain getTeamMain() { return teamMain; }
    public CoreMain getCoreMain() { return coreMain; }
    public TpaMain getTpaMain() { return tpaMain; }
    public GUIManager getGUIManager() { return guiManager; } // 추가됨
}