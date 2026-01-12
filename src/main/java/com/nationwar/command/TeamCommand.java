package com.nationwar.command;

import com.nationwar.NationWar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamCommand implements CommandExecutor {
    private final NationWar plugin;
    public TeamCommand(NationWar plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;

        if (args.length == 0) {
            plugin.getGUIManager().openTeamMenu(p);
            return true;
        }

        String teamName = args[0];
        if (!plugin.getTeamMain().getPlayerTeam(p.getUniqueId()).equals("방랑자")) {
            p.sendMessage("§c이미 팀에 소속되어 있습니다.");
            return true;
        }

        // 기준서: /팀 <이름> 명령어로 누구나 생성 가능, 생성자가 팀장이 됨
        plugin.getTeamMain().createTeam(teamName, p);
        p.sendMessage("§a팀 '" + teamName + "'이 생성되었습니다. 당신은 팀장입니다.");
        return true;
    }
}