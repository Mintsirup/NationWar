package com.nationwar.command;

import com.nationwar.menu.GUIManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MenuCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player player)) return true;

        // 가장 쉬운 메뉴: InfoMenu 먼저 연결
        GUIManager.openInfoMenu(player);
        return true;
    }
}
