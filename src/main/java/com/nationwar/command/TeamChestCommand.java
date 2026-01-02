package com.nationwar.command;

import com.nationwar.team.TeamChest;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class TeamChestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;

        TeamChest.openChest(p);
        return true;
    }
}
