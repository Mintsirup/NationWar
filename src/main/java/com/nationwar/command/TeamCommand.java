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

        if (!(sender instanceof Player player)) {
            sender.sendMessage("콘솔에서는 사용할 수 없는 명령어입니다.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("§c/팀 <팀이름>");
            return true;
        }

        String name = args[0];

        if (!teamMain.getTeam(player).equals("방랑자")) {
            player.sendMessage("§c이미 팀에 소속되어 있습니다.");
            return true;
        }

        if (teamMain.createTeam(player, name)) {
            player.sendMessage("§a팀이 성공적으로 생성되었습니다.");
        } else {
            player.sendMessage("§c이미 존재하는 팀 이름입니다.");
        }

        return true;
    }
}
