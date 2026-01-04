package com.nationwar.command;

import com.nationwar.menu.menulist.TeamMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (args.length == 0) {
            TeamMenu.open(player);
            return true;
        }

        // 추가적인 명령어 로직 (/팀 생성 [이름] 등)이 필요할 경우 여기에 작성
        return true;
    }
}