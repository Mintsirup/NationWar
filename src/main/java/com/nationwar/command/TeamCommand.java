package com.nationwar.command;

import com.nationwar.team.TeamMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage("사용법: /팀 <팀 이름>");
            return true;
        }

        if (TeamMain.hasTeam(player) &&
                !TeamMain.getTeam(player).equals(TeamMain.WANDERER)) {
            player.sendMessage("이미 팀에 속해 계십니다.");
            return true;
        }

        String teamName = args[0];

        if (TeamMain.teamExists(teamName)) {
            player.sendMessage("이미 존재하는 팀 이름입니다.");
            return true;
        }

        TeamMain.createTeam(teamName, player);
        player.sendMessage(teamName + " 팀이 생성되었습니다.");

        return true;
    }
}
