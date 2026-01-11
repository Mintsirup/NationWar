package com.nationwar.command;

import com.nationwar.core.CoreMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GameContinueCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) return true;

        boolean success = CoreMain.reloadCoresFromConfig();
        if (success) {
            sender.sendMessage("§a[!] core.json 데이터를 읽어 코어 가스트를 정상적으로 복구했습니다.");
        } else {
            sender.sendMessage("§c[!] 코어 데이터를 불러오는 중 오류가 발생했습니다. 콘솔을 확인하세요.");
        }
        return true;
    }
}