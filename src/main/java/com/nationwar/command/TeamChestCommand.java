package com.nationwar.command;

import com.nationwar.team.TeamMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TeamChestCommand implements CommandExecutor {
    private final TeamMain teamMain;

    public TeamChestCommand(TeamMain teamMain) {
        this.teamMain = teamMain;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        String teamName = teamMain.playerTeams.getOrDefault(player.getUniqueId(), "방랑자");

        if (teamName.equals("방랑자")) {
            player.sendMessage("소속된 팀이 없습니다. 먼저 팀을 생성하십시오.");
            return true;
        }

        Inventory inv = teamMain.teamChests.get(teamName);
        if (inv != null) {
            player.openInventory(inv);
        } else {
            player.sendMessage("팀 창고를 불러올 수 없습니다.");
        }
        return true;
    }
}