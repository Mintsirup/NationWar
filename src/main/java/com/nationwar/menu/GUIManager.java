package com.nationwar.menu;

import com.nationwar.menu.menulist.MainMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class GUIManager {

    private final MainMenu mainMenu;

    public GUIManager(Plugin plugin) {
        this.mainMenu = new MainMenu();
        Bukkit.getPluginManager().registerEvents(this.mainMenu, plugin);
    }

    public void openMainMenu(Player player) {
        this.mainMenu.openMain(player);
    }
}
