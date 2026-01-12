package com.nationwar.command;

import com.nationwar.NationWar;
import com.nationwar.team.TeamMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamCommand implements CommandExecutor {

    private final NationWar plugin;
    private final TeamMain teamMain;

    public TeamCommand(NationWar plugin) {
        this.plugin = plugin;
        this.teamMain = plugin.getTeamMain();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("플레이어만 사용할 수 있는 명령어입니다.");
            return true;
        }

        // /팀
        if (args.length == 0) {
            String team = teamMain.getTeam(player);
            player.sendMessage("§e[국가전쟁] §f당신의 팀: §a" + team);

            if (teamMain.isLeader(player)) {
                player.sendMessage("§7(당신은 팀장입니다)");
            }
            return true;
        }

        // /팀 <이름>
        if (args.length == 1) {
            String teamName = args[0];

            // 이미 팀 있음
            if (!teamMain.getTeam(player).equals(TeamMain.DEFAULT_TEAM)) {
                player.sendMessage("§c이미 팀에 속해 있습니다.");
                return true;
            }

            // 팀 생성
            boolean created = teamMain.createTeam(teamName, player);
            if (!created) {
                player.sendMessage("§c이미 존재하는 팀 이름입니다.");
                return true;
            }

            player.sendMessage("§a팀 '" + teamName + "' 이(가) 생성되었습니다!");
            player.sendMessage("§7당신은 이 팀의 팀장입니다.");
            return true;
        }

        player.sendMessage("§c사용법: /팀 <이름>");
        return true;
    }
}
