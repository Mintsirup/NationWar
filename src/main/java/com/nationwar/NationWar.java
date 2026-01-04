package com.nationwar;

import com.nationwar.command.GamestartCommand;
import com.nationwar.command.MenuCommand;
import com.nationwar.command.TpaCommand;
import com.nationwar.command.TeamCommand;
import com.nationwar.command.TeamChestCommand;
import com.nationwar.core.CoreMain;
import com.nationwar.listeners.*;
import com.nationwar.menu.GUIManager;
import com.nationwar.team.TeamMain;
import com.nationwar.tpa.TpaMain;
import org.bukkit.plugin.java.JavaPlugin;

public class NationWar extends JavaPlugin {
    private static NationWar instance;
    private TeamMain teamMain;
    private CoreMain coreMain;
    private TpaMain tpaMain;
    private GUIManager guiManager;

    @Override
    public void onEnable() {
        instance = this;

        this.teamMain = new TeamMain();
        this.coreMain = new CoreMain();
        this.tpaMain = new TpaMain();
        this.guiManager = new GUIManager();

        getCommand("gamestart").setExecutor(new GamestartCommand(coreMain));
        getCommand("메뉴").setExecutor(new MenuCommand());
        getCommand("tpa").setExecutor(new TpaCommand(tpaMain));
        getCommand("팀").setExecutor(new TeamCommand(teamMain));
        getCommand("국가창고").setExecutor(new TeamChestCommand(teamMain));

        getServer().getPluginManager().registerEvents(new PlayerDistanceDetect(coreMain, teamMain), this);
        getServer().getPluginManager().registerEvents(new MenuClickListener(guiManager), this);
        getServer().getPluginManager().registerEvents(new BlockProtection(coreMain), this);
        getServer().getPluginManager().registerEvents(new CoreDamageListener(coreMain, teamMain), this);
        getServer().getPluginManager().registerEvents(new SlimeSpawnListener(), this);
        getServer().getPluginManager().registerEvents(new PvpListener(teamMain), this);
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

    public GUIManager getGuiManager() {
        return guiManager;
    }
}