package com.nationwar.command;

import com.nationwar.team.TeamChest;
import com.nationwar.team.TeamMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamChestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("콘솔에서는 사용할 수 없습니다.");
            return true;
        }

        String team = TeamMain.getTeam(player);
        if (team == null) {
            player.sendMessage("소속된 국가가 없습니다.");
            return true;
        }

        player.openInventory(TeamChest.getChest(team));
        player.sendMessage("국가 창고를 열었습니다.");
        return true;
    }
}
