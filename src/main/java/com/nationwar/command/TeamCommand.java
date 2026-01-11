package com.nationwar.command;

import com.nationwar.team.TeamGson;
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

        if (args.length == 0) {
            player.sendMessage("§e[!] /팀 [이름] - 팀 생성");
            player.sendMessage("§e[!] /팀 해체 - (팀장전용) 팀 삭제");
            return true;
        }

        if (args[0].equals("해체")) {
            String team = TeamMain.getPlayerTeam(player);
            if (team.equals("방랑자")) {
                player.sendMessage("§c[!] 소속된 팀이 없습니다.");
                return true;
            }
            if (TeamGson.getTeams().get(team).owner.equals(player.getUniqueId())) {
                TeamMain.deleteTeam(team);
            } else {
                player.sendMessage("§c[!] 팀장만 팀을 해체할 수 있습니다.");
            }
            return true;
        }

        // 그 외에는 팀 생성으로 간주
        TeamMain.createTeam(player, args[0]);
        return true;
    }
}