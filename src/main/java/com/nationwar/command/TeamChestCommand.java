package com.nationwar.command;

import com.nationwar.team.TeamChest;
import com.nationwar.team.TeamMain;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TeamChestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        String team = TeamMain.getPlayerTeam(p);

        if (team.equals("방랑자")) return true;

        Inventory inv = Bukkit.createInventory(null, 54, "§8국가 창고 [" + team + "]");
        if (TeamChest.getChestData().containsKey(team)) {
            inv.setContents(TeamChest.fromBase64(TeamChest.getChestData().get(team)));
        }
        p.openInventory(inv);
        return true;
    }
}