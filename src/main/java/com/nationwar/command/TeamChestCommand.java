package com.nationwar.command;

import com.nationwar.NationWar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamChestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        String teamName = NationWar.getInstance().getTeamMain().getPlayerTeam(player.getUniqueId());
        if (teamName.equals("방랑자")) {
            player.sendMessage("§c소속된 팀이 없어 국가창고를 사용할 수 없습니다.");
            return true;
        }

        NationWar.getInstance().getTeamMain().getTeamChest().openChest(player, teamName);
        return true;
    }
}