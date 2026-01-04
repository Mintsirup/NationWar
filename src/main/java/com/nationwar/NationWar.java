package com.nationwar;

import com.nationwar.command.*;
import com.nationwar.core.CoreMain;
import com.nationwar.listeners.*;
import com.nationwar.team.TeamMain;
import com.nationwar.tpa.TpaMain;
import com.nationwar.menu.GUIManager;
import org.bukkit.plugin.java.JavaPlugin;

public class NationWar extends JavaPlugin {
    private static NationWar instance;
    private TeamMain teamMain;
    private CoreMain coreMain;
    private TpaMain tpaMain;
    private GUIManager guiManager;

    @Override
    public void onEnable() {
        instance = this; // 최우선 초기화

        this.teamMain = new TeamMain();
        this.coreMain = new CoreMain();
        this.tpaMain = new TpaMain();
        this.guiManager = new GUIManager();

        // 명령어 등록
        if (getCommand("gamestart") != null) getCommand("gamestart").setExecutor(new GamestartCommand(coreMain));
        if (getCommand("메뉴") != null) getCommand("메뉴").setExecutor(new MenuCommand());
        if (getCommand("tpa") != null) {
            TpaCommand tpaCmd = new TpaCommand(tpaMain);
            getCommand("tpa").setExecutor(tpaCmd);
            getCommand("tpa").setTabCompleter(tpaCmd);
        }
        if (getCommand("팀") != null) getCommand("팀").setExecutor(new TeamCommand(teamMain));
        if (getCommand("국가창고") != null) getCommand("국가창고").setExecutor(new TeamChestCommand(teamMain));

        // 리스너 등록
        getServer().getPluginManager().registerEvents(new PlayerDistanceDetect(coreMain, teamMain), this);
        getServer().getPluginManager().registerEvents(new MenuClickListener(guiManager), this);
        getServer().getPluginManager().registerEvents(new BlockProtection(coreMain), this);
        getServer().getPluginManager().registerEvents(new CoreDamageListener(coreMain, teamMain), this);
        getServer().getPluginManager().registerEvents(new SlimeSpawnListener(), this);
        getServer().getPluginManager().registerEvents(new PvpListener(teamMain), this);
    }

    public static NationWar getInstance() { return instance; }
    public TeamMain getTeamMain() { return teamMain; }
    public CoreMain getCoreMain() { return coreMain; }
    public TpaMain getTpaMain() { return tpaMain; }
    public GUIManager getGuiManager() { return guiManager; }
}