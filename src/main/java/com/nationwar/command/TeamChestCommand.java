package com.nationwar.command;

import com.nationwar.NationWar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamChestCommand implements CommandExecutor {
    private final NationWar plugin;
    public TeamChestCommand(NationWar plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        String team = plugin.getTeamMain().getPlayerTeam(p.getUniqueId());

        if (team.equals("방랑자")) {
            p.sendMessage("§c팀이 없는 방랑자는 창고를 사용할 수 없습니다.");
            return true;
        }

        // 기준서: 54칸의 공유 보관함 오픈
        p.openInventory(plugin.getTeamMain().getTeamChest().open(team));
        return true;
    }
}