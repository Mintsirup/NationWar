package com.nationwar.command;

import com.nationwar.team.TeamMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("플레이어만 사용할 수 있는 명령어입니다.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("/팀 <팀 이름> 형식으로 입력해 주세요.");
            return true;
        }

        if (!TeamMain.getPlayerTeam(player).equals(TeamMain.getWandererTeam())) {
            player.sendMessage("이미 팀에 소속되어 있습니다.");
            return true;
        }

        String teamName = args[0];

        TeamMain.createTeam(player, teamName);
        return true;
    }
}
