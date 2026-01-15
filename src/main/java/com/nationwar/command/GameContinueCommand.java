package com.nationwar.command;

import com.nationwar.NationWar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GameContinueCommand implements CommandExecutor {
    private final NationWar plugin;
    public GameContinueCommand(NationWar plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) return false;

        plugin.getCoreMain().respawnAllCores();
        sender.sendMessage("§a데이터를 기반으로 코어를 복구했습니다.");
        return true;
    }
}