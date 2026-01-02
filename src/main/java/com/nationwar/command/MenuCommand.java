package com.nationwar.command;

import com.nationwar.menu.GUIManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MenuCommand implements CommandExecutor {

    private final GUIManager guiManager;

    public MenuCommand(GUIManager guiManager) {
        this.guiManager = guiManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        guiManager.openMainMenu(player);
        player.sendMessage("§f메뉴를 여셨습니다.");
        return true;
    }
}
