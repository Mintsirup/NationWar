package com.nationwar;

import com.nationwar.command.GamestartCommand;
import com.nationwar.command.MenuCommand;
import com.nationwar.command.TpaCommand;
import com.nationwar.command.TeamCommand;
import com.nationwar.command.TeamChestCommand;
import com.nationwar.Listeners.PlayerDistanceDetect;
import com.nationwar.Listeners.PvpListener;
import org.bukkit.plugin.java.JavaPlugin;

public class NationWar extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("gamestart").setExecutor(new GamestartCommand());
        getCommand("메뉴").setExecutor(new MenuCommand());
        getCommand("tpa").setExecutor(new TpaCommand());
        getCommand("팀").setExecutor(new TeamCommand());
        getCommand("국가창고").setExecutor(new TeamChestCommand());

        getServer().getPluginManager().registerEvents(new PlayerDistanceDetect(), this);
        getServer().getPluginManager().registerEvents(new PvpListener(), this);
    }

    @Override
    public void onDisable() {
    }
}
