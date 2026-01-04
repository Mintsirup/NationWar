package com.nationwar.command;

import com.nationwar.NationWar;
import com.nationwar.team.TeamMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamChestCommand implements CommandExecutor {
    private final TeamMain teamMain;

    public TeamChestCommand(TeamMain teamMain) {
        this.teamMain = teamMain;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        String teamName = teamMain.playerTeams.get(player.getUniqueId());
        if (teamName != null && !teamName.equals("방랑자")) {
            player.openInventory(teamMain.teamChests.get(teamName));
        }
        return true;
    }
}