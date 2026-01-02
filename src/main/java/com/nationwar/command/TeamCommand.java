package com.nationwar.command;

import com.nationwar.team.TeamMain;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class TeamCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;

        if (args.length != 1) return true;

        String name = args[0];

        if (TeamMain.teamExists(name)) return true;

        TeamMain.createTeam(name, p);
        p.sendMessage("팀이 생성되었습니다.");

        return true;
    }
}
