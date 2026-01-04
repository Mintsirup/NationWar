package com.nationwar;

import com.nationwar.command.GamestartCommand;
import com.nationwar.command.MenuCommand;
import com.nationwar.command.TeamChestCommand;
import com.nationwar.command.TeamCommand;
import com.nationwar.command.TpaCommand;
import com.nationwar.core.CoreMain;
import com.nationwar.listeners.PlayerDistanceDetect;
import com.nationwar.listeners.PvpListener;
import com.nationwar.menu.GUIManager;
import com.nationwar.team.TeamChest;
import com.nationwar.team.TeamMain;
import com.nationwar.tpa.TpaMain;
import org.bukkit.plugin.java.JavaPlugin;

public class NationWar extends JavaPlugin {

    private static NationWar instance;

    public static NationWar getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        TeamMain.init(this);
        CoreMain.init(this);
        TpaMain.init(this);
        GUIManager.init(this);

        getCommand("gamestart").setExecutor(new GamestartCommand());
        getCommand("메뉴").setExecutor(new MenuCommand());
        getCommand("팀").setExecutor(new TeamCommand());
        getCommand("tpa").setExecutor(new TpaCommand());
        getCommand("국가창고").setExecutor(new TeamChestCommand());

        getServer().getPluginManager().registerEvents(new PlayerDistanceDetect(), this);
        getServer().getPluginManager().registerEvents(new PvpListener(), this);
    }

    @Override
    public void onDisable() {
        TeamChest.saveAll();
    }
}
