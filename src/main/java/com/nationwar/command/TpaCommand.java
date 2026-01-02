package com.nationwar.command;

import com.nationwar.tpa.TpaMain;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class TpaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player p = (Player) sender;
        if (args.length != 1) {
            return true;
        }

        TpaMain.sendRequest(p, args[0]);
        return true;
    }
}
