package com.nationwar.command;

import com.nationwar.team.TeamMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamCommand implements CommandExecutor {
    private final TeamMain teamMain;

    public TeamCommand(TeamMain teamMain) {
        this.teamMain = teamMain;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (args.length >= 1) {
            String teamName = args[0];
            // 지시사항: 팀 생성은 누구나 가능함
            teamMain.createTeam(teamName, player.getUniqueId());
            player.sendMessage(teamName + " 팀이 생성되었습니다.");
        }
        return true;
    }
}