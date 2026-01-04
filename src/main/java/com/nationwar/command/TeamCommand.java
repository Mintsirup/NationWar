package com.nationwar.command;

import com.nationwar.NationWar;
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

        if (args.length == 2 && args[0].equals("생성")) {
            teamMain.createTeam(args[1], player.getUniqueId());
            player.sendMessage(args[1] + " 팀이 생성되었습니다.");
        }
        return true;
    }
}