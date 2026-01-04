package com.nationwar.command;

import com.nationwar.NationWar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamestartCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage("§c권한이 없습니다.");
            return true;
        }

        NationWar.getInstance().getCoreMain().spawnCores();
        sender.sendMessage("§a[관리자] §f6개의 코어가 무작위 좌표에 생성되었습니다.");
        return true;
    }
}